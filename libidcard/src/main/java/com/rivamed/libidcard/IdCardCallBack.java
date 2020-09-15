package com.rivamed.libidcard;

public interface IdCardCallBack {

    /**
     * 设备连接状态返回
     *
     * @param deviceId  设备的唯一标识号
     * @param isConnect 连接的装态，，true为已连接，false为已断开
     */
    void onConnectState(String deviceId, boolean isConnect);

    /**
     * 接口读卡器的卡号
     *
     * @param cardNum 读到的卡号
     */
    void onReceiveCardNum(String cardNum);
}
