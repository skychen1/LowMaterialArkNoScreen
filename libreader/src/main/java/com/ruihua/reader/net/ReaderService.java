package com.ruihua.reader.net;

import androidx.annotation.NonNull;

import com.rivamed.libdevicesbase.utils.LogUtils;
import com.rivamed.libdevicesbase.utils.ThreadPoolProxyFactory;
import com.ruihua.reader.ReaderProducerType;
import com.ruihua.reader.bean.AntInfo;
import com.ruihua.reader.bean.EpcInfo;
import com.ruihua.reader.net.callback.ReaderHandler;
import com.ruihua.reader.net.callback.ReaderMessageListener;
import com.ruihua.reader.net.clou.ClouHandler;
import com.ruihua.reader.net.rodinbell.RodinbellHandler;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * describe ： 罗丹贝尔服务类
 * 不能使用netty的解析规则，需要自己手动解析，因为可能存在丢包的情况，如果丢包就会引起数据解析混乱
 *
 * @author : Yich
 * date: 2019/2/22
 */
public class ReaderService {
    private NioEventLoopGroup boss;
    private NioEventLoopGroup work;
    private NetReaderManager manager;
    private final Object mLock = new Object();

    /**
     * 构造方法，需要传入管理类， 要数据回调
     *
     * @param manager 管理类
     */
    public ReaderService(@NonNull NetReaderManager manager) {
        this.manager = manager;
    }

    /**
     * 开启一个socket服务，
     *
     * @param port 端口号
     * @param type 服务的类型，具体参看ReaderType
     */
    public void startService(int port, int type) {
        if (port <= 0) {
            LogUtils.e("服务启动失败：：:port=" + port);
            return;
        }
        ThreadPoolProxyFactory.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                start(port, type);
            }
        });
    }

    /**
     * 启动服务
     * * @param port 端口号
     *
     * @param type 服务的类型，具体参看ReaderType
     */
    private void start(int port, int type) {
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
                            LogUtils.e("接收到reader的链接请求" + channel.remoteAddress());
                            channel.config().setOption(ChannelOption.SO_RCVBUF, 1024);
                            String ip = channel.remoteAddress().toString();
                            switch (type) {
                                case ReaderProducerType.TYPE_NET_RODINBELL:
                                    //罗丹贝尔的数据通道，根据罗丹贝尔情况处理
                                    //添加心跳规则
                                    channel.pipeline().addLast(new IdleStateHandler(0, 0, 4));
                                    RodinbellHandler channelHandler = new RodinbellHandler();
                                    channelHandler.registerRodinbellMessageListener(new MyReaderMessageListener());
                                    channel.pipeline().addLast(ip,channelHandler);
                                    break;
                                case ReaderProducerType.TYPE_NET_COLU:
                                    //鸿陆的处理，在此添加
                                    ClouHandler clouHandler = new ClouHandler();
                                    clouHandler.registerClouMessageListener(new MyReaderMessageListener());
                                    channel.pipeline().addLast(ip,clouHandler);
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

    /**
     * 数据回调接口实现类,中间过度层，所有设备的的消息都集中回调给mananger，然后回调给用户
     */
    class MyReaderMessageListener implements ReaderMessageListener {

        @Override
        public void onConnectState(ReaderHandler mHandler, String deviceId, boolean isConnect) {
            //根据是连接还是断开，调用manager的方法添加或者删除通道，进行维护
            if (isConnect) {
                ReaderService.this.manager.addConnectHandler(deviceId, mHandler);
            } else {
                ReaderService.this.manager.delDisConnectHandler(deviceId);
            }
            //回调给用户层
            if (ReaderService.this.manager.getCallback() != null) {
                synchronized (mLock) {
                    ReaderService.this.manager.getCallback().onConnectState(deviceId, isConnect);
                }
            }
        }

        @Override
        public void onScanResult(String deviceId, Map<String, List<EpcInfo>> result) {
            if (ReaderService.this.manager.getCallback() != null) {
                synchronized (mLock) {
                    ReaderService.this.manager.getCallback().onScanResult(deviceId, result);
                }
            }
        }

        @Override
        public void onScanNewEpc(String deviceId, String epc, int ant) {
            if (ReaderService.this.manager.getCallback() != null) {
                synchronized (mLock) {
                    ReaderService.this.manager.getCallback().onScanNewEpc(deviceId, epc, ant);
                }
            }
        }

        @Override
        public void onSetPowerRet(String deviceId, boolean success) {
            if (ReaderService.this.manager.getCallback() != null) {
                synchronized (mLock) {
                    ReaderService.this.manager.getCallback().onSetPower(deviceId, success);
                }
            }
        }

        @Override
        public void onQueryPowerRet(String deviceId, int power) {
            if (ReaderService.this.manager.getCallback() != null) {
                synchronized (mLock) {
                    ReaderService.this.manager.getCallback().onGetPower(deviceId, power);
                }
            }
        }

        @Override
        public void onQueryPowerRet(String deviceId, int[] power) {
            if (ReaderService.this.manager.getCallback() != null) {
                synchronized (mLock) {
                    ReaderService.this.manager.getCallback().onGetPower(deviceId, power);
                }
            }
        }

        @Override
        public void onCheckAnt(String deviceId, List<AntInfo> ant) {
            if (ReaderService.this.manager.getCallback() != null) {
                synchronized (mLock) {
                    ReaderService.this.manager.getCallback().onCheckAnt(deviceId, ant);
                }
            }
        }

        @Override
        public void onLockOpen(String deviceId, boolean isSuccess) {
            if (ReaderService.this.manager.getCallback() != null) {
                synchronized (mLock) {
                    ReaderService.this.manager.getCallback().onLockOpen(deviceId, isSuccess);
                }
            }
        }

        @Override
        public void onLockClose(String deviceId, boolean isSuccess) {
            if (ReaderService.this.manager.getCallback() != null) {
                synchronized (mLock) {
                    ReaderService.this.manager.getCallback().onLockClose(deviceId, isSuccess);
                }
            }
        }

        @Override
        public void onLightOpen(String deviceId, boolean isSuccess) {
            if (ReaderService.this.manager.getCallback() != null) {
                synchronized (mLock) {
                    ReaderService.this.manager.getCallback().onLightOpen(deviceId, isSuccess);
                }
            }
        }

        @Override
        public void onLightClose(String deviceId, boolean isSuccess) {
            if (ReaderService.this.manager.getCallback() != null) {
                synchronized (mLock) {
                    ReaderService.this.manager.getCallback().onLightClose(deviceId, isSuccess);
                }
            }
        }

        @Override
        public void onLockState(String deviceId, boolean isOpened) {
            if (ReaderService.this.manager.getCallback() != null) {
                ReaderService.this.manager.getCallback().onLockState(deviceId, isOpened);
            }
        }

        @Override
        public void onLightState(String deviceId, boolean isOpened) {
            if (ReaderService.this.manager.getCallback() != null) {
                synchronized (mLock) {
                    ReaderService.this.manager.getCallback().onLightState(deviceId, isOpened);
                }
            }
        }
    }

}
