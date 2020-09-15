package com.rivamed.net;

import androidx.annotation.NonNull;

import com.rivamed.FingerType;
import com.rivamed.libdevicesbase.utils.LogUtils;
import com.rivamed.libdevicesbase.utils.ThreadPoolProxyFactory;
import com.rivamed.net.callback.FingerHandler;
import com.rivamed.net.callback.FingerMessageListener;
import com.rivamed.net.sanneng.SannengHandler;
import com.rivamed.net.zhiang.ZhiAngHandler;

import java.net.InetSocketAddress;
import java.util.List;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 指纹仪的服务类
 * 把Handler传过来，通过监听器的实现类注册监听，把经过处理过的指纹数据，
 * 通过设备管理类（Manager）的回调函数（callback），传到activity
 * create by 孙朝阳 on 2019-3-7
 */
public class FingerService {

    private NioEventLoopGroup boss;
    private NioEventLoopGroup work;
    private NetFingerManager mNetFingerManager;

    /**
     * 构造方法，需要传入设备管理类，要数据回调
     *
     * @param netFingerManager 管理类
     */
    public FingerService(@NonNull NetFingerManager netFingerManager) {
        this.mNetFingerManager = netFingerManager;
    }

    /**
     * 开启一个socket服务
     *
     * @param port 端口号
     */
    public void startService(int port, int type) {
        if (port < 0) {
            LogUtils.e("服务启动失败：port" + port);
            return;
        }
        ThreadPoolProxyFactory.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                start(port, type);
            }
        });
    }

    public void start(int port, int type) {
        boss = new NioEventLoopGroup();
        work = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.childOption(ChannelOption.TCP_NODELAY, true);
        b.childOption(ChannelOption.SO_KEEPALIVE, true);
        b.option(ChannelOption.SO_BACKLOG, 1024);
        b.group(boss, work)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.config().setOption(ChannelOption.SO_KEEPALIVE, true);
                        LogUtils.e("接收到指纹仪的链接请求" + channel.remoteAddress());
                        channel.config().setOption(ChannelOption.SO_RCVBUF, 1024);
                        String ip = channel.remoteAddress().toString();
                        switch (type) {
                            //三能类型
                            case FingerType.TYPE_NET_SANNENG:
                                SannengHandler sannengHandler = new SannengHandler();
                                sannengHandler.registerFingerMessageListener(new MyFingerMessageListener());
                                channel.pipeline().addLast(ip,sannengHandler);
                                break;
                            //指昂类型
                            case FingerType.TYPE_NET_ZHI_ANG:
                                //添加心跳规则(10秒没有收到或者发送数据了就发心跳包)
                                channel.pipeline().addLast(new IdleStateHandler(0, 0, 6));
                                ZhiAngHandler zhiAngHandler = new ZhiAngHandler();
                                zhiAngHandler.registerListener(new MyFingerMessageListener());
                                channel.pipeline().addLast(ip,zhiAngHandler);
                                break;
                            default:
                                break;
                        }
                    }
                });
        try {
            ChannelFuture cf = b.bind().sync();
            LogUtils.e("服务启动成功：port" + port);
            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            LogUtils.e("服务启动异常：port=" + port + e.toString());
        } finally {
            try {
                boss.shutdownGracefully().sync();
                work.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                LogUtils.e("服务启动异常关闭失败：port" + port + e.toString());
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
                        mNetFingerManager = null;
                    } catch (Exception e) {
                        LogUtils.e("服务关闭失败：：:port=" + e.toString());
                    }
                }
            }
        });
    }

    /**
     * 监听器的实现类，对监听器中方法的具体实现
     */
    class MyFingerMessageListener implements FingerMessageListener {

        /**
         * 设备连接状态
         *
         * @param mHandler  通道
         * @param deviceId  设备的唯一标识号
         * @param isConnect 连接的装态，true为已连接，false为已断开
         */
        @Override
        public void onConnectState(FingerHandler mHandler, String deviceId, boolean isConnect) {
            if (isConnect) {
                FingerService.this.mNetFingerManager.addConnectHandler(deviceId, mHandler);
            } else {
                FingerService.this.mNetFingerManager.delDisConnectHandler(deviceId);
            }
            if (FingerService.this.mNetFingerManager.getCallback() != null) {
                FingerService.this.mNetFingerManager.getCallback().onConnectState(deviceId, isConnect);
            }
        }

        @Override
        public void onFingerFeatures(String deviceId, String features) {
            if (FingerService.this.mNetFingerManager.getCallback() != null) {
                FingerService.this.mNetFingerManager.getCallback().onFingerFeatures(deviceId, features);
            }
        }

        @Override
        public void onRegisterResult(String deviceId, int code, String features, List<String> fingerPicPath, String msg) {
            if (FingerService.this.mNetFingerManager.getCallback() != null) {
                FingerService.this.mNetFingerManager.getCallback().onRegisterResult(deviceId, code, features, fingerPicPath, msg);
            }
        }

        @Override
        public void onFingerUp(String deviceId) {
            if (FingerService.this.mNetFingerManager.getCallback() != null) {
                FingerService.this.mNetFingerManager.getCallback().onFingerUp(deviceId);
            }
        }

        @Override
        public void onRegisterTimeLeft(String deviceId, long time) {
            if (FingerService.this.mNetFingerManager.getCallback() != null) {
                FingerService.this.mNetFingerManager.getCallback().onRegisterTimeLeft(deviceId, time);
            }
        }
    }
}
