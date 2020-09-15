package com.rivamed.libidcard.net;

import androidx.annotation.IntRange;

import com.rivamed.libdevicesbase.base.DeviceInfo;
import com.rivamed.libdevicesbase.base.DeviceType;
import com.rivamed.libdevicesbase.base.FunctionCode;
import com.rivamed.libidcard.IdCardCallBack;
import com.rivamed.libidcard.net.callback.IDCardHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NetIdCardManager {

    private static NetIdCardManager netIdCardManager;

    private NetIdCardManager() {

    }

    /**
     * 单例模式，一个设备只需要一个用户操作
     */
    public static synchronized NetIdCardManager getIdCardManager() {
        if (netIdCardManager == null) {
            netIdCardManager = new NetIdCardManager();
        }
        return netIdCardManager;
    }

    /**
     * 高频读卡器的回调
     * connectHandler 设备连接的通道集合，适应于多个设备的连接
     * idCardService 服务
     */
    private IdCardCallBack idCardCallBack;
    private Map<String, IDCardHandler> connectHandler = new ConcurrentHashMap<>();
    private IdCardService idCardService;

    /**
     * 每添加一个，就会增加一个通道进行数据传输
     * identification 设备对应通道的唯一标识
     * handler 通道
     */
    public void addConnectHandler(String identification, IDCardHandler handler) {
        IDCardHandler mHandler = connectHandler.get(identification);
        if (mHandler != null) {
            mHandler.closeChannel();
        }
        //加入到通道的集合中
        connectHandler.put(identification, handler);
    }

    /**
     * 当一个设备断开的时候，就通过通道的唯一标识，删除该设备对应的通道
     * identification 设备的唯一标识
     */
    public void delDisConnectHandler(String identification) {
        IDCardHandler mHandler = connectHandler.get(identification);
        if (mHandler != null) {
            mHandler.closeChannel();
        }
        connectHandler.remove(identification);
    }

    public List<DeviceInfo> getConnectedDevice() {
        List<DeviceInfo> deviceInfos = new ArrayList<>();
        for (Map.Entry<String, IDCardHandler> d : connectHandler.entrySet()) {
            if (d.getValue() != null) {
                deviceInfos.add(new DeviceInfo(d.getKey(), d.getValue().getRemoteIP(), d.getValue().getProducer(), d.getValue().getVersion(), DeviceType.DEVICE_TYPE_ID_READER));
            }
        }
        return deviceInfos;
    }

    /**
     * 开启一个服务
     *
     * @param port 端口号 范围是：1-65536
     */
    public void startService(@IntRange(from = 1, to = 65536) int port, int type) {
        //如果服务部存在创建该服务，只能有一个服务
        if (idCardService == null) {
            idCardService = new IdCardService(this);
            idCardService.startService(port, type);
        }
    }

    public int stopService() {
        if (idCardService != null) {
            idCardService.stopService();
            idCardService = null;
        }
        return FunctionCode.SUCCESS;
    }

    public int disConnect(String cardId) {
        IDCardHandler handler = connectHandler.get(cardId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.closeChannel();
    }

    /**
     * 开始读卡方法
     *
     * @param cardId 设备id
     * @return 返回码
     */
    public int startReadCard(String cardId) {
        IDCardHandler handler = connectHandler.get(cardId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.startReadCard();
    }

    /**
     * 停止读卡
     *
     * @param cardId 设备id
     * @return 返回码
     */
    public int stopReadCard(String cardId) {
        IDCardHandler handler = connectHandler.get(cardId);
        if (handler == null) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return handler.stopReadCard();
    }

    /**
     * 获取回调
     *
     * @return 回调
     */
    public IdCardCallBack getCardCallBack() {
        return idCardCallBack;
    }

    /**
     * 注册回调监听
     *
     * @param idCardCallBack 回调
     */
    public void registerCallBack(IdCardCallBack idCardCallBack) {
        this.idCardCallBack = null;
        this.idCardCallBack = idCardCallBack;
    }

    /**
     * //反注册监听回调 在页面销毁之前必须反注册，防止内存泄漏
     */

    public void unRegisterCallBack() {
        this.idCardCallBack = null;
    }
}
