package com.rivamed.libidcard.net;

import androidx.annotation.NonNull;

import com.rivamed.libdevicesbase.utils.LogUtils;
import com.rivamed.libdevicesbase.utils.ThreadPoolProxyFactory;
import com.rivamed.libidcard.IdCardProducerType;
import com.rivamed.libidcard.net.ande.AnDeHandler;
import com.rivamed.libidcard.net.callback.IDCardHandler;
import com.rivamed.libidcard.net.callback.IdCardMessageListener;
import com.rivamed.libidcard.net.yumin.YuminHandler;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class IdCardService {

    /**
     * 创建两个NioEventLoopGroup，用于逻辑隔离NIO Acceptor和NIO I/O线程。
     * idCardManager 管理类
     */
    private NioEventLoopGroup boss;
    private NioEventLoopGroup work;
    private NetIdCardManager netIdCardManager;

    /**
     * 构造方法
     *
     * @param netIdCardManager 需要传入的管理类
     */
    public IdCardService(@NonNull NetIdCardManager netIdCardManager) {
        this.netIdCardManager = netIdCardManager;
    }

    /**
     * 开启一个socket服务
     *
     * @param port 端口号
     */
    public void startService(final int port, int type) {
        if (port < 0) {
            LogUtils.e("服务启动失败，port=" + port);
            return;
        }
        ThreadPoolProxyFactory.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                start(port, type);
            }
        });
    }

    private void start(int port, int type) {
        boss = new NioEventLoopGroup(5);
        work = new NioEventLoopGroup(5);
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
                            LogUtils.e("接收到ic卡的链接请求" + channel.remoteAddress());
                            channel.config().setOption(ChannelOption.SO_RCVBUF, 1024);
                            switch (type) {
                                case IdCardProducerType.TYPE_NET_YUMIN:
                                    YuminHandler channelHandler = new YuminHandler();
                                    channelHandler.registerIdCardMessageListener(new MyIdCardMessageListener());
                                    channel.pipeline().addLast(channelHandler);
                                    break;
                                case IdCardProducerType.TYPE_NET_AN_DE:
                                    //添加心跳规则(10秒没有收到或者发送数据了就发心跳包)
                                    channel.pipeline().addLast(new IdleStateHandler(0, 0, 6));
                                    AnDeHandler andeHandler = new AnDeHandler();
                                    andeHandler.registerIdCardMessageListener(new MyIdCardMessageListener());
                                    channel.pipeline().addLast(andeHandler);
                                    break;
                                default:
                                    break;
                            }

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
                        netIdCardManager = null;
                    } catch (Exception e) {

                        LogUtils.e("服务关闭失败：port=" + e.toString());
                    }
                }
            }
        });
    }

    class MyIdCardMessageListener implements IdCardMessageListener {

        @Override
        public void onConnectState(IDCardHandler mHandler, String deviceId, boolean isConnect) {

            //根据是连接还是断开，调用manager的方法添加或者删除通道，进行维护
            if (isConnect) {
                IdCardService.this.netIdCardManager.addConnectHandler(deviceId, mHandler);
            } else {
                IdCardService.this.netIdCardManager.delDisConnectHandler(deviceId);
            }
            //回调给用户层
            if (IdCardService.this.netIdCardManager.getCardCallBack() != null) {
                IdCardService.this.netIdCardManager.getCardCallBack().onConnectState(deviceId, isConnect);
            }
        }

        @Override
        public void onReceiveIDCard(String cardNo) {
            if (IdCardService.this.netIdCardManager.getCardCallBack() != null) {
                IdCardService.this.netIdCardManager.getCardCallBack().onReceiveCardNum(cardNo);
            }
        }
    }
}
