package com.ruihua.reader.net.clou;

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

/**
 * 鸿陆的通道数据处理基类（主要处理发出数据，处理发包）
 */
public abstract class BaseClouHandler extends BaseNettyHandler {

    /**
     * mBtAryBuffer 上一条数据解析完，剩余的数据
     * mNLength剩余数据的长度
     */
    private byte[] m_btAryBuffer = new byte[4096];
    private int m_nLength = 0;


    @Override
    public void receiveData(byte[] buf) {

        try {
            int nCount = buf.length;
            byte[] btAryBuffer = new byte[nCount + m_nLength];
            System.arraycopy(m_btAryBuffer, 0, btAryBuffer, 0, m_nLength);
            System.arraycopy(buf, 0, btAryBuffer, m_nLength,
                    buf.length);
            int nIndex = 0;
            int nMarkIndex = 0;
            //遍历截取标准的协议数据
            for (int nLoop = 0; nLoop < btAryBuffer.length; nLoop++) {
                //数据的长度的位置在第4位和第5位（两位）
                if (btAryBuffer.length > nLoop + 4) {
                    //找到标识头
                    if (btAryBuffer[nLoop] == DataProtocol.HEAD_BEGIN) {
                        //拿到第三和第四位的数据长度位，解析成长度
                        int leng1 = (btAryBuffer[nLoop + 3] & 0xFF) * 256;
                        int leng2 = btAryBuffer[nLoop + 4] & 0xFF;
                        int nLen = leng1 + leng2;
                        //根据协议知道，数据的长度不可能超过38，炒股狗38，说明是错误数据
                        if (nLen > 38) {
                            continue;
                        }
                        if (nLoop + 6 + nLen < btAryBuffer.length) {
                            byte[] btAryAnaly = new byte[nLen + 7];
                            System.arraycopy(btAryBuffer, nLoop, btAryAnaly, 0,
                                    nLen + 7);
                            processData(btAryAnaly);
                            nLoop += 6 + nLen;
                            //标识剩余的位置
                            nIndex = nLoop + 1;
                        } else {
                            nLoop += 6 + nLen;
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
            if (nIndex < btAryBuffer.length) {
                m_nLength = btAryBuffer.length - nIndex;
                m_btAryBuffer = new byte[btAryBuffer.length - nIndex];
                System.arraycopy(btAryBuffer, nIndex, m_btAryBuffer, 0,
                        btAryBuffer.length - nIndex);
                LogUtils.e("解析完一个包剩余的数据：：" + TransferUtils.Byte2String(m_btAryBuffer));
            } else {
                m_nLength = 0;
            }
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        mChCtx = ctx;
        ThreadFactory namedThreadFactory = new BasicThreadFactory.Builder().namingPattern("registerNative-schedule-pool-%d").daemon(true).build();
        ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(1, namedThreadFactory);

        scheduled.schedule(new Runnable() {
            @Override
            public void run() {
                sendQueryMac();
            }
        }, 500, TimeUnit.MILLISECONDS);

        /*scheduled.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                sendQueryMac();
            }
        },500,TimeUnit.MILLISECONDS);*/
        super.channelActive(ctx);
    }

    public void sendQueryMac() {
        byte msgType = DataProtocol.MSG_TYPE_READER_OPTION;
        byte mid = DataProtocol.MID_READER_OPTION_QUERY_MAC;
        sendBuf(msgType, mid, null);
    }

    public void sendQueryAnt() {
        byte msgtype = DataProtocol.MSG_TYPE_RFID_OPERATION;
        byte mid = DataProtocol.MID_RFID_QUERY_ANT;
        sendBuf(msgtype, mid, null);
    }

    protected  void sendRfidQueryInfo() {
        byte mstType = DataProtocol.MSG_TYPE_RFID_OPERATION;
        byte mid = DataProtocol.MID_RFID_QUERY_RFIDINFO;
        sendBuf(mstType, mid, null);
    }

    protected synchronized boolean sendBuf(byte msgType, byte mid, byte[] data) {
        byte[] buf = new byte[(data == null ? 0 : data.length) + 7];
        buf[0] = DataProtocol.HEAD_BEGIN;
        buf[1] = 0;
        {
            //485 和 上传标识 默认为0；
            buf[1] |= msgType;
            //    buf[1] |= 0x10;
        }
        buf[2] = mid;
        if (data == null) {
            buf[3] = 0x00;
            buf[4] = 0x00;
        } else {
            byte[] buflen = DataProtocol.ReverseIntToU16Bytes(data.length);
            System.arraycopy(buflen, 0, buf, 3, buflen.length);
            System.arraycopy(data, 0, buf, 5, data.length);
        }
        try {
            byte[] checkBuf = new byte[buf.length - 3];
            System.arraycopy(buf, 1, checkBuf, 0, checkBuf.length);
            byte[] crc = DataProtocol.CalcCRC16(checkBuf, checkBuf.length);
            System.arraycopy(crc, 0, buf, buf.length - 2, crc.length);
            ByteBuf byteBuf = new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT, buf.length, buf.length);
            byteBuf.writeBytes(buf);
            mChCtx.writeAndFlush(byteBuf);
            LogUtils.e("向客户端发送信息成功:" + TransferUtils.Byte2String(buf));
            return true;
        } catch (Exception e) {
            //nothing
        }
        return false;
    }

    public abstract void processData(byte[] buf);
}
