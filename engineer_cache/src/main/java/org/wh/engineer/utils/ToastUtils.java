package org.wh.engineer.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import org.wh.engineer.base.App;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/13 15:40
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.utils
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class ToastUtils {

   private static final int                 DEFAULT_COLOR   = 0x12000000;
   private static       Toast               sToast;
   private static       WeakReference<View> sViewWeakReference;
   private static       int                 gravity         =
	   Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
   private static       int                 xOffset         = 0;
   private static       int                 yOffset         = (int) (
	   64 * UIUtils.getResources().getDisplayMetrics().density + 0.5);
   private static       int                 backgroundColor = DEFAULT_COLOR;
   private static       int                 bgResource      = -1;
   private static       int                 messageColor    = DEFAULT_COLOR;
   private static       Toast               mToast;
   private static       TextView            btn;
   private static       TextView            textMsg;

   private ToastUtils() {
	throw new UnsupportedOperationException("u can't instantiate me...");
   }

//   private static Toast toast;//实现不管我们触发多少次Toast调用，都只会持续一次Toast显示的时长

   private static Field sField_TN;
   private static Field sField_TN_Handler;

   static {
	if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
	   try {
		sField_TN = Toast.class.getDeclaredField("mTN");
		sField_TN.setAccessible(true);

		sField_TN_Handler = sField_TN.getType().getDeclaredField("mHandler");
		sField_TN_Handler.setAccessible(true);
	   } catch (Exception e) {
	   }
	}
   }

   private static void hook(Toast toast) {
	try {
	   Object tn = sField_TN.get(toast);
	   Handler preHandler = (Handler) sField_TN_Handler.get(tn);
	   sField_TN_Handler.set(tn, new SafelyHandlerWarpper(preHandler));
	} catch (Exception e) {
	}
   }

   public static void showToast(Context context, CharSequence cs, int length) {
	Toast toast = Toast.makeText(context, cs, length);
	if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
	   hook(toast);
	}
	toast.show();
   }

   private static class SafelyHandlerWarpper extends Handler {

	private Handler impl;

	public SafelyHandlerWarpper(Handler impl) {
	   this.impl = impl;
	}

	@Override
	public void handleMessage(Message msg) {
	   try {
		impl.handleMessage(msg);
	   } catch (Exception e) {
		e.printStackTrace();
	   }

	}
   }

   /**
    * 短时间显示Toast【居下】    处理7.1.版本API崩溃问题
    *
    * @param msg 显示的内容-字符串
    */
   public static void showShortToast(String msg) {
	if (App.getAppContext() != null) {
	   Toast toast = Toast.makeText(App.getAppContext(), msg, Toast.LENGTH_SHORT);
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
		   hook(toast);
		}
	   toast.show();
	}
   }
   /**
    * 短时间显示Toast【居下】    处理7.1.版本API崩溃问题
    *
    * @param msg 显示的内容-字符串
    */
   public static void showLongToast(String msg) {
	if (App.getAppContext() != null) {
	   Toast toast = Toast.makeText(App.getAppContext(), msg, Toast.LENGTH_LONG);
	   if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
		hook(toast);
	   }
	   toast.show();
	}
   }

   /**
    * 反射字段
    *
    * @param object    要反射的对象
    * @param fieldName 要反射的字段名称
    */
   private static Object getField(Object object, String fieldName) throws NoSuchFieldException,
	   IllegalAccessException {
	Field field = object.getClass().getDeclaredField(fieldName);
	if (field != null) {
	   field.setAccessible(true);
	   return field.get(object);
	}
	return null;
   }

   /**
    * 设置吐司位置
    *
    * @param gravity 位置
    * @param xOffset x偏移
    * @param yOffset y偏移
    */
   public static void setGravity(final int gravity, final int xOffset, final int yOffset) {
	ToastUtils.gravity = gravity;
	ToastUtils.xOffset = xOffset;
	ToastUtils.yOffset = yOffset;
   }

   /**
    * 设置吐司view
    *
    * @param layoutId 视图
    */
   public static void setView(@LayoutRes final int layoutId) {
	LayoutInflater inflate = (LayoutInflater) UIUtils.getContext()
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	sViewWeakReference = new WeakReference<>(inflate.inflate(layoutId, null));
   }

   /**
    * 设置吐司view
    *
    * @param view 视图
    */
   public static void setView(final View view) {
	sViewWeakReference = view == null ? null : new WeakReference<>(view);
   }

   /**
    * 获取吐司view
    *
    * @return view
    */
   public static View getView() {
	if (sViewWeakReference != null) {
	   final View view = sViewWeakReference.get();
	   if (view != null) {
		return view;
	   }
	}
	if (sToast != null) {
	   return sToast.getView();
	}
	return null;
   }

   /**
    * 设置背景颜色
    *
    * @param backgroundColor 背景色
    */
   public static void setBgColor(@ColorInt final int backgroundColor) {
	ToastUtils.backgroundColor = backgroundColor;
   }

   /**
    * 设置背景资源
    *
    * @param bgResource 背景资源
    */
   public static void setBgResource(@DrawableRes final int bgResource) {
	ToastUtils.bgResource = bgResource;
   }

   /**
    * 设置消息颜色
    *
    * @param messageColor 颜色
    */
   public static void setMessageColor(@ColorInt final int messageColor) {
	ToastUtils.messageColor = messageColor;
   }

   //   /**
   //    * 安全地显示短时吐司
   //    *
   //    * @param text 文本
   //    */
   //   public static void showShortSafe(@NonNull final CharSequence text) {
   //	sHandler.post(new Runnable() {
   //	   @Override
   //	   public void run() {
   //		show(text, Toast.LENGTH_SHORT);
   //	   }
   //	});
   //   }
   //
   //   /**
   //    * 安全地显示短时吐司
   //    *
   //    * @param resId 资源Id
   //    */
   //   public static void showShortSafe(@StringRes final int resId) {
   //	sHandler.post(new Runnable() {
   //	   @Override
   //	   public void run() {
   //		show(resId, Toast.LENGTH_SHORT);
   //	   }
   //	});
   //   }
   //
   //   /**
   //    * 安全地显示短时吐司
   //    *
   //    * @param resId 资源Id
   //    * @param args  参数
   //    */
   //   public static void showShortSafe(@StringRes final int resId, final Object... args) {
   //	sHandler.post(new Runnable() {
   //	   @Override
   //	   public void run() {
   //		show(resId, Toast.LENGTH_SHORT, args);
   //	   }
   //	});
   //   }
   //
   //   /**
   //    * 安全地显示短时吐司
   //    *
   //    * @param format 格式
   //    * @param args   参数
   //    */
   //   public static void showShortSafe(final String format, final Object... args) {
   //	sHandler.post(new Runnable() {
   //	   @Override
   //	   public void run() {
   //		show(format, Toast.LENGTH_SHORT, args);
   //	   }
   //	});
   //   }
   //
   //   /**
   //    * 安全地显示长时吐司
   //    *
   //    * @param text 文本
   //    */
   //   public static void showLongSafe(@NonNull final CharSequence text) {
   //	sHandler.post(new Runnable() {
   //	   @Override
   //	   public void run() {
   //		show(text, Toast.LENGTH_LONG);
   //	   }
   //	});
   //   }
   //
   //   /**
   //    * 安全地显示长时吐司
   //    *
   //    * @param resId 资源Id
   //    */
   //   public static void showLongSafe(@StringRes final int resId) {
   //	sHandler.post(new Runnable() {
   //	   @Override
   //	   public void run() {
   //		show(resId, Toast.LENGTH_LONG);
   //	   }
   //	});
   //   }
   //
   //   /**
   //    * 安全地显示长时吐司
   //    *
   //    * @param resId 资源Id
   //    * @param args  参数
   //    */
   //   public static void showLongSafe(@StringRes final int resId, final Object... args) {
   //	sHandler.post(new Runnable() {
   //	   @Override
   //	   public void run() {
   //		show(resId, Toast.LENGTH_LONG, args);
   //	   }
   //	});
   //   }
   //
   //   /**
   //    * 安全地显示长时吐司
   //    *
   //    * @param format 格式
   //    * @param args   参数
   //    */
   //   public static void showLongSafe(final String format, final Object... args) {
   //	sHandler.post(new Runnable() {
   //	   @Override
   //	   public void run() {
   //		show(format, Toast.LENGTH_LONG, args);
   //	   }
   //	});
   //   }

   /**
    * 显示短时吐司
    *
    * @param text 文本
    */
   public static void showShort(@NonNull final CharSequence text) {
	show(text, Toast.LENGTH_SHORT);
   }

   /**
    * 显示短时吐司
    *
    * @param text 文本
    */
   public static void showShort2(Context context, @NonNull final CharSequence text) {
	show2(context, text, Toast.LENGTH_SHORT);
   }

   /**
    * 显示短时吐司
    *
    * @param resId 资源Id
    */
   public static void showShort(@StringRes final int resId) {
	show(resId, Toast.LENGTH_SHORT);
   }

   /**
    * 显示短时吐司
    *
    * @param resId 资源Id
    * @param args  参数
    */
   public static void showShort(@StringRes final int resId, final Object... args) {
	show(resId, Toast.LENGTH_SHORT, args);
   }

   /**
    * 显示短时吐司
    *
    * @param format 格式
    * @param args   参数
    */
   public static void showShort(final String format, final Object... args) {
	show(format, Toast.LENGTH_SHORT, args);
   }

   /**
    * 显示长时吐司
    *
    * @param text 文本
    */
   public static void showLong(@NonNull final CharSequence text) {
	show(text, Toast.LENGTH_LONG);
   }

   /**
    * 显示长时吐司
    *
    * @param resId 资源Id
    */
   public static void showLong(@StringRes final int resId) {
	show(resId, Toast.LENGTH_LONG);
   }

   /**
    * 显示长时吐司
    *
    * @param resId 资源Id
    * @param args  参数
    */
   public static void showLong(@StringRes final int resId, final Object... args) {
	show(resId, Toast.LENGTH_LONG, args);
   }

   /**
    * 显示长时吐司
    *
    * @param format 格式
    * @param args   参数
    */
   public static void showLong(final String format, final Object... args) {
	show(format, Toast.LENGTH_LONG, args);
   }

   //   /**
   //    * 安全地显示短时自定义吐司
   //    */
   //   public static void showCustomShortSafe(@LayoutRes final int layoutId) {
   //	sHandler.post(new Runnable() {
   //	   @Override
   //	   public void run() {
   //		setView(layoutId);
   //		show("", Toast.LENGTH_SHORT);
   //	   }
   //	});
   //   }
   //
   //   /**
   //    * 安全地显示长时自定义吐司
   //    */
   //   public static void showCustomLongSafe(@LayoutRes final int layoutId) {
   //	sHandler.post(new Runnable() {
   //	   @Override
   //	   public void run() {
   //		setView(layoutId);
   //		show("", Toast.LENGTH_LONG);
   //	   }
   //	});
   //   }

   /**
    * 显示短时自定义吐司
    */
   public static void showCustomShort(@LayoutRes final int layoutId) {
	setView(layoutId);
	show("", Toast.LENGTH_SHORT);
   }

   /**
    * 显示长时自定义吐司
    */
   public static void showCustomLong(@LayoutRes final int layoutId) {
	setView(layoutId);
	show("", Toast.LENGTH_LONG);
   }

   //   /**
   //    * 安全地显示短时自定义吐司
   //    */
   //   public static void showCustomShortSafe(@NonNull final View view) {
   //	sHandler.post(new Runnable() {
   //	   @Override
   //	   public void run() {
   //		setView(view);
   //		show("", Toast.LENGTH_SHORT);
   //	   }
   //	});
   //   }
   //
   //   /**
   //    * 安全地显示长时自定义吐司
   //    */
   //   public static void showCustomLongSafe(@NonNull final View view) {
   //	sHandler.post(new Runnable() {
   //	   @Override
   //	   public void run() {
   //		setView(view);
   //		show("", Toast.LENGTH_LONG);
   //	   }
   //	});
   //   }

   /**
    * 显示短时自定义吐司
    */
   public static void showCustomShort(@NonNull final View view) {
	setView(view);
	show("", Toast.LENGTH_SHORT);
   }

   /**
    * 显示长时自定义吐司
    */
   public static void showCustomLong(@NonNull final View view) {
	setView(view);
	show("", Toast.LENGTH_LONG);
   }

   /**
    * 显示吐司
    *
    * @param resId    资源Id
    * @param duration 显示时长
    */
   private static void show(@StringRes final int resId, final int duration) {
	show(UIUtils.getContext().getResources().getText(resId).toString(), duration);
   }

   /**
    * 显示吐司
    *
    * @param resId    资源Id
    * @param duration 显示时长
    * @param args     参数
    */
   private static void show(@StringRes final int resId, final int duration, final Object... args) {
	show(String.format(UIUtils.getContext().getResources().getString(resId), args), duration);
   }

   /**
    * 显示吐司
    *
    * @param format   格式
    * @param duration 显示时长
    * @param args     参数
    */
   private static void show(final String format, final int duration, final Object... args) {
	show(String.format(format, args), duration);
   }

   /**
    * 显示吐司
    *
    * @param text     文本
    * @param duration 显示时长
    */
   private static void show(final CharSequence text, final int duration) {
	cancel();
	boolean isCustom = false;
	if (sViewWeakReference != null) {
	   final View view = sViewWeakReference.get();
	   if (view != null) {
		sToast = new Toast(UIUtils.getContext());
		sToast.setView(view);
		sToast.setDuration(duration);
		isCustom = true;
	   }
	}
	if (!isCustom) {
	   if (messageColor != DEFAULT_COLOR) {
		SpannableString spannableString = new SpannableString(text);
		ForegroundColorSpan colorSpan = new ForegroundColorSpan(messageColor);
		spannableString.setSpan(colorSpan, 0, spannableString.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		sToast = Toast.makeText(UIUtils.getContext(), spannableString, duration);
	   } else {
		sToast = Toast.makeText(UIUtils.getContext(), text, duration);
	   }
	}
	View view = sToast.getView();
	if (bgResource != -1) {
	   view.setBackgroundResource(bgResource);
	} else if (backgroundColor != DEFAULT_COLOR) {
	   view.setBackgroundColor(backgroundColor);
	}
	sToast.setGravity(gravity, xOffset, yOffset);
	sToast.show();
   }

   /**
    * 显示吐司
    *
    * @param text     文本
    * @param duration 显示时长
    */
   private static void show2(Context context, final CharSequence text, final int duration) {
	cancel();
	boolean isCustom = false;
	if (sViewWeakReference != null) {
	   final View view = sViewWeakReference.get();
	   if (view != null) {
		sToast = new Toast(context);
		sToast.setView(view);
		sToast.setDuration(duration);
		isCustom = true;
	   }
	}
	if (!isCustom) {
	   if (messageColor != DEFAULT_COLOR) {
		SpannableString spannableString = new SpannableString(text);
		ForegroundColorSpan colorSpan = new ForegroundColorSpan(messageColor);
		spannableString.setSpan(colorSpan, 0, spannableString.length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		sToast = Toast.makeText(context, spannableString, duration);
	   } else {
		sToast = Toast.makeText(context, text, duration);
	   }
	}
	View view = sToast.getView();
	if (bgResource != -1) {
	   view.setBackgroundResource(bgResource);
	} else if (backgroundColor != DEFAULT_COLOR) {
	   view.setBackgroundColor(backgroundColor);
	}
	sToast.setGravity(gravity, xOffset, yOffset);
	sToast.show();
   }

   /**
    * 取消吐司显示
    */
   public static void cancel() {
	if (sToast != null) {
	   sToast.cancel();
	   sToast = null;
	}
   }

   public static void showUiToast(Activity activity, String msg) {
	activity.runOnUiThread(new Runnable() {
	   @Override
	   public void run() {
		ToastUtils.showShort(msg);
	   }
	});
   }

}
