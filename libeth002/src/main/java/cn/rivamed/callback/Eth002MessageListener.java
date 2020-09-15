package cn.rivamed.callback;

import cn.rivamed.libeth002.Eth002Handler;

/**
 *  Eth002模块 监听器
 *  create by 孙朝阳 on 2019-3-31
 */
public interface Eth002MessageListener {


    public Eth002HandlerInterface getEth002HandlerInterface();

    public void setEth002HandlerInterface(Eth002HandlerInterface handlerInterface);

    /**
     * 接收到指纹注册结果，把指纹模板给到应用程序
     * @param regestedSuccess
     * @param fingerData
     * @param userid
     */
    void fingerRegisterRet(boolean regestedSuccess, String fingerData, String userid);

    /**
     * 获取指纹模板
     * @param fingerData 获取到的指纹模板数据
     */
    void fingerGetImage(String fingerData);

    /**
     *  通知应用指纹注册命令已执行
     */
    void fingerRegisgerCmdExcuted();


    /**
     *  设备连接状态返回，
     *  @param mHandler  通道
     *  @param deviceId  设备的唯一标识号
     *  @param isConnect 连接的装态，true为已连接，false为已断开
     */
    void onConnectState(Eth002Handler mHandler, String deviceId, boolean isConnect);

    void reciveIDCard(String cardNo);

    void doorClosed(boolean success);

    void doorOpenRet(boolean opened);

    void doorState(boolean opened);
}
