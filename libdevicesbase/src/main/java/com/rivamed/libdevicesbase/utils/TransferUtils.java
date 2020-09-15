package com.rivamed.libdevicesbase.utils;


import android.util.Base64;

/**
 * byte转换工具类
 *
 * @author : YiCH
 * @date : 2018/8/30 0030.
 */
public class TransferUtils {
    private TransferUtils() {
    }

    /**
     * byte【】转String
     *
     * @param buf 原始字节数组
     * @return 字符串
     */
    public static String Byte2String(byte[] buf) {
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < buf.length; i++) {
            int v = buf[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();
    }

    /**
     * byte【】转String(AscII码)
     *
     * @param buf 原始字节数组
     * @return 字符串
     */
    public static String Byte2StringAscII(byte[] buf) {
        if (buf == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : buf) {
            stringBuilder.append((char) b);
        }
        return stringBuilder.toString().toUpperCase();
    }

    /**
     * 字符串转成字节数组
     *
     * @param base64Str 字符串
     * @return 字节数组
     */
    public static byte[] base64String2ByteFun(String base64Str) {
        return Base64.decode(base64Str, Base64.DEFAULT);
    }

    /**
     * byte[]数组转字符串
     *
     * @param b 字符串
     * @return 字节数组
     */
    public static String byte2Base64StringFun(byte[] b) {
        return Base64.encodeToString(b, Base64.DEFAULT);
    }
}