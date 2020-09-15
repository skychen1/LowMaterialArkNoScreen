package com.rivamed.libdevicesbase.utils;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Iterator;

/**
 * describe ： usb工具类
 *
 * @author : Yich
 * date: 2019/6/13
 */
public class UsbUtils {
    private UsbUtils() {
    }

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    /**
     * 根据usb设备的名称找到设备（下部上传）
     *
     * @param context    上下文
     * @param deviceName 设备的名称，与下部约定，设备可以拿到
     * @return usb设备
     */
    public static UsbDevice getUsbDeviceByName(Context context, String deviceName) {
        //如果参数有问题，返回null
        if (context == null || TextUtils.isEmpty(deviceName)) {
            return null;
        }

        //拿到usb管理类
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        //管理类为空就返回null
        if (usbManager == null) {
            return null;
        }
        UsbDevice targetDevice = null;
        //拿到所有usb设备集合
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        Iterator<UsbDevice> iterator = deviceList.values().iterator();
        //遍历集合
        while (iterator.hasNext()) {
            UsbDevice device = iterator.next();
            LogUtils.e("vid:::::"+device.getVendorId()+">>>>>pid::::"+device.getProductId());
            //如果设备名称相同就返回该设备，并且退出循环
            if (deviceName.equals(device.getProductName())) {
                targetDevice = device;
                break;
            }
        }
        return targetDevice;
    }

    /**
     * 申请某个usb设备的动态权限；
     *
     * @param context  上下文
     * @param device   设备
     * @param callback 回调接口
     */

    public static void requstUsbPermission(@NonNull Context context, @NonNull UsbDevice device, @NonNull UsbPermissionCallback callback) {
        //拿到usb管理类
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        //管理类为空就返回null
        if (usbManager == null) {
            //因为要求传的参数不为空。所以不用判断回调接口为空
            callback.onUsbPermission(device, false);
            return;
        }
        if (usbManager.hasPermission(device)) {
            //如果usb设备本来就有权限，就直接回调有权限
            callback.onUsbPermission(device, true);
            return;
        }
        //没有权限就申请权限
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //申请权限回调
                //收到广播就要反注册，防止内存泄露
                context.unregisterReceiver(this);
                String action = intent.getAction();
                if (ACTION_USB_PERMISSION.equals(action)) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    //有权限就回调
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        callback.onUsbPermission(device, true);
                        return;
                    }
                    callback.onUsbPermission(device, false);
                }
            }
        }, filter);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        usbManager.requestPermission(device, pi);
    }

    /**
     * usb权限申请回调接口
     */

    public interface UsbPermissionCallback {
        /**
         * 申请权限的回调
         *
         * @param device  设备
         * @param success 是否有权限
         */
        void onUsbPermission(UsbDevice device, boolean success);
    }

}
