package org.wh.engineer.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rivamed.libdevicesbase.base.DeviceInfo;
import com.ruihua.reader.ReaderCallback;
import com.ruihua.reader.ReaderManager;
import com.ruihua.reader.ReaderProducerType;
import com.ruihua.reader.bean.AntInfo;
import com.ruihua.reader.bean.EpcInfo;
import com.ruihua.reader.net.NetReaderManager;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.wh.engineer.R;
import org.wh.engineer.R2;
import org.wh.engineer.adapter.RegistReaderAdapter;
import org.wh.engineer.base.SimpleFragment;
import org.wh.engineer.bean.Event;
import org.wh.engineer.utils.EventBusUtils;
import org.wh.engineer.utils.SPUtils;
import org.wh.engineer.utils.StringUtils;
import org.wh.engineer.utils.UIUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.widget.GridLayout.VERTICAL;
import static org.wh.engineer.base.App.ANIMATION_TIME;
import static org.wh.engineer.base.App.READER_TIME;
import static org.wh.engineer.cont.Constants.SAVE_ANIMATION_TIME;
import static org.wh.engineer.cont.Constants.SAVE_READER_TIME;

/**
 * 项目名称:    Android_PV_2.6.1
 * 创建者:      DanMing
 * 创建时间:    2018/11/27 18:16
 * 描述:        reader设置
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class RegisteReaderFrag extends SimpleFragment {

   @BindView(R2.id.frag_start)
   TextView           mFragStart;
   @BindView(R2.id.timely_ll)
   LinearLayout       mTimelyLl;
   @BindView(R2.id.header)
   MaterialHeader     mHeader;
   @BindView(R2.id.recyclerview)
   RecyclerView mRecyclerview;
   @BindView(R2.id.refreshLayout)
   SmartRefreshLayout mRefreshLayout;
   @BindView(R2.id.public_ll)
   LinearLayout       mPublicLl;
   @BindView(R2.id.txt_log)
   TextView           mTxtLog;
   @BindView(R2.id.scroll_log)
   ScrollView         mScrollLog;
   SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
   @BindView(R2.id.item_time_text)
   EditText     mItemTimeText;
   @BindView(R2.id.item_time_text2)
   EditText     mItemTimeText2;
   @BindView(R2.id.item_setting_time)
   TextView     mItemSettingTime;
   @BindView(R2.id.gone_ll)
   LinearLayout mGoneLl;

   //   @BindView(R.id.reader_left)
   //   RadioButton mReaderLeft;
   //   @BindView(R.id.radioproup)
   //   RadioGroup  mRadioGroup;
   //   @BindView(R.id.reader_right)
   //   RadioButton mReaderRight;
   private String mDiviceId;
   List<String> mDate = new ArrayList<>();
   private RegistReaderAdapter mAdapter;
   private int                 mType;

   /**
    * adapter显示
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onHomeNoClick(Event.EventTestIdAndPower event) {
	if (event.readerId == null) {
	   AppendLog(event.readerPower);
	} else {
	   ReaderManager.getManager().startScan(event.readerId, READER_TIME);
	   AppendLog("启动持续扫描,设备ID=" + event.readerId + "，扫描完成后" + READER_TIME + " ms 停止扫描;");
	}
   }

   /**
    * adapter显示
    *
    * @param event
    */
   @Subscribe(threadMode = ThreadMode.MAIN)
   public void onHomeNoClicks(Event.EventTestStopScan event) {
	int i = ReaderManager.getManager().stopScan(event.readerId);
	AppendLog("停止扫描,设备ID=" + event.readerId + "   " + i);

   }

   @Override
   public int getLayoutId() {
	return R.layout.registe_reader_layout;
   }

   public static RegisteReaderFrag newInstance() {
	RegisteReaderFrag fragment = new RegisteReaderFrag();
	return fragment;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
	EventBusUtils.register(this);
//	if (SYSTEMTYPE.equals(SYSTEMTYPES_2)) {
//	   if (SPUtils.getString(getAppContext(), READER_NAME) == null ||
//		 SPUtils.getString(getAppContext(), READER_NAME).equals(READER_NAME_RODINBELL)) {
//		SPUtils.putString(mContext, READER_NAME, READER_NAME_RODINBELL);
////		mRadioGroup.check(R.id.reader_right);
//	   } else if (SPUtils.getString(getAppContext(), READER_NAME) != null &&
//			  SPUtils.getString(getAppContext(), READER_NAME).equals(READER_NAME_COLU)) {
////		mRadioGroup.check(R.id.reader_left);
//	   }
//	}

	initReader();
	mGoneLl.setVisibility(View.GONE);
	//	mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
	//	   @Override
	//	   public void onCheckedChanged(RadioGroup group, int checkedId) {
	//		ToastUtils.showShortToast("Reader启动中，请勿频繁切换，稍后点击 开始检测");
	//		switch (checkedId) {
	//		   case R.id.reader_left:
	//			SPUtils.putString(mContext, READER_NAME, READER_NAME_COLU);
	//			new Thread(new Runnable() {
	//			   @Override
	//			   public void run() {
	//
	//				NetReaderManager.getManager().stopService();
	//				ReaderManager.getManager().connectReader(ReaderProducerType.TYPE_NET_COLU);
	//			   }
	//			}).start();
	//
	//			break;
	//		   case R.id.reader_right:
	//			SPUtils.putString(mContext, READER_NAME, READER_NAME_RODINBELL);
	//
	//			new Thread(new Runnable() {
	//			   @Override
	//			   public void run() {
	//				Log.i("reader","启动罗丹");
	//				NetReaderManager.getManager().stopService();
	//				ReaderManager.getManager().connectReader(ReaderProducerType.TYPE_NET_RODINBELL);
	//			   }
	//			}).start();
	//			break;
	//		}
	//	   }
	//	});

   }

   @Override
   public void onBindViewBefore(View view) {

   }

   public void AppendLog(String msg) {
	mTxtLog.post(new Runnable() {
	   @Override
	   public void run() {
		Date time = new Date();
		String s = ">>" + simpleDateFormat.format(time) + "  " + msg + "\n";
		mTxtLog.append(s);

		int offset = mTxtLog.getMeasuredHeight() - mScrollLog.getMeasuredHeight();
		if (offset < 0) {
		   offset = 0;
		}
		mScrollLog.scrollTo(0, offset);
		Log.i("fff", msg);
	   }
	});
   }

   /**
    * 初始化罗丹贝尔回调
    */
   private void initReader() {
	//设置回调
	ReaderManager.getManager().registerCallback(new ReaderCallback() {
	   @Override
	   public void onConnectState(String deviceId, boolean isConnect) {
		if (isConnect) {
		   AppendLog("Reader设备已连接:::ID=" + deviceId);
		} else {
		   AppendLog("Reader设备已断开:::ID" + deviceId);
		   new Thread(() -> {
			NetReaderManager.getManager().stopService();
			AppendLog("重新连接");
			ReaderManager.getManager().connectReader(ReaderProducerType.TYPE_NET_RODINBELL);
		   }).start();
		}
	   }

	   @Override
	   public void onScanResult(String deviceId, Map<String, List<EpcInfo>> result) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Reader (" + deviceId + ")扫描完成\n");
		stringBuilder.append("\t EPC数量" + result.size() + "个:\n");
		for (Map.Entry<String, List<EpcInfo>> v : result.entrySet()) {
		   stringBuilder.append(v.getKey() + ";");
		}
		AppendLog(stringBuilder.toString());
	   }

	   @Override
	   public void onScanNewEpc(String deviceId, String epc, int ant) {
		String string;
		string = ("\t EPC数量:1" + "个:" + epc + ", 被第" + ant + "根天线扫到");
		AppendLog(string);
	   }

	   @Override
	   public void onSetPower(String deviceId, boolean success) {
		if (success) {
		   AppendLog("RFID设置功率结果：标识: " + deviceId + "----成功！");
		} else {
		   AppendLog("RFID设置功率结果：标识: " + deviceId + "----失败！");
		}
	   }

	   @Override
	   public void onGetPower(String deviceId, int power) {
		AppendLog("RFID读取功率结果：标识: " + deviceId + "; 功率 = " + power);
	   }

	   @Override
	   public void onGetPower(String deviceId, int[] power) {

	   }

	   @Override
	   public void onGetFrequency(String deviceId, String frequency) {
		AppendLog("设备：：" + deviceId + "的频率值是::" + frequency);
	   }

	   @Override
	   public void onCheckAnt(String deviceId, List<AntInfo> ant) {
		//nothing（暂未完成）
	   }

	   @Override
	   public void onLockOpen(String deviceId, boolean isSuccess) {
		AppendLog("设备：：" + deviceId + "开锁是否成功::" + isSuccess);
	   }

	   @Override
	   public void onLockClose(String deviceId, boolean isSuccess) {
		AppendLog("设备：：" + deviceId + "关锁是否成功::" + isSuccess);
	   }

	   @Override
	   public void onLightOpen(String deviceId, boolean isSuccess) {
		AppendLog("设备：：" + deviceId + "开灯是否成功::" + isSuccess);
	   }

	   @Override
	   public void onLightClose(String deviceId, boolean isSuccess) {
		AppendLog("设备：：" + deviceId + "关灯是否成功::" + isSuccess);
	   }

	   @Override
	   public void onLockState(String deviceId, boolean isOpened) {
		AppendLog("设备：：" + deviceId + "检测所的状态::" + isOpened);
	   }

	   @Override
	   public void onLightState(String deviceId, boolean isOpened) {
		AppendLog("设备：：" + deviceId + "检测灯的状态::" + isOpened);
	   }
	});
   }

   @OnClick({R2.id.frag_start, R2.id.item_setting_time, R2.id.item_setting_time2})
   public void onViewClicked(View view) {
   	if (view.getId()==R.id.frag_start){
		if (mDate != null) {
			mDate.clear();
			mGoneLl.setVisibility(View.GONE);
			mTxtLog.setText("");
		}
		List<DeviceInfo> connectedDevice = ReaderManager.getManager().getConnectedReader();
		String s = "";
		for (DeviceInfo de : connectedDevice) {

			mDiviceId = de.getIdentification();
			mDate.add(mDiviceId);
			ReaderManager.getManager().getPower(mDiviceId);
			s += "\t  设备类型 \t" + de.getProduct() + ";\t\t设备ID \t" + de.getIdentification() +
					"\n";
		}
		AppendLog(StringUtils.isEmpty(s) ? "目前暂无reader连接" : ("已连接设备如下：\n" + s));
		mGoneLl.setVisibility(StringUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
		mItemTimeText2.setHint("动画延时:" + ANIMATION_TIME + "ms");
		mItemTimeText.setHint("扫描时间:" + READER_TIME + "ms");

		int mLayout = R.layout.item_reader_layout;
		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		} else {
			mAdapter = new RegistReaderAdapter(mLayout, mDate);
			mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
			mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
			mRefreshLayout.setEnableAutoLoadMore(false);
			mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
			mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
			mRecyclerview.setAdapter(mAdapter);
		}
	}else if (view.getId()==R.id.item_setting_time){
		try {
			int time = Integer.parseInt(mItemTimeText.getText().toString().trim());
			SPUtils.putInt(UIUtils.getContext(), SAVE_READER_TIME, time);
			READER_TIME = SPUtils.getInt(UIUtils.getContext(), SAVE_READER_TIME);
			AppendLog("设置RFID reader扫描完成：" + READER_TIME + " ms 后停止扫描！");
		} catch (Exception ex) {
		}
	}else if (view.getId()==R.id.item_setting_time2){
		try {
			int time = Integer.parseInt(mItemTimeText2.getText().toString().trim());
			SPUtils.putInt(UIUtils.getContext(), SAVE_ANIMATION_TIME, time);
			ANIMATION_TIME = SPUtils.getInt(UIUtils.getContext(), SAVE_ANIMATION_TIME);
			AppendLog("设置扫描动画无新耗材扫描到延时：" + ANIMATION_TIME + " ms 后停止动画！");
		} catch (Exception ex) {
		}
	}
/*	switch (view.getId()) {
	   case R.id.frag_start:
		if (mDate != null) {
		   mDate.clear();
		   mGoneLl.setVisibility(View.GONE);
		   mTxtLog.setText("");
		}
		List<DeviceInfo> connectedDevice = ReaderManager.getManager().getConnectedReader();
		String s = "";
		for (DeviceInfo de : connectedDevice) {

		   mDiviceId = de.getIdentification();
		   mDate.add(mDiviceId);
		   ReaderManager.getManager().getPower(mDiviceId);
		   s += "\t  设备类型 \t" + de.getProduct() + ";\t\t设备ID \t" + de.getIdentification() +
			  "\n";
		}
		AppendLog(StringUtils.isEmpty(s) ? "目前暂无reader连接" : ("已连接设备如下：\n" + s));
		mGoneLl.setVisibility(StringUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
		mItemTimeText2.setHint("动画延时:" + ANIMATION_TIME + "ms");
		mItemTimeText.setHint("扫描时间:" + READER_TIME + "ms");

		int mLayout = R.layout.item_reader_layout;
		if (mAdapter != null) {
		   mAdapter.notifyDataSetChanged();
		} else {
		   mAdapter = new RegistReaderAdapter(mLayout, mDate);
		   mRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, VERTICAL));
		   mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
		   mRefreshLayout.setEnableAutoLoadMore(false);
		   mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
		   mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
		   mRecyclerview.setAdapter(mAdapter);
		}
		break;

	   case R.id.item_setting_time:
		try {
		   int time = Integer.parseInt(mItemTimeText.getText().toString().trim());
		   SPUtils.putInt(UIUtils.getContext(), SAVE_READER_TIME, time);
		   READER_TIME = SPUtils.getInt(UIUtils.getContext(), SAVE_READER_TIME);
		   AppendLog("设置RFID reader扫描完成：" + READER_TIME + " ms 后停止扫描！");
		} catch (Exception ex) {
		}
		break;
	   case R.id.item_setting_time2:
		try {
		   int time = Integer.parseInt(mItemTimeText2.getText().toString().trim());
		   SPUtils.putInt(UIUtils.getContext(), SAVE_ANIMATION_TIME, time);
		   ANIMATION_TIME = SPUtils.getInt(UIUtils.getContext(), SAVE_ANIMATION_TIME);
		   AppendLog("设置扫描动画无新耗材扫描到延时：" + ANIMATION_TIME + " ms 后停止动画！");
		} catch (Exception ex) {
		}
		break;
	}*/
   }

   @Override
   public void onDestroy() {
	super.onDestroy();
	EventBusUtils.unregister(this);
   }

}
