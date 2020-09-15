package com.rivamed.libdevicesbase.utils;

import android.bluetooth.BluetoothDevice;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * describe ： 蓝牙配对工具类
 *
 * @author : Yich
 * date: 2019/8/2
 */
public class ClsUtils {
    private ClsUtils() {
    }

    /**
     * 与设备配对 参考源码：platform/packages/apps/Settings.git
     * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
     */
    public static boolean createBond(BluetoothDevice btDevice) throws Exception {
        Method createBond = btDevice.getClass().getMethod("createBond");
        return (Boolean) createBond.invoke(btDevice);
    }

    /**
     * 与设备解除配对 参考源码：platform/packages/apps/Settings.git
     * /Settings/src/com/android/settings/bluetooth/CachedBluetoothDevice.java
     */
    public static boolean removeBond(Class<?> btClass, BluetoothDevice btDevice) throws Exception {
        Method removeBondMethod = btClass.getMethod("removeBond");
        return (Boolean) removeBondMethod.invoke(btDevice);
    }

    public static boolean setPin(BluetoothDevice btDevice, String str) throws Exception {
        boolean is;
        try {
            Method removeBondMethod = btDevice.getClass().getDeclaredMethod("setPin", new Class[]{byte[].class});
            is = (Boolean) removeBondMethod.invoke(btDevice, new Object[]{str.getBytes()});
        } catch (Exception e) {
            is = false;
            LogUtils.e(e.toString());
        }
        return is;

    }

    public static boolean cancelPairingUserInput(Class<?> btClass, BluetoothDevice device) throws Exception {
        Method createBondMethod = btClass.getMethod("cancelPairingUserInput");
        return (Boolean) createBondMethod.invoke(device);
    }


    public static boolean cancelBondProcess(Class<?> btClass, BluetoothDevice device) throws Exception {
        Method createBondMethod = btClass.getMethod("cancelBondProcess");
        return (Boolean) createBondMethod.invoke(device);
    }

    /**
     * 确认配对
     *
     * @param device
     * @param isConfirm
     * @throws Exception
     */
    public static void setPairingConfirmation(BluetoothDevice device, boolean isConfirm)  {
        Method setPairingConfirmation = null;
        try {
            setPairingConfirmation = device.getClass().getDeclaredMethod("setPairingConfirmation", boolean.class);
        } catch (NoSuchMethodException e) {
            LogUtils.e("获取方法失败了");
        }
        try {
            setPairingConfirmation.invoke(device, isConfirm);
        } catch (IllegalAccessException e) {
            LogUtils.e("IllegalAccessException：："+e.toString());
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            LogUtils.e("cause：："+cause.toString());
            Throwable targetException = e.getTargetException();
            LogUtils.e("targetException：："+targetException.toString());
        }
    }


}
