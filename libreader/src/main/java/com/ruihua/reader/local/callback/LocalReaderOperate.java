package com.ruihua.reader.local.callback;

/**
 * describe ： 本地reader操作接口
 *
 * @author : Yich
 * date: 2019/3/22
 */
public interface LocalReaderOperate {

    /**
     * 连接设备
     * @param port 端口号
     * @param baud 波特率
     * @return 返回码
     */
    int connect(String port, int baud);

    /**
     * 与设备断开连接
     *
     * @return 返回码
     */
    int disConnect();

    /**
     * 设置功率
     *
     * @param powerInt 功率值
     * @return 返回码
     */
    int setPower(int powerInt);

    /**
     * 获取功率
     *
     * @return 返回码
     */
    int getPower();

    /**
     * 检测设备天线
     *
     * @return 返回码
     */
    int checkAnt();

    /**
     * 获取频率
     *
     * @return 操作返回码
     */
    int getFrequency();

    /**
     * 开始扫描标签
     *
     * @param timeOut 超时时间
     * @return 操作返回码
     */
    int startScan(int timeOut);

    /**
     * 停止扫描
     *
     * @return 操作返回码
     */
    int stopScan();

    /**
     * 重新开始扫描
     *
     * @return 操作返回码
     */
    int reScan();

    /**
     * 重置设备
     *
     * @return 重置
     */
    int reset();

    /**
     * 删除扫描到的标签中的一个标签，从而让这个标签能够再次扫描到
     *
     * @param epc 标签
     * @return 返回码
     */
    int delOneEpc(String epc);

    /**
     * 获取设备的厂家
     *
     * @return 厂家
     */
    String getProducer();

    /**
     * 获取设备的版本
     *
     * @return 版本
     */
    String getVersion();


}
