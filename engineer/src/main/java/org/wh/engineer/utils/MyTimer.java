package org.wh.engineer.utils;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.Toast;

import org.wh.engineer.activity.TestLoginActivity;

/**
 * 项目名称:    Android_PV_2.6.1
 * 创建者:      DanMing
 * 创建时间:    2018/9/10 14:58
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.utils
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class MyTimer extends CountDownTimer {
   public static MyTimer mInstance;

   private Context context;

   private long millisInFuture;

   /**
    * @param millisInFuture  总时长
    * @param countDownInterval 多少时间执行一次onTick
    * @param context
    * @return
    */
   public static MyTimer getInstance(long millisInFuture, long countDownInterval,Context context){
	//单例
	if (mInstance == null){
	   synchronized (MyTimer.class){
		if (mInstance == null) {
		   mInstance =
			   new MyTimer(millisInFuture,countDownInterval,context);
		}
	   }
	}
	return mInstance;

   }

   private MyTimer(long millisInFuture, long countDownInterval,Context context) {
	super(millisInFuture, countDownInterval);
	this.context = context;
	this.millisInFuture = millisInFuture;

   }

   @Override
   public void onTick(long time) {
	//TODO 每隔countDownInterval执行
	LogUtils.i("MyTimer","  time   "+time);
	if (time/1000==5){
	   Toast.makeText(UIUtils.getContext(),"当前无操作，5秒后将自动退出！",Toast.LENGTH_SHORT).show();
	}

   }

   @Override
   public void onFinish() {
	//TODO 计时完成
	context.startActivity(new Intent(context, TestLoginActivity.class));

   }
}
