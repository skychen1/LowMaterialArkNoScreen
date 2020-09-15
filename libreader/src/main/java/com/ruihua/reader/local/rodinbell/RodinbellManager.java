package com.ruihua.reader.local.rodinbell;

import android.text.TextUtils;
import android.util.Log;

import com.rivamed.libdevicesbase.base.FunctionCode;
import com.rivamed.libdevicesbase.utils.LogUtils;
import com.rivamed.libdevicesbase.utils.TransferUtils;
import com.ruihua.reader.bean.AntInfo;
import com.ruihua.reader.bean.EpcInfo;
import com.ruihua.reader.local.LocalReaderManager;
import com.ruihua.reader.local.callback.LocalReaderOperate;
import com.uhf.uhf.serialport.SerialPort;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * describe ： 罗丹贝尔串口连接方式管理类
 *
 * @author : Yich
 * date: 2019/7/23
 */
public class RodinbellManager extends ReaderBase implements LocalReaderOperate {

    private LocalReaderManager mReaderManager;
    private SerialPort mPort;
    private String mId;
    private volatile Map<String, List<EpcInfo>> epcList = new HashMap<>();
    private volatile boolean isBusying = false;
    private int scanTime = 1500;
    private long lastTipTime;
    private byte currentAntIndex = 0;
    private volatile boolean isCheckAnt = false;
    private List<AntInfo> antInfoList = new ArrayList<>();
    private volatile boolean isScanning = false;
    private String filter = "";


    public RodinbellManager(LocalReaderManager manager) {
        mReaderManager = manager;
    }

    @Override
    public int connect(String port, int baud) {
        if (mPort != null) {
            return FunctionCode.DEVICE_ALREADY_CONNECTED;
        }
        mPort = new SerialPort();
        boolean b = mPort.openPort(new File(port), baud, 0);
        if (!b) {
            mPort = null;
            return FunctionCode.CONNECT_FAILED;
        }
        //调用父类的方法，将通道传递过去
        setStream(mPort.getInputStream(), mPort.getOutputStream());
        sendGetId();
        return FunctionCode.SUCCESS;
    }

    /**
     * 发送获取设备id的命令
     */
    private void sendGetId() {
        byte[] command = Command.getCommand(Command.CMD_GET_DEVICE_ID, null);
        sendCommand(command);
    }

