package com.rivamed.libidcard.net.yumin;

import com.rivamed.libdevicesbase.base.FunctionCode;
import com.rivamed.libdevicesbase.utils.TransferUtils;
import com.rivamed.libidcard.net.callback.IDCardHandler;
import com.rivamed.libidcard.net.callback.IdCardMessageListener;

public class YuminHandler extends BaseYuminHandler implements IDCardHandler {

    /**
     * mListener 注册监听器
     * mHandler netty中传输数据的通道
     */
    private IdCardMessageListener mListener;
    private volatile boolean isReading = false;

    @Override
    public void processData(byte[] buf) {
        switch (buf[2]) {
            case DataProtocol.CMD_CARD_NUM:
                processIdCard(buf);
                break;
            case DataProtocol.CMD_ADDRESS:
                processDeviceId(buf);
                break;
            default:
                break;
        }
    }

    /**
     * 获取设备ID的返回结果
     *
     * @param buf 数据
     */
    private void processDeviceId(byte[] buf) {
        //如果获取设备id的状态不对就继续发送获取id命令
        if (buf[4] != 0x00) {
            sendGetDeviceId();
            return;
        }
        //拿到包的长度
        int length = buf[1] & 0xff;
        //算出id的长度
        length = length - 6;
        //获取成功解析数据，保存并回调
        byte[] idBuf = new byte[length];
        System.arraycopy(buf, 5, idBuf, 0, idBuf.length);
        setIdentification(TransferUtils.Byte2String(idBuf));
        //否则提醒用户设备连接成功
        if (mListener != null) {
            mListener.onConnectState(YuminHandler.this, getIdentification(), true);
        }
    }

    /**
     * 获取读到的卡号
     */
    private void processIdCard(byte[] buf) {
        if (buf[4] != 0x00) {
            sendGetDeviceId();
            return;
        }
        //拿到包的长度
        int length = buf[1] & 0xff;
        //算出id的长度
        length = length - 6;
        //获取成功解析数据，保存并回调
        byte[] idBuf = new byte[length];
        System.arraycopy(buf, 5, idBuf, 0, idBuf.length);
        String idNo = TransferUtils.Byte2String(idBuf);
        //解析完数据，如果有回调，并且在读取才回调数据
        if (this.mListener != null && isReading) {
            this.mListener.onReceiveIDCard(idNo);
        }

    }

    /**
     * 注册监听回调
     *
     * @param listener 回调
     */
    public void registerIdCardMessageListener(IdCardMessageListener listener) {
        this.mListener = listener;
    }

    @Override
    public int closeChannel() {
        //调用父类的关闭方法
        return close() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    @Override
    public int startReadCard() {
        if (isReading) {
            return FunctionCode.DEVICE_BUSY;
        }
        isReading = true;
        return FunctionCode.SUCCESS;
    }

    @Override
    public int stopReadCard() {
        isReading = false;
        return FunctionCode.SUCCESS;
    }

    @Override
    public String getRemoteIP() {
        return getIP();
    }

    @Override
    public String getProducer() {
        return "yumin";
    }

    @Override
    public String getVersion() {
        return "V:1.0";
    }
}
