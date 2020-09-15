package com.ruihua.libfacerecognitionv3.main.utils;

import android.util.Log;

/**
 * describe ：
 *
 * @author : Yich
 * date: 2019/12/20
 */
public class T {

    private static final String TAG = "testFace";

    public static void d(Class<?> clazz, Object o){
        Log.d(TAG, clazz.getSimpleName() + "_" + o);
    }
}
