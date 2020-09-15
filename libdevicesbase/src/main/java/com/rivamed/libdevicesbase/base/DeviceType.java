package com.rivamed.libdevicesbase.base;

/**
 * describe ： 设备的种类静态常量
 *
 * @author : Yich
 * date: 2019/11/21
 */
public class DeviceType {
    private DeviceType() {

    }

    /**
     * DEVICE_TYPE_RFID_READER rfid 扫描设备
     * DEVICE_TYPE_ID_READER id(ic)卡
     * DEVICE_TYPE_FINGER  指纹仪
     * DEVICE_TYPE_CONSUMABLE   3.0高值控制板
     * DEVICE_TYPE_ETH002 eth002（li模块er）
     */
    public static final int DEVICE_TYPE_RFID_READER = 1;
    public static final int DEVICE_TYPE_ID_READER = 2;
    public static final int DEVICE_TYPE_FINGER = 3;
    public static final int DEVICE_TYPE_CONSUMABLE = 4;
    public static final int DEVICE_TYPE_ETH002 = 5;


}
