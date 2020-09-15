package com.ruihua.libconsumables;

/**
 * describe ：高值控制板协议（试用与公司内部所有设备）
 *
 * @author : Yich
 * date: 2019/9/10
 */
public class ControlBoardProtocol {
    private ControlBoardProtocol() {
    }

    public static final byte HEAD_1 = (byte) 0xFC;
    public static final byte HEAD_2 = (byte) 0xFC;
    private static final byte BAG_TAG = 0X00;
    private static final byte[] ADDRESS_FROM = {0x00, 0x00};
    private static final byte[] ADDRESS_TO = {0x00, 0x00};

    /**
     * GET_VERSION 获取设备版本号
     * RESTART 重启设备
     * GET_WORK_MODE 获取工作模式
     * CLEAN_DATA 擦除数据
     * SEND_UPDATE 发送升级文件数据
     * UPDATE_CHECK 升级校验命令
     * UPDATE_LOAD  升级加载
     * HART_BEAT  心跳
     * CONTROL  设备控制协议
     */
    public static final byte GET_VERSION = 0x02;
    public static final byte RESTART = 0x09;
    public static final byte GET_WORK_MODE = 0x0C;
    public static final byte CLEAN_DATA = 0x20;
    public static final byte SEND_UPDATE = 0x21;
    public static final byte UPDATE_CHECK = 0x22;
    public static final byte UPDATE_LOAD = 0x23;
    public static final byte HART_BEAT = 0x08;
    public static final byte CONTROL = 0x05;


    /**
     * 按照协议组合数据
     *
     * @param cmd  指令类型
     * @param data 数据数据
     * @return 组合完成的指令数据
     */
    public static byte[] pieceCommand(byte cmd, byte[] data) {
        /**
         *  head  |  cmd |   包的标识 |  原地址 | 目标地址 | length（data的长度）|  data    | check
         *  --------------------------------------------------------————————————————
         *  2byte|  1byte |  1byte |   2byte   | 2byte    | 2byte              |  N byte  |  2Byte
         *
         * */
        //7个数据
        byte[] pieceBuf = new byte[(data == null ? 0 : data.length) + 10];
        //加上标识头
        pieceBuf[0] = HEAD_1;
        pieceBuf[1] = HEAD_2;
        //命令集
        pieceBuf[2] = cmd;
        //包标识
        pieceBuf[3] = BAG_TAG;
        //地址
        System.arraycopy(ADDRESS_FROM, 0, pieceBuf, 4, 2);
        System.arraycopy(ADDRESS_TO, 0, pieceBuf, 6, 2);
        //长度
        int length = data == null ? 0 : data.length;
        byte[] length1 = getLength(length);
        System.arraycopy(length1, 0, pieceBuf, 8, length1.length);
        //数据
        if (data != null) {
            System.arraycopy(data, 0, pieceBuf, 10, data.length);
        }
        //尾部校验值
        byte[] bytes = getCrc(pieceBuf);
        byte[] finalBuf = new byte[pieceBuf.length + bytes.length];
        System.arraycopy(pieceBuf, 0, finalBuf, 0, pieceBuf.length);
        System.arraycopy(bytes, 0, finalBuf, pieceBuf.length, bytes.length);
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
     * crc 校验计算
     *
     * @param buf 需要校验的数据
     * @return 校验值
     */
    public static byte[] getCrc(byte[] buf) {
        int crcReg = 0xffff;
        boolean flag = true;
        for (byte b : buf) {
            int dcdBitMask = 0x80;
            if (flag) {
                flag = false;
                continue;
            }
            for (int i = 0; i < 8; i++) {
                boolean bit1 = ((crcReg >> 15 & 1) == 1);
                crcReg = crcReg * 2;
                crcReg = crcReg & 0xffff;
                if (((b & dcdBitMask) == dcdBitMask)) {
                    crcReg |= 1;
                }
                if (bit1) {
                    crcReg = crcReg ^ 0x1021;
                }
                dcdBitMask = dcdBitMask / 2;
            }
        }

        return intToU16Bytes(crcReg);
    }

    /**
     * 将int转成byte【】
     *
     * @param i int 值
     * @return byte素组
     */
    private static byte[] intToU16Bytes(int i) {
        i &= 0xffff;
        byte[] ret = new byte[]{0x00, 0x00};
        ret[0] = (byte) ((i / 256) & 0xff);
        ret[1] = (byte) ((i % 256) & 0xff);
        return ret;
    }
}
