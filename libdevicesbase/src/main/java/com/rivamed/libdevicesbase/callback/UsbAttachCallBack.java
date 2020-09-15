package com.rivamed.libdevicesbase.callback;

import android.content.Context;

/**
 * describe ：Usb拔插监听接口
 *
 * @author : Yich
 * date: 2019/6/24
 */
public interface UsbAttachCallBack {

    /**
     * Usb设备监听回调
     *
     * @param context    上下文
     * @param pid        设备的pid
     * @param vid        设备的Vid
     * @param name       设备名称
     * @param isAttached 插入或者拔掉（true为插入，false是拔出）
     */
    void onUsbAttachCallBack(Context context, int pid, int vid, String name, boolean isAttached);

}
