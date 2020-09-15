package com.rivamed.libdevicesbase.base;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.rivamed.libdevicesbase.callback.UsbAttachCallBack;

import java.util.HashMap;
import java.util.Map;

/**
 * describe ： Usb拔插管理类  (单例模式)
 *
 * @author : Yich
 * date: 2019/6/24
 */
public class UsbAttachManager {
    private static UsbAttachManager manager;
    private Application mContext;
    private UsbReceiver mUsbReceiver = new UsbReceiver();
    private Map<String, UsbAttachCallBack> mCallBacks = new HashMap<>();

    public static synchronized UsbAttachManager getManager() {
        if (manager == null) {
            manager = new UsbAttachManager();
        }
        return manager;
    }

    /**
     * 注册广播监听，必须是application注册，防止内训泄露
     *
     * @param context application
     */
    public void registerUsbReceiver(@NonNull Application context) {
        mContext = context;
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        mContext.registerReceiver(mUsbReceiver, filter);
    }

    /**
     * 反注册广播 （不在使用usb拔插监听的时候调用）
     */
    public void unRegisterUsbReceiver() {
        //先清空所有回调，防止内训泄露，
        clearAllCallback();
        if (mContext != null) {
            mContext.unregisterReceiver(mUsbReceiver);
        }
        mContext = null;
    }

    /**
     * 注册回调（一类设备只能注册一个回调）
     *
     * @param key      类型值作为key
     * @param callBack 回调
     */
    public void registerCallBack(String key, UsbAttachCallBack callBack) {
        //如何集合中包含该回调
        if (mCallBacks.containsKey(key)) {
            mCallBacks.remove(key);
        }
        mCallBacks.put(key, callBack);
    }

    /**
     * 反注册监听回调（必要的，如果不再接收该监听回调就注销回调，防止内存泄露）
     *
     * @param key key值
     */
    public void unRegisterCallBack(String key) {
        if (mCallBacks.containsKey(key)) {
            mCallBacks.remove(key);
        }
    }

    /**
     * 清空所有的回调，
     */
    public void clearAllCallback() {
        if (!mCallBacks.isEmpty()) {
            mCallBacks.clear();
        }
    }


    /**
     * usb 广播
     */
    public class UsbReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 这里可以拿到插入的USB设备对象
            UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
            String action = intent.getAction();
            //判断设备和过滤器是否为空
            if (usbDevice == null || TextUtils.isEmpty(action)) {
                return;
            }
            switch (action) {
                //插入设备
                case UsbManager.ACTION_USB_DEVICE_ATTACHED:
                    for (Map.Entry<String, UsbAttachCallBack> entry : mCallBacks.entrySet()) {
                        entry.getValue().onUsbAttachCallBack(mContext, usbDevice.getProductId(), usbDevice.getVendorId(), usbDevice.getProductName(), true);
                    }
                    break;
                // 拔出USB设备
                case UsbManager.ACTION_USB_DEVICE_DETACHED:
                    for (Map.Entry<String, UsbAttachCallBack> entry : mCallBacks.entrySet()) {
                        entry.getValue().onUsbAttachCallBack(mContext, usbDevice.getProductId(), usbDevice.getVendorId(), usbDevice.getProductName(), false);
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
