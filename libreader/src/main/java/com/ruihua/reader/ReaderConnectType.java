package com.ruihua.reader;

/**
 * describe ：  Reader的类型，(本地或者网络)
 *
 * @author : Yich
 * date: 2019/4/4
 */
public class ReaderConnectType {
    private ReaderConnectType() {

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
