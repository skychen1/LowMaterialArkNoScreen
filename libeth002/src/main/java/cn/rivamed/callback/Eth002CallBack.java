package cn.rivamed.callback;

public interface Eth002CallBack {

    /**
     *  设备连接状态返回，
     *  @param deviceId  设备的唯一标识号
     *  @param isConnect 连接的装态，true为已连接，false为已断开
     */
    void onConnectState(String deviceId, boolean isConnect);

    /***
     * 指纹采集通知
     * 每组柜子仅有一个指纹仪，故不作设备标识
     * @param deviceId 指纹仪连接的EHT020模块的设备ID
     * @param fingerFea  指纹数据
     */
    void onFingerFea(String deviceId, String fingerFea);

    /***
     * 指纹注册指令执行通知，该指令执行结果为success,则表示 指纹仪已处于指纹注册模式，
     *
     * 需要提示用户连续按下3次指纹，然后会接收到OnFingerRegisterRet 回调
     * 每组柜子仅有一个指纹仪，故不作设备标识
     * @param deviceId 指纹仪连接的EHT020模块的设备ID
     * @param success 是否注册成功
     *
     */
    void onFingerRegExcuted(String deviceId, boolean success);

    /***
     * 指纹注册结果通知
     * 每组柜子仅有一个指纹仪，故不作设备标识
     * @param deviceId 指纹仪连接的EHT020模块的设备ID
     * @param success 是否注册成功
     * @param fingerData 指纹注册数据
     *
     */
    void onFingerRegisterRet(String deviceId, boolean success, String fingerData);

    /***
     * 刷卡器刷卡通知
     * 每组柜子仅有一个读卡器，故不做设备标识
     * @param  deviceId  刷卡器连接的Eth002模块的设备ID，每个柜子一个ETH002模块
     * @param idCard  卡片信息
     */
    void onIDCard(String deviceId, String idCard);

    /***
     * 柜门打开通知
     * @param success  是否开门成功  true 门以打开 ，false 门未打开
     * @param deviceIndentify
     */
    void onDoorOpened(String deviceIndentify, boolean success);


    /***
     * 柜门关闭通知
     * @param success  是否开门成功  true 门已关闭 ，false 门为关闭
     * @param deviceIndentify
     */
    void onDoorClosed(String deviceIndentify, boolean success);


    /***
     * 柜门状态变通知
     * @param deviceIndentify
     */
    void onDoorCheckedState(String deviceIndentify, boolean opened);

}
