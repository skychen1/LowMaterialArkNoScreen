package com.rivamed.local.zhiang;

/**
 * describe ： 指纹数据处理工具类
 *
 * @author : Yich
 * date: 2019/5/15
 */
public class CharUtils {
    private CharUtils() {
    }

    /**
     * byte素组转16进制字符串
     *
     * @param val byte数组
     * @param len 需要前多少位
     * @return 字符串
     */
    public static String byteToHexString(byte[] val, int len) {
        String temp = "";
        for (int i = 0; i < len; i++) {
            String hex = Integer.toHexString(0xff & val[i]);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            temp += hex.toUpperCase();
        }
        return temp;
    }
}
