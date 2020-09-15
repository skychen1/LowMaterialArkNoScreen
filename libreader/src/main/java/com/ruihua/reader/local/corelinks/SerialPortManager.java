package com.ruihua.reader.local.corelinks;

import android.os.Handler;
import android.os.HandlerThread;

import com.aill.androidserialport.SerialPort;
import com.rivamed.libdevicesbase.base.FunctionCode;
import com.rivamed.libdevicesbase.utils.LogUtils;
import com.rivamed.libdevicesbase.utils.TransferUtils;
import com.ruihua.reader.local.LocalReaderManager;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * describe ：串口管理类，
 * 连接打开设备，发送协议指令（拼装协议）
 * 接收数据，解析数据，将数据返回给使用者；
 * 连发三次数，保证数据正常，如果数据出错，提醒用户；
 *
 * @author : Yich
 * date: 2019/4/19
 */
public class SerialPortManager {


    /**
     * mId 数据通道
     * outputStream 数据发送流
     * inputStream 数据读取流
     * scheduled 线程池
     * mHandler 延迟操作
     * bufSend 需要发送的指令
     * isSending 是否在横在发送指令中
     * mDeviceId 设备的编号
     * callback 数据回调接口
     */
    private FileDescriptor mId;
    private FileOutputStream outputStream = null;
    private FileInputStream inputStream = null;
    private ScheduledThreadPoolExecutor scheduled = null;
    private Handler mHandler;
    private volatile boolean needRead = false;
    private byte[] bufSend;
    private OnDataReceiveCallback callback;


    public SerialPortManager(LocalReaderManager manager) {
        if (scheduled == null) {
            ThreadFactory namedThreadFactory = new BasicThreadFactory.Builder().namingPattern("SerialPort-schedule-pool-%d").daemon(true).build();
            scheduled = new ScheduledThreadPoolExecutor(3, namedThreadFactory);
        }
        HandlerThread mHandlerThread = new HandlerThread("SerialPort::::" + this.hashCode());
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    /**
     * 连接设备
     *
     * @return 返回码
     */
    public int connect(String path, int bound) {
        mId = SerialPort.open(path, bound, 0);
        if (mId != null) {
            outputStream = new FileOutputStream(mId);
            inputStream = new FileInputStream(mId);
            return FunctionCode.SUCCESS;
        } else {
            return FunctionCode.CONNECT_FAILED;
        }
    }


    /**
     * 断开设备
     *
     * @return 返回码
     */
    public void disConnect() {
        if (mId == null) {
            return ;
        }

        //延时500ms关闭通道，保证所有指令全部发出
        scheduled.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    //修改读取标识位置
                    needRead = false;
                    if (outputStream != null) {
                        outputStream.close();
                        outputStream = null;
                    }
                    if (inputStream != null) {
                        inputStream.close();
                        inputStream = null;
                    }
                } catch (Exception e) {
                   LogUtils.e(e.toString());
                }
            }
        }, 500, TimeUnit.MILLISECONDS);

    }


    /**
     * 发送指令   同时只能发送一条单指令
     *
     * @param data 数据
     * @return 返回是否成
     */
    public int sendCmd(byte[] data) {
        //如果没有输出流就表示设备没有连接，直接返回
        if (outputStream == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        bufSend = data;
        //在发指令的时候就不需要读取了
        needRead = false;
        mHandler.removeCallbacksAndMessages(null);
        sendData();
        return FunctionCode.SUCCESS;
    }

    /**
     * 发送数据,耗时操作，需要再线程中进行
     */
    private void sendData() {
        scheduled.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    outputStream.write(bufSend);
                    outputStream.flush();
                    //发送成功以后延迟读取数据
                    mHandler.postDelayed(mReadRunnable, CoreLinksConfig.READ_DELAY_TIME);
                } catch (IOException e) {
                    LogUtils.e(e.toString());
                }
            }
        });
    }

    private Runnable mReadRunnable = new Runnable() {
        @Override
        public void run() {
            //一直延迟循环读取数据
            //标识，是否读取到数据
            needRead = true;
            boolean hasReadData = false;
            long lastTime = System.currentTimeMillis();
            while (needRead) {
                try {
                    //判断流中是否有可读的数据，有可读的数据，就读取可读数据长度的数据
                    if (inputStream.available() > 0) {
                        //修改是否已读到数据标识,推迟收到数据的时间
                        hasReadData = true;
                        lastTime = System.currentTimeMillis();
                        //读取数据
                        byte[] buf = new byte[inputStream.available()];
                        //有数据就回调数据
                        if (inputStream.read(buf) > 0 && callback != null) {
                            callback.onReceive(buf);
                        }
                    }
                } catch (Exception e) {
                    LogUtils.e(e.toString());
                }
                //循环读完一次之后，判断是否还要继续读取数据，
                //连续多长时间（CoreLinksConfig.READ_TIME_OUT）没有收到消息就不再继续读取数据，
                if (System.currentTimeMillis() - lastTime > CoreLinksConfig.READ_TIME_OUT) {
                    //如果从未读到数据，就回调超时
                    if (!hasReadData && callback != null) {
                        callback.onReceiveOutTime();
                    }
                    break;
                }
                //如果还需要继续读取数据，就继续延迟一个等待时间继续读取
                try {
                    Thread.sleep(CoreLinksConfig.READ_DELAY_TIME);
                } catch (Exception e) {
                    LogUtils.e(e.toString());
                }
            }
        }
    };


    /**
     * 设置数据回调
     *
     * @param callback 接口
     */
    public void registerOnDataReceiveCallback(OnDataReceiveCallback callback) {
        this.callback = callback;
    }

    /**
     * 数据回调接口
     */
    protected interface OnDataReceiveCallback {
        /**
         * 收到数据回调接口
         *
         * @param buf 数据
         */
        void onReceive(byte[] buf);

        /**
         * 接收数据超时（从未收到数据）
         */
        void onReceiveOutTime();
    }

}
