package com.ruihua.libconsumables.callback;

/**
 * describe ： 高值操作接口
 *
 * @author : Yich
 * date: 2019/9/10
 */
public interface ConsumableOperate {

    /**
     * 关闭该通道的方法
     *
     * @return 返回码
     */
    int closeChannel();

    /**
     * 开锁
     *
     * @param which 哪个锁(有0和1)
     * @return 返回码
     */
    int openDoor(int which);

    /**
     * 打开所有锁
     * @return 返回码
     */
    int openDoor();

    /**
     * 关锁
     *
     * @param which 哪个锁（有0和1)
     * @return 返回码
     */
    int closeDoor(int which);

    /**
     * 关闭所有锁
     * @return 返回码
     */
    int closeDoor();

    /**
     * 检测锁状态
     *
     * @param which 哪个锁（有0和1)
     * @return 返回码
     */
    int checkDoorState(int which);
    /**
     * 检测所有锁状态
     * @return 返回码
     */
    int checkDoorState();

    /**
     * 开灯
     *
     * @param which 哪个灯（只能是2或者3）
     * @return 返回码
     */
    int openLight(int which);

    /**
     * 打开所有灯
     * @return 返回码
     */
    int openLight();

    /**
     * 关灯
     *
     * @param which 哪个灯（只能是2或者3）
     * @return 返回码
     */
    int closeLight(int which);

    /**
     * 关闭所有灯
     * @return 返回码
     */
    int closeLight();
    /**
     * 检测锁装填
     *
     * @param which 哪个灯（只能是2或者3）
     * @return 返回码
     */
    int checkLightState(int which);

    /**
     * 检测所有灯的状态
     * @return 返回码
     */
    int checkLightState();

    /**
     * 获取设备固件系统版本号
     *
     * @return 返回码
     */
    int getFirmwareVersion();

    /**
     * 重启设备
     *
     * @return 返回码
     */
    int restart();


    /**
     * 升级设备
     *
     * @return 返回码
     */
    int update();

    /**
     * 发送升级文件
     *
     * @param filePath 升级文件
     * @return 返回码
     */
    int sendUpDateFile(String filePath);

    /**
     * 获取设备的ip
     *
     * @return ip
     */
    String getRemoteIP();

    /**
     * 获取设备的厂家
     *
     * @return 厂家
     */
    String getProducer();

    /**
     * 获取设备的版本
     *
     * @return 版本
     */
    String getVersion();


}
