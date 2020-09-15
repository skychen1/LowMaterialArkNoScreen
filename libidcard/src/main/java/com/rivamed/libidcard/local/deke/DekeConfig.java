package com.rivamed.libidcard.local.deke;

/**
 * describe ： 德科ic卡配置类
 *
 * @author : Yich
 * date: 2019/7/22
 */
public class DekeConfig {
    public static final String PATH = "/dev/ttyS5";
    public static final int BAUD = 9600;
    public static final int MIN_LENGTH = 3;
    /**
     * 最多连续发送指令收不到数据次数，就认为设备出故障了
     */
    public static final int MAX_SEND_TIMES = 3;

    /**
     * 读取数据超时时间（发送指令后，连续多久没有读到消息）(ms)
     */
    public static final int READ_TIME_OUT = 2000;

}
