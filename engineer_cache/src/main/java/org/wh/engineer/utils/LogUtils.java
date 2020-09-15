package org.wh.engineer.utils;

import com.orhanobut.logger.Logger;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/13 15:05
 * 描述:       log日志打印
 * 包名:        high.rivamed.myapplication.utils
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class LogUtils {

   public static boolean isDebug = true;

   public static void e(String tag, Throwable throwable) {
	if (isDebug) {
	   Logger.t(tag).e(tag, throwable);
	}
   }

   public static void i(String tag, String s) {
	if (isDebug) {
	   Logger.t(tag).i(s);
	}
   }

   public static void json(String tag, String json) {
	if (isDebug) {
	   Logger.t(tag).json(json);
	}
   }

   public static void d(String tag, Object o) {
	if (isDebug) {
	   Logger.t(tag).d(o);
	}
   }

   public static void w(String tag, String s) {
	if (isDebug) {
	   Logger.t(tag).w(s);
	}
   }
   public static void e(String tag, String s) {
	if (isDebug) {
	   Logger.t(tag).e(s);
	}
   }

	/*public static void w(Object o) {
		if(isDebug) {
			LogUtil.w(TAG, o);
		}
	}*/

	/*public static void d(String msg) {
		if(isDebug) {
			Logger.d(msg);
		}
	}*/

	/*public static void e(Object o) {
		if(isDebug) {
			LogUtil.e(TAG, o);
		}
	}*/




	/*public static void i(String msg) {
		if(isDebug) {
			Logger.i(msg);
		}
	}*/

}