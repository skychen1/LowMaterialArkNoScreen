package com.rivamed.libidcard.net.ande;

import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;

import com.rivamed.libdevicesbase.base.FunctionCode;
import com.rivamed.libdevicesbase.utils.TransferUtils;
import com.rivamed.libidcard.net.callback.IDCardHandler;

/**
 * describe ： 安的网络模块儿
 *
 * @author : Yich
 * date: 2019/10/28
 */
public class AnDeHandler extends BaseAnDeHandler implements IDCardHandler {


    private String mId;
    private volatile boolean isReading = false;
    private Handler mHandler;
    private String lastCardNum = "";
    private HandlerThread mHandlerThread;

    public AnDeHandler() {
        mHandlerThread = new HandlerThread("delay_handler::::" + this.hashCode());
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }



    @Override
    void onLostConnect() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        if (mHandlerThread != null) {
            //断开连接就要退出循环，销毁
            mHandlerThread.quit();
            mHandlerThread = null;
        }
        if (mListener != null && !TextUtils.isEmpty(mId)) {
            mListener.onConnectState(this, mId, false);
            mListener = null;
        }

    }

    @Override
    public void processData(byte[] buf) {
        switch (buf[4]) {
            case AnDeProtocol.CMD_GET_ID:
                processGetId(buf);
                break;
            case AnDeProtocol.CMD_GET_ICARD_NUM:
                processCardNum(buf);
                break;
            default:
                break;
        }
    }

    /**
     * 解析获取设备id
     */
    private void processGetId(byte[] buf) {
        //准备接收id的buf
        byte[] data = new byte[5];
        //解析设备序列号作为id
        System.arraycopy(buf, 12, data, 0, data.length);
        mId = TransferUtils.Byte2String(data);
        if (mListener != null) {
            mListener.onConnectState(this, mId, true);
        }
    }

    /**
     * 解析读到的卡号
     *
     * @param buf 数据
     */
    private void processCardNum(byte[] buf) {
        //如果读到的卡数大于0
        if ((buf[8] & 0xff) > 0 && buf.length > 19) {
            //解析卡号
            byte[] cardBuf = new byte[4];
            System.arraycopy(buf, 15, cardBuf, 0, cardBuf.length);
            String cardNum = TransferUtils.Byte2String(cardBuf);
            //如果读到的卡号与上次读到的卡号（一定时间）不同就回调卡号
            if (!lastCardNum.equals(cardNum) && isReading && mListener != null) {
                //取消上次延时
                cancelCleanCardNumDelay();
                //记录读到的卡号
                lastCardNum = cardNum;
                //延迟清空
                cleanCardNumDelay();
                mListener.onReceiveIDCard(cardNum);
            }
        }
        //如果还在读卡，就继续发送读卡指令
        if (isReading) {
            sendGetCardNum();
        }
    }

    /**
     * 延时清空上次读到的卡号
     */
    private void cleanCardNumDelay() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lastCardNum = "";
            }
        }, 2 * 1000);
    }

    /**
     * 取消延时清空上次卡号；
     */
    private void cancelCleanCardNumDelay() {
        mHandler.removeCallbacksAndMessages(null);
    }


    @Override
    public int closeChannel() {
        return close() ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
    }

    @Override
    public int startReadCard() {
        if (isReading) {
            return FunctionCode.DEVICE_BUSY;
        }
        isReading = true;
        //发送读卡的协议包
        return sendGetCardNum();
    }

    /**
     * 发送获取卡号
     *
     * @return 返回码
     */
    private int sendGetCardNum() {
        byte[] data = new byte[]{AnDeProtocol.CMD_GET_ICARD_NUM, 0x02, 0x07, 0x0E, 0x00, 0x00, 0x04, 0x00, 0x00, 0x0D, 0x00, 0x00, 0x06, 0x00, 0x03, 0x00, (byte) 0xFF, 0x00, 0x03, 0x00, 0x02, 0x00, 0x00, 0x02, 0x00, 0x00, 0x01, 0x00, 0x02, 0x00, 0x00};
        return sendPacket(data) ? FunctionCode.SUCCESS : FunctionCode.OPERATION_FAIL;
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
        return "An_De";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }
}
