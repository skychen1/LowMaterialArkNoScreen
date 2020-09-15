package org.wh.engineer.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.wh.engineer.R;
import org.wh.engineer.bean.Event;
import org.wh.engineer.utils.EventBusUtils;

import java.lang.ref.WeakReference;


/**
 * 项目名称:    Rivamed_High_2.6
 * 创建者:      LiangDanMing
 * 创建时间:    2018/3/28 9:22
 * 描述:        加载的dialog
 * 包名:        high.rivamed.myapplication.views;
 * <p/>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class LoadingDialog extends Dialog {

   public LoadingDialog(Context context) {
	super(context);
   }

   public LoadingDialog(Context context, int theme) {
	super(context, theme);
   }

   public static class Builder {

	private Activity                mContext;
	private ImageView               mLoading;
	public  LoadingDialog           mDialog;
	public  AnimationDrawable       mAnimationDrawable;
	public  Handler                 mHandler;
	private WeakReference<Activity> activitySRF = null;

	public Builder(Activity context) {
	   this.mContext = context;
	}

	public LoadingDialog create() {
	   activitySRF = new WeakReference<Activity>(mContext);
	   LayoutInflater inflater = (LayoutInflater) activitySRF.get().getSystemService(
		   Context.LAYOUT_INFLATER_SERVICE);
	   mDialog = new LoadingDialog(activitySRF.get(), R.style.Dialog);
	   mDialog.setCancelable(false);
	   View layout = inflater.inflate(R.layout.dialog_loading_layout, null);
	   mLoading = (ImageView) layout.findViewById(R.id.animProgress);
	   mDialog.addContentView(layout,
					  new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
									     ViewGroup.LayoutParams.MATCH_PARENT));
	   mAnimationDrawable = (AnimationDrawable) mLoading.getBackground();
	   mAnimationDrawable.start();
	   EventBusUtils.post(new Event.EventHomeEnable(true));
	   if (null != activitySRF && null != activitySRF.get() && !activitySRF.get().isFinishing()) {
	      if (mHandler!=null){
		   postDialog();
		}else {
		   mHandler = new Handler(activitySRF.get().getMainLooper());
		   postDialog();
		}
	   }

	   return mDialog;
	}

	private void postDialog() {
	   mHandler.postDelayed(new Runnable() {
		@Override
		public void run() {
		   if (null != activitySRF && null != activitySRF.get() && !activitySRF.get().isFinishing()) {
			if (mDialog.isShowing()) {
			   mAnimationDrawable.stop();
			   mHandler.removeCallbacksAndMessages(null);
			   mHandler=null;
			   mDialog.dismiss();
			}
		   }
		}
	   }, 25000);
	}

   }
}

