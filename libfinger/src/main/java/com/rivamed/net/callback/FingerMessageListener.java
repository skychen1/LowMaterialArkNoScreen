package com.rivamed.net.callback;

import java.util.List;

/**
 * 深圳三能智控指纹仪 监听器
 * create by 孙朝阳 on 2019-3-7
 */
public interface FingerMessageListener {


    /**
     * 设备连接状态返回，
     *
     * @param mHandler  通道
     * @param deviceId  设备的唯一标识号
     * @param isConnect 连接的装态，true为已连接，false为已断开
     */
    void onConnectState(FingerHandler mHandler, String deviceId, boolean isConnect);



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







//    /**
//     * 接收到指纹注册结果,把指纹模板给到应用程序。
//     *
//     * @param deviceId   设备的唯一标识号
//     * @param isSuccess  是否成功
//     * @param fingerData 指纹数据
//     */
//    void onRegisterResult(String deviceId, boolean isSuccess, String fingerData);
//
//    /**
//     * 通知应用采集到指纹了，可以发送待比对的模板数据，进行比对了。
//     *
//     * @param deviceId 设备的唯一标识号
//     */
//    void onFingerCanMatch(String deviceId);
//
//    /**
//     * 指纹对比结果
//     *
//     * @param deviceId  设备的唯一标识号
//     * @param isSuccess 是否成功
//     * @param reason    原因
//     */
//    void onFingerMatchResult(String deviceId, boolean isSuccess, String reason);
//
//    /**
//     * 通知应用指纹注册命令已执行
//     *
//     * @param deviceId 设备的唯一标识号
//     */
//    void onFingerRegisterCmdExcuted(String deviceId);
//
//    /**
//     * 设备出错
//     *
//     * @param deviceId     设备的唯一标识号
//     * @param disconnected 是否断开
//     */
//    void onFingerDeviceError(String deviceId, boolean disconnected);
}
