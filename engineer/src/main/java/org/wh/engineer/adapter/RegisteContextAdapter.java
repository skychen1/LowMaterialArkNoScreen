package org.wh.engineer.adapter;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.wh.engineer.R;
import org.wh.engineer.bean.TBaseDevices;
import org.wh.engineer.fragment.RegisteFrag;
import org.wh.engineer.utils.LogUtils;
import org.wh.engineer.utils.SPUtils;
import org.wh.engineer.utils.UIUtils;
import org.wh.engineer.views.MacPopupWindow;
import org.wh.engineer.views.TypePopupWindow;

import java.util.ArrayList;
import java.util.List;

import static org.wh.engineer.cont.Constants.SAVE_ACTIVATION_REGISTE;
import static org.wh.engineer.cont.Constants.SAVE_ONE_REGISTE;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/16 13:41
 * 描述:        柜体信息内部部件列表adapter
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class RegisteContextAdapter extends BaseQuickAdapter<TBaseDevices.tBaseDevices, BaseViewHolder>{
public           RecyclerView                    recyclerView;
   public        TBaseDevices.tBaseDevices       mTBaseDevice;
   private       DeleteClickListener             mDeleteListener;
   private       List<TBaseDevices.tBaseDevices> data;
   private final List<TBaseDevices.tBaseDevices> mTBaseList = new ArrayList<>();;
   public String mString;
   public int mTopPos;
   public TextView mMFootMac;
   private MacPopupWindow mMacPopupWindow;
   public TextView mMFootName;
   public EditText mMFootIp;
   public ImageView mMFootDelete;
   public ImageView mMFootAdd;
   private TypePopupWindow mTypePopupWindow;
   private List<TBaseDevices.tBaseDevices.partsmacBean> mPartsmac;
   private List<TBaseDevices.tBaseDevices.partsnameBean> mPartsmacName;
   private TextView mGone_dictid;
   private TextView mGone_devicetype;
   private TextView mGone_deviceCode;
   ;

   public RegisteContextAdapter(
		   int layoutResId, @Nullable List<TBaseDevices.tBaseDevices> data, RecyclerView recyclerView, int mTopPos) {
	super(layoutResId, data);
	this.recyclerView =recyclerView;
//	this.mDeleteListener = listener;
	this.data = data;
	this.mTopPos = mTopPos;


   }

   @Override
   protected void convert(
	   final BaseViewHolder helper, TBaseDevices.tBaseDevices item) {
	mMFootName = (TextView) helper.getView(R.id.foot_name);
	mMFootMac = (TextView) helper.getView(R.id.foot_mac);
	mMFootIp = (EditText) helper.getView(R.id.foot_ip);
	mMFootDelete = (ImageView) helper.getView(R.id.foot_delete);
	mMFootAdd = (ImageView) helper.getView(R.id.foot_add);
	mGone_dictid = (TextView)helper.getView(R.id.gone_dictid);
	mGone_devicetype =(TextView) helper.getView(R.id.gone_devicetype);
	mGone_deviceCode =(TextView) helper.getView(R.id.gone_device_code);
	final LinearLayout mLinearLayout = (LinearLayout) helper.getView(R.id.foot_ll);

	mPartsmac = item.getPartsmac();
	mPartsmacName = item.getPartsmacName();

	if (SPUtils.getBoolean(UIUtils.getContext(), SAVE_ONE_REGISTE)) {

	   mMFootName.setText(item.getPartsname());
	   mMFootMac.setText(item.getPartmac());
	   mMFootIp.setText(item.getPartip());
	   mGone_dictid.setText(item.getDictId());
	   mGone_devicetype.setText(item.getDeviceType());
	   mGone_deviceCode.setText(item.getDeviceId());
	}

	if (SPUtils.getBoolean(UIUtils.getContext(), SAVE_ACTIVATION_REGISTE)){
	   mLinearLayout.setVisibility(View.GONE);
	}else {
	   mLinearLayout.setVisibility(View.VISIBLE);
	}


	if (mData.size()==1){
	   mMFootDelete.setVisibility(View.GONE);
	   mMFootAdd.setVisibility(View.VISIBLE);
	}else if (mData.size()==helper.getAdapterPosition()){
	   mMFootDelete.setVisibility(View.VISIBLE);
	   mMFootAdd.setVisibility(View.VISIBLE);
	}else {
	   mMFootDelete.setVisibility(View.VISIBLE);
	   mMFootAdd.setVisibility(View.GONE);
	}

	mMFootDelete.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		int pos = helper.getAdapterPosition();
		LogUtils.i("RegisteFrag","pos   "+pos);

//		RegisteSmallAdapter.tBaseDevice.remove(pos-1);
		data.remove(pos-1);
		RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
		if (mData.size()==1){
		   layoutManager.getChildAt(mData.size()).findViewById(R.id.foot_delete).setVisibility(View.GONE);
		   layoutManager.getChildAt(mData.size()).findViewById(R.id.foot_add).setVisibility(View.VISIBLE);
		}else {
		   layoutManager.getChildAt(mData.size()).findViewById(R.id.foot_delete).setVisibility(View.VISIBLE);
		   layoutManager.getChildAt(mData.size()).findViewById(R.id.foot_add).setVisibility(View.VISIBLE);
		}
		if (mMacPopupWindow!=null){
		   mMacPopupWindow.mMealPopAdapter.notifyDataSetChanged();

		}

		//		if (mMacPopupWindow!=null&&mMacPopupWindow.mRecyclerView!=null){
//		   String toString = ((TextView) mMacPopupWindow.mRecyclerView.getChildAt(pos - 1)
//			   .findViewById(R.id.item_meal)).getText().toString();
//		   for (int i=0;i<mPartsmac.size();i++){
//			mPartsmac.add()
//		   }
//		}


	   }
	});


	mMFootAdd.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		int pos = helper.getAdapterPosition();
		mMFootDelete.setVisibility(View.VISIBLE);
		mMFootAdd.setVisibility(View.GONE);

		TBaseDevices.tBaseDevices tBaseDevice = new TBaseDevices.tBaseDevices();
		if (mMacPopupWindow!=null&&mMacPopupWindow.mMacDates!=null){
		   tBaseDevice.setPartsmac(mMacPopupWindow.mMacDates);
		}else {
		   tBaseDevice.setPartsmac(mPartsmac);
		}
		tBaseDevice.setPartsmacName(mPartsmacName);
		data.add(pos, tBaseDevice);
		notifyItemInserted(pos+1);

	   }
	});


	mMFootMac.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		int pos = helper.getAdapterPosition();
		Log.i("xxa",helper.getAdapterPosition()+"   条");
		Log.i("xxa","mPartsmac   "+(data.get(pos-1).getPartsmac()));
		Log.i("xxa","mPartsmac   "+(data.get(pos-1).getPartsmac().size()));
//		mMacPopupWindow = new MacPopupWindow(mContext, data.get(pos-1).getPartsmac());
		mMacPopupWindow = new MacPopupWindow(mContext, RegisteFrag.mDeviceInfos);
		mMacPopupWindow.showPopupWindow(helper.getView(R.id.foot_mac),helper.getView(R.id.foot_ip),pos);
	   }
	});

	mMFootName.setOnClickListener(new View.OnClickListener() {
	   @Override
	   public void onClick(View v) {
		int pos = helper.getAdapterPosition();

		Log.i("xxa",helper.getAdapterPosition()+"   条");
		View foot_name = helper.getView(R.id.foot_name);

		mTypePopupWindow = new TypePopupWindow(mContext, data.get(pos-1).getPartsmacName());
		mTypePopupWindow.showPopupWindow(foot_name, mGone_dictid, mGone_devicetype, pos);
	   }
	});

   }
   public static abstract class DeleteClickListener implements View.OnClickListener {

	/**
	 * 基类的onClick方法
	 */
	@Override
	public void onClick(View v) {
	   myOnClick((Integer) v.getTag(), v);
	}

	public abstract void myOnClick(int position, View v);
   }
}
