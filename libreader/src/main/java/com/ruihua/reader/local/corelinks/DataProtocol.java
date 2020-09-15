package com.ruihua.reader.local.corelinks;

/**
 * describe ：
 *
 * @author : Yich
 * date: 2019/4/19
 */
public class DataProtocol {
    private DataProtocol() {
    }

    /**
     * HEAD  HEAD_2 协议头（2位）
     * RECEIVE_STATE_OK 收到协议的正确状态
     * CMD_DEVICE_ID   设备id指令
     * CMD_INVENTORY  盘存命令
     */
    public static final byte HEAD = (byte) 0xFF;
    protected static final byte[] RECEIVE_STATE_OK = new byte[]{0x00, 0x00};
    public static final byte CMD_RUN_APP = (byte) 0x04;
    public static final byte CMD_DEVICE_ID = (byte) 0x10;
    public static final byte CMD_GET_ANT_INFO = (byte) 0x61;
    public static final byte CMD_SET_ANT_INFO = (byte) 0x91;
    public static final byte CMD_INVENTORY = (byte) 0xAA;


    /**
     * 按照协议组合数据
     *
     * @param cmd  指令类型
     * @param data 数据数据
     * @return 组合完成的指令数据
     */
    public static byte[] pieceCommand(byte cmd, byte[] data) {
        /**
         *  head  | length（data的长度）| cmd     |   data    | check
         *  --------------------------------------------------------————————————————
         *  1byte|   1byte             |  1byte |  N byte   |  2Byte
         *
         * */
        //前3
        byte[] pieceBuf = new byte[(data == null ? 0 : data.length) + 3];
        //加上标识头
        pieceBuf[0] = HEAD;
        //长度
        int length = data == null ? 0 : data.length;
        pieceBuf[1] = (byte) (length & 0xff);
        //命令集
        pieceBuf[2] = cmd;
        //数据
        if (data != null) {
            System.arraycopy(data, 0, pieceBuf, 3, data.length);
        }
        //拼装尾部校验值
        byte[] crc = calcCRC(pieceBuf);
        byte[] finalBuf = new byte[pieceBuf.length + crc.length];
        System.arraycopy(pieceBuf, 0, finalBuf, 0, pieceBuf.length);
        System.arraycopy(crc, 0, finalBuf, pieceBuf.length, crc.length);
        return finalBuf;
    }


    public static byte[] calcCRC(byte[] buf) {
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

    /**
     * 检验数据的合法性（罗丹贝尔的）
     *
     * @param buf    数据
     * @param offset 起值
     * @param len    数据长度
     * @return 检验值
     */
    public static byte checkSum(byte[] buf, int offset, int len) {
        int uSum = 0;
        for (int i = 0; i < len; i++) {
            int v = buf[i + offset];
            uSum += 0xff & v;
        }
        uSum = (~(uSum & 0xff)) + 1;
        uSum = (uSum) & 0xff;
        return (byte) ((uSum % 256) & 0xff);
    }

}


