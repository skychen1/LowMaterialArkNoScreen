package com.rivamed.libdevicesbase.base;

import android.util.Log;

import com.rivamed.libdevicesbase.utils.LogUtils;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * describe ：设备建立服务的基类
 *
 * @author : Yich
 * data: 2019/2/21
 */
public class BaseService {
    private static final String LOG_TAG = "BaseService";
    private EventLoopGroup boss;
    private EventLoopGroup work;

    /**
     * 开启一个socket服务，
     *
     * @param port           端口号
     * @param  stateHandler  心跳处理规则（用来监测设备是不是已经断开）
     * @param channelHandler 通道处理数据
     * @param decoder        数据解析规则（如果传入null表示不添加粘包解析规则）
     * @return 是否开启成功
     */
    public boolean startService(int port, IdleStateHandler stateHandler,BaseNettyHandler channelHandler, LengthFieldBasedFrameDecoder decoder) {
        if (port <= 0) {
            LogUtils.e("服务启动失败：：:port=" + port);
            return false;
        }
        boolean isOpen;
        boss = new NioEventLoopGroup();
        work = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.TCP_NODELAY, true);
            b.childOption(ChannelOption.SO_KEEPALIVE, true);
            b.group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel channel) {
                            channel.config().setOption(ChannelOption.SO_KEEPALIVE, true);
                            Log.i(LOG_TAG, "接收到新的链接请求" + channel.remoteAddress());
                            //心跳规则不为null，就添加心跳规则
                            if(stateHandler!=null){
                                channel.pipeline().addLast(stateHandler);
                            }
                            //如果解析规则不为空，就添加解析规则
                            if (decoder != null) {
                                channel.pipeline().addLast(decoder);
                            }
                            channel.pipeline().addLast(channelHandler);
                        }
                    });
            ChannelFuture f = b.bind().sync();
            LogUtils.e("服务启动成功：：:port=" + port);
            isOpen = true;
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            LogUtils.e("服务启动异常：：:port=" + port + e.toString());
            isOpen = false;
        } finally {
            try {
                boss.shutdownGracefully().sync();
                work.shutdownGracefully().sync();
            } catch (Exception e) {
                LogUtils.e("服务启动异常关闭失败：：:port=" + port + e.toString());
            }
        }
        return isOpen;
    }


    public boolean stopService() {
        //两个处理池都不为空（理论上两个是共同存在的）
        //标识，如果正常关闭返回正常，如果关闭异常就返回fasle
        boolean isClose = true;
        if (boss != null && work != null) {
            try {
                boss.shutdownGracefully().sync();
                work.shutdownGracefully().sync();
            } catch (Exception e) {
                isClose = false;
                LogUtils.e("服务关闭失败：：:port=" + e.toString());
            }
        }
        return isClose;
    }
}
