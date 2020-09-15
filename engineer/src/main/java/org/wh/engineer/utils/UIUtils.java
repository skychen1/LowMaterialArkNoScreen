package org.wh.engineer.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import org.wh.engineer.R;
import org.wh.engineer.activity.TestLoginActivity;
import org.wh.engineer.base.App;
import org.wh.engineer.bean.BoxSizeBean;
import org.wh.engineer.bean.ConfigBean;
import org.wh.engineer.bean.HomeAuthorityMenuBean;
import org.wh.engineer.bean.SocketLeftTopBean;
import org.wh.engineer.dbmodel.ChildrenBean;
import org.wh.engineer.devices.AllDeviceCallBack;

import java.util.ArrayList;
import java.util.List;

import static org.wh.engineer.base.App.mAppContext;
import static org.wh.engineer.cont.Constants.DOWN_MENU_YICHU;
import static org.wh.engineer.cont.Constants.REFRESH_TOKEN;
import static org.wh.engineer.cont.Constants.SAVE_CONFIG_STRING;
import static org.wh.engineer.cont.Constants.SAVE_MENU_DOWN_TYPE;
import static org.wh.engineer.cont.Constants.SAVE_MENU_LEFT_TYPE;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/13 15:34
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.utils
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class UIUtils {

   private static Handler mHandler = new Handler();
   private static Gson sGson =new Gson();

   /**
    * 在ui线程里面开启子线程
    *
    * @param task
    */
   public static void runInUIThread(Runnable task) {
	mHandler.post(task);
   }

   /**
    * 在ui线程里面开启延迟子线程
    *
    * @param task
    */
   public static void runInUIThread(Runnable task, long delayMillis) {
	mHandler.postDelayed(task, delayMillis);
   }

   /**
    * 移除所有主线程的Handler中的所有队列
    */
   public static void removeAllCallbacks() {
	mHandler.removeCallbacksAndMessages(null);
   }

   /**
    * 得到上下文
    *
    * @return
    */
   public static Context getContext() {
	return App.getInstance().getApplicationContext();
   }

   /**
    * 得到Resources对象
    *
    * @return
    */
   public static Resources getResources() {
	return getContext().getResources();
   }

   /**
    * 得到color.xml的颜色信息
    *
    * @param resId
    * @return
    */
   public static int getColor(int resId) {
	return getResources().getColor(resId);
   }

   /**
    * 得到String.xml中的字符
    *
    * @param resId 资源id
    * @return
    */
   public static String getString(int resId) {
	return getResources().getString(resId);
   }

   /**
    * 得到应用程序的包名
    *
    * @return
    */
   public static String getPackageName() {
	return getContext().getPackageName();
   }

   public static void hideSoftInput(Context context, View view) {
	InputMethodManager imm = (InputMethodManager) context.getSystemService(
		Context.INPUT_METHOD_SERVICE);
	imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
   }

   private static long lastClickTime = 0L;
   private static long lastClickTime1 = 0L;
   private static long lastClickTime2 = 0L;
   private static long lastClickTime3 = 0L;
   private static int  lastButtonId  = -1;

   public static boolean isFastDoubleClick() {
	long time = System.currentTimeMillis();
	if (time - lastClickTime1 < 2000) {
	   return true;
	}
	lastClickTime1 = time;
	return false;

   }
   public static boolean isFastDoubleClick2() {
	long time = System.currentTimeMillis();
	if (time - lastClickTime < 3000) {
	   return true;
	}
	lastClickTime = time;
	return false;

   }
   public static boolean isFastDoubleClick3() {
	long time = System.currentTimeMillis();
	if (time - lastClickTime2 < 1500) {
	   return true;
	}
	lastClickTime2 = time;
	return false;

   }
   public static boolean isFastDoubleClick(int buttonId) {
	long time = System.currentTimeMillis();
	if (lastButtonId == buttonId && time - lastClickTime3 < 3000) {
	   return true;
	}
	lastClickTime3 = time;
	lastButtonId = buttonId;
	return false;
   }

   /**
    * 设置某个效期的背景
    *
    * @param type     0 过期，1 小鱼30   2小鱼90    3  小鱼120   4
    * @param textview
    */
   public static void initTermOfValidity(
	   Context mContext, int IsErrorOperation, int type, TextView textview) {
	if (type == 0) {
	   textview.setTextColor(mContext.getResources().getColor(R.color.color_overdue_prompt));
	} else if (type == 3) {
	   textview.setTextColor(mContext.getResources().getColor(R.color.color_100_prompt));
	} else if (type == 2) {
	   textview.setTextColor(mContext.getResources().getColor(R.color.color_70_prompt));
	} else if (type == 1) {
	   textview.setTextColor(mContext.getResources().getColor(R.color.color_28_prompt));
	} else if (type == 4) {

	   if (IsErrorOperation == 1) {
		textview.setTextColor(mContext.getResources().getColor(R.color.text_color_9));
	   } else {
		textview.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
	   }
	}
   }

   /**
    * 底色不同
    *
    * @param mContext
    * @param helper
    * @param type
    * @param textview
    */
   public static void initTermOfValidity2(
	   Context mContext, BaseViewHolder helper, int type, TextView textview) {

	if (type == 0) {
	   textview.setBackgroundResource(R.drawable.bg_text_red);
	   textview.setTextColor(mContext.getResources().getColor(R.color.bg_f));
	} else if (type == 3) {
	   textview.setBackgroundResource(R.drawable.bg_text_yellow1);
	   textview.setTextColor(mContext.getResources().getColor(R.color.bg_f));
	} else if (type == 2) {
	   textview.setBackgroundResource(R.drawable.bg_text_yellow2);
	   textview.setTextColor(mContext.getResources().getColor(R.color.bg_f));
	} else if (type == 1) {
	   textview.setBackgroundResource(R.drawable.bg_text_orange);
	   textview.setTextColor(mContext.getResources().getColor(R.color.bg_f));
	} else {
	   textview.setBackgroundResource(R.color.bg_f);
	   textview.setTextColor(mContext.getResources().getColor(R.color.text_color_3));
	}

   }

   private static InputFilter filter = new InputFilter() {
	@Override
	public CharSequence filter(
		CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
	   //返回null表示接收输入的字符,返回空字符串表示不接受输入的字符
	   if (source.equals(" ")) {
		return "";
	   } else {
		return null;
	   }
	}
   };

   public static void setInputLenWithNoBlank(TextView textView, int max) {

	textView.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max), filter});

   }

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
    * 获取配置项
    *
    * @param context
    * @param code
    * @return
    */
   public static boolean getConfigType(Context context, String code) {
	String string = SPUtils.getString(context, SAVE_CONFIG_STRING);
	//	LogUtils.i("ContentConsumeOperateFrag","SAVE_CONFIG_STRING   "+string);
	ConfigBean configBean = sGson.fromJson(string, ConfigBean.class);
	if (configBean != null) {
	   List<ConfigBean.ConfigVosBean> tCstConfigVos = configBean.getConfigVos();
	   if (tCstConfigVos!=null){
		for (ConfigBean.ConfigVosBean configType : tCstConfigVos) {
		   if (code.equals(configType.getCode())) {
			return true;
		   }
		}
	   }
	   return false;
	}
	return false;
   }
   /**
    * 获取配置项
    *
    * @param code
    * @return
    */
   public static boolean getConfigLoginType(List<ConfigBean.ConfigVosBean> tCstConfigVos, String code) {
	//	LogUtils.i("ContentConsumeOperateFrag","SAVE_CONFIG_STRING   "+string);
	   if (tCstConfigVos!=null){
		for (ConfigBean.ConfigVosBean configType : tCstConfigVos) {
		   if (code.equals(configType.getCode())) {
			return true;
		   }
		}
	   }
	   return false;
   }
   /**
    * 获取权限配置主按钮
    * @param context
    * @param title
    * @return
    */
   public static boolean getMenuLeftType(Context context, String title) {
	String string = SPUtils.getString(context, SAVE_MENU_LEFT_TYPE);
	List<HomeAuthorityMenuBean> fromJson = sGson.fromJson(string,
									     new TypeToken<List<HomeAuthorityMenuBean>>() {}
										     .getType());
	if (fromJson != null && fromJson.size() > 0) {
	   for (HomeAuthorityMenuBean mType : fromJson) {
		if (title.equals(mType.getTitle())) {
		   return true;
		}
	   }
	}
	return false;
   }

   /**
    * 获取权限配置主按钮
    * @param context
    * @param title
    * @return
    */
   public static boolean getMenuDownType(Context context, String title) {

	String string = SPUtils.getString(context, SAVE_MENU_DOWN_TYPE);
	List<ChildrenBean> fromJson = sGson.fromJson(string,
								  new TypeToken<List<ChildrenBean>>() {}.getType());
	if (fromJson != null && fromJson.size() > 0) {
	   for (ChildrenBean mType : fromJson) {
		if (title.equals(mType.getTitle())) {
		   return true;
		}
	   }
	}
	return false;
   }
   /**
    * 获取权限配置主按钮只有领用退回的
    * @param context
    * @param title
    * @return
    */
   public static boolean getMenuOnlyType(Context context, String title) {
	String string = SPUtils.getString(context, SAVE_MENU_DOWN_TYPE);
	List<ChildrenBean> fromJson = sGson.fromJson(string,
								  new TypeToken<List<ChildrenBean>>() {}.getType());
	if (fromJson != null && fromJson.size() == 1) {
	   for (ChildrenBean mType : fromJson) {
		if (title.equals(mType.getTitle())) {
		   return true;
		}
	   }
	}
	if (fromJson != null && fromJson.size() == 2) {
	   List<String> s =new ArrayList<>();
	   for (ChildrenBean bean : fromJson) {
		s.add(bean.getTitle());
	   }
	   if (s.contains(DOWN_MENU_YICHU)&&s.contains(title)){
		return true;
	   }
	}
	return false;
   }
   public static String getRefreshToken() {
	String RefreshToken = SPUtils.getString(UIUtils.getContext(), REFRESH_TOKEN);
	return RefreshToken;
   }

   /**
    * 全屏显示，隐藏虚拟按钮
    *
    * @param view
    */
   public static void fullScreenImmersive(View view) {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
	   int uiOptions =
		   View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
		   View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
		   View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN |
		   View.SYSTEM_UI_FLAG_IMMERSIVE;
	   view.setSystemUiVisibility(uiOptions);
	}
   }

   /**
    * 禁止radiogroup点击
    *
    * @param testRadioGroup
    */
   public static void disableRadioGroup(RadioGroup testRadioGroup) {
	for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
	   testRadioGroup.getChildAt(i).setEnabled(false);
	}
   }

   /**
    * 恢复点击
    * @param testRadioGroup
    */
   public static void enableRadioGroup(RadioGroup testRadioGroup) {
	for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
	   testRadioGroup.getChildAt(i).setEnabled(true);
	}
   }
   /**
    * 判断服务是否开启
    *
    * @return
    */
   public static boolean isServiceRunning(Context context, String ServiceName) {
	if (TextUtils.isEmpty(ServiceName)) {
	   return false;
	}
	ActivityManager myManager = (ActivityManager) context
		.getSystemService(Context.ACTIVITY_SERVICE);
	ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
		.getRunningServices(100);
	for (int i = 0; i < runningService.size(); i++) {
	   if (runningService.get(i).service.getClassName().toString()
		   .equals(ServiceName)) {
		return true;
	   }
	}
	return false;
   }

   /**
    * epc前面4个0的过滤
    * @return
    */
   public static boolean isFourZeorType(String epc){
      if (epc.length()==16||!epc.startsWith("0000")){
         return true;
	}
      return false;
   }
   /**
    * 初始化耗材柜TabLayout
    *
    * @param mTitleDataList 标题列表
    * @param mCttimecheckViewpager 关联Viewpager
    * @param mMagicIndicator
    */
   public static void initPvTabLayout(List<BoxSizeBean.DevicesBean> mTitleDataList, ViewPager mCttimecheckViewpager, MagicIndicator mMagicIndicator) {
	CommonNavigator mCommonNavigator = new CommonNavigator(mAppContext);
	//        commonNavigator.setAdjustMode(true);
	mCommonNavigator.setAdapter(new CommonNavigatorAdapter() {

	   @Override
	   public int getCount() {
		return mTitleDataList == null ? 0 : mTitleDataList.size();
	   }

	   @Override
	   public IPagerTitleView getTitleView(Context context, final int index) {
		ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
		colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
		colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
		colorTransitionPagerTitleView.setPadding(mAppContext.getResources().getDimensionPixelSize(R.dimen.x55),0,mAppContext.getResources().getDimensionPixelSize(R.dimen.x55),0);
		colorTransitionPagerTitleView.setText(mTitleDataList.get(index).getDeviceName());
		//		colorTransitionPagerTitleView.setMinimumWidth(textWidth);
		colorTransitionPagerTitleView.setTextSize(getResources().getDimensionPixelOffset(R.dimen.x30));
		colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
		   @Override
		   public void onClick(View view) {
			mCttimecheckViewpager.setCurrentItem(index);
		   }
		});
		return colorTransitionPagerTitleView;
	   }

	   @Override
	   public IPagerIndicator getIndicator(Context context) {
		LinePagerIndicator indicator = new LinePagerIndicator(context);
		//                indicator.setStartInterpolator(new AccelerateInterpolator());
		//                indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
		indicator.setYOffset(getResources().getDimensionPixelOffset(R.dimen.y60));
		indicator.setLineHeight( getResources().getDimensionPixelOffset(R.dimen.y3));
		indicator.setColors(getResources().getColor(R.color.color_drak_green));
		return indicator;
	   }
	});
	setTiltleSelected(mCttimecheckViewpager, mMagicIndicator, mCommonNavigator);
   }
   /**
    * 初始化耗材柜TabLayout
    *
    * @param mTitleDataList 标题列表
    * @param mCttimecheckViewpager 关联Viewpager
    * @param mMagicIndicator
    */
   public static void initPvTabLayout2(List<SocketLeftTopBean.CstExpirationVosBean> mTitleDataList, ViewPager mCttimecheckViewpager, MagicIndicator mMagicIndicator) {
	CommonNavigator mCommonNavigator = new CommonNavigator(mAppContext);
	//        commonNavigator.setAdjustMode(true);
	mCommonNavigator.setAdapter(new CommonNavigatorAdapter() {
	   @Override
	   public int getCount() {
		return mTitleDataList == null ? 0 : mTitleDataList.size();
	   }

	   @Override
	   public IPagerTitleView getTitleView(Context context, final int index) {
		ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
		colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
		colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
		colorTransitionPagerTitleView.setPadding(mAppContext.getResources().getDimensionPixelSize(R.dimen.x55),0,mAppContext.getResources().getDimensionPixelSize(R.dimen.x55),0);
		colorTransitionPagerTitleView.setText(mTitleDataList.get(index).getDeviceName());
		//		colorTransitionPagerTitleView.setMinimumWidth(textWidth);
		colorTransitionPagerTitleView.setTextSize(getResources().getDimensionPixelOffset(R.dimen.x30));
		colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
		   @Override
		   public void onClick(View view) {
			mCttimecheckViewpager.setCurrentItem(index);
		   }
		});
		return colorTransitionPagerTitleView;
	   }

	   @Override
	   public IPagerIndicator getIndicator(Context context) {
		LinePagerIndicator indicator = new LinePagerIndicator(context);
		//                indicator.setStartInterpolator(new AccelerateInterpolator());
		//                indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
		indicator.setYOffset(getResources().getDimensionPixelOffset(R.dimen.y60));
		indicator.setLineHeight( getResources().getDimensionPixelOffset(R.dimen.y3));
		indicator.setColors(getResources().getColor(R.color.color_drak_green));
		return indicator;
	   }
	});
	setTiltleSelected(mCttimecheckViewpager, mMagicIndicator, mCommonNavigator);
   }
   /**
    * 初始化耗材柜TabLayout
    *
    * @param mTitleDataList 标题列表
    * @param mCttimecheckViewpager 关联Viewpager
    * @param mMagicIndicator
    */
   public static void initPvTabLayouts(String[] mTitleDataList, ViewPager mCttimecheckViewpager, MagicIndicator mMagicIndicator) {
	CommonNavigator mCommonNavigator = new CommonNavigator(mAppContext);
	//        commonNavigator.setAdjustMode(true);
	mCommonNavigator.setAdapter(new CommonNavigatorAdapter() {

	   @Override
	   public int getCount() {
		return mTitleDataList == null ? 0 : mTitleDataList.length;
	   }

	   @Override
	   public IPagerTitleView getTitleView(Context context, final int index) {
		ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
		colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
		colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
		colorTransitionPagerTitleView.setPadding(mAppContext.getResources().getDimensionPixelSize(R.dimen.x55),0,mAppContext.getResources().getDimensionPixelSize(R.dimen.x55),0);
		colorTransitionPagerTitleView.setText(mTitleDataList[index]);
		//		colorTransitionPagerTitleView.setMinimumWidth(textWidth);
		colorTransitionPagerTitleView.setTextSize(getResources().getDimensionPixelOffset(R.dimen.x30));
		colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
		   @Override
		   public void onClick(View view) {
			mCttimecheckViewpager.setCurrentItem(index);
		   }
		});
		return colorTransitionPagerTitleView;
	   }

	   @Override
	   public IPagerIndicator getIndicator(Context context) {
		LinePagerIndicator indicator = new LinePagerIndicator(context);
		//                indicator.setStartInterpolator(new AccelerateInterpolator());
		//                indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
		indicator.setYOffset(getResources().getDimensionPixelOffset(R.dimen.y60));
		indicator.setLineHeight( getResources().getDimensionPixelOffset(R.dimen.y3));
		indicator.setColors(getResources().getColor(R.color.color_drak_green));
		return indicator;
	   }
	});
	setTiltleSelected(mCttimecheckViewpager, mMagicIndicator, mCommonNavigator);
   }

   private static void setTiltleSelected(
	   ViewPager mCttimecheckViewpager, MagicIndicator mMagicIndicator,
	   CommonNavigator mCommonNavigator) {
	mMagicIndicator.setNavigator(mCommonNavigator);
	//        LinearLayout titleContainer = mCommonNavigator.getTitleContainer(); // must after setNavigator
	//        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
	//        titleContainer.setDividerDrawable(getResources().getDrawable(R.drawable.bg_btn_gray_nor2));
	ViewPagerHelper.bind(mMagicIndicator, mCttimecheckViewpager);
	mCommonNavigator.getTitleContainer().setPadding(0, (int)getResources().getDimension(R.dimen.y6), 0, 0);
	mCommonNavigator.getTitleContainer()
		.getChildAt(0)
		.setBackgroundColor(getResources().getColor(R.color.white));
	mCttimecheckViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
	   @Override
	   public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	   }

	   @Override
	   public void onPageSelected(int position) {
		for (int i = 0; i < mCommonNavigator.getTitleContainer().getChildCount(); i++) {
		   if (i == position) {
			mCommonNavigator.getTitleContainer()
				.getChildAt(i)
				.setBackgroundColor(getResources().getColor(R.color.white));
		   } else {
			mCommonNavigator.getTitleContainer()
				.getChildAt(i)
				.setBackgroundColor(getResources().getColor(R.color.bg_0));
		   }
		}
	   }

	   @Override
	   public void onPageScrollStateChanged(int state) {

	   }
	});
   }
   /**
    * 退出登录
    * @param activity
    */
   public static void removeAllAct(Activity activity) {
	Intent intent = new Intent(activity, TestLoginActivity.class);
	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
	activity.startActivity(intent);
	activity.finish();
	AllDeviceCallBack.getInstance().closeLightStart();

   }
}