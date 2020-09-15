
package com.ruihua.reader.local.rodinbell;

import com.rivamed.libdevicesbase.utils.LogUtils;
import com.rivamed.libdevicesbase.utils.TransferUtils;
import com.ruihua.reader.net.rodinbell.DataProtocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class ReaderBase {
    private WaitThread mWaitThread = null;
    private InputStream mInStream;
    private OutputStream mOutStream;

    private ExecutorService mExecutorService = Executors.newFixedThreadPool(5);

    /**
     * Connection Lost.
     */
    abstract void onLostConnect();

    /**
     * Rewritable function,This function will be called after parse a packet data.
     *
     * @param buf Parsed packet
     */
    abstract void receiveData(byte[] buf);

    private byte[] btAryBuffer = new byte[4096];
    private int nLength = 0;

    /**
     * With reference constructor. Construct a Reader with input and output streams.
     *
     * @param in  Input Stream
     * @param out Output Stream
     */
    public void setStream(InputStream in, OutputStream out) {
        this.mInStream = in;
        this.mOutStream = out;
        startWait();
    }

    private void startWait() {
        mWaitThread = new WaitThread();
        mWaitThread.start();
    }

    /**
     * Loop receiving data thread.
     *
     * @author Jie
     */
    private class WaitThread extends Thread {
        private boolean mShouldRunning;

        private WaitThread() {
            mShouldRunning = true;
        }

        @Override
        public void run() {
            byte[] bytes = new byte[4096];
            while (mShouldRunning) {
                try {
                    int nLenRead = mInStream.read(bytes);
                    if (nLenRead > 0) {
                        byte[] btAryReceiveData = new byte[nLenRead];
                        System.arraycopy(bytes, 0, btAryReceiveData, 0,
                                nLenRead);
                        runReceiveDataCallback(btAryReceiveData);
                    }
                } catch (Exception e) {
                    LogUtils.e(e.toString());
                    onLostConnect();
                    return;
                }
            }
        }

        private void signOut() {
            mShouldRunning = false;
            interrupt();
        }
    }


    /**
     * Exit receive thread.
     */
    protected void signOut() {
        if (mWaitThread != null) {
            mWaitThread.signOut();
            mWaitThread = null;
        }

        try {
            if (mInStream != null) {
                mInStream.close();
                mInStream = null;
            }
            if (mOutStream != null) {
                mOutStream.close();
                mOutStream = null;
            }
        } catch (IOException e) {
            LogUtils.e(e.toString());
        }
    }

    /**
     * After reading the data from Instream, it will call this function.
     *
     * @param btAryReceiveData Received Data
     */
    private void runReceiveDataCallback(byte[] btAryReceiveData) {
        try {
            int nCount = btAryReceiveData.length;
            byte[] buffer = new byte[nCount + nLength];
            System.arraycopy(this.btAryBuffer, 0, buffer, 0, nLength);
            System.arraycopy(btAryReceiveData, 0, buffer, nLength,
                    btAryReceiveData.length);
            int nIndex = 0;
            int nMarkIndex = 0;
            for (int nLoop = 0; nLoop < buffer.length; nLoop++) {
                if (buffer.length > nLoop + 1) {
                    if (buffer[nLoop] == Command.HEAD) {
                        int nLen = buffer[nLoop + 1] & 0xFF;
                        if (nLoop + 1 + nLen < buffer.length) {
                            byte[] btAryAnaly = new byte[nLen + 2];
                            System.arraycopy(buffer, nLoop, btAryAnaly, 0, nLen + 2);
                            checkData(btAryAnaly);
                            nLoop += 1 + nLen;
                            nIndex = nLoop + 1;
                        } else {
                            nLoop += 1 + nLen;
                        }
                    } else {
                        LogUtils.e("出现标志位异常的情况：：");
                        nMarkIndex = nLoop;
                    }
                }
            }
            if (nIndex < nMarkIndex) {
                nIndex = nMarkIndex + 1;
            }
            if (nIndex < buffer.length) {
                nLength = buffer.length - nIndex;
                Arrays.fill(this.btAryBuffer, 0, 4096, (byte) 0);
                System.arraycopy(buffer, nIndex, this.btAryBuffer, 0,
                        buffer.length - nIndex);
            } else {
                nLength = 0;
            }
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
    }

    /**
     * 检验数据的合法性（根据协议规则）
     *
     * @param buf 一条条协议数据
     */
    private void checkData(byte[] buf) {
        //协议最小数据是5，如果数据小于5就直接退出，防止错误数据崩溃
        if (buf.length < 5) {
            LogUtils.e("数据出错，数据长度小于6：：" + TransferUtils.Byte2String(buf));
            return;
        }
        //检测数据的合法性
        int check = DataProtocol.checkSum(buf, 0, buf.length - 1);
        //如果数据不合法，直接丢掉数据包,判断上一条协议如果是扫描，就要重置设备
        if ((check & 0xff) != (0xff & buf[buf.length - 1])) {
            LogUtils.e("效验码验证未通过  数据为" + TransferUtils.Byte2String(buf) + "计算校验码为" + check);
            return;
        }
        //如果数据合法就交给实现层自己处理
        receiveData(buf);
    }


    /**
     * Send data,Use synchronized() to prevent concurrent operation.
     *
     * @param btArySenderData To send data
     */
    protected void sendCommand(final byte[] btArySenderData) {
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (mOutStream) {
                        mOutStream.write(btArySenderData);
                        mOutStream.flush();
                    }
                } catch (Exception e) {
                    LogUtils.e(e.toString());
                    onLostConnect();
                }
            }
        });
    }
}
