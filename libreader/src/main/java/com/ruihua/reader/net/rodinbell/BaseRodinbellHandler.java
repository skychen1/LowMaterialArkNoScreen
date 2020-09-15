package com.ruihua.reader.net.rodinbell;

import com.rivamed.libdevicesbase.base.BaseNettyHandler;
import com.rivamed.libdevicesbase.utils.LogUtils;
import com.rivamed.libdevicesbase.utils.ThreadPoolProxyFactory;
import com.rivamed.libdevicesbase.utils.TransferUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledHeapByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * describe ：罗丹贝尔通道数据处理基类（主要处理发出数据，处理发包的懂）
 *
 * @author : Yich
 * date: 2019/2/26
 */
public abstract class BaseRodinbellHandler extends BaseNettyHandler {

    /**
     * mBtAryBuffer 上一条数据解析完，剩余的数据 mNLength剩余数据的长度
     * continueIdleCount 当前连续发送心跳未手打回复的次数
     */
    private byte[] mBtAryBuffer = new byte[4096];
    private int mNLength = 0;
    private int continueIdleCount = 0;


    /**
     * 数据解析（根据协议规则截取数据）
     * 丢弃掉不完整（丢包）协议的数据（防止丢包的错误数据引起解析错误，导致流程中断）
     *
     * @param buf 字节数组
     */
    @Override
    public void receiveData(byte[] buf) {
        continueIdleCount = 0;
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
                    if (btAryBuffer[nLoop] == DataProtocol.BEGIN_FLAG) {
                        //根据协议，数据的长度在标识头的后一位数据，拿到数据长度
                        int nLen = btAryBuffer[nLoop + 1] & 0xFF;
                        //根据协议，数据的长度不可能超过39，所以屏蔽掉数据中有标识头，误以为是标识头的情况。
                        if (nLen > 39) {
                            continue;
                        }
                        //如果数据长度足够这个协议数据，就截取这这个协议数据
                        if (nLoop + 1 + nLen < btAryBuffer.length) {
                            byte[] btAryAnaly = new byte[nLen + 2];
                            System.arraycopy(btAryBuffer, nLoop, btAryAnaly, 0, nLen + 2);
                            //拿到协议数据先检测协议数据的正确性
                            checkData(btAryAnaly);
                            nLoop += 1 + nLen;
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
        processData(buf);
    }

    /**
     * 心跳机制方法
     *
     * @param ctx 通道
     * @param evt 时间
     * @throws Exception 异常
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent stateEvent = (IdleStateEvent) evt;
            switch (stateEvent.state()) {
                case READER_IDLE:
                case ALL_IDLE:
                case WRITER_IDLE:
                    //连续发送9次，收不到回包就断开
                    continueIdleCount++;
                    if (continueIdleCount > DataProtocol.MAX_HART_TIME) {
                        close();
                        return;
                    }
                    //发送获取天线的包作为心跳包？
                    sendHartPacket();
                    break;
                default:
                    break;
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    /**
     * 通道连接建立的时候
     *
     * @param ctx 通道
     * @throws Exception 异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        mChCtx = ctx;
        ThreadPoolProxyFactory.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    LogUtils.e(e.toString());
                }
                sendGetDeviceId();
            }
        });
        super.channelActive(ctx);
    }


    /**
     * 发送获取设备id的方法
     */
    public void sendGetDeviceId() {
        sendPacket(DataProtocol.CMD_GET_DEVICE_ID, null);
    }

    /**
     * 发送心跳包(用获取当前天线包作为心跳包)
     */
    private void sendHartPacket() {
        byte[] data = new byte[]{};
        sendPacket(DataProtocol.CMD_GET_WORK_ANT, data);
    }


    /**
     * 发送协议的
     *
     * @param cmd 协议的指令（由协议决定）
     * @param buf 类容
     * @return 发送指令是否成功
     */
    protected boolean sendPacket(byte cmd, byte[] buf) {
        try {
            //按照协议拼装数据，（地址为默认值，指令，和）
            byte[] bufSend = DataProtocol.pieceCommond(DataProtocol.ADDRESS, cmd, buf);
            //按照netty的数据方式组装数据
            ByteBuf byteBuf = new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT, bufSend.length, bufSend.length);
            //复制数据
            byteBuf.writeBytes(bufSend);
            //真正的发送数据
            mChCtx.write(byteBuf);
            //flush缓冲区
            mChCtx.flush();
//            LogUtils.e("罗丹贝尔发送数据了：："+TransferUtils.Byte2String(bufSend));
            return true;
        } catch (Exception e) {
            //nothing
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
