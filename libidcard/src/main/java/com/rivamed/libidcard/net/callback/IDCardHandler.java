package com.rivamed.libidcard.net.callback;

/**
 * describe ： id卡操作接口类
 *
 * @author : Yich
 * date: 2019/7/17
 */
public interface IDCardHandler {

    /**
     * 断开某个铜套
     *
     * @return 返回码
     */
    int closeChannel();

    /**
     * 开始读卡
     *
     * @return 返回码
     */
    int startReadCard();

    /**
     * 停止读卡
     *
     * @return 返回码
     */
    int stopReadCard();

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
