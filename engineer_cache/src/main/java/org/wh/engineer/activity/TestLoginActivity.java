package org.wh.engineer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.wh.engineer.BuildConfig;
import org.wh.engineer.R;
import org.wh.engineer.base.SimpleActivity;
import org.wh.engineer.devices.AllDeviceCallBack;
import org.wh.engineer.utils.SPUtils;
import org.wh.engineer.utils.StringUtils;
import org.wh.engineer.utils.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/19 13:30
 * 描述:        工程模式的login
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class TestLoginActivity extends SimpleActivity {

   @BindView(R.id.login_name)
   EditText       mLoginName;
   @BindView(R.id.login_password)
   EditText       mLoginPassword;
   @BindView(R.id.dialog_left)
   TextView       mDialogLeft;
   @BindView(R.id.dialog_right)
   TextView       mDialogRight;
   @BindView(R.id.intent_right)
   TextView       mIntentRight;
   @BindView(R.id.login_all)
   RelativeLayout mLoginAll;
   private String mUserPhone;
   private String mPassword;
   final static int  COUNTS2   = 3;// 点击次数  2s内点击3次进入注册界面
   final static long DURATION2 = 1200;// 规定有效时间
   long[] mHits2 = new long[COUNTS2];
   @Override
   public int getLayoutId() {
	return R.layout.test_login_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
      /*
      我们自己使用，不输入密码直接进工程模式
       */
	mIntentRight.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View view) {
		continuousClick2();
	   }
	});
	if (BuildConfig.DEBUG) {
	   mLoginName.setText("admin");
	   mLoginPassword.setText("rivamed");
	}
   }

   @Override
   public void onBindViewBefore() {

   }

   @Override
   public Object newP() {
	return null;
   }



   @OnClick({R.id.dialog_left, R.id.dialog_right, R.id.login_all})
   public void onViewClicked(View view) {
	switch (view.getId()) {
	   case R.id.dialog_left:
		finish();
		break;
	   case R.id.dialog_right:
		if (UIUtils.isFastDoubleClick(R.id.dialog_right)) {
		   return;
		} else {

		   if (isvalidate()&&contrast()) {
			loadLogin();
		   } else {
			Toast.makeText(mContext, "登录失败，请重试！", Toast.LENGTH_SHORT).show();
		   }
		}
		break;
	   case R.id.login_all:
		mLoginName.clearFocus();
		mLoginPassword.clearFocus();
		break;
	}
   }
   private boolean isvalidate() {
	// 获取控件输入的值
	mUserPhone = mLoginName.getText().toString().trim();
	mPassword = mLoginPassword.getText().toString().trim();
	if (StringUtils.isEmpty(mUserPhone)) {
	   Toast.makeText(mContext, "用户名不能为空", Toast.LENGTH_SHORT).show();
	   return false;
	}

	if (StringUtils.isEmpty(mPassword)) {
	   Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
	   return false;
	}

	return true;
   }
   //对比名字和密码
   public boolean contrast(){
	String testLoginName = SPUtils.getString(this, "TestLoginName");
	String testLoginPass = SPUtils.getString(this, "TestLoginPass");
	if (!mUserPhone.equals(testLoginName)) {
	   Toast.makeText(mContext, "用户名错误！", Toast.LENGTH_SHORT).show();
	   return false;
	}
	if (!mPassword.equals(testLoginPass)) {
	   Toast.makeText(mContext, "密码错误！", Toast.LENGTH_SHORT).show();
	   return false;
	}
      return true;
   }
   /**
    * 获取登录
    */
   private void loadLogin() {
	startActivity(new Intent(this,RegisteActivity.class));
	AllDeviceCallBack.getInstance().closeLightStart();
	finish();
   }
   private void continuousClick2() {
	//每次点击时，数组向前移动一位
	System.arraycopy(mHits2, 1, mHits2, 0, mHits2.length - 1);
	//为数组最后一位赋值
	mHits2[mHits2.length - 1] = SystemClock.uptimeMillis();
	if (mHits2[0] >= (SystemClock.uptimeMillis() - DURATION2)) {
	   mHits2 = new long[COUNTS2];//重新初始化数组
	   startActivity(new Intent(this, RegisteActivity.class));
	   finish();
	}
   }
}
