package com.ruihua.libconsumables;

import com.rivamed.libdevicesbase.base.BaseNettyHandler;
import com.rivamed.libdevicesbase.utils.LogUtils;
import com.rivamed.libdevicesbase.utils.TransferUtils;

import java.util.Arrays;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledHeapByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * describe ： 所有公司内部硬件设备（控制板基类）
 *
 * @author : Yich
 * date: 2019/9/10
 */
public abstract class BaseConsumableHandler extends BaseNettyHandler {

    /**
     * btAryBuffer 按照协议解析完剩余的数据
     * nLength 剩余的数据长度
     */
    private byte[] btAryBuffer = new byte[5102];
    private int nLength = 0;
    private int continueIdleCount = 0;


    /**
     * 断开连接了
     */
    abstract void onLostConnect();

    /**
     * 收到标准的协议数据
     *
     * @param buf 协议数据
     */
    abstract void processData(byte[] buf);


    @Override
    public void receiveData(byte[] buf) {
        continueIdleCount = 0;
        byte[] buffer = new byte[buf.length + nLength];
        System.arraycopy(this.btAryBuffer, 0, buffer, 0, nLength);
        System.arraycopy(buf, 0, buffer, nLength, buf.length);
//        LogUtils.e("需要解析的数据：：:::"+TransferUtils.Byte2String(buffer));
        int nIndex = 0;
        int nMarkIndex = 0;
        for (int nLoop = 0; nLoop < buffer.length; nLoop++) {
            if (buffer.length > nLoop + 9) {
                //找到头标识
                if (buffer[nLoop] == ControlBoardProtocol.HEAD_1 && buffer[nLoop + 1] == ControlBoardProtocol.HEAD_2) {
                    int length1 = (buffer[nLoop + 8] & 0xFF) * 256;
                    int length2 = buffer[nLoop + 9] & 0xFF;
                    int nLen = length1 + length2;
                    if (nLoop + 11 + nLen < buffer.length) {
                        byte[] btAryAnaly = new byte[nLen + 12];
                        System.arraycopy(buffer, nLoop, btAryAnaly, 0, nLen + 12);
                        checkData(btAryAnaly);
                        //记录剩余的标志位数据的下一位
                        nIndex = nLoop + 12 + nLen;
                    }
                    //推动循环标志位
                    nLoop += 11 + nLen;
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
            //清空原有数据
            Arrays.fill(this.btAryBuffer, 0, 4096, (byte) 0);
            //保存剩余的数据
            System.arraycopy(buffer, nIndex, this.btAryBuffer, 0, buffer.length - nIndex);
        } else {
            nLength = 0;
        }
    }

    /**
     * 检验数据的合法性（根据协议规则）
     *
     * @param buf 一条条协议数据
     */
    private void checkData(byte[] buf) {
        //协议最小数据是5，如果数据小于5就直接退出，防止错误数据崩溃
        if (buf.length < 12) {
            LogUtils.e("数据出错，数据长度小于12：：" + TransferUtils.Byte2String(buf));
            return;
        }
        byte[] data = new byte[buf.length - 2];
        System.arraycopy(buf, 0, data, 0, data.length);
        byte[] crc = ControlBoardProtocol.getCrc(data);
        //如果数据不合法，直接丢掉数据包,判断上一条协议如果是扫描，就要重置设备
        if (crc[0] != buf[buf.length - 2] || crc[1] != buf[buf.length - 1]) {
            LogUtils.e("效验码验证未通过!!!!数据为:::" + TransferUtils.Byte2String(buf) + "计算校验码为::" + TransferUtils.Byte2String(crc));
            return;
        }
        //如果数据合法就交给实现层自己处理
        processData(buf);
    }


    /**
     * 有设备连接
     *
     * @param ctx 通道操作管理
     * @throws Exception 异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        mChCtx = ctx;
        super.channelActive(ctx);
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
                    //连续发送3次，收不到回包就断开
                    continueIdleCount++;
                    if (continueIdleCount > 3) {
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
     * 发送心跳包(用获取当前天线包作为心跳包)
     */
    private void sendHartPacket() {
        byte[] bytes = ControlBoardProtocol.pieceCommand(ControlBoardProtocol.HART_BEAT, null);
        sendData(bytes);
    }


    /**
     * 设备断开
     *
     * @param ctx 通道操作管理
     * @throws Exception 异常
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        onLostConnect();
        super.channelInactive(ctx);
    }

    /**
     * 发送数据
     *
     * @param data 数据
     * @return 是否成功
     */
    public boolean sendData(byte[] data) {
        try {
//            LogUtils.e("发送消息了：：：" + TransferUtils.Byte2String(data));
            ByteBuf byteBuf = new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT, data.length, data.length);
            //复制数据
            byteBuf.writeBytes(data);
            //真正的发送数据
            mChCtx.writeAndFlush(byteBuf);
            return true;
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        return false;
    }


}
