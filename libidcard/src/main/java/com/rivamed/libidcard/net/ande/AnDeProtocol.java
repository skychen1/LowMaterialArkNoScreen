package com.rivamed.libidcard.net.ande;

/**
 * describe ：
 *
 * @author : Yich
 * date: 2019/10/28
 */
public class AnDeProtocol {

    public static final byte HEAD = (byte) 0XFA;
    private static final byte ADDRESS = (byte) 0XFF;
    public static final byte CMD_GET_ID = (byte) 0X04;
    public static final byte CMD_GET_ICARD_NUM = (byte) 0X31;


    /**
     * 按照协议组合数据
     *
     * @param pcb  指令类型
     * @param data 数据数据
     * @return 组合完成的指令数据
     */
    public static byte[] pieceCommand(byte pcb, byte[] data) {
        /**
         *  head  |  length |   地址 |  cmd  |  data    | check
         *  --------------------------------------------------------—
         *  1byte|  1byte |  1byte | 1byte  |n byte    | 2byte
         *
         * */
        //准备数组
        byte[] pieceBuf = new byte[(data == null ? 0 : data.length) + 3];
        //填入长度
        byte length = (byte) (pieceBuf.length + 2);
        pieceBuf[0] = length;
        //填入地址
        pieceBuf[1] = ADDRESS;
        //填入命令
        pieceBuf[2] = pcb;
        //如果有数据，填入数据
        if (data != null) {
            System.arraycopy(data, 0, pieceBuf, 3, data.length);
        }
        //计算crc校验
        byte[] crc = getCrc(pieceBuf);
        //准备最后的数据
        byte[] finalBuf = new byte[pieceBuf.length + 3];
        //填入头
        finalBuf[0] = HEAD;
        //填入数据
        System.arraycopy(pieceBuf, 0, finalBuf, 1, pieceBuf.length);
        //填入校验位
        System.arraycopy(crc, 0, finalBuf, pieceBuf.length + 1, crc.length);
        return finalBuf;
    }

    /**
     * 瓶装协议数据（特殊协议，不包含数据字段）
     *
     * @param data 数据
     * @return 组装好的协议数据
     */
    public static byte[] pieceCommand(byte[] data) {
        byte[] crc = AnDeProtocol.getCrc(data);
        byte[] sendBuf = new byte[3 + data.length];
        sendBuf[0] = AnDeProtocol.HEAD;
        System.arraycopy(data, 0, sendBuf, 1, data.length);
        System.arraycopy(crc, 0, sendBuf, 1 + data.length, crc.length);
        return sendBuf;
    }


    /**
     * 校验计算
     *
     * @param buffer 需要校验的数据
     * @return 返回值
     */
    public static byte[] getCrc(byte[] buffer) {
        int in = 0xffff;
        int poly = 0x8408;
        for (byte b : buffer) {
            in ^= ((int) b & 0x00ff);
            for (int j = 0; j < 8; j++) {
                if ((in & 0x0001) != 0) {
                    in >>= 1;
                    in ^= poly;
                } else {
                    in >>= 1;
                }
            }
        }
        //将int转成byte【】
        return intToU16Bytes(in);
    }

    /**
     * 将int转成byte[] 低位在前面
     *
     * @param i int 值
     * @return byte素组
     */
    private static byte[] intToU16Bytes(int i) {
        i &= 0xffff;
        byte[] ret = new byte[]{0x00, 0x00};
        ret[0] = (byte) ((i % 256) & 0xff);
        ret[1] = (byte) ((i / 256) & 0xff);
        return ret;
    }
}
