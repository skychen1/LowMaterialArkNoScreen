package org.wh.engineer.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 项目名称:
 * 创建者:      LiangDanMing
 * 创建时间:    2017/3/28 9:22
 * 描述         获取版本信息工具类
 * 包名
 */
public class PackageUtils {

   /**
    * 获得版本信息
    *
    * @param context
    * @return
    */
   public static String getVersionName(Context context) {
	// 获得包管理器
	PackageManager pm = context.getPackageManager();
	try {
	   // 清单文件的对象
	   PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
	   return packageInfo.versionName;

	} catch (PackageManager.NameNotFoundException e) {
	   e.printStackTrace();
	   return "未知版本";
	}
   }

   /**
    * 获得版本号
    *
    * @param context
    * @return
    */
   public static int getVersionCode(Context context) {
	// 获得包管理器

	PackageManager pm = context.getPackageManager();
	try {
	   // 清单文件的对象
	   PackageInfo packageInfo = pm.getPackageInfo(
		   context.getPackageName(), 0);
	   return packageInfo.versionCode;
	} catch (PackageManager.NameNotFoundException e) {
	   e.printStackTrace();

	   return -1;
	}
   }
}
