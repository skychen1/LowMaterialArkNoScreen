package com.rivamed.net;

import androidx.annotation.IntRange;

import com.rivamed.FingerCallback;
import com.rivamed.libdevicesbase.base.DeviceInfo;
import com.rivamed.libdevicesbase.base.DeviceType;
import com.rivamed.libdevicesbase.base.FunctionCode;
import com.rivamed.net.callback.FingerHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 指纹仪的设备管理类
 * 它是一个单实例对象：开启服务，添加handler，注册回调函数，传递数据给回调函数
 * create by 孙朝阳 on 2019-3-7
 */
public class NetFingerManager {

    private static NetFingerManager netFingerManager;

    private NetFingerManager() {
    }

    /**
     * 单例模式，一个种设备只需要一个地方操作
     *
     * @return 实例
     */
    public static synchronized NetFingerManager getManager() {
        if (netFingerManager == null) {
            netFingerManager = new NetFingerManager();
        }
        return netFingerManager;
    }

    /**
     * mCallback 回调。给外部使用的地方调用
     * connectHandler 所有连接设备的道的集合，添加连接上的设备
     * fingerService 服务
     */
    private FingerCallback mCallback;
    private Map<String, FingerHandler> connectHandler = new ConcurrentHashMap<>();
    private FingerService fingerService;

    /**
     * 添加一个设备的通道进行维护
     *
     * @param identification 通道唯一标识
     * @param handler        通道
     */
    public void addConnectHandler(String identification, FingerHandler handler) {
        FingerHandler fingerHandler = connectHandler.get(identification);
        if (fingerHandler != null) {
            fingerHandler.closeChannel();
        }
        //加入到通道的集合中维护
        connectHandler.put(identification, handler);
    }

    /**
     * 当一个设备断开的时候就删除集合中对应的通道
     *
     * @param identification 唯一标识
     */
    public void delDisConnectHandler(String identification) {
        FingerHandler fingerHandler = connectHandler.get(identification);
        if (fingerHandler != null) {
            fingerHandler.closeChannel();
        }
        connectHandler.remove(identification);
    }

    /**
     * 获取已连接的设备
     *
     * @return 集合
     */
    public List<DeviceInfo> getConnectedDevice() {
        List<DeviceInfo> deviceInfos = new ArrayList<>();
        for (Map.Entry<String, FingerHandler> d : connectHandler.entrySet()) {
            if (d.getValue() != null) {
                deviceInfos.add(new DeviceInfo(d.getKey(), d.getValue().getRemoteIP(), d.getValue().getProducer(), d.getValue().getVersion(), DeviceType.DEVICE_TYPE_FINGER));
            }
        }
        return deviceInfos;
    }


    /**
     * 开启服务
     *
     * @param port 端口号
     */
    public int startService(@IntRange(from = 1, to = 65536) int port, int type) {
        if (fingerService == null) {
            fingerService = new FingerService(this);
            fingerService.startService(port, type);
            return FunctionCode.SUCCESS;
        }
        return FunctionCode.ALREADY_START_SERVICE;
    }

    /**
     * 关闭服务，不再使用稻苗设备的时候可以关闭服务
     */
    public int stopService() {
        if (fingerService != null) {
            fingerService.stopService();
            fingerService = null;
        }
        return FunctionCode.SUCCESS;
    }

    /**
     * 开始读取指纹
     *
     * @param fingerId 指纹id
     * @return 返回码
     */
    public int startReadFinger(String fingerId) {
        FingerHandler fingerHandler = connectHandler.get(fingerId);
        if (fingerHandler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return fingerHandler.startReadFinger();
    }

    /**
     * 停止读取指纹
     *
     * @param fingerId 指纹id
     * @return 返回码
     */
    public int stopReadFinger(String fingerId) {
        FingerHandler fingerHandler = connectHandler.get(fingerId);
        if (fingerHandler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return fingerHandler.stopReadFinger();
    }

    /**
     * 开始注册指纹
     *
     * @param fingerId 指纹id
     * @param timeOut  超时时长（s）
     * @param filePath 图片文件保存位置（文件夹）
     * @return 返回码
     */
    public int startRegisterFinger(String fingerId, int timeOut, String filePath) {
        FingerHandler fingerHandler = connectHandler.get(fingerId);
        if (fingerHandler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return fingerHandler.startRegisterFinger(timeOut, filePath);

    }

    /**
     * 停止注册指纹
     *
     * @param fingerId 指纹id
     * @return 返回码
     */
    public int stopRegisterFinger(String fingerId) {
        FingerHandler fingerHandler = connectHandler.get(fingerId);
        if (fingerHandler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return fingerHandler.stopRegisterFinger();
    }


    /**
     * 断开某个设备的连接
     *
     * @param deviceId 设备id
     * @return 返回码
     */
    public int closeChannel(String deviceId) {
        FingerHandler fingerHandler = connectHandler.get(deviceId);
        if (fingerHandler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return fingerHandler.closeChannel();
    }

    /**
     * 拿回调
     *
     * @return 用户回调
     */
    public FingerCallback getCallback() {
        return mCallback;
    }


    public void registerCallBack(FingerCallback callback) {
        this.mCallback = null;
        this.mCallback = callback;
    }

    /**
     * 反注册回调
     */
    public void unRegisterCallback() {
        this.mCallback = null;
    }
}
