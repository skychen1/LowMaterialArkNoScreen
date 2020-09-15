package com.rivamed.net.sanneng;

import java.util.Arrays;

public class DataProtocol {

    private DataProtocol(){
    }

    protected static final int MAX_DATA_LEN = 498;
    protected static final int IMAGE_DATA_UNIT = 496;
    protected static final int ID_NOTE_SIZE = 64;
    protected static final int MODULE_SN_LEN = 16;

    private static final int SCSI_TIMEOUT = 5000; //ms
    private static final int COMM_SLEEP_TIME = 40;    //ms

    private static final int CMD_PACKET_LEN = 26;
    private static final int RCM_PACKET_LEN = 26;
    private static final int RCM_DATA_OFFSET = 10;
    //源标识（1byte） Soruce Device ID；目标标识（1byte） Destination Device ID
    protected static byte m_bySrcDeviceID = 1, m_byDstDeviceID = 1;

    /***************************************************************************/
    /***************************************************************************/
    private static final int CMD_PREFIX_CODE = 0xAA55;
    private static final int CMD_DATA_PREFIX_CODE = 0xA55A;
    private static final int RCM_PREFIX_CODE = 0x55AA;
    protected static final int RCM_DATA_PREFIX_CODE = 0x5AA5;

    /***************************************************************************
     * System Code (0x0000 ~ 0x001F, 0x0000 : Reserved)
     ***************************************************************************/
    private static final short CMD_TEST_CONNECTION = 0x0001;
    private static final short CMD_SET_PARAM = 0x0002;
    protected static final short CMD_GET_PARAM = 0x0003;
    private static final short CMD_GET_DEVICE_INFO = 0x0004;
    private static final short CMD_ENTER_ISPMODE = 0x0005;
    private static final short CMD_SET_ID_NOTE = 0x0006;
    private static final short CMD_GET_ID_NOTE = 0x0007;
    private static final short CMD_SET_MODULE_SN = 0x0008;
    private static final short CMD_GET_MODULE_SN = 0x0009;

    /***************************************************************************
     * Sensor Code (0x0020 ~ 0x003F)
     ***************************************************************************/
    protected static final short CMD_GET_IMAGE = 0x0020;
    private static final short CMD_FINGER_DETECT = 0x0021;
    private static final short CMD_UP_IMAGE = 0x0022;
    private static final short CMD_DOWN_IMAGE = 0x0023;
    private static final short CMD_SLED_CTRL = 0x0024;

    /***************************************************************************
     * Template Code (0x0040 ~ 0x005F)
     ***************************************************************************/
    private static final short CMD_STORE_CHAR = 0x0040;
    private static final short CMD_LOAD_CHAR = 0x0041;
    protected static final short CMD_UP_CHAR = 0x0042;
    protected static final short CMD_DOWN_CHAR = 0x0043;
    private static final short CMD_DEL_CHAR = 0x0044;
    private static final short CMD_GET_EMPTY_ID = 0x0045;
    private static final short CMD_GET_STATUS = 0x0046;
    private static final short CMD_GET_BROKEN_ID = 0x0047;
    private static final short CMD_GET_ENROLL_COUNT = 0x0048;

    /***************************************************************************
     * FingerPrint Alagorithm Code (0x0060 ~ 0x007F)
     ***************************************************************************/
    protected static final short CMD_GENERATE = 0x0060;
    protected static final short CMD_MERGE = 0x0061;
    protected static final short CMD_MATCH = 0x0062;
    private static final short CMD_SEARCH = 0x0063;
    private static final short CMD_VERIFY = 0x0064;

    /***************************************************************************
     * Unknown Command
     ***************************************************************************/
    private static final short RCM_INCORRECT_COMMAND = 0x00FF;

    /***************************************************************************
     * Error Code
     ***************************************************************************/
    protected static final int ERR_SUCCESS = 0;
    protected static final int ERR_FAIL = 1;
    protected static final int ERR_CONNECTION = 2;
    protected static final int ERR_VERIFY = 0x10;
    protected static final int ERR_IDENTIFY = 0x11;
    protected static final int ERR_TMPL_EMPTY = 0x12;
    protected static final int ERR_TMPL_NOT_EMPTY = 0x13;
    protected static final int ERR_ALL_TMPL_EMPTY = 0x14;
    protected static final int ERR_EMPTY_ID_NOEXIST = 0x15;
    protected static final int ERR_BROKEN_ID_NOEXIST = 0x16;
    protected static final int ERR_INVALID_TMPL_DATA = 0x17;
    protected static final int ERR_DUPLICATION_ID = 0x18;
    protected static final int ERR_BAD_QUALITY = 0x19;
    protected static final int ERR_MERGE_FAIL = 0x1A;
    protected static final int ERR_NOT_AUTHORIZED = 0x1B;
    protected static final int ERR_MEMORY = 0x1C;
    protected static final int ERR_INVALID_TMPL_NO = 0x1D;
    protected static final int ERR_INVALID_PARAM = 0x22;
    protected static final int ERR_GEN_COUNT = 0x25;
    protected static final int ERR_INVALID_BUFFER_ID = 0x26;
    protected static final int ERR_INVALID_OPERATION_MODE = 0x27;
    protected static final int ERR_FP_NOT_DETECTED = 0x28;

