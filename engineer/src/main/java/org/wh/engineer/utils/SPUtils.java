package org.wh.engineer.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/12 18:06
 * 描述:        对sp的操作的封装
 * 包名:         high.rivamed.myapplication.utils
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class SPUtils {

   private final static String NAME = "rivamed";
   private static SharedPreferences mPreferences;

   /**
    * 获得preference
    *
    * @param context
    * @return
    */
   private static SharedPreferences getPreferences(Context context) {
	if (mPreferences == null) {
	   mPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
	}
	return mPreferences;
   }

   /**
    * 获得boolean类型的数据,如果没有返回false
    *
    * @param context
    * @param key
    * @return
    */
   public static boolean getBoolean(Context context, String key) {
	return getBoolean(context, key, false);
   }

   /**
    * 获得boolean类型的数据
    *
    * @param context
    * @param key
    * @param defValue
    * @return
    */
   public static boolean getBoolean(
	   Context context, String key, boolean defValue) {
	// 频繁的读文件
	SharedPreferences sp = getPreferences(context);
	return sp.getBoolean(key, defValue);
   }

   /**
    * 存储boolean数据
    *
    * @param context
    * @param key
    * @param value
    */
   public static void putBoolean(Context context, String key, boolean value) {
	SharedPreferences sp = getPreferences(context);
	SharedPreferences.Editor editor = sp.edit();
	editor.putBoolean(key, value);
	editor.commit();
   }

   /**
    * 获得String类型的数据,如果没有返回null
    *
    * @param context
    * @param key
    * @return
    */
   public static String getString(Context context, String key) {
	return getString(context, key, null);
   }

   /**
    * 获得String类型的数据
    *
    * @param context
    * @param key
    * @param defValue
    * @return
    */
   public static String getString(Context context, String key, String defValue) {
	// 频繁的读文件
	SharedPreferences sp = getPreferences(context);
	return sp.getString(key, defValue);
   }

   /**
    * 存储String数据
    *
    * @param context
    * @param key
    * @param value
    */
   public static void putString(Context context, String key, String value) {
	SharedPreferences sp = getPreferences(context);
	SharedPreferences.Editor editor = sp.edit();
	editor.putString(key, value);
	editor.commit();
   }

   /**
    * 获得int类型的数据,如果没有返回-1
    *
    * @param context
    * @param key
    * @return
    */
   public static int getInt(Context context, String key) {
	return getInt(context, key, -1);
   }

   /**
    * 获得int类型的数据
    *
    * @param context
    * @param key
    * @param defValue
    * @return
    */
   public static int getInt(Context context, String key, int defValue) {
	// 频繁的读文件
	SharedPreferences sp = getPreferences(context);
	return sp.getInt(key, defValue);
   }

   /**
    * 存储int数据
    *
    * @param context
    * @param key
    * @param value
    */
   public static void putInt(Context context, String key, int value) {
	SharedPreferences sp = getPreferences(context);
	SharedPreferences.Editor editor = sp.edit();
	editor.putInt(key, value);
	editor.commit();
   }
}
