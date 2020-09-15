package com.ruihua.libconsumables;

/**
 * describe ： 高值控制板，配置类
 *
 * @author : Yich
 * date: 2019/9/11
 */
public class ConsumableConfig {

    private ConsumableConfig() {

    }

    /**
     * 最大重发次数
     */
    public static final int MAX_SEND_TIMES = 3;
    /**
     * 超时重发数据时间(ms)
     */
    public static final int RESEND_DATA_DELAY_TIME = 15 * 1000;
    /**
     * 发送升级包，单包数据长度 (512byte)
     */
    public static final int UPDATE_DATA_LENGTH = 512;
}
