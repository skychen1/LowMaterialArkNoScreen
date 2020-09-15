package org.wh.engineer.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.wh.engineer.R;
import org.wh.engineer.R2;
import org.wh.engineer.base.SimpleFragment;
import org.wh.engineer.bean.ThingDto;
import org.wh.engineer.http.BaseResult;
import org.wh.engineer.http.NetRequest;
import org.wh.engineer.utils.EventBusUtils;
import org.wh.engineer.utils.SPUtils;
import org.wh.engineer.utils.ToastUtils;
import org.wh.engineer.utils.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static org.wh.engineer.activity.RegisteActivity.mRegisteViewpager;
import static org.wh.engineer.base.App.mAppContext;
import static org.wh.engineer.cont.Constants.FACE_UPDATE_TIME;
import static org.wh.engineer.cont.Constants.SN_NUMBER;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/19 11:26
 * 描述:        数据恢复界面
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class RegisteRecoverFrag extends SimpleFragment {

   @BindView(R2.id.one)
   TextView     mOne;
   @BindView(R2.id.frag_registe_name_edit)
   EditText     mFragRegisteNameEdit;
   @BindView(R2.id.frag_registe_name)
   LinearLayout mFragRegisteName;
   @BindView(R2.id.fragment_btn_one)
   TextView     mFragmentBtnOne;
   @BindView(R2.id.activity_down_btn_one_ll)
   LinearLayout mActivityDownBtnOneLl;

   public static RegisteRecoverFrag newInstance() {
	Bundle args = new Bundle();
	RegisteRecoverFrag fragment = new RegisteRecoverFrag();
	//	args.putInt(TYPE_SIZE, param);
	//	args.putString(TYPE_PAGE, type);
	//	fragment.setArguments(args);
	return fragment;

   }

   @Override
   public int getLayoutId() {
	return R.layout.frg_recover_layout;
   }

   @Override
   public void initDataAndEvent(Bundle savedInstanceState) {
//	if (BuildConfig.DEBUG) {
//	   mFragRegisteNameEdit.setText("2ab");
//	}else {
	   mFragRegisteNameEdit.setText(SPUtils.getString(mAppContext,SN_NUMBER));
//	}
   }

   private void loadDate() {
	String sn = mFragRegisteNameEdit.getText().toString().trim();
	NetRequest.getInstance().getRecoverDate(sn, mContext, new BaseResult() {
	   @Override
	   public void onSucceed(String result) {
		Log.i("RegisteFrag","sn    "+result);
		ThingDto snRecoverBean = mGson.fromJson(result, ThingDto.class);
		if (snRecoverBean.isOperateSuccess()){
		   EventBusUtils.postSticky(snRecoverBean);
		   SPUtils.putString(UIUtils.getContext(), FACE_UPDATE_TIME, "");
		   mRegisteViewpager.setCurrentItem(0);
		}else {
		   ToastUtils.showShortToast("数据恢复失败！");
		}

	   }
	});
   }

   @Override
   public void onBindViewBefore(View view) {

   }


   @OnClick(R2.id.fragment_btn_one)
   public void onViewClicked() {
	if (UIUtils.isFastDoubleClick(R.id.fragment_btn_one)) {
	   return;
	} else {
	   loadDate();
	}

   }
}
