package com.rivamed.libidcard.net.yumin;

public class DataProtocol {

    /**
     * ID标识头
     */
    public static final byte BEGIN_FLAG_ID = (byte) 0x02;

    /**
     * 卡号头标识
     */
    public static final byte BEGIN_FLAG_NUM = (byte) 0x04;

    /**
     * 读到卡号的命令代号
     */
    public static final byte CMD_CARD_NUM = (byte) 0x02;

    /**
     * 查询读写器地址
     */
    public static final byte CMD_ADDRESS = (byte) 0xF9;

    /**
     * 校验数据的合法性
     *
     * @param buf 数据
     * @param len 数据长度
     */
    public static byte checkSum(byte[] buf, int len) {
        byte i;
        byte checksum;
        checksum = 0;
        for (i = 0; i < len; i++) {
            checksum ^= buf[i];
        }
        return (byte) ~checksum;
    }
}
