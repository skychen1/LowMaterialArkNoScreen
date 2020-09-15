package org.wh.engineer.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import static android.content.Context.WIFI_SERVICE;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/7/20 14:04
 * 描述:        网络工具类，判断当前网络链接类型
 * 包名:        high.rivamed.myapplication.utils
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class WifiUtils {
   public static int isWifi(Context con) {
	ConnectivityManager connectMgr = (ConnectivityManager) con
		.getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo info = connectMgr.getActiveNetworkInfo();
	if (info == null) {
	   // 没网
	   return 0;
	} else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
	   return 1; //WIFI
	} else {
	   return 2;//NET
	}
   }
   //获取wifi本地IP和主机名
   public static String getLocalIpAddress(Context context) {
	WifiManager wifiManager = (WifiManager) context.getSystemService(
		WIFI_SERVICE);
	WifiInfo wifiInfo = wifiManager.getConnectionInfo();
	// 获取32位整型IP地址
	int ipAddress = wifiInfo.getIpAddress();
	//返回整型地址转换成“*.*.*.*”地址
	return String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
				   (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
   }

   /**
    * 获取本地以太网IP
    * @return
    */
   public static String getHostIP() {
	String hostIp = null;
	try {
	   Enumeration nis = NetworkInterface.getNetworkInterfaces();
	   InetAddress ia = null;
	   while (nis.hasMoreElements()) {
		NetworkInterface ni = (NetworkInterface) nis.nextElement();
		Enumeration<InetAddress> ias = ni.getInetAddresses();
		while (ias.hasMoreElements()) {
		   ia = ias.nextElement();
		   if (ia instanceof Inet6Address) {
			continue;// skip ipv6
		   }
		   String ip = ia.getHostAddress();
		   if (!"127.0.0.1".equals(ip)) {
			hostIp = ia.getHostAddress();
			Log.i("MainActivity", "hostIp   " + hostIp);
			break;
		   }
		}
	   }
	} catch (SocketException e) {
	   Log.i("MainActivity", "SocketException");
	   e.printStackTrace();
	}
	return hostIp;
   }

   public static boolean isAvailableByPing(String ip) {
	if (ip == null || ip.length() <= 0) {
	   ip = "192.168.0.0";// 阿里巴巴公共ip
	}
	Runtime runtime = Runtime.getRuntime();
	Process ipProcess = null;
	try {
	   //-c 后边跟随的是重复的次数，-w后边跟随的是超时的时间，单位是秒，不是毫秒，要不然也不会anr了
	   ipProcess = runtime.exec("ping -c 3 -w 3 "+ip);
	   int exitValue = ipProcess.waitFor();
	   return (exitValue == 0);
	} catch (IOException | InterruptedException e) {
	   e.printStackTrace();
	} finally {
	   //在结束的时候应该对资源进行回收
	   if (ipProcess != null) {
		ipProcess.destroy();
	   }
	   runtime.gc();
	}
	return false;
   }
}
