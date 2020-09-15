package com.rivamed.libidcard;

import android.content.Context;

import com.rivamed.libdevicesbase.base.DeviceInfo;
import com.rivamed.libdevicesbase.base.FunctionCode;
import com.rivamed.libidcard.local.LocalIdCardManager;
import com.rivamed.libidcard.net.NetIdCardManager;

import java.util.ArrayList;
import java.util.List;

/**
 * create by 孙朝阳 on 29019-5-23
 */
public class IdCardManager {

    private static IdCardManager idCardManager;

    private IdCardManager() {
    }

    /**
     * 单例模式，一个设备只需要一个用户操作
     */
    public static synchronized IdCardManager getIdCardManager() {
        if (idCardManager == null) {
            idCardManager = new IdCardManager();
        }
        return idCardManager;
    }

    /**
     * 高频读卡器的回调
     * connectHandler 设备连接的通道集合，适应于多个设备的连接
     * idCardService 服务
     */
    private int connectType = IdCardConnectType.TYPE_NET;
    private IdCardCallBack cardCallBack;

    /**
     * 获取已经连接的设备
     *
     * @return 连接的设备集合
     */
    public List<DeviceInfo> getConnectedDevice() {
        List<DeviceInfo> connectedDevice = new ArrayList<>();
        switch (connectType) {
            case IdCardConnectType.TYPE_NET:
                connectedDevice = NetIdCardManager.getIdCardManager().getConnectedDevice();
                break;
            case IdCardConnectType.TYPE_LOCAL:
                connectedDevice = LocalIdCardManager.getLocalIdCardManager().getConnectedDevice();
                break;
            default:
                break;
        }
        return connectedDevice;
    }

    public void connectIdCard(Context context, int producerType) {
        //根据选择的厂家的类型设置连接的模式（100-200之间是网络连接，200—300之间是本地连接）
        if (producerType >= 100 && producerType < 200) {
            connectType = IdCardConnectType.TYPE_NET;
        } else if (producerType >= 200 && producerType < 300) {
            connectType = IdCardConnectType.TYPE_LOCAL;
        }
        //如果有回调，要在连接之前注册回调
        if (cardCallBack != null) {
            register(cardCallBack);
        }
        switch (producerType) {
            case IdCardProducerType.TYPE_NET_YUMIN:
                NetIdCardManager.getIdCardManager().startService(40002, producerType);
                break;
            case IdCardProducerType.TYPE_NET_AN_DE:
                NetIdCardManager.getIdCardManager().startService(8030, producerType);
                break;
            case IdCardProducerType.TYPE_LOCAL_AN_DE:
            case IdCardProducerType.TYPE_LOCAL_DEKE:
                LocalIdCardManager.getLocalIdCardManager().connect(context, producerType);
                break;
            default:
                break;
        }
    }

    public int startReadCard(String deviceId) {
        int code;
        switch (connectType) {
            case IdCardConnectType.TYPE_LOCAL:
                code = LocalIdCardManager.getLocalIdCardManager().startReadCard(deviceId);
                break;
            case IdCardConnectType.TYPE_NET:
                code = NetIdCardManager.getIdCardManager().startReadCard(deviceId);
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }

    public int stopReadCard(String deviceId) {
        int code;
        switch (connectType) {
            case IdCardConnectType.TYPE_LOCAL:
                code = LocalIdCardManager.getLocalIdCardManager().stopReadCard(deviceId);
                break;
            case IdCardConnectType.TYPE_NET:
                code = NetIdCardManager.getIdCardManager().stopReadCard(deviceId);
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }

    /**
     * 断开设备连接，主要用于本地，因为不断开，下次可能会连不上
     *
     * @param cardId 设备的id
     * @return 是否成功
     */
    public int disConnectIdCard(String cardId) {
        int code;
        switch (connectType) {
            case IdCardConnectType.TYPE_NET:
                code = NetIdCardManager.getIdCardManager().disConnect(cardId);
                break;
            case IdCardConnectType.TYPE_LOCAL:
                code = LocalIdCardManager.getLocalIdCardManager().disConnect(cardId);
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }


    /**
     * 停止上一种类型的ID卡，切换reader类型
     *
     * @return 返回码
     */
    public int destroyIdCard() {
        int code;
        switch (connectType) {
            case IdCardConnectType.TYPE_NET:
                code = NetIdCardManager.getIdCardManager().stopService();
                break;
            case IdCardConnectType.TYPE_LOCAL:
                code = LocalIdCardManager.getLocalIdCardManager().stopAllIdCards();
                break;
            default:
                code = FunctionCode.CONNECT_TYPE_ERROR;
                break;
        }
        return code;
    }


    /**
     * 获取回调
     *
     * @return 回调
     */
    public IdCardCallBack getCardCallBack() {
        return cardCallBack;
    }

    /**
     * 注册回调监听
     *
     * @param cardCallBack 回调
     */
    public void registerCallBack(IdCardCallBack cardCallBack) {
        this.cardCallBack = null;
        this.cardCallBack = cardCallBack;
        register(cardCallBack);
    }

    /**
     * 注册回调到具体实现中
     *
     * @param cardCallBack 回调
     */
    private void register(IdCardCallBack cardCallBack) {
        switch (connectType) {
            case IdCardConnectType.TYPE_NET:
                NetIdCardManager.getIdCardManager().registerCallBack(cardCallBack);
                break;
            case IdCardConnectType.TYPE_LOCAL:
                LocalIdCardManager.getLocalIdCardManager().registerCallback(cardCallBack);
                break;
            default:
                break;
        }
    }

    /**
     * //反注册监听回调 在页面销毁之前必须反注册，防止内存泄漏
     */
    public void unRegisterCallBack() {
        this.cardCallBack = null;
        switch (connectType) {
            case IdCardConnectType.TYPE_NET:
                NetIdCardManager.getIdCardManager().unRegisterCallBack();
                break;
            case IdCardConnectType.TYPE_LOCAL:
                LocalIdCardManager.getLocalIdCardManager().unRegisterCallback();
                break;
            default:
                break;
        }
    }
}
