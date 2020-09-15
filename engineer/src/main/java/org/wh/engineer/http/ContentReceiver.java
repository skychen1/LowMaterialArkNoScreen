package org.wh.engineer.http;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.wh.engineer.activity.SplashActivity;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      LiangDanMing
 * 创建时间:    2018/8/20 19:07
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.http
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class ContentReceiver extends BroadcastReceiver {
   @Override
   public void onReceive(Context context, Intent intent) {
	if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
	   Intent it=new Intent(context, SplashActivity.class);
	   it.setAction("android.intent.action.MAIN");
	   it.addCategory("android.intent.category.LAUNCHER");
	   it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	   context.startActivity(it);
	}
   }
}