    /***************************************************************************
     * Make Command Packet 制作命令包
     ***************************************************************************/

    public static byte[] InitCmdPacket(short wCMDCode, byte[] pbyData, int nDataLen) {
        byte[] m_abyPacket = new byte[64 * 1024];
        short w_wCheckSum;
        int CMD_PREFIX_CODE = 0xAA55;//头部
        int CMD_PACKET_LEN = 26;//长度

        //byte[] buf=DataProtocol.InitCmdPacket(DataProtocol.CMD_MERGE,  w_abyData, 3);

        Arrays.fill(m_abyPacket, 0, CMD_PACKET_LEN, (byte) 0);
        //g_pCmdPacket->m_wPrefix = CMD_PREFIX_CODE;
        m_abyPacket[0] = (byte) (CMD_PREFIX_CODE & 0xFF);
        m_abyPacket[1] = (byte) ((CMD_PREFIX_CODE >> 8) & 0xFF);

        //g_pCmdPacket->m_bySrcDeviceID = p_bySrcDeviceID;
        m_abyPacket[2] = m_bySrcDeviceID;

        //g_pCmdPacket->m_byDstDeviceID = p_byDstDeviceID;
        m_abyPacket[3] = m_byDstDeviceID;

        //g_pCmdPacket->m_wCMDCode = p_wCMDCode;
        m_abyPacket[4] = (byte) (wCMDCode & 0xFF);
        m_abyPacket[5] = (byte) ((wCMDCode >> 8) & 0xFF);

        //g_pCmdPacket->m_wDataLen = p_wDataLen;
        m_abyPacket[6] = (byte) (nDataLen & 0xFF);
        m_abyPacket[7] = (byte) ((nDataLen >> 8) & 0xFF);

        if (nDataLen > 0)
            //memcpy(g_pCmdPacket->m_abyData, p_pbyData, wDataLen);
            System.arraycopy(pbyData, 0, m_abyPacket, 8, nDataLen);

        w_wCheckSum = CalcChkSumOfPkt(m_abyPacket, CMD_PACKET_LEN - 2);

        //g_pCmdPacket->m_wCheckSum = w_wCheckSum;
        m_abyPacket[24] = (byte) (w_wCheckSum & 0xFF);
        m_abyPacket[25] = (byte) ((w_wCheckSum >> 8) & 0xFF);

        byte[] rebyte = new byte[CMD_PACKET_LEN];
        System.arraycopy(m_abyPacket, 0, rebyte, 0, CMD_PACKET_LEN);
        return rebyte;
    }

    /***************************************************************************
     * Get 2bytes packet checksum(pDataPkt[0] + pDataPkt[1] + ....)
     ***************************************************************************/
    private static short CalcChkSumOfPkt(byte[] pDataPkt, int nSize) {
        int i, nChkSum = 0;

        for (i = 0; i < nSize; i++) {
            if ((int) pDataPkt[i] < 0)
                nChkSum = nChkSum + ((int) pDataPkt[i] + 256);
            else
                nChkSum = nChkSum + pDataPkt[i];
        }

        return (short) nChkSum;
    }


    /***************************************************************************
     * Make Data Packet 制作数据包
     ***************************************************************************/
    public static byte[] InitCmdDataPacket(short wCMDCode, byte[] pbyData, int nDataLen) {
        byte[] m_abyPacket = new byte[64 * 1024];
        short w_wCheckSum;

        //g_pCmdPacket->m_wPrefix = CMD_DATA_PREFIX_CODE;
        m_abyPacket[0] = (byte) (CMD_DATA_PREFIX_CODE & 0xFF);
        m_abyPacket[1] = (byte) ((CMD_DATA_PREFIX_CODE >> 8) & 0xFF);

        //g_pCmdPacket->m_bySrcDeviceID = p_bySrcDeviceID;
        m_abyPacket[2] = m_bySrcDeviceID;

        //g_pCmdPacket->m_byDstDeviceID = p_byDstDeviceID;
        m_abyPacket[3] = m_byDstDeviceID;

        //g_pCmdPacket->m_wCMDCode = p_wCMDCode;
        m_abyPacket[4] = (byte) (wCMDCode & 0xFF);
        m_abyPacket[5] = (byte) ((wCMDCode >> 8) & 0xFF);

        //g_pCmdPacket->m_wDataLen = p_wDataLen;
        m_abyPacket[6] = (byte) (nDataLen & 0xFF);
        m_abyPacket[7] = (byte) ((nDataLen >> 8) & 0xFF);

        //memcpy(&g_pCmdPacket->m_abyData[0], p_pbyData, p_wDataLen);
        System.arraycopy(pbyData, 0, m_abyPacket, 8, nDataLen);

        //. Set checksum
        w_wCheckSum = CalcChkSumOfPkt(m_abyPacket, nDataLen + 8);

        m_abyPacket[nDataLen + 8] = (byte) (w_wCheckSum & 0xFF);
        m_abyPacket[nDataLen + 9] = (byte) ((w_wCheckSum >> 8) & 0xFF);


        byte[] rebyte = new byte[510];
        System.arraycopy(m_abyPacket, 0, rebyte, 0, rebyte.length);
        return rebyte;

    }

