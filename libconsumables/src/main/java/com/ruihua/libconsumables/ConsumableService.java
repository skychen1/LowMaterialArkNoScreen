package com.ruihua.libconsumables;

import androidx.annotation.NonNull;

import com.rivamed.libdevicesbase.utils.LogUtils;
import com.rivamed.libdevicesbase.utils.ThreadPoolProxyFactory;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * describe ： 控制板统一服务开启类
 * 不能使用netty的解析规则，需要自己手动解析，因为可能存在丢包的情况，如果丢包就会引起数据解析混乱
 *
 * @author : Yich
 * date: 2019/2/22
 */
public class ConsumableService {
    private NioEventLoopGroup boss;
    private NioEventLoopGroup work;
    private ConsumableManager manager;

    /**
     * 构造方法，需要传入管理类， 要数据回调
     *
     * @param manager 管理类
     */
    public ConsumableService(@NonNull ConsumableManager manager) {
        this.manager = manager;
    }

    /**
     * 开启一个socket服务，
     *
     * @param port 端口号
     */
    public void startService(int port) {
        if (port <= 0) {
            LogUtils.e("服务启动失败：：:port=" + port);
            return;
        }
        ThreadPoolProxyFactory.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                start(port);
            }
        });
    }

    /**
     * 启动服务
     * * @param port 端口号
     */
    private void start(int port) {
        boss = new NioEventLoopGroup();
        work = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.childOption(ChannelOption.TCP_NODELAY, true);
            b.childOption(ChannelOption.SO_KEEPALIVE, true);
            b.option(ChannelOption.SO_BACKLOG, 1024);
            b.group(boss, work)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel channel) {
                            channel.config().setOption(ChannelOption.SO_KEEPALIVE, true);
                            LogUtils.e("接收到新的链接请求" + channel.remoteAddress());
                            channel.config().setOption(ChannelOption.SO_RCVBUF, 10 * 1024);
                            //添加心跳规则
                            String ip = channel.remoteAddress().toString();
                            channel.pipeline().addLast(new IdleStateHandler(0, 0, 6));
                            ConsumableHandler consumableHandler = new ConsumableHandler(manager);
                            channel.pipeline().addLast(ip, consumableHandler);
                        }
                    });
            ChannelFuture f = b.bind().sync();
            LogUtils.e("服务启动成功：：:port=" + port);
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            LogUtils.e("服务启动异常：：:port=" + port + e.toString());
        } finally {
            try {
                boss.shutdownGracefully().sync();
                work.shutdownGracefully().sync();
            } catch (Exception e) {
                LogUtils.e("服务启动异常关闭失败：：:port=" + port + e.toString());
            }
        }
    }

    /**
     * 关闭服务方法
     */
    public void stopService() {
        ThreadPoolProxyFactory.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                //两个处理池都不为空（理论上两个是共同存在的）F
                //标识，如果正常关闭返回正常，如果关闭异常就返回fasle
                if (boss != null && work != null) {
                    try {
                        boss.shutdownGracefully().sync();
                        work.shutdownGracefully().sync();
                        boss = null;
                        work = null;
                        manager = null;
                    } catch (Exception e) {
                        LogUtils.e("服务关闭失败：：:port=" + e.toString());
                    }
                }
            }
        });
    }
}
