package com.rivamed.libdevicesbase.base;

import android.util.Log;

import com.rivamed.libdevicesbase.utils.LogUtils;
import com.rivamed.libdevicesbase.utils.TransferUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.StringUtil;

/**
 * describe ：netty 通道基础类
 * 1，收到的数据转换成字节数组
 * 2，通道加上标识（到时候发数据到制定的设备）
 *
 * @author : Yich
 * data: 2019/2/21
 */
@ChannelHandler.Sharable
public abstract class BaseNettyHandler extends SimpleChannelInboundHandler {

    private static final String LOG_TAG = "BaseNettyHandler";
    /**
     * 通道的唯一标识（用于管理设备，一个设备一个标识）
     * mChCtx 通道的管理
     */
    private String identification;
    protected ChannelHandlerContext mChCtx;

    public String getIdentification() {
        return identification;
    }

    protected void setIdentification(String identification) {
        this.identification = identification;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof ByteBuf)) {
            return;
        }
        //收到的所有数据都为ByteBuf类型，转换数据
        ByteBuf in = (ByteBuf) msg;
        if (in.isReadable()) {
            byte[] buf = new byte[in.readableBytes()];
            in.readBytes(buf);
            //转换数据为byte【】
            LogUtils.e("接收到消息" + TransferUtils.Byte2String(buf));
            //交给子类自己去解析数据
            receiveData(buf);
        }
        //以下代码是必须的，释放管道数据，防止内存泄露；
        in.retain();
        ctx.write(in);
    }

    //    /**
    //     * 接收到数据
    //     *
    //     * @param ctx 通道
    //     * @param msg 数据
    //     * @throws Exception 异常
    //     */
    //    @Override
    //    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    //        if (!(msg instanceof ByteBuf)) {
    //            return;
    //        }
    //        //收到的所有数据都为ByteBuf类型，转换数据
    //        ByteBuf in = (ByteBuf) msg;
    //        if (in.isReadable()) {
    //            byte[] buf = new byte[in.readableBytes()];
    //            in.readBytes(buf);
    //            //转换数据为byte【】
    //            LogUtils.e("接收到消息" + TransferUtils.Byte2String(buf));
    //            //交给子类自己去解析数据
    //            receiveData(buf);
    //        }
    //        //以下代码是必须的，释放管道数据，防止内存泄露；
    //        in.retain();
    //        ctx.write(in);
    //        super.channelRead(ctx, msg);
    //    }

    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
        LogUtils.e(cause.toString());
        ctx.close();
    }

    /**
     * 获取通道的ip地址
     *
     * @return 通道的ip
     */
    public String getIP() {
        String address = mChCtx == null ? "" : mChCtx.pipeline().channel().remoteAddress().toString();
        if (!StringUtil.isNullOrEmpty(address)) {
            address = address.replace("/", "");
            address = address.substring(0, address.indexOf(":"));
        }
        return address;
    }

    /**
     * 关闭通道，断开连接
     */
    public boolean close() {
        try {
            mChCtx.close().sync();
            mChCtx.channel().close().sync();
            mChCtx.channel().closeFuture().sync();
            mChCtx.channel().pipeline().close().sync();
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.toString());
            return false;
        }
        return true;
    }

    /**
     * 接收到数据（解析数据，需要自己根据自己的协议去解析）
     *
     * @param buf 字节数组
     */
    public abstract void receiveData(byte[] buf);


}
