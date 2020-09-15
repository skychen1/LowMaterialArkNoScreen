package com.rivamed.libidcard.net.callback;

/**
 * 昱闵高频读写器ID卡 监听器
 * create by 孙朝阳 on 2019-3-6
 */
public interface IdCardMessageListener {

    /**
     * 设备连接状态返回，
     *
     * @param mHandler  通道
     * @param deviceId  设备的唯一标识号
     * @param isConnect 连接的装态，，true为已连接，false为已断开
     */
    void onConnectState(IDCardHandler mHandler, String deviceId, boolean isConnect);

    /**
     * 接口读卡器的卡号
     *
     * @param cardNo 读到的卡号
     */
    void onReceiveIDCard(String cardNo);
}
