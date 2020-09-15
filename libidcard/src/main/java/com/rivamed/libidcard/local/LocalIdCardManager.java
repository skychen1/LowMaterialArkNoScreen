package com.rivamed.libidcard.local;

import android.content.Context;

import com.rivamed.libdevicesbase.base.DeviceInfo;
import com.rivamed.libdevicesbase.base.DeviceType;
import com.rivamed.libdevicesbase.base.FunctionCode;
import com.rivamed.libidcard.IdCardCallBack;
import com.rivamed.libidcard.IdCardProducerType;
import com.rivamed.libidcard.local.ande.AndeManager;
import com.rivamed.libidcard.local.callback.IdCardOperate;
import com.rivamed.libidcard.local.deke.DekeManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalIdCardManager {

    private static LocalIdCardManager localIdCardManager;

    private LocalIdCardManager() {

    }

    public static synchronized LocalIdCardManager getLocalIdCardManager() {
        if (localIdCardManager == null) {
            localIdCardManager = new LocalIdCardManager();
        }
        return localIdCardManager;
    }

    /**
     * 保存各个已连接的ID卡的集合
     */
    private Map<String, IdCardOperate> connectIdCard = new HashMap<>();
    private IdCardCallBack idCardCallBack;

    /**
     * 添加连接好的设备到集合中
     *
     * @param cardId        设备唯一标识：id
     * @param idCardOperate 设备监听接口
     */
    public void addConnectIdCard(String cardId, IdCardOperate idCardOperate) {
        if (connectIdCard.containsKey(cardId)) {
            connectIdCard.remove(cardId);
        }
        connectIdCard.put(cardId, idCardOperate);
    }

    /**
     * 删除断开连接的设备
     *
     * @param cardId 设备唯一标识：id
     */
    public void delDisConnectIdCard(String cardId) {
        if (connectIdCard.containsKey(cardId)) {
            connectIdCard.remove(cardId);
        }
    }

    /**
     * 获取已连接的设备
     *
     * @return 连接设备的集合
     */
    public List<DeviceInfo> getConnectedDevice() {

        List<DeviceInfo> deviceInfos = new ArrayList<>();
        for (Map.Entry<String, IdCardOperate> d : connectIdCard.entrySet()) {
            if (d.getValue() != null) {
                deviceInfos.add(new DeviceInfo(d.getKey(), "", d.getValue().getProducer(), d.getValue().getVersion(), DeviceType.DEVICE_TYPE_ID_READER));
            }
        }
        return deviceInfos;
    }

    /**
     * 传入连接设备的类型
     *
     * @param type 设备类型
     */
    public int connect(Context context, int type) {
        int code;
        switch (type) {
            case IdCardProducerType.TYPE_LOCAL_AN_DE:
                AndeManager anCardManager = new AndeManager(this);
                code = anCardManager.connect(context);
                break;
            case IdCardProducerType.TYPE_LOCAL_DEKE:
                DekeManager dekeManager = new DekeManager(this);
                code = dekeManager.connect(context);
                break;
            default:
                code = FunctionCode.NOT_SUPPORT_PRODUCER_TYPE;
                break;
        }
        return code;
    }

    /**
     * 断开连接的某一设备
     *
     * @param cardId 设备的唯一标识ID值
     * @return
     */
    public int disConnect(String cardId) {

        if (!connectIdCard.containsKey(cardId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return connectIdCard.get(cardId).disConnect();
    }

    /**
     * 开始盘存
     *
     * @param deviceId 设备唯一标识
     * @return
     */
    public int startReadCard(String deviceId) {

        if (!connectIdCard.containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return connectIdCard.get(deviceId).startRead();
    }

    /**
     * 停止盘存
     *
     * @param deviceId 设备唯一标识
     * @return
     */
    public int stopReadCard(String deviceId) {

        if (!connectIdCard.containsKey(deviceId)) {
            return FunctionCode.DEVICE_NOT_EXIST;
        }
        return connectIdCard.get(deviceId).stopRead();
    }

    /**
     * 关闭所有已连接的设备
     *
     * @return 返回码
     */
    public int stopAllIdCards() {
        //遍历集合中所有的reader，并且关闭
        for (IdCardOperate idcard : connectIdCard.values()) {
            idcard.disConnect();
        }
        //清空集合
        connectIdCard.clear();
        return FunctionCode.SUCCESS;
    }

    /**
     * 获取回调
     *
     * @return 回调接口实例
     */
    public IdCardCallBack getCallback() {
        return idCardCallBack;
    }

    /**
     * 注册监听回调
     *
     * @param idCardCallBack 回调
     */
    public void registerCallback(IdCardCallBack idCardCallBack) {
        this.idCardCallBack = null;
        this.idCardCallBack = idCardCallBack;
    }

    /**
     * 反注册回调
     */
    public void unRegisterCallback() {
        this.idCardCallBack = null;
    }
}
