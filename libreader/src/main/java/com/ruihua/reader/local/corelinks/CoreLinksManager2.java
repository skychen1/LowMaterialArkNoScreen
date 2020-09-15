package com.ruihua.reader.local.corelinks;

import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;

import com.rivamed.libdevicesbase.base.FunctionCode;
import com.rivamed.libdevicesbase.utils.LogUtils;
import com.rivamed.libdevicesbase.utils.TransferUtils;
import com.ruihua.reader.bean.AntInfo;
import com.ruihua.reader.bean.EpcInfo;
import com.ruihua.reader.local.LocalReaderManager;
import com.ruihua.reader.local.callback.LocalReaderOperate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * describe ： 芯联USB连接reader管理类
 *
 * @author : Yich
 * date: 2019/4/18
 */
public class CoreLinksManager2 implements LocalReaderOperate {

    /**
     * deviceId 设备id，唯一标识
     * hReader 设备打开的标识，操作需要用
     */
    private LocalReaderManager localReaderManager;
    private String mDeviceId;
    private SerialPortManager portManager;
    private byte[] sendBuf;
    private volatile boolean isBusy = false;
    private volatile int sendTime = 0;
    private int antNum = 16;
    private boolean isScan = false;
    private byte[] mBtAryBuffer = new byte[4096];
    private int mNLength = 0;
    private Handler mHandler;

    private Map<String, List<EpcInfo>> epcList = new HashMap<>();
    private int timeOut;
    private String filter = "";


