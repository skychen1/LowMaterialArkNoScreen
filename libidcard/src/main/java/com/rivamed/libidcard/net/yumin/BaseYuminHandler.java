package com.rivamed.libidcard.net.yumin;

import com.rivamed.libdevicesbase.base.BaseNettyHandler;
import com.rivamed.libdevicesbase.utils.LogUtils;
import com.rivamed.libdevicesbase.utils.TransferUtils;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledHeapByteBuf;
import io.netty.channel.ChannelHandlerContext;

public abstract class BaseYuminHandler extends BaseNettyHandler {

    /**
     * mBtAryBuffer  上一条数据解析剩下的数据
     * mNLength 剩下的长度
     */
    private byte[] mBtAryBuffer = new byte[128];
    private int mNLength = 0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        mChCtx = ctx;
        ThreadFactory namedThreadFactory = new BasicThreadFactory.Builder().namingPattern("idCard-schedule-pool-%d").daemon(true).build();
        ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(1, namedThreadFactory);
        scheduled.schedule(new Runnable() {
            @Override
            public void run() {
                LogUtils.e("发送获取id了");
                sendGetDeviceId();
            }
        }, 500, TimeUnit.MILLISECONDS);

        super.channelActive(ctx);
    }


    @Override
    public void receiveData(byte[] buf) {
        try {
            //拼接上一条数据包剩余的数据
            byte[] btAryBuffer = new byte[buf.length + mNLength];
            System.arraycopy(mBtAryBuffer, 0, btAryBuffer, 0, mNLength);
            System.arraycopy(buf, 0, btAryBuffer, mNLength, buf.length);
            LogUtils.e("需要解析的数据" + TransferUtils.Byte2String(btAryBuffer));
            int nIndex = 0;
            int nMarkIndex = 0;
            //循环找截取数据为一个个协议数据
            for (int nLoop = 0; nLoop < btAryBuffer.length; nLoop++) {
                //协议的数据至少为5（这部分代码是厂家demo的，理论应该不止5）
                if (btAryBuffer.length > nLoop + 5) {
                    //找到协议的标识头
                    if (btAryBuffer[nLoop] == DataProtocol.BEGIN_FLAG_ID) {
                        //根据协议，数据的长度在标识头的后一位数据，拿到数据长度
                        int nLen = btAryBuffer[nLoop + 1] & 0xFF;
                        if (nLoop + nLen <= btAryBuffer.length) {
                            byte[] bufId = new byte[nLen];
                            System.arraycopy(btAryBuffer, nLoop, bufId, 0, nLen);
                            //拿到协议数据先检测协议数据的正确性
                            checkData(bufId);
                            nLoop += nLen - 1;
                            nIndex = nLoop + 1;
                        } else {
                            nLoop += 1 + nLen;
                        }
                    } else if (btAryBuffer[nLoop] == DataProtocol.BEGIN_FLAG_NUM) {
                        //根据协议，数据的长度在标识头的后一位数据，拿到数据长度
                        int nLen = btAryBuffer[nLoop + 1] & 0xFF;
                        //如果数据长度足够这个协议数据，就截取这这个协议数据
                        if (nLoop + nLen <= btAryBuffer.length) {
                            byte[] btAryAnaly = new byte[nLen];
                            System.arraycopy(btAryBuffer, nLoop, btAryAnaly, 0, nLen);
                            //拿到协议数据先检测协议数据的正确性
                            checkData(btAryAnaly);
                            nLoop += nLen - 1;
                            nIndex = nLoop + 1;
                        } else {
                            nLoop += 1 + nLen;
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
     * 检验设备ID的合法性（根据协议规则）
     *
     * @param buf 一条条协议数据
     */
    private void checkData(byte[] buf) {
        //根据校验位校验数据
        byte b = DataProtocol.checkSum(buf, buf.length - 1);
        if (buf[buf.length - 1] != b) {
            LogUtils.e("数据校验出错：" + TransferUtils.Byte2String(buf));
            return;
        }
        processData(buf);
    }


    protected void sendGetDeviceId() {
        byte[] buf = new byte[7];
        buf[0] = (byte) 0x02;
        buf[1] = (byte) 0x08;
        buf[2] = (byte) 0xF9;
        buf[3] = (byte) 0x20;
        buf[4] = (byte) 0x00;
        buf[5] = (byte) 0x00;
        buf[6] = (byte) 0x00;
        byte b = DataProtocol.checkSum(buf, buf.length);
        byte[] send = new byte[buf.length + 1];
        System.arraycopy(buf, 0, send, 0, buf.length);
        send[send.length - 1] = b;
        sendBuf(send);
    }

    private synchronized void sendBuf(byte[] buf) {
        try {
            ByteBuf byteBuf = new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT, buf.length, buf.length);
            byteBuf.writeBytes(buf);
            mChCtx.writeAndFlush(byteBuf);

        } catch (Exception ex) {
            LogUtils.e(ex.toString());
        }
    }

    /**
     * 抽象方法 具体怎么处理收到的数据
     *
     * @param buf 一条条完整的协议数据
     */
    public abstract void processData(byte[] buf);



}
