package com.ruihua.libconsumables.callback;

/**
 * describe ： 高值板回调接口
 *
 * @author : Yich
 * date: 2019/9/10
 */
public interface ConsumableCallBack {
    /**
     * 设备连接状态返回，
     *
     * @param deviceId  设备的唯一标识号(ip地址)
     * @param isConnect 连接的装态，，true为已连接，false为已断开
     */
    void onConnectState(String deviceId, boolean isConnect);

    /**
     * 开锁结果回调
     *
     * @param deviceId  设备的唯一标识号(ip地址)
     * @param which     那个锁
     * @param isSuccess 是否成功（true 为成功。 false为失败）
     */
    void onOpenDoor(String deviceId, int which, boolean isSuccess);

    /**
     * 关锁结果回调
     *
     * @param deviceId  设备的唯一标识号(ip地址)
     * @param which     那个锁
     * @param isSuccess 是否成功（true 为成功。 false为失败）
     */
    void onCloseDoor(String deviceId, int which, boolean isSuccess);

    /**
     * 检测锁状态
     *
     * @param deviceId 设备的唯一标识号(ip地址)
     * @param which    那个锁
     * @param state    锁状态（true为开，false 为关）
     */
    void onDoorState(String deviceId, int which, boolean state);

    /**
     * 开灯结果回调
     *
     * @param deviceId  设备的唯一标识号(ip地址)
     * @param which     那个灯
     * @param isSuccess 是否成功（true 为成功。 false为失败）
     */
    void onOpenLight(String deviceId, int which, boolean isSuccess);

    /**
     * 开灯结果回调
     *
     * @param deviceId  设备的唯一标识号(ip地址)
     * @param which     那个灯
     * @param isSuccess 是否成功（true 为成功。 false为失败）
     */
    void onCloseLight(String deviceId, int which, boolean isSuccess);

    /**
     * 检测灯状态
     *
     * @param deviceId 设备的唯一标识号(ip地址)
     * @param which    那个灯
     * @param state    灯状态（true为开，false 为关）
     */
    void onLightState(String deviceId, int which, boolean state);

    /**
     * 版本返回
     *
     * @param deviceId 设备的唯一标识号(ip地址)
     * @param version  版本号
     */
    void onFirmwareVersion(String deviceId, String version);

    /**
     * 该设备需要升级文件
     *
     * @param deviceId 设备的唯一标识号(ip地址)
     */
    void onNeedUpdateFile(String deviceId);

    /**
     * 升级进度回调
     *
     * @param deviceId 设备id
     * @param percent 升级的进度（每次为0.5k）p
     */
    void onUpdateProgress(String deviceId, int percent);

    /**
     * 升级结果
     *
     * @param deviceId  设备ip地址
     * @param isSuccess 是否成功
     */
    void onUpdateResult(String deviceId, boolean isSuccess);

}
