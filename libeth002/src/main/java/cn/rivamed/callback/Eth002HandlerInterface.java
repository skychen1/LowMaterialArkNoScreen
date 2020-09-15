package cn.rivamed.callback;

/**
 *  eth002设备通用方法接口
 *  create by 孙朝阳 on 2019-3-31
 */
public interface Eth002HandlerInterface {

    /**
     * 获取eth002设备标识 ID
     * @return
     */
    String getIdentification();

    int openDoor();

    int checkLockState();

    int openLight();

    int closeLight();

    /**
     * 获取设备IP
     * @return
     */
    String getRemoteIP();

    /**
     * 获取设备厂家
     * @return
     */
    String getProducer();

    /**
     * 获取设备版本
     * @return
     */
    String getVersion();

    /**
     * 关闭通道的方法
     * @return 返回码
     */
    int closeChannel();

    /**
     * 注册指纹
     * @return 返回码
     */
    int fingerReg();

    void registerMessageListener(Eth002MessageListener eth002MessageListener);

}
