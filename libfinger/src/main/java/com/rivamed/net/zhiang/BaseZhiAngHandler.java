package com.rivamed.net.zhiang;

import com.rivamed.libdevicesbase.base.BaseNettyHandler;
import com.rivamed.libdevicesbase.utils.LogUtils;
import com.rivamed.libdevicesbase.utils.ThreadPoolProxyFactory;
import com.rivamed.libdevicesbase.utils.TransferUtils;
import com.rivamed.net.callback.FingerMessageListener;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.Arrays;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledHeapByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * describe ： 指昂指纹仪连接通道基类。收到数据的基本处理，
 *
 * @author : Yich
 * date: 2019/10/14
 */
public abstract class BaseZhiAngHandler extends BaseNettyHandler {
    /**
     * btAryBuffer 按照协议解析完剩余的数据
     * nLength 剩余的数据长度
     * mListener 回调监听
     * mLastSendCmd 记录上一次发送的命令,用以记录收到的数据是什么数据
     */
    private byte[] btAryBuffer = new byte[4096];
    private int nLength = 0;
    protected FingerMessageListener mListener;
    private int continueIdleCount = 0;
    protected byte mLastSendCmd;
    protected String mId;


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
        //只要收到数据就认为连接正常，将心跳重复次数置为0
        continueIdleCount = 0;
        byte[] buffer = new byte[buf.length + nLength];
        //拼接上次剩余的数据
        System.arraycopy(this.btAryBuffer, 0, buffer, 0, nLength);
        System.arraycopy(buf, 0, buffer, nLength, buf.length);
        int nIndex = 0;
        int nMarkIndex = 0;
        int nLoop = 0;
        //标志位小于数据长度，
        while (nLoop < buffer.length) {
            //保证能拿到长度位
            if (buffer.length > nLoop + 8) {
                //找到头标识
                if (buffer[nLoop] == ZhiAngProtocol.HEAD[0] && buffer[nLoop + 1] == ZhiAngProtocol.HEAD[1]) {
                    //拿到数据长度
                    int length1 = (buffer[nLoop + 7] & 0xFF) * 256;
                    int length2 = buffer[nLoop + 8] & 0xFF;
                    int nLen = length1 + length2;
                    //计算是否够一条完整的协议，够，就截取协议
                    if (nLoop + 9 + nLen <= buffer.length) {
                        byte[] btAryAnaly = new byte[nLen + 9];
                        System.arraycopy(buffer, nLoop, btAryAnaly, 0, nLen + 9);
                        checkData(btAryAnaly);
                        //记录剩余的标志位数据的下一位（用以查看是否还有剩余数据）
                        nIndex = nLoop + 9 + nLen;
                    }
                    //推动循环标志位
                    nLoop += 9 + nLen;
                } else {
                    LogUtils.e("出现标志位异常的情况：：");
                    nLoop++;
                    nMarkIndex = nLoop;
                }
            } else {
                //如果数据连长度都拿不到了，就直接退出循环了
                break;
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
        if (buf.length < 11) {
            LogUtils.e("数据出错，数据长度小于11：：" + TransferUtils.Byte2String(buf));
            return;
        }
        byte[] data = new byte[buf.length - 2];
        System.arraycopy(buf, 0, data, 0, data.length);
        //计算校验位
        byte[] crc = ZhiAngProtocol.checkSum(data, 6, data.length - 6);
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
        //通道连接了就延时500ms获取设备的id
        ThreadPoolProxyFactory.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    LogUtils.e(e.toString());
                }
                sendGetDeviceId();
                sendGetDeviceId();
            }
        });
        super.channelActive(ctx);
    }

    /**
     * 发送获取设备id的指令（获取设备参数指令）
     */
    private void sendGetDeviceId() {
        byte[] bytes = ZhiAngProtocol.pieceCommand(ZhiAngProtocol.BAG_TAG_CMD, new byte[]{ZhiAngProtocol.CMD_GET_PARAM});
        sendData(bytes);
        mLastSendCmd = ZhiAngProtocol.CMD_GET_PARAM;
    }


    /**
     * 设备断开
     *
     * @param ctx 通道操作管理
     * @throws Exception 异常
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        btAryBuffer = null;
        onLostConnect();
        super.channelInactive(ctx);
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
        byte[] bytes = ZhiAngProtocol.pieceCommand(ZhiAngProtocol.BAG_TAG_CMD, new byte[]{ZhiAngProtocol.CMD_GET_TEMP_NUM});
        sendData(bytes);
        mLastSendCmd = ZhiAngProtocol.CMD_GET_TEMP_NUM;
    }

    /**
     * 发送数据
     *
     * @param data 数据
     * @return 是否成功
     */
    public boolean sendData(byte[] data) {
        try {
            ByteBuf byteBuf = new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT, data.length, data.length);
            //复制数据
            byteBuf.writeBytes(data);
            //真正的发送数据
            mChCtx.write(byteBuf);
            //flush缓冲区
            mChCtx.flush();
            return true;
        } catch (Exception e) {
            LogUtils.e(e.toString());
        }
        return false;
    }


    /**
     * 注册回调监听
     *
     * @param listener 回调接口
     */
    public void registerListener(FingerMessageListener listener) {
        this.mListener = listener;
    }

}
