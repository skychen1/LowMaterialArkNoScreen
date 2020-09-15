package com.rivamed.libdevicesbase.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * 日志工具类，根据是否是测试版打印对应的日志
 *
 * @author : YiCH
 * @date : 2018/8/30 0030.
 */
public class LogUtils {
    private static boolean debug = true;
    private static final String TAG = "device";

    private LogUtils() {
    }

    public static void setDebugMode(boolean debug) {
        LogUtils.debug = debug;
    }

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public static void e(final String msg) {
        if (debug) {
            Log.e(TAG, msg);
        }
    }

    public static void toastShow(final String msg) {
        if (debug) {
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.show(msg);
                }
            });
            e(msg);
        }
    }

    /**
     * debug长数据
     *
     * @param msg
     */
    public static void eLong(String msg) {
        if (!debug) {
            return;
        }
        msg = msg.trim();
        int index = 0;
        int maxLength = 500;
        String sub;
        while (index < msg.length()) {
            // java的字符不允许指定超过总的长度end
            if (msg.length() <= index + maxLength) {
                sub = msg.substring(index);
            } else {
                sub = msg.substring(index, maxLength + index);
            }

            index += maxLength;
            Log.e(TAG, sub.trim());
        }

    }

    public static void w(final String msg) {
        if (debug) {
            Log.w(TAG, msg);
        }
    }

    public static void d(String msg) {
        if (debug) {
            Log.d(TAG, msg);
        }
    }


}
