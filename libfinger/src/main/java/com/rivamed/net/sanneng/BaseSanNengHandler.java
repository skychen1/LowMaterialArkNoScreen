package com.rivamed.net.sanneng;

import com.rivamed.libdevicesbase.base.BaseNettyHandler;
import com.rivamed.libdevicesbase.utils.LogUtils;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledHeapByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * 指纹仪通道数据处理基类（主要处理收发数据）
 * create by 孙朝阳 on 2019-3-7
 */
public abstract class BaseSanNengHandler extends BaseNettyHandler {

    protected short currentCMD = 0;

    /**
     * 数据解析（根据协议规则截取数据）
     * 丢弃不完整（丢包）协议的数据（防止丢包的错误数据引起解析错误，导致流程中断）
     *
     * @param buf 字节数组
     */
    @Override
    public void receiveData(byte[] buf) {
        processData(buf);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        mChCtx = ctx;
        ThreadFactory namedThreadFactory = new BasicThreadFactory.Builder().namingPattern("三能指纹仪注册线程").daemon(true).build();
        ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(1, namedThreadFactory);
        scheduled.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    LogUtils.e("设备连接了，发送获取设备id命令");
                    sendGetId();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 500, TimeUnit.MILLISECONDS);
        super.channelActive(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        LogUtils.e("channelRegistered事件发生.....");
        super.channelRegistered(ctx);
    }

    /**
     * @return byte[]
     * @Author Eamonn
     * @Description 获取设备ID, 三能指纹仪
     * @Date 2018/12/20 9:04
     * @Param []
     **/
    protected boolean sendGetId() throws Exception {
        byte[] bytes = new byte[26];
        bytes[0] = (byte) 0x55;
        bytes[1] = (byte) 0xAA;
        bytes[2] = (byte) 0x01;
        bytes[3] = (byte) 0x01;
        bytes[4] = (byte) 0x03;
        bytes[5] = (byte) 0x00;
        bytes[6] = (byte) 0x01;
        bytes[7] = (byte) 0x00;
        bytes[8] = (byte) 0x00;
        bytes[9] = (byte) 0x00;
        bytes[10] = (byte) 0x00;
        bytes[11] = (byte) 0x00;
        bytes[12] = (byte) 0x00;
        bytes[13] = (byte) 0x00;
        bytes[14] = (byte) 0x00;
        bytes[15] = (byte) 0x00;
        bytes[16] = (byte) 0x00;
        bytes[17] = (byte) 0x00;
        bytes[18] = (byte) 0x00;
        bytes[19] = (byte) 0x00;
        bytes[20] = (byte) 0x00;
        bytes[21] = (byte) 0x00;
        bytes[22] = (byte) 0x00;
        bytes[23] = (byte) 0x00;
        bytes[24] = (byte) 0x05;
        bytes[25] = (byte) 0x01;
        return sendBuf(DataProtocol.CMD_GET_PARAM, bytes);
    }

    /**
     * @return boolean
     * @Description 发送命令
     * @Param cmd关键命令字，[buf]发送的命令
     **/
    protected synchronized boolean sendBuf(short cmd, byte[] buf) {
        try {
            ByteBuf byteBuf = new UnpooledHeapByteBuf(ByteBufAllocator.DEFAULT, buf.length, buf.length);
            byteBuf.writeBytes(buf);
            currentCMD = cmd;
            mChCtx.writeAndFlush(byteBuf);
            return true;
        } catch (Exception ex) {
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
