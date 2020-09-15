package com.rivamed.libidcard.local.deke;

public class DataProtocol {

    /**
     * HEAD 头标识
     * CMD_SEND_READ 开始读取指令
     * CMD_RECEIVE_READ 开始读取收到的指令
     * CMD_RECEIVE_CARD 接收到卡号
     */
    public static final byte HEAD = (byte) 0xAA;
    public static final byte CMD_SEND_READ = (byte) 0x95;
    public static final byte CMD_RECEIVE_READ = (byte) 0xFE;
    public static final byte CMD_RECEIVE_CARD = (byte) 0x01;

    /**
     * 按照协议组合数据
     *
     * @param cmd  指令类型
     * @param data 数据数据
     * @return 组合完成的指令数据
     */
    public static byte[] pieceCommand(byte cmd, byte[] data) {
        /**
         *  head  | length（data的长度）| cmd     |   data
         *  --------------------------------------------------------————————————————
         *  1byte|   1byte             |  1byte |  N byte
         *
         * */
        //前3
        byte[] pieceBuf = new byte[(data == null ? 0 : data.length) + 3];
        //加上标识头
        pieceBuf[0] = HEAD;
        //长度
        int length = (data == null ? 0 : data.length) + 1;
        pieceBuf[1] = (byte) (length & 0xff);
        //命令集
        pieceBuf[2] = cmd;
        //数据
        if (data != null) {
            System.arraycopy(data, 0, pieceBuf, 3, data.length);
        }
        return pieceBuf;
    }

}
