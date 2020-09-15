package com.ruihua.reader.net.rodinbell;


/**
 * describe ： 罗丹贝尔通信协议数据，指令集合
 * 数据协议部分（仅包含部分数据协议）
 * 在进行开发时，仍然需要参照厂家的开发文档
 *
 * @author : Yich
 * data: 2019/2/22
 */
public class DataProtocol {
    private DataProtocol() {
    }

    /**
     * BEGIN_FLAG  协议标识头（每个协议开始的标识）
     * CMD_GET_DEVICE_ID  获取设备的id（唯一标识，用来记录设备通道，发送指定消息）
     * CMD_SET_OUTPUT_POWER  设置输出功率
     * CMD_GET_OUTPUT_POWER  获取输出功率
     * CMD_RESET 重置设备
     * CMD_SET_WORK_ANT  设置工作天线天线
     * CMD_GET_WORK_ANT  获取当前工作天线（我们将这个协议用来当做心跳包）
     * CMD_GPIO_READ  gpio口读取数据指令
     * CMD_GPIO_WRITE gpio 写入数据指令
     * GET_RF_PORT_RETURN_LOSS 获取天线端口损耗
     */
    public static final byte BEGIN_FLAG = (byte) 0xA0;

    public static final byte CMD_GET_DEVICE_ID = (byte) 0x68;
    public static final byte CMD_SET_OUTPUT_POWER = (byte) 0x76;
    public static final byte CMD_GET_OUTPUT_POWER = (byte) 0x97;
    public static final byte CMD_RESET = (byte) 0x70;
    public static final byte CMD_SET_WORK_ANT = 0X74;
    public static final byte CMD_GET_WORK_ANT = 0X75;
    public static final byte CMD_GPIO_READ = (byte) 0x60;
    public static final byte CMD_GPIO_WRITE = (byte) 0x61;
    public static final byte GET_RF_PORT_RETURN_LOSS = 0x7E;
    /**
     * 罗丹贝尔的提供两个gpio口的控制， 3和4
     * 根据高低电平来控制锁和灯的开关
     */
    public static final byte GPIO3_WRITE = (byte) 0x03;
    public static final byte GPIO4_WRITE = (byte) 0x04;
    public static final byte GPIO_WRITE_LOW = (byte) 0x00;
    public static final byte GPIO_WRITE_HIGH = (byte) 0x01;
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
    public static final byte CMD_FAILED = 0X11;
    public static final byte ADDRESS = 0x01;
    public static final int MAX_HART_TIME = 4;
    protected static final byte[] ANTS = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07};

    /**
     * 按照协议组合数据
     *
     * @param address 地址，默认值
     * @param cmd     指令类型
     * @param data    数据数据
     * @return 组合完成的指令数据
     */
    public static byte[] pieceCommond(byte address, byte cmd, byte[] data) {
        /**
         *  head |  Len   |   Address |  cmd    |  data    | check
         *  --------------------------------------------------------
         *  1byte|  1byte |  1byte    |  1byte  |  N byte  |  1Byte
         *
         * */
        //前4 后1
        byte[] picecBuf = new byte[(data == null ? 0 : data.length) + 5];
        picecBuf[0] = BEGIN_FLAG;
        //len  从address开始计算直至结尾，不含len
        picecBuf[1] = (byte) ((picecBuf.length - 2) & 0xff);
        picecBuf[2] = (byte) (address & 0xff);
        picecBuf[3] = cmd;
        if (data != null) {
            System.arraycopy(data, 0, picecBuf, 4, data.length);
        }
        picecBuf[picecBuf.length - 1] = (byte) checkSum(picecBuf, 0, picecBuf.length - 1);
        return picecBuf;
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
