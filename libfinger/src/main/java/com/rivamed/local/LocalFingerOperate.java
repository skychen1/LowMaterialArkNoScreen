package com.rivamed.local;

import android.app.Activity;

/**
 * describe ： 指纹本地操作接口
 *
 * @author : Yich
 * date: 2019/5/14
 */
public interface LocalFingerOperate {
    /**
     * 连接设备
     *
     * @return 返回码
     */
    int connect(Activity context);

    /**
     * 与设备断开连接
     *
     * @return 返回码
     */
    int disConnect();

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
     * 注册指纹
     *
     * @param timeOut 超时时间（s）
     * @param filePath 图片文件保存的位置
     * @return 返回码
     */
    int startRegisterFinger(int timeOut, String filePath);

    /**
     * 停止注册指纹
     *
     * @return 返回码
     */
    int stopRegisterFinger();

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