    public static byte LOBYTE(short s) {
        return (byte) (s & 0xFF);
    }

    public static byte HIBYTE(short s) {
        return (byte) ((s >> 8) & 0xFF);
    }

    public static short MAKEWORD(byte low, byte high) {
        short s;
        s = (short) ((int) ((high << 8) & 0x0000FF00) | (int) (low & 0x000000FF));
        return s;
    }

    //获取RET返回码CODE
    public static short GetRetCode(byte[] readBytes) {
        return (short) ((int) ((readBytes[9] << 8) & 0x0000FF00) | (int) (readBytes[8] & 0x000000FF));
    }

    //获取RET返回码对应的解释
    public static String GetRetName(short retCode) {
        String retName = "指令处理成功";//ERR_SUCCESS
        switch (retCode) {
            case 0x00:
                break;//ERR_SUCCESS
            case 0x01:
                retName = "指令处理失败";//ERR_FAIL
                break;
            case 0x10:
                retName = "与指定编号中 Template 的 1:1比对失败";//ERR_VERIFY
                break;
            case 0x11:
                retName = "已进行 1:N 比对，但相同 Template 不存在。";//ERR_IDENTIFY
                break;
            case 0x12:
                retName = "在指定编号中不存在已注册的 Template 。";//ERR_TMPL_EMPTY
                break;
            case 0x13:
                retName = "在指定编号中已存在 Template 。";//ERR_TMPL_NOT_EMPTY
                break;
            case 0x14:
                retName = "不存在已注册的 Template 。"; //ERR_ALL_TMPL_EMPTY
                break;
            case 0x15:
                retName = "不存在可注册的 Template ID 。";//ERR_EMPTY_ID_NOEXIST
                break;
            case 0x16:
                retName = "不存在已损坏的 Template 。";//ERR_BROKEN_ID_NOEXIST
                break;
            case 0x17:
                retName = "指定的 Template Data 无效";//ERR_INVALID_TMPL_DATA
                break;
            case 0x18:
                retName = "该指纹已注册。";//ERR_DUPLICATION_ID
                break;
            case 0x19:
                retName = "指纹图像质量不好";//ERR_BAD_QUALITY
                break;
            case 0x1A://0x1A
                retName = "Template 合成失败";//ERR_MERGE_FAIL
                break;
            case 0x1B:
                retName = "没有进行通讯密码确认。";//ERR_NOT_AUTHORIZED
                break;
            case 0x1C:
                retName = "外部Flash 烧写出错。";//ERR_MEMORY
                break;
            case 0x1D:
                retName = "指定 Template 编号无效。";//ERR_INVALID_TMPL_NO
                break;
            case 0x22:
                retName = "使用了不正确的参数。";//ERR_INVALID_PARAM
                break;
            case 0x23:
                retName = "在TimeOut时间内没有输入指纹。";//ERR_TIME_OUT。
                break;
            case 0x25:
                retName = "指纹合成个数无效";//ERR_GEN_COUNT
                break;
            case 0x26:
                retName = "Buffer ID 值不正确。";//ERR_INVALID_BUFFER_ID
                break;
            case 0x28:
                retName = "采集器上没有指纹输入";//ERR_FP_NOT_DETECTED
                break;
            case 0x41:
                retName = "指令被取消。";//ERR_FP_CANCEL
                break;


        }
        return retName;
    }

    /***************************************************************************
     * Check Packet
     ***************************************************************************/
    public static boolean checkReceive(byte[] pbyPacket, int nPacketLen, short wPrefix, short wCMDCode) {
        short w_wCalcCheckSum, w_wCheckSum, w_wTmp;

        //. Check prefix code
        w_wTmp = (short) ((int) ((pbyPacket[1] << 8) & 0x0000FF00) | (int) (pbyPacket[0] & 0x000000FF));

        if (wPrefix != w_wTmp) {
            return false;
        }

        //. Check checksum
        w_wCheckSum = (short) ((int) ((pbyPacket[nPacketLen - 1] << 8) & 0x0000FF00) | (int) (pbyPacket[nPacketLen - 2] & 0x000000FF));

        w_wCalcCheckSum = CalcChkSumOfPkt(pbyPacket, nPacketLen - 2);

        if (w_wCheckSum != w_wCalcCheckSum) {
            return false;
        }

        //. Check Command Code
        w_wTmp = (short) ((int) ((pbyPacket[5] << 8) & 0x0000FF00) | (int) (pbyPacket[4] & 0x000000FF));
        if (wCMDCode != w_wTmp) {
            return false;
        }

        return true;
    }


}
