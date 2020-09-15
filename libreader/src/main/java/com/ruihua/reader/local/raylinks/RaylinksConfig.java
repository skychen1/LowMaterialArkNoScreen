package com.ruihua.reader.local.raylinks;

/**
 * describe ： 睿芯联科配置信息类
 *
 * @author : Yich
 * date: 2019/3/22
 */
public class RaylinksConfig {
    private RaylinksConfig() {
    }

    /**
     * 连接设备的端口
     */
    public static final String COM_STR = "/dev/ttyUSB0";
    /**
     * 设备的波特率
     */
    public static final int BAND_RATE = 57600;

    /**
     * 连接设备的特征值
     */
    public static final byte FLAG_CRC = 0X00;
    /**
     * 扫描的  。Q 值范围0-15，RLM 使用默认Q 值为3
     */
    public static final byte INIT_Q = 3;
}
