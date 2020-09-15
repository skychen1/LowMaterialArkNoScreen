package org.wh.engineer.views;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.wh.engineer.R;
import org.wh.engineer.adapter.PartsNamePopAdapter;
import org.wh.engineer.adapter.PartsmacPopAdapter;
import org.wh.engineer.bean.TBaseDevices;
import org.wh.engineer.utils.LogUtils;

import java.util.List;

import static android.widget.LinearLayout.VERTICAL;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/15 15:20
 * 描述:        部件类型选择的PopupWindow
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class TypePopupWindow extends PopupWindow {

   // 坐标的位置（x、y）
   private final int[] mLocation = new int[2];

   private final RecyclerView mRecyclerView;
   private final View         mView;

   private String TAG = "SettingPopupWindow";
   private       OnClickListener mItemClickListener;
   private PartsmacPopAdapter mMealPopAdapter;

   private       TextView            mTextView;
   private       TextView            mGoneDictid;
   private       TextView            mGoneDevicetype;
   private final PartsNamePopAdapter mPartsNamePopAdapter;

   public TypePopupWindow(Context context, List<TBaseDevices.tBaseDevices.partsnameBean> data) {
	mView = LayoutInflater.from(context).inflate(R.layout.mac_popupwindow, null);
	mRecyclerView = (RecyclerView) mView.findViewById(R.id.search_rv);

	mPartsNamePopAdapter = new PartsNamePopAdapter(R.layout.item_mac_single, data);
	mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
	mRecyclerView.addItemDecoration(new DividerItemDecoration(context, VERTICAL));
	mRecyclerView.setAdapter(mPartsNamePopAdapter);

	mPartsNamePopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
	   @Override
	   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
		TextView textView = (TextView) view.findViewById(R.id.item_meal);
		String dictId = data.get(position).getDictId();
		String name = data.get(position).getName();
		String deviceType = data.get(position).getDeviceType();
		LogUtils.i(TAG, "dictId  " + dictId + "   name   " + name + "   " + deviceType);
		String trim = textView.getText().toString().trim();
		mGoneDictid.setText(dictId);
		mGoneDevicetype.setText(deviceType);
		mTextView.setText(trim);
		dismiss();
	   }
	});

//	heightMeth(data);

//	mEditText.addTextChangedListener(new TextWatcher() {
//	   @Override
//	   public void beforeTextChanged(CharSequence mObject, int start, int count, int after) {
//
//	   }
//
//	   @Override
//	   public void onTextChanged(CharSequence mObject, int start, int before, int count) {
//		String trim = mEditText.getText().toString().trim();
//		mMovies1.clear();
//
//		for (int i = 0; i < mMovies.size() - 1; i++) {
//		   String string = mMovies.get(i).mString;
//		   if (string.contains(trim)) {
//			mMovies1.add(mMovies.get(i));
//		   }
//		}
//		if (trim.equals("")) {
//		   mMovies1.addAll(mMovies);
//		}
//
//		heightMeth();
//		mMealPopAdapter.notifyDataSetChanged();
//
//	   }
//
//	   @Override
//	   public void afterTextChanged(Editable mObject) {
//
//	   }
//	});
   }

   private void heightMeth(List<TBaseDevices.tBaseDevices.partsnameBean> data) {
	ViewGroup.LayoutParams lp = mRecyclerView.getLayoutParams();
	if (data.size() > 8) {
	   lp.height = 400;
	} else {
	   lp.height = 72 * data.size();
	}
	mRecyclerView.setLayoutParams(lp);
   }

   public void showPopupWindow(View parent,View gone_dictid,View gone_devicetype,int pos) {
	parent.getLocationOnScreen(mLocation);
	this.mTextView =(TextView) parent;
	this.mGoneDictid =(TextView) gone_dictid;
	this.mGoneDevicetype =(TextView) gone_devicetype;
	this.setContentView(mView);
	this.setWidth(parent.getWidth()+10);
	this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
	this.setFocusable(true);
	this.setOutsideTouchable(true);
	this.update();
	// 实例化一个ColorDrawable颜色为半透明
	ColorDrawable dw = new ColorDrawable(0000000000);
	// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
	this.setBackgroundDrawable(dw);
	this.setAnimationStyle(R.style.social_pop_anim);

	if (!this.isShowing()) {

	   showAtLocation(parent, Gravity.NO_GRAVITY, mLocation[0]-5,
				mLocation[1] + parent.getHeight());
	   //	   showAtLocation(parent, Gravity.NO_GRAVITY, 100,
	   //				240);
	} else {
	   dismiss();
	}
   }

}
