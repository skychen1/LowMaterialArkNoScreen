package com.rivamed.libdevicesbase.utils;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

/**
 * toast工具类，只能在application中注册，因为要持有Context，不然内存泄露的情况
 *
 * @author : YiCH
 * @date : 2018/8/30 0030.
 */

public class ToastUtils {
    private static Application mContext;

    private ToastUtils() {
    }

    /**
     * 在Application 中注册。
     *
     * @param context
     */
    public static void register(Application context) {
        mContext = context;
    }

    public static void show(int resId) {
        if (mContext == null) {
            throw new NullPointerException("please register context firstly.");
        } else {
            Toast.makeText(mContext, resId, Toast.LENGTH_SHORT).show();
        }

    }

    public static void show(String string) {
        if (mContext == null) {
            throw new NullPointerException("please register context firstly.");
        }
        Toast.makeText(mContext, string, Toast.LENGTH_SHORT).show();
    }

}
