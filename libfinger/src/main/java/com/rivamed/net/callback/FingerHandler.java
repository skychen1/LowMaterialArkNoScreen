package com.rivamed.net.callback;

/**
 * finger指纹仪设备通用方法接口
 * create by 孙朝阳 on 2019-3-12
 */
public interface FingerHandler {

    /**
     * 关闭该通道的方法
     *
     * @return 返回码
     */
    int closeChannel();

    /**
     * 开始读取指纹
     *
     * @return 返回码
     */
    int startReadFinger();

    /**
     * 停止读取指纹
     *
     * @return 返回码
     */
    int stopReadFinger();

    /**
     * 开始注册指纹
     *
     * @param timeOut  超时时长（s）
     * @param filePath 图片文件保存位置（文件夹）
     * @return 返回码
     */
    int startRegisterFinger(int timeOut, String filePath);

    /**
     * 停止获取指纹
     *
     * @return 返回码
     */
    int stopRegisterFinger();


    /**
     * 获取设备IP
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
