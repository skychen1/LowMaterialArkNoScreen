package com.rivamed.libidcard.local.callback;

import android.content.Context;

public interface IdCardOperate {

    /**
     * 连接设备
     *
     * @param context 上下文
     * @return 返回码
     */
    int connect(Context context);

    /**
     * 设备断开连接
     * @return 返回码
     */
    int disConnect();

    /**
     * 开始读卡
     * @return 返回码
     */
    int startRead();

    /**
     * 停止读卡
     * @return 返回码
     */
    int stopRead();

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
