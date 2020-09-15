package cn.rivamed;


import androidx.annotation.IntRange;

import com.rivamed.libdevicesbase.base.DeviceInfo;
import com.rivamed.libdevicesbase.base.FunctionCode;
import com.rivamed.libdevicesbase.utils.LogUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.rivamed.callback.Eth002CallBack;
import cn.rivamed.callback.Eth002HandlerInterface;
import cn.rivamed.libeth002.Eth002Handler;
import cn.rivamed.test.TestEth002Handler;

/**
 * 指纹仪的设备管理类
 * 它是一个单实例对象：开启服务，添加handler，注册回调函数，传递数据给回调函数
 * create by 孙朝阳 on 2019-3-31
 */
public class Eth002Manager {

    private static Eth002Manager eth002Manager;

    private Eth002Manager() {
    }

    /**
     * 单例模式，一个设备只需要一个地方操作
     *
     * @return 单例对象
     */
    public static synchronized Eth002Manager getEth002Manager() {
        if (eth002Manager == null) {
            eth002Manager = new Eth002Manager();
        }
        return eth002Manager;
    }

    /**
     * eth002CallBack 回调。给外部使用的地方调用
     * connectHandler 所有连接设备的道的集合，添加连接上的设备
     * eth002Service ETH002服务
     */
    private Eth002CallBack eth002CallBack;
    private Map<String, Eth002Handler> connectHandler = new HashMap<>();
    private Eth002Service eth002Service;
    private TestEth002Handler testEth002Handler;
    private boolean isSimulation = false;

    public void setSimulation(boolean isSimulation){
        this.isSimulation = isSimulation;
        if (isSimulation){
            testEth002Handler = new TestEth002Handler(this);
        }
    }

    /**
     * 添加一个设备的通道进行维护
     *
     * @param identification 通道唯一标识
     * @param handler        通道
     */
    public void addConnectHandler(String identification, Eth002Handler handler) {
        if (connectHandler.containsKey(identification) && connectHandler.get(identification).equals(handler)) {
            //查看如果以前有这个相同的设备通道，就关闭以前的那个通道
            connectHandler.get(identification).closeChannel();
        }
        //加入到通道的集合中维护
        connectHandler.put(identification, handler);
    }

    /**
     * 当一个设备断开的时候就删除集合中对应的通道
     *
     * @param identification 设备id
     */
    public void delDisConnectHandler(String identification) {
        if (connectHandler.containsKey(identification)) {
            connectHandler.remove(identification);
        }
    }

    /**
     * 添加/存储连接设备的信息：远端IP、厂家、版本
     *
     * @return 所有设备的集合
     */
    public List<DeviceInfo> getConnectedDevice() {
        List<DeviceInfo> deviceInfos = new ArrayList<>();
        for (Map.Entry<String, Eth002Handler> d : connectHandler.entrySet()) {
            if (d.getValue() != null) {
                deviceInfos.add(new DeviceInfo(d.getKey(), d.getValue().getRemoteIP(), d.getValue().getProducer(), d.getValue().getVersion()));
            }
        }
        return deviceInfos;
    }


    /***
     * 开锁
     * @param deviceId 电子锁连接的Eth模块的设备ID ，每个柜子对应一个Eth002模块
     *
     * @return 返回结果仅表示命令已发送，具体执行结果参照回调函数
     */
    public int openDoor(String deviceId) {
        LogUtils.e("接收开门指令；DEVICEID=" + deviceId);
        if (!connectHandler.containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return connectHandler.get(deviceId).openDoor();
    }

    /***
     * 检查开门状态开门
     * @param deviceId 电子锁连接的Eth模块的设备ID ，每个柜子对应一个Eth002模块
     * @return 返回结果仅表示命令已发送，具体执行结果参照回调函数
     */
    public int checkDoorState(String deviceId) {
        if (!connectHandler.containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return connectHandler.get(deviceId).checkLockState();
    }

    /**
     * 开灯
     *
     * @param deviceId 设备id
     * @return 返回码
     */
    public int openLight(String deviceId) {
        if (!connectHandler.containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return connectHandler.get(deviceId).openLight();
    }

    /**
     * 关灯
     *
     * @param deviceId 设备id
     * @return 返回码
     */
    public int closeLight(String deviceId) {
        if (!connectHandler.containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return connectHandler.get(deviceId).closeLight();
    }

    /***
     * 指纹注册
     * @param deviceId 指纹仪连接的Eth模块的设备ID ，每个柜子对应一个Eth002模块
     * @return 返回码
     */
    public int fingerReg(String deviceId) {

        if (isSimulation){
            testEth002Handler.fingerReg();
            return FunctionCode.SUCCESS;
        }
        if (!connectHandler.containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        if (!(connectHandler.get(deviceId) instanceof Eth002HandlerInterface)){
            return FunctionCode.DEVICE_NOT_SUPPORT;
        }
        return (connectHandler.get(deviceId)).fingerReg();
    }


    public static String getIP() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            LogUtils.e(ex.toString());
        }
        return null;
    }


    /**
     * 开启服务
     *
     * @param port 端口号
     */
    public void startService(@IntRange(from = 1, to = 65536) int port) {
        LogUtils.e("三能指纹仪服务启动....");
        if (eth002Service == null) {
            eth002Service = new Eth002Service(this);
            eth002Service.startService(port);
        }
    }

    /**
     * 关闭服务，不再使用稻苗设备的时候可以关闭服务
     */
    public void stopService() {
        if (eth002Service == null) {
            return;
        }
        eth002Service.stopService();
    }

    /**
     * 拿回回调
     *
     * @return 用户回调
     */
    public Eth002CallBack getEth002CallBack() {
        return eth002CallBack;
    }

    /**
     * 注册回调
     *
     * @param fingerCallBack 回调接口
     */
    public void registerCallBack(Eth002CallBack fingerCallBack) {
        this.eth002CallBack = null;
        this.eth002CallBack = fingerCallBack;
    }

    public void unRegisterCallBack() {
        this.eth002CallBack = null;
    }
}