    public CoreLinksManager2(LocalReaderManager manager) {
        localReaderManager = manager;
        HandlerThread mHandlerThread = new HandlerThread(" CoreLinks2" + this.hashCode());
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    @Override
    public int connect(String port, int baud) {
        //调用本地方法进行连接
        //如果有连接,就提醒用户已经连接了
        if (portManager != null) {
            return FunctionCode.DEVICE_ALREADY_CONNECTED;
        }
        portManager = new SerialPortManager(localReaderManager);
        //打开通道
        int connect = portManager.connect(port, baud);
        //如果成功，就发送获取id的指令
        if (connect == FunctionCode.SUCCESS) {
            //注册数据回调
            portManager.registerOnDataReceiveCallback(new MyReceiveDataCallback());
            //发送运行在APP层指令，必须发送这个指令才能成功执行后续的其他指令
            sendApp();
            return FunctionCode.SUCCESS;
        } else {
            //打开不成功就回调连接失败，失败就没有id
            return FunctionCode.CONNECT_FAILED;
        }

    }

    private void sendApp() {
        sendTime = 1;
        //拼装协议,并发送数据
        sendBuf = DataProtocol.pieceCommand(DataProtocol.CMD_RUN_APP, null);
        portManager.sendCmd(sendBuf);
    }

    /**
     * 获取设备id
     */
    private void sendGetId() {
        sendTime = 1;
        byte[] bytes = {0x00, 0x00};
        //拼装协议,并发送数据
        sendBuf = DataProtocol.pieceCommand(DataProtocol.CMD_DEVICE_ID, bytes);
        portManager.sendCmd(sendBuf);
    }

    @Override
    public int disConnect() {
        if (portManager == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        //删除集合中的数据
        localReaderManager.delDisConnectReader(mDeviceId);
        if (localReaderManager.getCallback() != null) {
            localReaderManager.getCallback().onConnectState(mDeviceId, false);
        }
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        //断开连接之前先发停止扫描指令
        stopScan();
        //断开连接
        portManager.disConnect();
        return FunctionCode.SUCCESS;
    }

    @Override
    public int setPower(int powerInt) {
        if (isBusy) {
            return FunctionCode.DEVICE_BUSY;
        }
        sendTime = 1;
        isBusy = true;
        isScan = false;
        //准备数据
        byte[] data = new byte[antNum * 5 + 1];
        data[0] = 0x03;
        //需要转换数据成协议认识的（大100倍）
        powerInt = powerInt * 100;
        int high = powerInt / 256;
        int low = powerInt % 256;
        for (int i = 1; i < data.length; i = i + 5) {
            data[i] = (byte) (i / 5 + 1);
            data[i + 1] = (byte) high;
            data[i + 2] = (byte) low;
            data[i + 3] = (byte) high;
            data[i + 4] = (byte) low;
        }
        //拼装协议,并发送数据
        sendBuf = DataProtocol.pieceCommand(DataProtocol.CMD_SET_ANT_INFO, data);
        return portManager.sendCmd(sendBuf);
    }

    @Override
    public int getPower() {
        if (isBusy) {
            return FunctionCode.DEVICE_BUSY;
        }
        sendTime = 1;
        isBusy = true;
        byte[] bytes = {0x03};
        //拼装协议,并发送数据
        sendBuf = DataProtocol.pieceCommand(DataProtocol.CMD_GET_ANT_INFO, bytes);
        return portManager.sendCmd(sendBuf);
    }

    @Override
    public int checkAnt() {
        if (isBusy) {
            return FunctionCode.DEVICE_BUSY;
        }
        sendTime = 1;
        isBusy = true;
        isScan = false;
        return sendCheckAnt();
    }

    private int sendCheckAnt() {
        byte[] bytes = {0x05};
        //拼装协议,并发送数据
        sendBuf = DataProtocol.pieceCommand(DataProtocol.CMD_GET_ANT_INFO, bytes);
        return portManager.sendCmd(sendBuf);
    }

    @Override
    public int getFrequency() {
        return FunctionCode.DEVICE_NOT_SUPPORT;
    }

    @Override
    public int startScan(int timeOut) {
        if (isBusy) {
            return FunctionCode.DEVICE_BUSY;
        }
        this.timeOut = timeOut;
        sendTime = 1;
        isBusy = true;
        isScan = true;
        epcList.clear();
        filter = localReaderManager.getFilter();
        //延时发送结果
        sendComplete();
        //先获连接的天线的指令
        return sendCheckAnt();
    }


    @Override
    public int stopScan() {
        byte[] data = new byte[]{0x4D, 0x6F, 0x64, 0x75, 0x6C, 0x65, 0x74, 0x65, 0x63, 0x68, (byte) 0xAA, 0x49, (byte) 0xF3, (byte) 0xBB};
        byte[] bytes = DataProtocol.pieceCommand(DataProtocol.CMD_INVENTORY, data);
        return portManager.sendCmd(bytes);
    }

    @Override
    public int reScan() {
        return FunctionCode.DEVICE_NOT_EXIST;
    }

    @Override
    public int reset() {
        return FunctionCode.DEVICE_NOT_SUPPORT;
    }

    @Override
    public int delOneEpc(String epc) {
        return FunctionCode.DEVICE_NOT_SUPPORT;
    }

    @Override
    public String getProducer() {
        return "coreLinks";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    /**
     * 发送设置工作天线的命令
     *
     * @param workAnt 工作天线的数组
     */
    private void sendWokAnt(byte[] workAnt) {
        byte[] data = new byte[workAnt.length + 1];
        data[0] = 0x02;
        System.arraycopy(workAnt, 0, data, 1, workAnt.length);
        sendBuf = DataProtocol.pieceCommand(DataProtocol.CMD_SET_ANT_INFO, data);
        int i = portManager.sendCmd(sendBuf);
        //如果发送失败就回调扫描失败；
        if (i != FunctionCode.SUCCESS) {
            localReaderManager.getCallback().onScanResult(mDeviceId, null);
        }
    }

    /**
     * 发送扫描指令
     */
    private void sendScan() {
        byte[] data = new byte[]{0x4D, 0x6F, 0x64, 0x75, 0x6C, 0x65, 0x74, 0x65, 0x63, 0x68, (byte) 0xAA, 0x48, 0x00, 0x3F, 0x00, (byte) 0x80, 0x03, (byte) 0xB4, (byte) 0xBB};
        sendBuf = DataProtocol.pieceCommand(DataProtocol.CMD_INVENTORY, data);
        portManager.sendCmd(sendBuf);
    }

    /**
     * 接收读取数据的回调接口
     */
    class MyReceiveDataCallback implements SerialPortManager.OnDataReceiveCallback {

        @Override
        public void onReceive(byte[] buf) {
            //收到数据以后就可以再发其他指令
            isBusy = false;
            //解析数据
            receiveData(buf);

        }

        @Override
        public void onReceiveOutTime() {
            //超时就重发数据
            LogUtils.e("超时重发了");
            reSend();
        }
    }

    /**
     * 接收到数据，按照协议一条条解包
     *
     * @param buf 数据
     */
    private void receiveData(byte[] buf) {
        try {
            //拼接上一条数据包剩余的数据
            byte[] btAryBuffer = new byte[buf.length + mNLength];
            System.arraycopy(mBtAryBuffer, 0, btAryBuffer, 0, mNLength);
            System.arraycopy(buf, 0, btAryBuffer, mNLength, buf.length);
            int nIndex = 0;
            int nMarkIndex = 0;
            //循环找截取数据为一个个协议数据
            for (int nLoop = 0; nLoop < btAryBuffer.length; nLoop++) {
                //协议的数据至少为1（这部分代码是厂家demo的，理论应该不止1）
                if (btAryBuffer.length > nLoop + 1) {
                    //找到协议的标识头
                    if (btAryBuffer[nLoop] == DataProtocol.HEAD) {
                        //根据协议，数据的长度在标识头的后一位数据，拿到数据长度
                        int nLen = btAryBuffer[nLoop + 1] & 0xFF;
                        //如果数据长度足够这个协议数据，就截取这这个协议数据
                        if (nLoop + 6 + nLen < btAryBuffer.length) {
                            byte[] btAryAnaly = new byte[nLen + 7];
                            System.arraycopy(btAryBuffer, nLoop, btAryAnaly, 0, nLen + 7);
                            //拿到协议数据先检测协议数据的正确性
                            checkData(btAryAnaly);
                            nLoop += 6 + nLen;
                            nIndex = nLoop + 1;
                        } else {
                            nLoop += 6 + nLen;
                        }
                    } else {
                        //理论不丢包的情况就是完整的一条一条的数据，但是如果丢包就需要丢弃其中一个协议包
                        LogUtils.e("出现标志位异常的情况：：");
                        nMarkIndex = nLoop;
                    }
                }
            }
            if (nIndex < nMarkIndex) {
                nIndex = nMarkIndex + 1;
            }
            //如果有剩余数据，就记录下剩余的数据
            if (nIndex < btAryBuffer.length) {
                mNLength = btAryBuffer.length - nIndex;
                mBtAryBuffer = new byte[btAryBuffer.length - nIndex];
                System.arraycopy(btAryBuffer, nIndex, mBtAryBuffer, 0, btAryBuffer.length - nIndex);
                LogUtils.e("解析完一个包剩余的数据：：" + TransferUtils.Byte2String(mBtAryBuffer));
            } else {
                mNLength = 0;
            }
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
    }

    /**
     * 检验数据合法性
     *
     * @param buf 数据
     */
    private void checkData(byte[] buf) {
        //检测数据的合法性
        byte[] checkBuf = new byte[buf.length - 2];
        System.arraycopy(buf, 0, checkBuf, 0, checkBuf.length);
        byte[] check = DataProtocol.calcCRC(checkBuf);
        //如果数据不合法，直接丢掉数据包,
        if (check[0] != buf[buf.length - 2] || check[1] != buf[buf.length - 1]) {
            LogUtils.e("效验码验证未通过  数据为" + TransferUtils.Byte2String(buf));
            return;
        }
        //回调收到的合理的数据,回调给用户
        processData(buf);
    }

    /**
     * 重新发送指令，需要检查是否超过最大次数
     */
    private void reSend() {
        //超过次数，认为数据没办法沟通了，就断开设备，提醒用户
        if (sendTime >= CoreLinksConfig.MAX_SEND_TIMES) {
            //断开设备连接
            disConnect();
        } else {
            //没有超过规定的次数。继续发送上次发送的指令
            sendTime++;
            portManager.sendCmd(sendBuf);
        }
    }

    /**
     * 解析数据
     *
     * @param buf 协议数据
     */
    private void processData(byte[] buf) {
        LogUtils.e("解析数据：" + TransferUtils.Byte2String(buf));
        switch (buf[2]) {
            case DataProtocol.CMD_RUN_APP:
                //命令运行到app层
                if (buf[3] != DataProtocol.RECEIVE_STATE_OK[0] || buf[4] != DataProtocol.RECEIVE_STATE_OK[1]) {
                    break;
                }
                //获取设备id
                sendGetId();
                break;
            case DataProtocol.CMD_DEVICE_ID:
                processId(buf);
                break;
            case DataProtocol.CMD_SET_ANT_INFO:
                processSetAnt(buf);
                break;
            case DataProtocol.CMD_GET_ANT_INFO:
                processGetAnt(buf);
                break;
            case DataProtocol.CMD_INVENTORY:
                processInventory(buf);
                break;
            default:
                break;
        }
    }

    /**
     * 解析读到的标签
     */
    private void processInventory(byte[] buf) {
        //数据只是回调，不保存，所有没有糊掉就不处理了
        if (localReaderManager.getCallback() == null) {
            return;
        }
        //盘存命令的返回
        if (buf[5] == 0x4d && buf[6] == 0x6f) {
            //发送盘存指令失败回调盘存失败，成功了就会有盘存数据
            if (buf[3] != DataProtocol.RECEIVE_STATE_OK[0] || buf[4] != DataProtocol.RECEIVE_STATE_OK[1]) {
                localReaderManager.getCallback().onScanResult(mDeviceId, null);
            }
            return;
        }
        //实际的标签数据
        if (buf[5] == 0x00 && buf[6] == 0x3f) {
            //拿到epc的长度
            int length = buf[19] & 0xff;
            byte[] epcByte = new byte[length - 4];
            System.arraycopy(buf, 22, epcByte, 0, length - 4);
            String epc = TransferUtils.Byte2String(epcByte);
            //如果需要过滤（过滤规则不为空），并且标签不是以规则开始的，就去掉标签
            if (!TextUtils.isEmpty(filter) && !epc.startsWith(filter)) {
                return;
            }
            EpcInfo info = new EpcInfo();
            info.setPc(epc);
            info.setRssi(buf[8] & 0xff);
            info.setAnt(buf[9] & 0xff);
            //如果是已有标签，就加一条
            if (epcList.containsKey(epc)) {
                epcList.get(epc).add(info);
            } else {
                //获取到新标签就重新计时
                cancelSendComplete();
                //回调数据
                localReaderManager.getCallback().onScanNewEpc(mDeviceId, epc, buf[9] & 0xff);
                //创建一个新的集合，保存数据，并存入list中
                ArrayList<EpcInfo> list = new ArrayList<>();
                list.add(info);
                epcList.put(epc, list);
                sendComplete();
            }

        }
    }

    /**
     * 解析获取天线信息
     *
     * @param buf 参数
     */
    private void processGetAnt(byte[] buf) {
        //数据只是回调，不保存，所有没有糊掉就不处理了
        if (localReaderManager.getCallback() == null) {
            return;
        }
        //获取设备功率
        if (buf[5] == 0x03) {
            if (buf[3] != DataProtocol.RECEIVE_STATE_OK[0] || buf[4] != DataProtocol.RECEIVE_STATE_OK[1]) {
                localReaderManager.getCallback().onGetPower(mDeviceId, -1);
            } else {
                //拿到对应的数据（需要从16进制转换成10进制），然后回调数据
                int i = ((buf[7] & 0xff) * 256 + (buf[8] & 0xff)) / 100;
                localReaderManager.getCallback().onGetPower(mDeviceId, i);
            }
        } else if (buf[5] == 0x05) {
            //获取可用天线
            if (buf[3] != DataProtocol.RECEIVE_STATE_OK[0] || buf[4] != DataProtocol.RECEIVE_STATE_OK[1]) {
                if (!isScan) {
                    localReaderManager.getCallback().onCheckAnt(mDeviceId, null);
                } else {
                    localReaderManager.getCallback().onScanResult(mDeviceId, null);
                }
            } else {
                int length = buf[1] & 0xff;
                byte[] data = new byte[length - 1];
                List<AntInfo> list = new ArrayList<>();
                System.arraycopy(buf, 6, data, 0, data.length);
                List<Integer> connectAnt = new ArrayList<>();
                //两个一条数据,循环解析数据，保存到集合中
                for (int i = 0; i < data.length; i = i + 2) {
                    if (!isScan) {
                        AntInfo antInfo = new AntInfo();
                        antInfo.setAntNum(i / 2 + 1);
                        antInfo.setUsable(data[i + 1] == 0x01);
                        list.add(antInfo);
                    } else {
                        //如果是连接的天线就记录到集合中
                        if (data[i + 1] == 0x01) {
                            connectAnt.add(i / 2 + 1);
                        }
                    }
                }
                if (!isScan) {
                    localReaderManager.getCallback().onCheckAnt(mDeviceId, list);
                } else {
                    //拼装工作天线
                    byte[] workAnt = new byte[connectAnt.size() * 2];
                    for (int i = 0; i < connectAnt.size(); i++) {
                        workAnt[2 * i] = connectAnt.get(i).byteValue();
                        workAnt[2 * i + 1] = connectAnt.get(i).byteValue();
                    }
                    sendWokAnt(workAnt);
                }
            }
        }
    }

    /**
     * 解析设置天线指令
     *
     * @param buf 数据
     */
    private void processSetAnt(byte[] buf) {
        //数据只是回调，不保存，所有没有糊掉就不处理了
        if (localReaderManager.getCallback() == null) {
            return;
        }
        //因为设置功率和设置工作天线的回包一样，扫描的时候就是设置的工作天线
        if (buf[3] != DataProtocol.RECEIVE_STATE_OK[0] || buf[4] != DataProtocol.RECEIVE_STATE_OK[1]) {
            if (!isScan) {
                localReaderManager.getCallback().onSetPower(mDeviceId, false);
            } else {
                localReaderManager.getCallback().onScanResult(mDeviceId, null);
            }
        } else {
            if (!isScan) {
                localReaderManager.getCallback().onSetPower(mDeviceId, true);
            } else {
                sendScan();
            }
        }
    }


    /**
     * 解析获取设备id指令
     *
     * @param buf 数据
     */
    private void processId(byte[] buf) {
        //检测指令状态，操作是否成功,获取天线失败就直接返回不做处理
        if (buf[3] != DataProtocol.RECEIVE_STATE_OK[0] || buf[4] != DataProtocol.RECEIVE_STATE_OK[1]) {
            return;
        }
        //取出设备序列号转成String类型
        StringBuilder builder = new StringBuilder();
        //加上前缀
        builder.append("SEAB000001");
        for (int i = 0; i < 12; i++) {
            int id = buf[5 + i] & 0xff;
            builder.append(id);
        }
        mDeviceId = builder.toString();
        //添加到集合中，回调连接成功
        localReaderManager.addConnectReader(mDeviceId, CoreLinksManager2.this);
        if (localReaderManager.getCallback() != null) {
            localReaderManager.getCallback().onConnectState(mDeviceId, true);
        }
    }

    /**
     * 延时发送结果；
     */
    private void sendComplete() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //停止扫描,修改标识，回调结果
                isBusy = false;
                if (localReaderManager.getCallback() != null) {
                    localReaderManager.getCallback().onScanResult(mDeviceId, epcList);
                }
                stopScan();
            }
        }, timeOut);
    }

    /**
     * 取消发送结果
     */
    private void cancelSendComplete() {
        mHandler.removeCallbacksAndMessages(null);
    }

}
