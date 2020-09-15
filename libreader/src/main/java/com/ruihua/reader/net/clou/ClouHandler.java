package com.ruihua.reader.net.clou;

import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.util.Log;

import com.rivamed.libdevicesbase.base.FunctionCode;
import com.rivamed.libdevicesbase.utils.LogUtils;
import com.rivamed.libdevicesbase.utils.TransferUtils;
import com.ruihua.reader.bean.AntInfo;
import com.ruihua.reader.bean.EpcInfo;
import com.ruihua.reader.net.NetReaderManager;
import com.ruihua.reader.net.callback.ReaderHandler;
import com.ruihua.reader.net.callback.ReaderMessageListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;

public class ClouHandler extends BaseClouHandler implements ReaderHandler {


    /**
     * 持续扫描的时间，默认3秒，实际由 StartScan 方法进行赋值
     */
    private int scanTime = 2000;
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    private Map<String, List<EpcInfo>> epcList = new HashMap<>();
    private ReaderMessageListener messageListener;
    private boolean scanMode = false;
    private long lastTipTime;
    private int mMaxAnt = 8;
    private int antByte = 0x0;
    private volatile boolean isCheckAnt = false;
    private int[] ants = new int[]{0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80};
    private int currentAnt = 0;
    private List<AntInfo> antList = new ArrayList<>();
    private String filter = "";

