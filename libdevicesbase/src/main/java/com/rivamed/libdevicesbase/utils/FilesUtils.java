package com.rivamed.libdevicesbase.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述:人脸底库照片文件夹
 *
 * @author : YiCH
 * @date : 2018/10/23 0023.
 */
public class FilesUtils {
    private static String facePath = null;


    /**
     * 获得文件存储路径
     *
     * @return
     */
    public static String getFilePath(Context context) {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()) {
            //如果外部储存可用
            //获得外部存储路径,默认路径为 /storage/emulated/0/Android/data/
            return context.getExternalFilesDir(null).getAbsolutePath();
        } else {
            //直接存在/data/data里，非root手机是看不到的
            return context.getFilesDir().getAbsolutePath();
        }
    }


    /**
     * g根据路径获取文件名
     *
     * @param pathName
     * @return
     */
    public static String getFileName(String pathName) {
        //为空，返回空
        if (TextUtils.isEmpty(pathName)) {
            return null;
        }
        int start = pathName.lastIndexOf("/");
        int end = pathName.lastIndexOf(".");
        if (start <= 0 && end <= 0) {
            //不包含这个字符，就不是文件，返回空
            return null;
        } else {
            //返回两个字符串中间的字符串，即文件名
            return pathName.substring(start + 1, end);
        }
    }

    /**
     * 删除指定文件
     *
     * @param pathName
     */
    public static void deleteFile(String pathName) {
        File file = new File(pathName);
        if (file.exists()) {
            file.delete();
        }
    }


}
