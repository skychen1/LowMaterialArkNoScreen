package org.wh.engineer.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;

import org.wh.engineer.base.mvp.IPresent;
import org.wh.engineer.base.mvp.IViewFrg;
import org.wh.engineer.base.mvp.KnifeKit;
import org.wh.engineer.base.mvp.VDelegate;
import org.wh.engineer.base.mvp.VDelegateBase;
import org.wh.engineer.bean.Event;
import org.wh.engineer.utils.DevicesUtils;
import org.wh.engineer.utils.EventBusUtils;
import org.wh.engineer.utils.MyTimer;
import org.wh.engineer.utils.UIUtils;

import java.util.List;

import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

import static org.wh.engineer.base.App.SYSTEMTYPE;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 16:22
 * 描述:        抽取的fragment，没有标题栏
 * 包名:        high.rivamed.myapplication.base
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public abstract class SimpleFragment<P extends IPresent> extends SupportFragment
	implements IViewFrg<P> {

   private static final String TAG = "SimpleFragment";
   private VDelegate vDelegate;
   private   P              p;
   protected Activity       mContext;
   private   View           rootView;
   protected LayoutInflater layoutInflater;

   public  Gson         mGson;
   public  List<String> mReaderDeviceId;
   public  List<String> eth002DeviceIdList;
   private Unbinder     unbinder;
   private Object       mTitleName;
   private MyTimer countTimerView;
   //倒计时总时间

   @Nullable
   @Override
   public View onCreateView(
	   LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	layoutInflater = inflater;
	//	init();
//	countTimerView = MyTimer.getInstance(COUNTDOWN_TIME, 1000, UIUtils.getContext());

	eth002DeviceIdList = DevicesUtils.getBomDeviceId(SYSTEMTYPE);
	mReaderDeviceId = DevicesUtils.getReaderDeviceId();
	if (rootView == null && getLayoutId() > 0) {
	   rootView = inflater.inflate(getLayoutId(), null);
	   onBindViewBefore(rootView);
	   bindUI(rootView);
	   getTitleName();
	} else {
	   ViewGroup viewGroup = (ViewGroup) rootView.getParent();
	   if (viewGroup != null) {
		viewGroup.removeView(rootView);
	   }
	}
	mGson = new Gson();

	rootView.setOnTouchListener(new View.OnTouchListener() {
	   @Override
	   public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		   //获取触摸动作，如果ACTION_UP，计时开始。
		   case MotionEvent.ACTION_UP:
//			LogUtils.i(TAG, "   ACTION_UP  ");
			EventBusUtils.post(new Event.EventTouch(true));
//			if (SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_DATA) != null &&
//			    !SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_DATA).equals("")) {
//			   countTimerView.cancel();
//			   countTimerView.start();
//			}
			break;
		   //否则其他动作计时取消
		   default:
			EventBusUtils.post(new Event.EventTouch(false));
//			countTimerView.cancel();
//			LogUtils.i(TAG, "   其他操作  ");
			break;
		}
		return false;
	   }
	});

	return rootView;
   }

   @Override
   public void onActivityCreated(@Nullable Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	bindEvent();
	initDataAndEvent(savedInstanceState);
   }

   @Override
   public void bindUI(View rootView) {
	unbinder = KnifeKit.bind(this, rootView);
   }

   protected VDelegate getvDelegate() {
	if (vDelegate == null) {
	   vDelegate = VDelegateBase.create(mContext);
	}
	return vDelegate;
   }

   protected P getP() {
	if (p == null) {
	   p = newP();
	   if (p != null) {
		p.attachV(this);
	   }
	}
	return p;
   }

   @Override
   public void onStart() {
	super.onStart();
	eth002DeviceIdList = DevicesUtils.getBomDeviceId(SYSTEMTYPE);
	mReaderDeviceId = DevicesUtils.getReaderDeviceId();
   }

   @Override
   public void onResume() {
//	if (SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_DATA) != null &&
//	    !SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_DATA).equals("")) {
//	   countTimerView.cancel();
//	   countTimerView.start();
//	}
	super.onResume();
   }

   @Override
   public void onPause() {
//	countTimerView.cancel();
	super.onPause();
   }

   @Override
   public void onAttach(Context context) {
	super.onAttach(context);
	if (context instanceof Activity) {
	   this.mContext = (Activity) context;
	}
   }

   @Override
   public void onDetach() {
	super.onDetach();
	mContext = null;
   }

   @Override
   public boolean useEventBus() {
	return false;
   }

   @Override
   public P newP() {
	return null;
   }

   @Override
   public void onDestroyView() {
	super.onDestroyView();
	OkGo.getInstance().cancelTag(this);

	UIUtils.removeAllCallbacks();
	if (getP() != null) {
	   getP().detachV();
	}
	getvDelegate().destory();
	p = null;
	vDelegate = null;
   }

   @Override
   public int getOptionsMenuId() {
	return 0;
   }

   @Override
   public void bindEvent() {

   }

   public void getTitleName() {
   }

   @Override
   public void onDestroy() {
	super.onDestroy();
	EventBusUtils.unregister(this);
   }
}

