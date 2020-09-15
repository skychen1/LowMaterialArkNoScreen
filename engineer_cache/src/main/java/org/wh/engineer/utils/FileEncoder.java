package org.wh.engineer.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 文件编码
 */
public class FileEncoder {
    /**
     * 根据文件获取base64 utf-8编码字符串
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String encodeFileToBase64String(String filePath) throws Exception {
        byte[] bytes = encodeBase64File(filePath);
        if (bytes != null) {
            return new String(bytes);
        } else {
            return "";
        }
    }
    /**
     * 根据文件获取base64 utf-8编码字符串
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String encoderFileToUtf8(String filePath) throws Exception {
        byte[] bytes = encodeBase64File(filePath);
        if (bytes != null) {
            return encoderByteToUtf8(bytes);
        } else {
            return "";
        }
    }

    /**
     * @return 把byte[]数组转码为utf-8的字符串
     * @throws UnsupportedEncodingException
     */
    public static String encoderByteToUtf8(byte[] bytes) throws UnsupportedEncodingException {
        return new String(bytes, "utf-8");
    }

    /**
     * 根据文件获取base64 utf-8编码字符串  URL编码
     */
    public static String URLEncoderFileToUtf8(String filePath) throws Exception {
        byte[] bytes = encodeBase64File(filePath);
        if (bytes != null) {
            return URLEncoderByteToUtf8(bytes);
        } else {
            return "";
        }
    }

    /**
     * @return 把byte[]数组转码为utf-8的字符串 URL编码
     * @throws UnsupportedEncodingException
     */
    public static String URLEncoderByteToUtf8(byte[] bytes) throws UnsupportedEncodingException {
        return URLEncoder.encode(new String(bytes, "utf-8"), "utf-8");
    }

    /**
     * 根据文件获取base64 byte数组
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static byte[] encodeBase64File(String path) throws Exception {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encode(buffer, Base64.NO_WRAP);
    }

    public static String encodeString(String string) throws Exception {
        return Base64.encodeToString(string.getBytes(), Base64.NO_WRAP);

    }

    /**
     * 将base64字符保存文本文件
     *
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */

    public static void toFile(String base64Code, String targetPath)
            throws Exception {

        byte[] buffer = base64Code.getBytes();
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
    }
}
