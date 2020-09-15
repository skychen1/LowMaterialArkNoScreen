package cn.rivamed;

import androidx.annotation.NonNull;

import com.rivamed.libdevicesbase.utils.LogUtils;
import com.rivamed.libdevicesbase.utils.ThreadPoolProxyFactory;

import java.net.InetSocketAddress;

import cn.rivamed.callback.Eth002HandlerInterface;
import cn.rivamed.callback.Eth002MessageListener;
import cn.rivamed.libeth002.Eth002Handler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.StringUtil;

public class Eth002Service {

    private EventLoopGroup boss;
    private EventLoopGroup work;
    private Eth002Manager eth002Manager;

    public Eth002Service(@NonNull Eth002Manager eth002Manager){
        this.eth002Manager = eth002Manager;
    }

    /**
     * 开启一个socket服务
     * @param port 端口号
     */
    public void startService(int port){
        if (port<0){
            LogUtils.e("服务启动失败：port" + port);
            return;
        }
        ThreadPoolProxyFactory.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                start(port);
            }
        });
    }

    public void start(int port){

        boss = new NioEventLoopGroup();
        work = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.option(ChannelOption.TCP_NODELAY,true);
        b.childOption(ChannelOption.SO_KEEPALIVE,true);
        b.option(ChannelOption.SO_BACKLOG,1024);
        b.group(boss,work)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.config().setOption(ChannelOption.SO_KEEPALIVE,true);
                        LogUtils.e("接收到新的链接请求" + channel.remoteAddress());
                        channel.config().setOption(ChannelOption.SO_RCVBUF,1024);
                        Eth002Handler eth002Handler = new Eth002Handler();
                        eth002Handler.registerMessageListener(new MyEth002MessageListener());
                        channel.pipeline().addLast(eth002Handler);
                    }
                });
        try {
            ChannelFuture cf = b.bind().sync();
            LogUtils.e("服务启动：port" + port);
            cf.channel().closeFuture().sync();
        } catch (Exception e) {
            LogUtils.e("服务启动异常：port=" + port + e.toString());
        }finally {
            try {
                boss.shutdownGracefully().sync();
                work.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                LogUtils.e("服务启动异常关闭失败：port" + port + e.toString());
            }
        }

    }

    public boolean stopService(){

        boolean isClose = true;
        if (boss != null && work != null){
            try {
                boss.shutdownGracefully().sync();
                work.shutdownGracefully().sync();
                boss = null;
                work = null;
                eth002Manager = null;
            } catch (Exception e) {
                isClose = false;
                LogUtils.e("服务关闭失败：port" + e.toString());
            }

        }

        return isClose;
    }

    private class MyEth002MessageListener implements Eth002MessageListener{

        Eth002HandlerInterface eth002HandlerInterface;

        @Override
        public Eth002HandlerInterface getEth002HandlerInterface() {
            return eth002HandlerInterface;
        }

        @Override
        public void setEth002HandlerInterface(Eth002HandlerInterface handlerInterface) {
            this.eth002HandlerInterface = handlerInterface;
        }

        @Override
        public void onConnectState(Eth002Handler mHandler, String deviceId, boolean isConnect) {

            if (isConnect){
                Eth002Service.this.eth002Manager.addConnectHandler(deviceId,mHandler);
            }else {
                Eth002Service.this.eth002Manager.delDisConnectHandler(deviceId);
            }

            if (Eth002Service.this.eth002Manager.getEth002CallBack() != null){
                Eth002Service.this.eth002Manager.getEth002CallBack().onConnectState(deviceId,isConnect);
            }
        }

        @Override
        public void reciveIDCard(String cardNo) {
            if (Eth002Service.this.eth002Manager != null){
                if (Eth002Service.this.eth002Manager.getEth002CallBack() != null){
                    Eth002Service.this.eth002Manager.getEth002CallBack().onIDCard(this.eth002HandlerInterface.getIdentification(),cardNo);
                }
            }
        }

        @Override
        public void doorClosed(boolean success) {
            if (Eth002Service.this.eth002Manager != null){
                if (Eth002Service.this.eth002Manager.getEth002CallBack() != null){
                    Eth002Service.this.eth002Manager.getEth002CallBack().onDoorClosed(this.eth002HandlerInterface.getIdentification(),success);
                }
            }
        }

        @Override
        public void doorOpenRet(boolean opened) {
            if (Eth002Service.this.eth002Manager != null){
                if (Eth002Service.this.eth002Manager.getEth002CallBack() != null){
                    Eth002Service.this.eth002Manager.getEth002CallBack().onDoorOpened(this.eth002HandlerInterface.getIdentification(),opened);
                }
            }
        }

        @Override
        public void doorState(boolean opened) {
            if (Eth002Service.this.eth002Manager != null){
                if (Eth002Service.this.eth002Manager.getEth002CallBack() != null){
                    Eth002Service.this.eth002Manager.getEth002CallBack().onDoorCheckedState(this.eth002HandlerInterface.getIdentification(),opened);
                }
            }
        }

        @Override
        public void fingerRegisterRet(boolean regestedSuccess, String fingerData, String userid) {
            if (Eth002Service.this.eth002Manager != null){
                if (Eth002Service.this.eth002Manager.getEth002CallBack() != null){
                    Eth002Service.this.eth002Manager.getEth002CallBack().onFingerRegisterRet(this.eth002HandlerInterface.getIdentification(),regestedSuccess,fingerData);
                }
            }
        }

        @Override
        public void fingerGetImage(String fingerData) {
            if (StringUtil.isNullOrEmpty(fingerData)){
                return;
            }
            if (Eth002Service.this.eth002Manager != null){
                if (Eth002Service.this.eth002Manager.getEth002CallBack() != null){
                    Eth002Service.this.eth002Manager.getEth002CallBack().onFingerFea(this.eth002HandlerInterface.getIdentification(),fingerData);
                }
            }
        }

        @Override
        public void fingerRegisgerCmdExcuted() {
            if (Eth002Service.this.eth002Manager != null){
                if (Eth002Service.this.eth002Manager.getEth002CallBack() != null){
                    Eth002Service.this.eth002Manager.getEth002CallBack().onFingerRegExcuted(this.eth002HandlerInterface.getIdentification(),true);
                }
            }
        }
    }

}
