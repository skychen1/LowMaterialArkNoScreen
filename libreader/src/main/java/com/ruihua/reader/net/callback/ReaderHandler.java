package com.ruihua.reader.net.callback;

/**
 * describe ：reader扫描设备通用方法接口
 *
 * @author : Yich
 * date: 2019/2/22
 */
public interface ReaderHandler {

    /**
     * 关闭该通道的方法
     *
     * @return 返回码
     */
    int closeChannel();

    /**
     * 开始扫描的方法，
     *
     * @param timeout 没有新标签的时间
     * @return 成功失败
     */
    int startScan(int timeout);

    /**
     * 开始扫描的方法，
     *
     * @param timeout 没有新标签的时间
     * @param ants    扫描的天线
     * @return 成功失败
     */
    int startScan(int timeout, byte[] ants);

    /**
     * 停止扫描 （该方法只能在此通道服务端停止扫描操作，不能直接停止底层扫描返回）
     * 所以调用改方法以后需要延时2s以后才能进行下一次扫描
     *
     * @return 无
     */
    int stopScan();

    /**
     * 设置设备功率
     *
     * @param power 功率值
     * @return 返回码
     */
    int setPower(byte power);

    /**
     * 设置设备功率(所有天线的功率值)
     *
     * @param powers 功率值
     * @return 返回码
     */
    int setPower(byte[] powers);

    /**
     * 查询功率
     *
     * @return 返回码
     */
    int getPower();

    /**
     * 检测所有天线状态 （都是采用回调的方式）
     *
     * @return 返回码
     */
    int checkAnts();

    /**
     * 重置的命令
     *
     * @return 返回码
     */
    int reset();

    /**
     * 开锁的操作
     *
     * @return 返回码
     */
    int openLock();

    /**
     * 关锁操作
     *
     * @return 返回码
     */
    int closeLock();

    /**
     * 开灯操作
     *
     * @return 返回码
     */
    int openLight();

    /**
     * 关灯操作
     *
     * @return 返回码
     */
    int closeLight();

    /**
     * 检测锁的状态
     *
     * @return 返回码
     */
    int checkLockState();

    /**
     * 检测灯的状态
     *
     * @return 返回码
     */
    int checkLightState();

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
