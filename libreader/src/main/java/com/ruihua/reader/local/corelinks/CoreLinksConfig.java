package com.ruihua.reader.local.corelinks;

/**
 * describe ：芯联设备配置
 *
 * @author : Yich
 * date: 2019/4/18
 */
public class CoreLinksConfig {
    private CoreLinksConfig() {
    }

    /**
     * 连接设备的端口
     */
    public static final String READER_COM_STR = "/dev/ttyUSB20";
    public static final int READER_BOUND = 115200;

    /**
     * 几根天线的设备
     */
    public static final int PORT_NUM = 8;



    /**
     * 最多连续发送指令收不到数据次数，就认为设备出故障了
     */
    public static final int MAX_SEND_TIMES = 3;
    /**
     * 发送指令以后读取延迟的时间(ms)
     */
    public static final int READ_DELAY_TIME = 100;
    /**
     * 读取数据超时时间（发送指令后，连续多久没有读到消息）(ms)
     */
    public static final int READ_TIME_OUT = 2000;


}
