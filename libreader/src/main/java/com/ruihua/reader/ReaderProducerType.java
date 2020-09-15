package com.ruihua.reader;

/**
 * describe ： reader类型
 * 规则： 0测试类型，
 * 100-199之间是网络连接类型；
 * 200-299 之间是本地累计额类型
 *
 * @author : Yich
 * date: 2019/4/4
 */
public class ReaderProducerType {

    private ReaderProducerType() {
    }

    public static final int TYPE_TEST = 0;
    /**
     * 罗丹贝尔类型
     */
    public static final int TYPE_NET_RODINBELL = 101;
    /**
     * 鸿陆类型
     */
    public static final int TYPE_NET_COLU = 102;
    /**
     * 睿芯联科
     */
    public static final int TYPE_LOCAL_RAYLINKS = 201;
    /**
     * 芯联串口连接reader
     */
    public static final int TYPE_LOCAL_CORELINKS = 202;
    /**
     * 芯联串口连接reader（连接两个串口的reader的时候使用）
     */
    public static final int TYPE_LOCAL_CORELINKS_TWO = 203;
    /**
     * 罗丹贝尔，串口连接
     */
    public static final int TYPE_LOCAL_RODINBELL = 204;

}