    public ClouHandler() {
        mHandlerThread = new HandlerThread("delay_clou_thread:::" + this.hashCode());
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    @Override
    public void processData(byte[] buf) {
        //计算校验码  判断校验码
        byte[] crcCheckBuf = new byte[buf.length - 3];
        System.arraycopy(buf, 1, crcCheckBuf, 0, crcCheckBuf.length);
        byte[] crc = DataProtocol.CalcCRC16(crcCheckBuf, crcCheckBuf.length);
        if (crc[0] != buf[buf.length - 2] || crc[1] != buf[buf.length - 1]) {
            LogUtils.e("接收到的信息有误，校验码未通过  origin=" + TransferUtils.Byte2String(buf) + "||ji算的校验码=" + TransferUtils.Byte2String(crc));
            return;
        }
        if (buf.length < 7) {
            LogUtils.e("根据数据协议,消息长度最少为7,当前数据长度有误;origindata=" + TransferUtils.Byte2String(buf));
            return;
        }
        //  8-11位为消息类型
        switch (buf[1] & 0xf) {
            case DataProtocol.MSG_TYPE_READER_ERROR:
                //没有标签的时候，开扫描就会出现这个结果
                //延时返回结果
                sendScanResultDelay();
                break;
            case DataProtocol.MSG_TYPE_READER_OPTION:
                processReaderOption(buf);
                break;
            case DataProtocol.MSG_TYPE_RFID_OPERATION:
                processRfidOperation(buf);
                break;
            case DataProtocol.MSG_TYPE_READER_LOG:
                break;
            case DataProtocol.MSG_TYPE_READER_UPDATE:
                break;
            case DataProtocol.MSG_TYPE_READER_TEST:
                processReaderTest(buf);
                break;
            default:
                break;
        }
    }


    /**
     * 延时返回扫描结果
     * 没有标签的时候回返回错误数据，需要启动延时
     */
    private void sendScanResultDelay() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("扫描返回", "延时返回结果了");
                stopScan();
                if (messageListener != null) {
                    messageListener.onScanResult(getIdentification(), epcList);
                }
            }
        }, scanTime);
    }

    /**
     * 取消延时
     */
    private void cancelSendScanResultDelay() {
        mHandler.removeCallbacksAndMessages(null);
    }


    /**
     * 处理阅读器配置和管理信息
     *
     * @param buf 数据
     */
    private void processReaderOption(byte[] buf) {
        switch (buf[2]) {
            case 0x01:
                break;
            case DataProtocol.MID_READER_OPTION_QUERYCONN:
                processConnQuery(buf);
                break;
            case DataProtocol.MID_READER_OPTION_QUERY_MAC:
                processQueryMac(buf);
                break;
            default:
                break;
        }
    }

    /**
     * 处理阅读器配置和管理信息  获取MAC
     *
     * @param buf 数据
     */
    private void processQueryMac(byte[] buf) {
        byte[] data = getData(buf);
        if (data.length <= 0) {
            sendQueryMac();
            return;
        }
        String mac = TransferUtils.Byte2String(data);
        //通知
        LogUtils.e("获取到MAC地址:" + mac);
        setIdentification(mac);
        sendQueryAnt();
        sendRfidQueryInfo();
        if (this.messageListener != null) {
            this.messageListener.onConnectState(ClouHandler.this, getIdentification(), true);
        }
    }

    /**
     * 处理阅读器配置和管理信息 心跳
     *
     * @param buf 数据
     */
    private void processConnQuery(byte[] buf) {
        byte[] data = getData(buf);
        if (data.length <= 0) {
            Log.d("接收到心跳", "没有心跳数据");
        } else {
            sendConnQuery(data);
        }
    }

    private void sendConnQuery(byte[] data) {
        byte msgType = DataProtocol.MSG_TYPE_READER_OPTION;
        byte mid = DataProtocol.MID_READER_OPTION_QUERYCONN;
        sendBuf(msgType, mid, data);
    }


    private byte[] getData(byte[] buf) {
        int lenPre = buf[3] & 0xff;
        int lenAft = buf[4] & 0xff;
        int len = lenPre * 256 + lenAft;
        if (buf.length != len + 7) {
            LogUtils.e("获取数据部分错误,数据长度校验不正确 len=" + len + ";Origin=" + TransferUtils.Byte2String(buf));
        }
        if (len > 0) {
            byte[] data = new byte[len];
            System.arraycopy(buf, 5, data, 0, data.length);
            return data;
        } else {
            return new byte[0];
        }
    }

    private void processRfidOperation(byte[] buf) {
        //第12位，1标识阅读器主动上传，0 标识上位机指令或阅读器响应上位机指令
        int dst = buf[1] & 0x10;
        switch (buf[2]) {
            case 0x00:
                if (dst == 0X10) {
                    //阅读器主动上传  ox00 EPC 数据上传信息
                    processRfidEpc(buf);
                } else {
                    //==0 阅读器响应
                    processRfidInfo(buf);
                }
                break;
            case DataProtocol.MID_RFID_QUERY_ANT:
                processRfidQueryAnt(buf);
                break;
            case 0x01:
                // /设定功率  和 读取结束  依赖上传表示判断
                if (dst != 0x10) {
                    processRfidOptionPower(buf);
                } else {
                    processInventoryEnd(buf);
                }
                break;
            case DataProtocol.MID_RFID_QUERY_POWER:
                processRfidQueryPower(buf);
                break;
            case DataProtocol.MID_RFID_INVENTORY_EPC:
                //没有标签返回的时候就延时操作
                sendScanResultDelay();
                break;
            case DataProtocol.MID_RFID_STOP:
                if (isCheckAnt) {
                    currentAnt++;
                    sendNextAntPort();
                }
                break;
            default:
                break;
        }
    }

    private void processRfidEpc(byte[] buf) {
        //收到新的标签协议了就要取消延时，因为中途可能有错误数据
        cancelSendScanResultDelay();
        byte[] data = getData(buf);
        if (data.length < 6) {
            return;
        }
        String epc;
        int rssi;
        String pc;
        int antIndex;
        int ret;
        boolean getRssi = false;
        for (int i = data.length - 1; i >= 0; i--) {
            //数据长度不够 或者已经解析完成，就退出
            if (i < 7 || getRssi) {
                return;
            }
            if ((data[i - 1] != 0x01 && data[i - 1] != 0x02)) {
                i--;
                continue;
            }
            if (data[i - 1] == 0x01) {
                getRssi = true;
                rssi = data[i];
                antIndex = data[i - 2];
                byte[] bpc = new byte[2];
                System.arraycopy(data, i - 4, bpc, 0, 2);
                pc = TransferUtils.Byte2String(bpc).toUpperCase();
                int epcLen = data.length - 5 - 2;
                byte[] bEpc = new byte[epcLen];
                System.arraycopy(data, 2, bEpc, 0, epcLen);
                epc = TransferUtils.Byte2String(bEpc);
                //如果需要过滤（过滤规则不为空），并且标签不是以规则开始的，就去掉标签
                if (!TextUtils.isEmpty(filter) && !epc.startsWith(filter)) {
                    return;
                }
                LogUtils.e("获取到EPC信息:EPC=" + epc + ";rssi=" + rssi + ";ant=" + antIndex + ";pc=" + pc);
                EpcInfo epcInfo = new EpcInfo();
                epcInfo.setAnt(antIndex);
                epcInfo.setPc(pc);
                epcInfo.setRssi(0 - rssi);
                appendNewTag(epc, epcInfo);
            } else if (data[i - 1] == 0x02) {
                ret = data[i];
                if (ret != 0) {
                    LogUtils.e("接收到错误的EPC读取结果:RET=" + data[i]);
                }
            }
        }
    }

    /**
     * 内部方法，用于和 Colu Service 互动；
     */
    private synchronized void appendNewTag(String epc, EpcInfo epcInfo) {
        if (!scanMode) {
            return;
        }
        if (this.epcList.containsKey(epc)) {
            this.epcList.get(epc).add(epcInfo);
            if (System.currentTimeMillis() - lastTipTime > scanTime) {
                stopScan();
                scanMode = false;
                if (this.messageListener != null) {
                    //发送回调
                    LogUtils.d("触发扫描完成回调");
                    this.messageListener.onScanResult(this.getIdentification(), epcList);
                }
            }
        } else {
            lastTipTime = System.currentTimeMillis();
            List<EpcInfo> tagInfos = new ArrayList<>();
            tagInfos.add(epcInfo);
            this.epcList.put(epc, tagInfos);
            this.messageListener.onScanNewEpc(this.getIdentification(), epc, epcInfo.getAnt() + 1);
        }
    }

    private void processRfidInfo(byte[] buf) {
        byte[] data = getData(buf);
        if (data.length < 2) {
            return;
        }
        int mMaxPower = data[1];
        mMaxAnt = data[2];
        LogUtils.e("获取RFID信息:最大天线数目：" + mMaxAnt + ";最大支持功率:" + mMaxPower);
    }

    private void processRfidQueryAnt(byte[] buf) {
        int len = (0xff & buf[3]) * 256 + buf[4] & 0xff;
        if (len == 0) {
            antByte = 0;
        } else if (len == 1) {
            antByte = buf[5] & 0xff;
        } else if (len == 2) {
            antByte = (buf[5] & 0xff) * 256 + (buf[6] & 0xff);
        }

        LogUtils.e("获取到天线数据:" + Integer.toHexString(antByte));
    }

    private void processRfidOptionPower(byte[] buf) {
        byte[] data = getData(buf);
        if (data.length != 1) {
            return;
        }
        LogUtils.e("设置功率结果=" + (data[0] == 0x00));
        if (messageListener != null) {
            messageListener.onSetPowerRet(this.getIdentification(), data[0] == 0);
        }
    }

    private void processInventoryEnd(byte[] buf) {
        byte[] data = getData(buf);
        if (data.length != 1) {
            return;
        }
        if (data[0] == 0x00) {
            LogUtils.e("RFID扫描结束");
            stopScan();
            if (this.messageListener != null) {
                //发送回调
                LogUtils.e("触发扫描完成回调");
                this.messageListener.onScanResult(this.getIdentification(), epcList);
            }
        }
    }

    private void processRfidQueryPower(byte[] buf) {
        byte[] data = getData(buf);
        if (data.length <= 0) {
            return;
        }
        if (data.length % 2 != 0) {
            return;
        }
        int power = 100;
        int tmpPower;
        for (int i = 0; i < mMaxAnt; i++) {
            int ant = (int) Math.pow(2, data[i * 2]);
            if ((antByte & ant) == ant) {
                tmpPower = data[2 * i + 1] & 0xff;
                if (tmpPower < power) {
                    power = tmpPower;
                }
            }
        }
        if (this.messageListener != null) {
            this.messageListener.onQueryPowerRet(this.getIdentification(), power);
        }
    }

    /**
     * 解析测试指令
     *
     * @param buf 数据
     */
    private void processReaderTest(byte[] buf) {
        switch (buf[2]) {
            case DataProtocol.MSG_TYPE_READER_SEND_PORT:
                //如果返回的数据是0.就表示法送载波成功，就要发送检测天线指令
                if (buf[5] == 0) {
                    sendCheckAnt();
                } else {
                    //设置失败就认为这根天线不可用，设置下一根天线的驻波
                    AntInfo info = new AntInfo();
                    info.setAntNum(currentAnt + 1);
                    antList.add(info);
                    currentAnt++;
                    sendNextAntPort();
                }
                break;
            case DataProtocol.MID_READER_OPTION_CHECK_ANT:
                //收到检测驻波的包，解析
                int check = DataProtocol.byte2int(new byte[]{buf[5]}) - DataProtocol.byte2int(new byte[]{buf[6]});
                LogUtils.e("天线…" + currentAnt + "的差值是：：" + check);
                AntInfo info = new AntInfo();
                info.setAntNum(currentAnt + 1);
                //如果驻波差值大于40就认为是正常的天线
                if (check >= 40) {
                    info.setUsable(true);
                }
                antList.add(info);
                //需要停止操作，才能再设置下一根天线的载波
                sendStop();
                break;
            default:
                break;
        }
    }


    @Override
    public int stopScan() {
        scanMode = false;
        return sendStop() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    private boolean sendStop() {
        byte msgType = DataProtocol.MSG_TYPE_RFID_OPERATION;
        byte mid = DataProtocol.MID_RFID_STOP;
        return sendBuf(msgType, mid, null);
    }

    @Override
    public int closeChannel() {
        //调用父类的关闭方法
        return close() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    @Override
    public int startScan(int timeout) {
        //如果正在扫描就不直接返回
        if (scanMode) {
            return FunctionCode.DEVICE_BUSY;
        }
        lastTipTime = System.currentTimeMillis();
        if (timeout <= 0) {
            timeout = 2000;
        }
        //拿到过滤规则
        filter = NetReaderManager.getManager().getFilter();
        this.scanTime = timeout;
        scanMode = true;
        epcList.clear();
        return sendStartInventory(null) ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    @Override
    public int startScan(int timeout, byte[] ants) {
        return FunctionCode.DEVICE_NOT_SUPPORT;
    }

    private boolean sendStartInventory(byte[] password) {
        byte msttype = DataProtocol.MSG_TYPE_RFID_OPERATION;
        byte mid = DataProtocol.MID_RFID_INVENTORY_EPC;
        byte[] data;
        if (password == null || password.length != 4) {
            data = new byte[2];
            //天线
            data[0] = (byte) antByte;
            //连续扫描 1 单次扫描 0
            data[1] = 0X01;
        } else {
            data = new byte[7];
            data[0] = (byte) antByte;
            data[1] = 0x01;
            //密码
            data[2] = 0x05;
            System.arraycopy(password, 0, data, 3, password.length);
        }
        Log.e("发送扫描指令了", "发送扫描指令了");
        return sendBuf(msttype, mid, data);

    }

    @Override
    public int setPower(byte power) {
        if (power <= 0 || power > 30) {
            return FunctionCode.PARAM_ERROR;
        }
        return sendOptionPower(power) ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    @Override
    public int setPower(byte[] powers) {
        return FunctionCode.DEVICE_NOT_SUPPORT;
    }

    private boolean sendOptionPower(byte power) {
        byte msgtype = DataProtocol.MSG_TYPE_RFID_OPERATION;
        byte mid = DataProtocol.MID_RFID_OPTION_POWER;
        byte[] datatmp = new byte[1024];
        int pos = 0;
        for (byte antIndex = 0; antIndex < mMaxAnt; antIndex++) {
            int antNum = (int) Math.pow(2, antIndex);
            if ((antNum & antByte) == antNum) {
                datatmp[pos++] = (byte) (antIndex + 1);
                datatmp[pos++] = power;
            }
        }
        if (pos <= 0) {
            return false;
        }
        byte[] data = new byte[pos];
        System.arraycopy(datatmp, 0, data, 0, pos);
        sendBuf(msgtype, mid, data);
        return true;
    }

    @Override
    public int getPower() {
        sendQueryPower();
        return FunctionCode.SUCCESS;
    }

    private void sendQueryPower() {
        byte msgtype = DataProtocol.MSG_TYPE_RFID_OPERATION;
        byte mid = DataProtocol.MID_RFID_QUERY_POWER;
        sendBuf(msgtype, mid, null);
    }

    @Override
    public int checkAnts() {
        if (scanMode) {
            return FunctionCode.DEVICE_BUSY;
        }
        scanMode = true;
        isCheckAnt = true;
        antList.clear();
        currentAnt = 0;
        return sendNextAntPort() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;

    }

    /**
     * 发送载波指令
     *
     * @return 返回成功石板
     */
    private boolean sendNextAntPort() {
        if (currentAnt >= ants.length) {
            //回调天线
            if (messageListener != null) {
                messageListener.onCheckAnt(getIdentification(), antList);
            }
            scanMode = false;
            isCheckAnt = false;
            return false;
        }
        byte msgtype = DataProtocol.MSG_TYPE_READER_TEST;
        byte mid = DataProtocol.MSG_TYPE_READER_SEND_PORT;
        byte[] data = new byte[2];
        data[0] = (byte) ants[currentAnt];
        data[1] = 0;
        return sendBuf(msgtype, mid, data);
    }

    /**
     * 发送检测天线指令（驻波数据）
     */
    private void sendCheckAnt() {
        byte msgtype = DataProtocol.MSG_TYPE_READER_TEST;
        byte mid = DataProtocol.MID_READER_OPTION_CHECK_ANT;
        sendBuf(msgtype, mid, null);
    }

    @Override
    public int reset() {
        sendReset();
        return FunctionCode.SUCCESS;
    }

    private void sendReset() {
        byte msgType = DataProtocol.MSG_TYPE_READER_OPTION;
        byte mid = DataProtocol.MID_READER_OPTION_RESET;
        sendBuf(msgType, mid, null);
    }

    @Override
    public int openLock() {
        return FunctionCode.DEVICE_NOT_SUPPORT;
    }

    @Override
    public int closeLock() {
        return FunctionCode.DEVICE_NOT_SUPPORT;
    }

    @Override
    public int openLight() {
        return FunctionCode.DEVICE_NOT_SUPPORT;
    }

    @Override
    public int closeLight() {
        return FunctionCode.DEVICE_NOT_SUPPORT;
    }

    @Override
    public int checkLockState() {
        return FunctionCode.DEVICE_NOT_SUPPORT;
    }

    @Override
    public int checkLightState() {
        return FunctionCode.DEVICE_NOT_SUPPORT;
    }

    @Override
    public String getRemoteIP() {
        return getIP();
    }

    @Override
    public String getProducer() {
        return "Clou";
    }

    @Override
    public String getVersion() {
        return "V1.0";
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LogUtils.e("连接断开了");
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        if (mHandlerThread != null) {
            //断开连接就要退出循环，销毁
            mHandlerThread.quit();
            mHandlerThread = null;
        }

        if (messageListener != null) {
            messageListener.onConnectState(this, getIdentification(), false);
            messageListener = null;
        }
        super.channelInactive(ctx);
    }

    public void registerClouMessageListener(ReaderMessageListener listener) {
        this.messageListener = listener;
    }


}
