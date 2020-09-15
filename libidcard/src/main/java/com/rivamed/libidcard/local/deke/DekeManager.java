package com.rivamed.libidcard.local.deke;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import com.rivamed.libdevicesbase.base.FunctionCode;
import com.rivamed.libdevicesbase.utils.LogUtils;
import com.rivamed.libdevicesbase.utils.TransferUtils;
import com.rivamed.libidcard.local.LocalIdCardManager;
import com.rivamed.libidcard.local.callback.IdCardOperate;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

import android_serialport_api.SerialPort;

/**
 * describe ：
 *
 * @author : Yich
 * date: 2019/7/22
 */
public class DekeManager implements IdCardOperate {

    private String mUid = "deke_00001";

    private ScheduledThreadPoolExecutor scheduled = null;
    private Handler mHandler;
    private SerialPort serialPort;
    private LocalIdCardManager mManager;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private volatile int sendTime = 0;
    private volatile boolean isReading = false;
    private volatile boolean isSending = false;
    private volatile boolean isStart = false;
    private byte[] mSendBuf;


    public DekeManager(LocalIdCardManager manager) {
        mManager = manager;
        if (scheduled == null) {
            ThreadFactory namedThreadFactory = new BasicThreadFactory.Builder().namingPattern("SerialPort-schedule-pool-%d").daemon(true).build();
            scheduled = new ScheduledThreadPoolExecutor(2, namedThreadFactory);
        }
        HandlerThread mHandlerThread = new HandlerThread("deke_card::::" + this.hashCode());
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    @Override
    public int connect(Context context) {
        serialPort = new SerialPort();
        boolean open = serialPort.openPort(new File(DekeConfig.PATH), DekeConfig.BAUD, 0);
        if (!open) {
            return FunctionCode.CONNECT_FAILED;
        }
        //获取输入输出流
        mInputStream = serialPort.getInputStream();
        mOutputStream = serialPort.getOutputStream();
        mManager.addConnectIdCard(mUid, this);
        if (mManager.getCallback() != null) {
            mManager.getCallback().onConnectState(mUid, true);
        }
        //通道打开就开始读数据
        isReading = true;
        scheduled.execute(mReadRunnable);
        return FunctionCode.SUCCESS;
    }

    @Override
    public int disConnect() {
        if (mInputStream == null || mOutputStream == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        isReading = false;
        //删除集合中的数据
        mManager.delDisConnectIdCard(mUid);
        //回调断开
        if (mManager.getCallback() != null) {
            mManager.getCallback().onConnectState(mUid, false);
        }
        //关闭数据流通道
        try {
            mInputStream.close();
            mInputStream = null;
            mOutputStream.flush();
            mOutputStream.close();
            mOutputStream = null;
        } catch (IOException e) {
            LogUtils.e(e.toString());
        }
        //关闭串口通道
        if (serialPort != null) {
            SerialPort.close();
            serialPort = null;
        }
        mManager.delDisConnectIdCard(mUid);
        return FunctionCode.SUCCESS;
    }

    @Override
    public int startRead() {
        if (isSending) {
            return FunctionCode.DEVICE_BUSY;
        }
        if (isStart) {
            return FunctionCode.DEVICE_BUSY;
        }
        isSending = true;
        isStart = true;
        sendTime = 1;
        byte[] data = new byte[]{(byte) 0xFF, 0x14, 0x02};
        byte[] bytes = DataProtocol.pieceCommand(DataProtocol.CMD_SEND_READ, data);
        sendData(bytes);
        return FunctionCode.SUCCESS;
    }

    @Override
    public int stopRead() {
        //如果在发送指令就返回设备在忙
        if (isSending) {
            return FunctionCode.DEVICE_BUSY;
        }
        isSending = true;
        isStart = false;
        sendTime = 1;
        byte[] data = new byte[]{(byte) 0x00, 0x14, 0x02};
        byte[] bytes = DataProtocol.pieceCommand(DataProtocol.CMD_SEND_READ, data);
        sendData(bytes);
        return FunctionCode.SUCCESS;
    }

    @Override
    public String getProducer() {
        return "deke";
    }

    @Override
    public String getVersion() {
        return "v_1.0.0";
    }

    /**
     * 发送指令
     *
     * @param buf 指令
     */
    private void sendData(byte[] buf) {
        mSendBuf = buf;
        scheduled.execute(new Runnable() {
            @Override
            public void run() {
                if (mOutputStream == null) {
                    return;
                }
                try {
                    mOutputStream.write(mSendBuf);
                    mOutputStream.flush();
                    //延时重发
                    mHandler.postDelayed(mSendRunnable, DekeConfig.READ_TIME_OUT);
                } catch (IOException e) {
                    LogUtils.e(e.toString());
                }
            }
        });
    }

    /**
     * 重发指令
     */
    private Runnable mSendRunnable = new Runnable() {
        @Override
        public void run() {
            if (sendTime > DekeConfig.MAX_SEND_TIMES) {
                //断开设备
                disConnect();
                return;
            }
            //没有超过规定的次数。继续发送上次发送的指令
            sendTime++;
            sendData(mSendBuf);
        }
    };

    /**
     * 读取数据线程
     */
    private Runnable mReadRunnable = new Runnable() {
        @Override
        public void run() {
            while (isReading) {
                byte[] buffer = new byte[1024];
                //如果没有输入流了就直接退出
                if (mInputStream == null) {
                    return;
                }
                try {
                    int size = mInputStream.read(buffer);
                    if (size > 0) {
                        byte[] bytes = new byte[size];
                        System.arraycopy(buffer, 0, bytes, 0, size);
                        onReceive(bytes);
                    }
                } catch (IOException e) {
                    LogUtils.e(e.toString());
                }
            }
        }
    };

    /**
     * 接收到数据
     *
     * @param buf 数据
     */
    private void onReceive(byte[] buf) {
        //如果头标识不对 或者数据小于最小数据就丢弃数据
        if (buf[0] != DataProtocol.HEAD || buf.length < DekeConfig.MIN_LENGTH) {
            return;
        }
        switch (buf[2]) {
            case DataProtocol.CMD_RECEIVE_READ:
                //收到数据了就直接取消重发指令
                mHandler.removeCallbacksAndMessages(null);
                isSending = false;
                break;
            case DataProtocol.CMD_RECEIVE_CARD:
                int length = buf[1] & 0xff - 1;
                byte[] bytes = new byte[length];
                System.arraycopy(buf, 3, bytes, 0, length);
                if (mManager.getCallback() != null && isStart) {
                    mManager.getCallback().onReceiveCardNum(TransferUtils.Byte2String(bytes));
                }
                break;
            default:
                break;
        }
    }
}
