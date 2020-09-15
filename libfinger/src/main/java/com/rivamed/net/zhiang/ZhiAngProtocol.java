package com.rivamed.net.zhiang;

/**
 * describe ： 指昂协议工具类
 *
 * @author : Yich
 * date: 2019/10/11
 */
public class ZhiAngProtocol {
    private ZhiAngProtocol() {
    }

    /**
     * HEAD 协议头
     * ADDRESS 目标地址；
     */
    public static final byte[] HEAD = {(byte) 0xEF, (byte) 0x01};
    private static final byte[] ADDRESS = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};

    /**
     * 包标识：
     * BAG_TAG_CMD :指令包；
     * BAG_TAG_DATA： 数据包：且有后续包；数据包不能单独进入执行流程，必须跟在指令包或应答包后面。
     * BAG_TAG_ACK：表示是应答包（，可以有后续包。
     * BAG_TAG_END 表示是最后一个数据包，即结束包
     */
    public static final byte BAG_TAG_CMD = 0X01;
    public static final byte BAG_TAG_DATA = 0X02;
    public static final byte BAG_TAG_ACK = 0X07;
    public static final byte BAG_TAG_END = 0X08;

    /**
     * 协议指令
     * CMD_GET_IMG ：获取图像指令：探测手指，探测到后录入指纹图像存于ImageBuffer，并返回录入成功确认码。若探测不到手指，直接返回无手指确认码。
     * CMD_GEN_CHAR: 图像生成特征码指令：将ImageBuffer中的原始图像生成指纹特征,文件存于CharBuffer1或CharBuffer2。
     * CMD_REG_MODEL:合成指纹模板：将CharBuffer1与CharBuffer2中的特征文件合并生成模板，结果存于CharBuffer1与CharBuffer2(两者内容相同)。
     * CMD_UP_CHAR ：上传指纹模板或者特诊：将特征缓冲区CharBuffer1 或CharBuffer2 中的特征文件上传给上位机
     * CMD_GET_PARAM :获取系统参数，读取模块的状态寄存器和系统基本配置参数
     * CMD_GET_TEMP_NUM :获取模块中指纹模板底库个数，用来做为心跳包
     */
    public static final byte CMD_GET_IMG = 0X01;
    public static final byte CMD_GEN_CHAR = 0X02;
    public static final byte CMD_REG_MODEL = 0X05;
    public static final byte CMD_UP_CHAR = 0X08;
    public static final byte CMD_GET_PARAM = 0X0F;
    public static final byte CMD_GET_TEMP_NUM = 0X1D;


    /**
     * 按照协议组合数据
     *
     * @param bagTag 包标识
     * @param data   数据数据
     * @return 组合完成的指令数据
     */
    public static byte[] pieceCommand(byte bagTag, byte[] data) {
        /**
         *  head |   Address | bagTag | length |  data    | check
         *  --------------------------------------------------------
         * 2byte|  4byte    | 1byte  | 2byte  |  N byte  |  1Byte
         *
         * */

        byte[] buf = new byte[(data == null ? 0 : data.length) + 9];
        //添加头标识
        System.arraycopy(HEAD, 0, buf, 0, HEAD.length);
        //添加地址位
        System.arraycopy(ADDRESS, 0, buf, 2, ADDRESS.length);
        //添加包标识位
        buf[6] = bagTag;
        //长度(包含两位检验位)
        int length = (data == null ? 0 : data.length) + 2;
        byte[] length1 = getLength(length);
        System.arraycopy(length1, 0, buf, 7, length1.length);
        //数据
        if (data != null) {
            System.arraycopy(data, 0, buf, 9, data.length);
        }
        //获取校验位（只包含动态的包标识，数据长度和数据）
        byte[] bytes = checkSum(buf, 6, buf.length - 6);
        //添加校验位
        byte[] finalBuf = new byte[buf.length + bytes.length];
        System.arraycopy(buf, 0, finalBuf, 0, buf.length);
        System.arraycopy(bytes, 0, finalBuf, buf.length, bytes.length);
        return finalBuf;
    }

    /**
     * 将int类型的长度转换成2位的16进制数据
     *
     * @param length 长度
     * @return 长度
     */
    private static byte[] getLength(int length) {
        byte[] len = new byte[2];
        len[0] = (byte) ((length / 256) & 0xff);
        len[1] = (byte) ((length % 256) & 0xff);
        return len;
    }

    /**
     * 检验数据的合法性
     *
     * @param buf    数据
     * @param offset 起值
     * @param len    数据长度
     * @return 检验值
     */
    public static byte[] checkSum(byte[] buf, int offset, int len) {
        int uSum = 0;
        for (int i = 0; i < len; i++) {
            int v = buf[i + offset];
            uSum += 0xff & v;
        }
        //取后16位（根据协议文档，只要两个字节）
        uSum = uSum & 0xffff;
        //分成两位返回
        byte[] result = new byte[2];
        int a = uSum / 256;
        result[0] = (byte) a;
        int b = uSum % 256;
        result[1] = (byte) b;
        return result;
    }
}