    @Override
    public int disConnect() {
        if (mPort == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        signOut();
        //删除集合中的数据
        mReaderManager.delDisConnectReader(mId);
        mPort = null;
        return FunctionCode.SUCCESS;
    }

    @Override
    public int setPower(int powerInt) {
        if (mPort == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        if (isBusying) {
            return FunctionCode.DEVICE_BUSY;
        }
        isBusying = true;
        byte[] data = new byte[]{(byte) powerInt};
        byte[] command = Command.getCommand(Command.CMD_SET_OUTPUT_POWER, data);
        sendCommand(command);
        return FunctionCode.SUCCESS;
    }

    @Override
    public int getPower() {
        if (mPort == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        if (isBusying) {
            return FunctionCode.DEVICE_BUSY;
        }
        isBusying = true;
        byte[] command = Command.getCommand(Command.CMD_GET_OUTPUT_POWER, null);
        sendCommand(command);
        return FunctionCode.SUCCESS;
    }

    @Override
    public int checkAnt() {
        if (mPort == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        if (isBusying) {
            return FunctionCode.DEVICE_BUSY;
        }
        isBusying = true;
        isCheckAnt = true;
        currentAntIndex = 0;
        antInfoList.clear();
        sendNextWorkAnt();
        return FunctionCode.SUCCESS;
    }

    @Override
    public int getFrequency() {
        return FunctionCode.DEVICE_NOT_SUPPORT;
    }

    @Override
    public int startScan(int timeOut) {
        if (mPort == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        if (isBusying) {
            return FunctionCode.DEVICE_BUSY;
        }
        isBusying = true;
        isScanning = true;
        //记录没有新标签的时间
        scanTime = timeOut;
        //如果是点击开始扫描的话就设置上次收到新标签的时间为当前时间
        lastTipTime = System.currentTimeMillis();
        //数据回到初始状态
        epcList.clear();
        filter = mReaderManager.getFilter();
        //开始扫描默认是第1根天线
        currentAntIndex = 0;
        sendNextWorkAnt();
        return FunctionCode.SUCCESS;
    }

    /**
     * 设置下一根天线
     */
    private void sendNextWorkAnt() {
        //超过天线的最大值就设为第一根天线
        if ((currentAntIndex > Command.ANTS.length - 1)) {
            //如果是检测天线的命令就直接回调数据，结束流程了
            if (isCheckAnt) {
                //修改标识
                isCheckAnt = false;
                isBusying = false;
                //回调数据
                if (mReaderManager.getCallback() != null) {
                    mReaderManager.getCallback().onCheckAnt(mId, antInfoList);
                }
                return;
            } else {
                currentAntIndex = 0;
            }
        }
        byte[] command = Command.getCommand(Command.CMD_SET_WORK_ANT, new byte[]{Command.ANTS[currentAntIndex]});
        sendCommand(command);

    }

    @Override
    public int stopScan() {
        if (mPort == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        if (!isScanning) {
            return FunctionCode.OPERATION_FAIL;
        }
        //回调结果，调整标志位
        processScanCompleted();
        return 0;
    }

    @Override
    public synchronized int reScan() {
        epcList.clear();
        return FunctionCode.SUCCESS;
    }

    @Override
    public int reset() {
        byte[] command = Command.getCommand(Command.CMD_RESET, null);
        sendCommand(command);
        return FunctionCode.SUCCESS;
    }

    @Override
    public synchronized int delOneEpc(String epc) {
        epcList.remove(epc);
        return FunctionCode.SUCCESS;
    }

    @Override
    public String getProducer() {
        return "rodinbell";
    }

    @Override
    public String getVersion() {
        return "v_1.0.0";
    }

    /**
     * 设备断开
     */
    @Override
    void onLostConnect() {
        mReaderManager.delDisConnectReader(mId);
        if (mReaderManager.getCallback() != null) {
            mReaderManager.getCallback().onConnectState(mId, false);
        }
    }

    /**
     * 数据解析
     *
     * @param buf Parsed packet
     */
    @Override
    void receiveData(byte[] buf) {
        LogUtils.e("收到数据了：：：" + TransferUtils.Byte2String(buf));
        //根据协议，第三个字节流是类型数据，根据不同类型的数据做不同的处理
        switch (buf[3]) {
            case Command.CMD_RESET:
                //重置没有回复的消息
                break;
            //获取读写器的识别码
            case Command.CMD_GET_DEVICE_ID:
                processGetDeviceId(buf);
                break;
            //设置读写器射频输出功率
            case Command.CMD_SET_OUTPUT_POWER:
                processSetPower(buf);
                break;
            //获取读写器射频输出功率
            case Command.CMD_GET_OUTPUT_POWER:
                processGetPower(buf);
                break;
            //设置读写器工作天线
            case Command.CMD_SET_WORK_ANT:
                processSetWorkAnt(buf);
                break;
            // 读取天线回波损耗
            case Command.GET_RF_PORT_RETURN_LOSS:
                processPortLoss(buf);
                break;
            //获取读写器工作天线
            case Command.CMD_GET_WORK_ANT:
                //获取工作天线，在该项目中用作心跳包处理，所以不不需要处理
                break;
            //自定义session和target盘存
            case Command.CMD_REALTIME_INVENTORY:
                processRealTimeInventory(buf);
                break;
            default:
                break;
        }
    }

    /**
     * 解析获取设备id号结果
     *
     * @param buf 数据
     */
    private void processGetDeviceId(byte[] buf) {
        //如果获取数据出错，重新发送获取指令
        if (buf.length != 17) {
            sendGetId();
            return;
        }
        //获取成功解析数据，保存并回调
        byte[] idBuf = new byte[12];
        System.arraycopy(buf, 4, idBuf, 0, 12);
        mId = TransferUtils.Byte2String(idBuf);
        mReaderManager.addConnectReader(mId, RodinbellManager.this);
        //否则提醒用户设备连接成功
        if (mReaderManager.getCallback() != null) {
            mReaderManager.getCallback().onConnectState(mId, true);
        }
    }

    /**
     * 设置功率
     *
     * @param buf 协议数据
     */
    private void processSetPower(byte[] buf) {
        //回调设置功率结果，成功或者失败
        isBusying = false;
        if (mReaderManager.getCallback() != null) {
            mReaderManager.getCallback().onSetPower(mId, buf[4] == Command.CMD_SUCCESS);
        }
    }

    /**
     * 获取功率
     *
     * @param buf 协议数据
     */
    private void processGetPower(byte[] buf) {
        //解析数据
        int allPower;
        if (buf.length == 6) {
            allPower = buf[4];
        } else {
            allPower = -1;
            for (int i = 0; i < 4; i++) {
                if (allPower < buf[4 + i]) {
                    allPower = buf[4 + i];
                }
            }
        }
        isBusying = false;
        if (mReaderManager.getCallback() != null) {
            mReaderManager.getCallback().onGetPower(mId, allPower);
        }
    }

    /**
     * 解析设置天线数据包
     *
     * @param buf 协议数据
     */
    private void processSetWorkAnt(byte[] buf) {
        //拿到设置成功或者失败
        boolean success = buf[4] == Command.CMD_SUCCESS;
        //如果成功
        if (success) {
            //如果是检测天线就发送测量天线回波损耗指令
            if (isCheckAnt) {
                //发送检测天线指令
                sendPortReturnLoss();
            } else {
                //设置天线成功就发送盘存指令
                sendRtInventory();
            }
        } else {
            //设置下一根天线
            isCheckAnt();
        }
    }

    /**
     * 解析回波损耗
     *
     * @param buf 数据
     */
    private void processPortLoss(byte[] buf) {
        AntInfo antInfo = new AntInfo();
        antInfo.setAntNum(currentAntIndex + 1);
        //如果损耗值大于3就表示可用，小于3就是不可用
        if (buf[4] > 3) {
            antInfo.setUsable(true);
        }
        //加入到集合中
        antInfoList.add(antInfo);
        //设置下一根天线
        currentAntIndex++;
        sendNextWorkAnt();
    }

    /**
     * 盘存的结果解析
     *
     * @param buf 协议数据
     */
    private void processRealTimeInventory(byte[] buf) {
        //如果没有在扫描了就直接取消；
        if (!isScanning) {
            return;
        }
        //读取扫描数据出错的话直接跳下一根天线继续读
        if (buf.length == 6) {
            Log.e("解析数据出错", currentAntIndex + "继续扫描下一根天线");
            //继续扫描，设置天线
            currentAntIndex++;
            sendNextWorkAnt();
            return;
        }
        //如果是一根天先扫描完成的结束包（一根正确的天线扫描结束）
        if (buf.length == 12 && buf[4] >= 0x00 && buf[4] <= 0x03) {
            if (scanTime == 0 || System.currentTimeMillis() - lastTipTime < scanTime) {
                //如果时间不够，继续扫描
                currentAntIndex++;
                sendNextWorkAnt();
            } else {
                Log.e("对比是否有新标签了", "没有新标签了，返回最后结果");
                //超过设置时间间隔就发送扫描结果发送扫描结果，
                processScanCompleted();
            }
        } else {
            //返回的数据正确，就解析数据
            processEpcData(buf);
        }
    }

    /**
     * 解析读取的标签数据
     *
     * @param buf 标签数据
     */
    private void processEpcData(byte[] buf) {
        //因为前面数据已经处理过(每一条数据就是一条标准的协议标签数据，所以不需要再验证标签数据)，
        byte ant = (byte) ((buf[4] & 0x03) & 0xff);
        byte[] bPc = new byte[2];
        //解析pc值
        System.arraycopy(buf, 5, bPc, 0, 2);
        String pc = TransferUtils.Byte2String(bPc);
        int lenEpc = buf.length - 9;
        byte[] bEpc = new byte[lenEpc];
        System.arraycopy(buf, 7, bEpc, 0, lenEpc);
        String epc = TransferUtils.Byte2String(bEpc);
        //如果需要过滤（过滤规则不为空），并且标签不是以规则开始的，就去掉标签
        if (!TextUtils.isEmpty(filter) && !epc.startsWith(filter)) {
            return;
        }
        //转变成真实的RSSI,参见开发文档第五节
        byte brssi = buf[5 + lenEpc];
        int rssi = 0 - (129 - (brssi & 0xff));
        EpcInfo info = new EpcInfo(rssi, ant, pc);
        //判断以前是否有这个标签，如果有，就直接添加本次扫描的状态到集合中
        if (epcList.containsKey(epc)) {
            epcList.get(epc).add(info);
        } else {
            //如果是新标签，就重新记录上一个新标签的时间，用于结束使用；
            lastTipTime = System.currentTimeMillis();
            if (mReaderManager.getCallback() != null) {
                mReaderManager.getCallback().onScanNewEpc(mId, epc, currentAntIndex + 1);
            }
            //创建一个新的集合，保存数据，并存入list中
            ArrayList<EpcInfo> list = new ArrayList<>();
            list.add(info);
            epcList.put(epc, list);
        }
    }

    /**
     * 扫描天线完成，回调扫描结果
     */
    private void processScanCompleted() {
        isBusying = false;
        isScanning = false;
        //回调扫描完成结果
        if (mReaderManager.getCallback() != null) {
            //扫描结果
            mReaderManager.getCallback().onScanResult(mId, epcList);
        }
    }


    /**
     * 发送盘存的命令
     */
    private void sendRtInventory() {
        //准备0x8B盘存指令
        byte[] data = new byte[]{Command.RTINVENTORY_SESSION, Command.RTINVENTORY_TARGET, Command.RTINVENTORY_REPEAT};
        byte[] command = Command.getCommand(Command.CMD_REALTIME_INVENTORY, data);
        //将盘存的全部指令发送出去
        sendCommand(command);
    }

    /**
     * 发送获取回波损耗值
     */
    private void sendPortReturnLoss() {
        byte[] data = new byte[]{(byte) 33};
        byte[] command = Command.getCommand(Command.GET_RF_PORT_RETURN_LOSS, data);
        sendCommand(command);
    }


    /**
     * 判断是否是检测天线的状态，
     * 如果是检测天线的状态就需要添加这根天线不可用
     * 然后执行设置下一根天线的操作
     */
    private void isCheckAnt() {
        //如果是检测天线的状态就需要添加这根天线不可用
        if (isCheckAnt) {
            AntInfo info = new AntInfo();
            info.setAntNum(currentAntIndex + 1);
            antInfoList.add(info);
        }
        currentAntIndex++;
        sendNextWorkAnt();
    }
}
