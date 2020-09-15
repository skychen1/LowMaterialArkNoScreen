package com.rivamed;

import java.util.List;

/**
 * describe ： 指纹操作回调接口
 *
 * @author : Yich
 * date: 2019/5/14
 */
public interface FingerCallback {
    /**
     * 设备连接状态返回，
     *
     * @param deviceId  设备的唯一标识号
     * @param isConnect 连接的装态，，true为已连接，false为已断开
     */
    void onConnectState(String deviceId, boolean isConnect);

    /**
     * 指纹特征回调（开启以后有指纹数据就会回调，设备主动发送数据）
     *
     * @param deviceId 设备id
     * @param features 指纹特征
     */
    void onFingerFeatures(String deviceId, String features);

    /**
     * 注册指纹数据回调
     *
     * @param deviceId      设备id
     * @param code          返回码（0，成功，1超时，2,合成失败）
     * @param features      指纹特征
     * @param fingerPicPath 指纹图片保存位置
     * @param msg           失败的解释
     */
    void onRegisterResult(
	    String deviceId, int code, String features, List<String> fingerPicPath, String msg);

    /**
     * 提醒用户抬起手指回调
     *
     * @param deviceId 设备id
     */
    void onFingerUp(String deviceId);

    /**
     * 注册剩余时间回调
     *
     * @param deviceId 设备id
     * @param time     剩余时间
     */
    void onRegisterTimeLeft(String deviceId, long time);
}
