package com.rivamed;

/**
 * describe ：指纹设备连接方式
 *
 * @author : Yich
 * date: 2019/5/14
 */
public class FingerConnectType {
    private FingerConnectType(){

    }
    /**
     * 网络类型的
     */
    public static final int TYPE_NET = 1001;
    /**
     * 本地直接连接类型（usb或者串口）
     */
    public static final int TYPE_LOCAL = 1002;
}
