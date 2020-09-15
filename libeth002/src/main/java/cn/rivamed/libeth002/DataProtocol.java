package cn.rivamed.libeth002;

public class DataProtocol {

    /**
     * 数据的标识头
     */
    public static final byte BEGIN_FLAG = (byte) 0xB0;
    /**
     * 设备自己数据标识
     */
    public static final byte CHAN_DEVICESELF = (byte) 0x00;
    /**
     *，锁数据
     */
    public static final byte CHAN_IO = (byte) 0x01;
    /**
     * 串口1 ID卡数据
     */
    public static final byte CHAN_SER1_HFREADER = (byte) 0x02;
    /**
     * 串口2  指纹数据
     */
    public static final byte CHAN_SER2_FINGER = (byte) 0x03;

    public static byte[] pieceCommond(byte[] buf) {
        byte[] picecBuf = new byte[buf.length + 4];
        //前三 后1
        int len1 = (buf.length + 1) / 256;
        int len2 = (buf.length + 1) % 256;
        picecBuf[0] = BEGIN_FLAG;
        picecBuf[1] = (byte) (len1 & 0xff);
        picecBuf[2] = (byte) (len2 & 0xff);
        System.arraycopy(buf, 0, picecBuf, 3, buf.length);
        picecBuf[picecBuf.length - 1] = (byte) checkSum(picecBuf, 0, picecBuf.length - 1);
        return picecBuf;
    }


    public static int checkSum(byte[] buf, int offset, int len) {
        int i = 0, uSum = 0;
        for (i = 0; i < len; i++) {
            int v = buf[i + offset];
            uSum += 0xff & v;
        }
        uSum = (~(uSum & 0xff)) + 1;
        return (uSum) & 0xff;
    }

    public static byte[] fingerCheckSum(byte[] buf, int offset, int len) {
        int uSum = 0;
        for (int i = 0; i < len; i++) {
            int v = buf[i + offset];
            uSum += 0xff & v;
        }
        byte[] ret = new byte[]{0x00, 0x00};
        ret[0] = (byte) ((uSum / 256) & 0xff);
        ret[1] = (byte) ((uSum % 256) & 0xff);
        return ret;
    }

}
