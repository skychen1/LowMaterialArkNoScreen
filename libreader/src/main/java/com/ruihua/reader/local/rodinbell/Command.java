package com.ruihua.reader.local.rodinbell;

/**
 * describe ： 指令集
 *
 * @author : Yich
 * date: 2019/7/23
 */
public class Command {
    private Command() {
    }

    public static final byte HEAD = (byte) 0xA0;
    public static final byte CMD_GET_DEVICE_ID = (byte) 0x68;
    public static final byte CMD_SET_OUTPUT_POWER = (byte) 0x76;
    public static final byte CMD_GET_OUTPUT_POWER = (byte) 0x77;
    public static final byte CMD_RESET = (byte) 0x70;
    public static final byte CMD_SET_WORK_ANT = 0X74;
    public static final byte CMD_GET_WORK_ANT = 0X75;
    public static final byte GET_RF_PORT_RETURN_LOSS = 0x7E;

    /**
     * CMD_REALTIME_INVENTORY 指定天线盘存（速度最快，还有其他方式的盘存，具体开协议文档），与CMD_SET_WORK_ANT 配合
     * 盘存模式的指令 都是默认值
     * RTINVENTORY_SESSION //此命令是0x8B盘存模式的SESSION指令
     * RTINVENTORY_TARGET  //此命令是0x8B盘存模式的TARGET指令
     * RTINVENTORY_REPEAT //注：此命令为0x8B盘存模式的repeat指令，0X01的时候，盘存最快，用的时间最短
     */
    public static final byte CMD_REALTIME_INVENTORY = (byte) 0x8B;
    public static final byte RTINVENTORY_SESSION = (byte) 0x01;
    public static final byte RTINVENTORY_TARGET = (byte) 0x00;
    public static final byte RTINVENTORY_REPEAT = (byte) 0x01;
    /**
     * CMD_SUCCESS  CMD_FAILED命令操作成功与失败
     * ADDRESS :485地址，默认设置位0x01
     * MAX_HART_TIME 最多多少次发送心跳收不到回复就认为断开
     * ANTS 天线的根数（默认8根）
     */
    public static final byte CMD_SUCCESS = 0X10;
    public static final byte ADDRESS = 0x01;
    protected static final byte[] ANTS = {0x00, 0x01, 0x02, 0x03};

    public static byte[] getCommand(byte cmd, byte[] data) {
        /**
         *  head |  Len   |   Address |  cmd    |  data    | check
         *  --------------------------------------------------------
         *  1byte|  1byte |  1byte    |  1byte  |  N byte  |  1Byte
         *
         * */
        //前4 后1
        byte[] buf = new byte[(data == null ? 0 : data.length) + 5];
        buf[0] = HEAD;
        //len  从address开始计算直至结尾，不含len
        buf[1] = (byte) ((buf.length - 2) & 0xff);
        buf[2] = (byte) (ADDRESS & 0xff);
        buf[3] = cmd;
        if (data != null) {
            System.arraycopy(data, 0, buf, 4, data.length);
        }
        buf[buf.length - 1] = (byte) checkSum(buf, 0, buf.length - 1);
        return buf;
    }

    /**
     * 检验数据的合法性（罗丹贝尔的）
     *
     * @param buf    数据
     * @param offset 起值
     * @param len    数据长度
     * @return 检验值
     */
    public static int checkSum(byte[] buf, int offset, int len) {
        int uSum = 0;
        for (int i = 0; i < len; i++) {
            int v = buf[i + offset];
            uSum += 0xff & v;
        }
        uSum = (~(uSum & 0xff)) + 1;
        return (uSum) & 0xff;
    }

}
