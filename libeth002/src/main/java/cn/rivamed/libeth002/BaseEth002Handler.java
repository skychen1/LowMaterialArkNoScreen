package cn.rivamed.libeth002;

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

public abstract class BaseEth002Handler extends BaseNettyHandler {

    /**
     * mBtAryBuffer 上一条数据解析完，剩余的数据 mNLength剩余数据的长度
     * continueIdleCount 当前连续发送心跳未手打回复的次数
     */
    private byte[] mBtAryBuffer = new byte[4096];
    private int mNLength = 0;


    /**
     * 数据解析（根据协议规则截取数据）
     * 丢弃不完整（丢包）协议的数据（防止丢包的错误数据引起解析错误，导致流程中断）
     *
     * @param buf 字节数组
     */
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
                //协议的数据至少为1（这部分代码是厂家demo的，理论应该不止1）
                if (btAryBuffer.length > nLoop + 1) {
                    //找到协议的标识头
                    if (btAryBuffer[nLoop] == DataProtocol.BEGIN_FLAG) {
                        //根据协议，数据的长度在标识头的后一位数据，拿到数据长度
                        int nLen = btAryBuffer[nLoop + 1] * 256 + btAryBuffer[nLoop + 2];
                        LogUtils.e("数据长度为" + nLen);
                        //如果数据长度足够这个协议数据，就截取这这个协议数据
                        if (nLoop + 1 + nLen < btAryBuffer.length) {
                            byte[] btAryAnaly = new byte[nLen + 3];
                            System.arraycopy(btAryBuffer, nLoop, btAryAnaly, 0, nLen + 3);
                            //拿到协议数据先检测协议数据的正确性
                            checkData(btAryAnaly);
                            nLoop += 2 + nLen;
                            nIndex = nLoop + 2;
                        } else {
                            nLoop += 2 + nLen;
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

    private void checkData(byte[] btAryAnaly) {
        String s = TransferUtils.Byte2String(btAryAnaly);
        if (btAryAnaly[0] != DataProtocol.BEGIN_FLAG || (btAryAnaly[btAryAnaly.length - 1] & 0xff) != DataProtocol.checkSum(btAryAnaly, 0, btAryAnaly.length - 1)) {
            LogUtils.e("对客户端" + getIdentification() + "的消息校验不通过，消息体=" + s + "\r 将强制断开设备");
            return;
        }
        processData(btAryAnaly);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LogUtils.e("channelActive 事件发生");
        mChCtx = ctx;
        ThreadFactory namedThreadFactory = new BasicThreadFactory.Builder().namingPattern("eth002模块线程").daemon(true).build();
        ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(1, namedThreadFactory);
        scheduled.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    LogUtils.e("设备连接了，发送获取设备id命令");
                    sendGetId();
                } catch (Exception e) {
                    LogUtils.e(e.toString());
                }
            }
        }, 500, TimeUnit.MILLISECONDS);
        super.channelActive(ctx);
    }

    protected void sendGetId() {
        byte[] buf = new byte[]{0x00, 0x68};
        sendBuf(buf);
    }

    protected synchronized boolean sendBuf(byte[] buf) {
        try {
            byte[] bufSend = DataProtocol.pieceCommond(buf);
            ByteBuf byteBuf = new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT, bufSend.length, bufSend.length);
            byteBuf.writeBytes(bufSend);
            mChCtx.writeAndFlush(byteBuf);
            LogUtils.e("向客户端" + getIdentification() + "发送命令 " + TransferUtils.Byte2String(bufSend));
            return true;
        } catch (Exception ex) {
            LogUtils.e(ex.toString());
        }
        return false;
    }

    /**
     * 抽象方法 具体怎么处理收到的数据
     *
     * @param buf 一条条完整的协议数据
     */
    public abstract void processData(byte[] buf);
}
