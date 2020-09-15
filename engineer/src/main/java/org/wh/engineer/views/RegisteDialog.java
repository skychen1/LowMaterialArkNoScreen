package org.wh.engineer.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import org.wh.engineer.R;
import org.wh.engineer.bean.HospNameBean;
import org.wh.engineer.bean.Movie;
import org.wh.engineer.http.BaseResult;
import org.wh.engineer.http.NetRequest;
import org.wh.engineer.utils.LogUtils;
import org.wh.engineer.utils.ToastUtils;
import org.wh.engineer.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/7/2 11:27
 * 描述:        2个按钮的dialog
 * 包名:       high.rivamed.myapplication.views
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class RegisteDialog extends Dialog {
	private DisplayMetrics mDm;
   public RegisteDialog(Context context, int theme) {
	super(context, theme);
	   WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	   if (wm != null) {
		   mDm = new DisplayMetrics();
		   wm.getDefaultDisplay().getMetrics(mDm);
	   }
   }

   @Override
   public void show() {

	WindowManager.LayoutParams layoutParams = getWindow().getAttributes();

	layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
	layoutParams.height = mDm.heightPixels;
	int left = UIUtils.getContext().getResources().getDimensionPixelSize(R.dimen.x530);
	int top = UIUtils.getContext().getResources().getDimensionPixelSize(R.dimen.y200);
	int right = UIUtils.getContext().getResources().getDimensionPixelSize(R.dimen.x530);
//	int bottom = UIUtils.getContext().getResources().getDimensionPixelSize(R.dimen.x200);
//	layoutParams.gravity = Gravity.c
	getWindow().getDecorView().setPadding(left, top, right, 0);
//	getWindow().getDecorView().setc(left, top, right, bottom);
	getWindow().setAttributes(layoutParams);
	super.show();
   }

   public static class Builder {

	private static final String TAG = "RegisteDialog";
	ImageView mDialogCloss;
	EditText  mAddressOne;
	TextView  mAddressTwo;
	EditText  mAddressThree;
	TextView  mAddressFour;
	TextView  mDialogLeft;
	TextView  mDialogRight;
	private Context         mContext;
	private Activity        mActivity;
	private String          mMsgTwo;
	private String          mMsgText;
	private String          mLeftText;
	private String          mRightText;
	private OnClickListener mLeftBtn;
	private OnClickListener mRightBtn;
	private TextView        mRigtht;
	private TextView        mLeft;
	private int mLeftTextColor = -1;
	private int      mRightTextColor;
	private int      mType;
	private TextView mDialogMsg;
	private TextView mDialogBtn;
	//	List<Movie> mMovies;
	List<Movie> mMovies1 = new ArrayList<>();
	private hospitalPopupWindow                   mMhospWindow;
	private TextView                              mGoneFourType;
	private TextView                              mGoneThreeType;
	private TextView                              mGoneTwoType;
	private String                                mTrim1;
	private HospNameBean mHospDept;

	public Builder(Context context, Activity activity) {
	   this.mContext = context;
	   this.mActivity = activity;

	}

	public Builder setTwoMsg(String title) {
	   this.mMsgTwo = title;
	   return this;
	}

	public Builder setMsg(String msg) {
	   this.mMsgText = msg;
	   return this;
	}

	public Builder setLeft(String left, int color, OnClickListener listener) {
	   this.mLeftText = left;
	   this.mLeftTextColor = color;
	   this.mLeftBtn = listener;
	   return this;
	}

	public Builder setLeft(String left, OnClickListener listener) {
	   this.mLeftText = left;
	   this.mLeftBtn = listener;
	   return this;
	}

	public Builder setRight(String left, int color, OnClickListener listener) {
	   this.mRightText = left;
	   this.mRightTextColor = color;
	   this.mRightBtn = listener;
	   return this;
	}

	public Builder setRight(String left, OnClickListener listener) {
	   this.mRightText = left;
	   this.mRightBtn = listener;
	   return this;
	}

	public RegisteDialog create() {

	   LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
		   Context.LAYOUT_INFLATER_SERVICE);
	   final RegisteDialog dialog = new RegisteDialog(mContext, R.style.Dialog);
	   dialog.setCancelable(false);
	   final View layout = inflater.inflate(R.layout.dialog_registe_layout, null);

	   dialog.addContentView(layout, new ViewGroup.LayoutParams(
		   mContext.getResources().getDimensionPixelSize(R.dimen.x860),
		   ViewGroup.LayoutParams.WRAP_CONTENT));
	   mDialogCloss = (ImageView) layout.findViewById(R.id.dialog_closs);
	   mAddressTwo = (TextView) layout.findViewById(R.id.address_two);
	   mAddressThree = (EditText) layout.findViewById(R.id.address_three);
	   mAddressFour = (TextView) layout.findViewById(R.id.address_four);
	   mGoneTwoType = (TextView) layout.findViewById(R.id.gone_two_type);
	   mGoneThreeType = (TextView) layout.findViewById(R.id.gone_three_type);
	   mGoneFourType = (TextView) layout.findViewById(R.id.gone_four_type);
	   mDialogLeft = (TextView) layout.findViewById(R.id.dialog_left);
	   mDialogRight = (TextView) layout.findViewById(R.id.dialog_right);

	   //所属院区
	   mAddressTwo.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		   if (UIUtils.isFastDoubleClick(v.getId())) {
			return;
		   } else {
		      getHospBranch(mAddressTwo, mGoneTwoType, 2);
		   }
		}
	   });
	   mAddressThree.addTextChangedListener(new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		   String trim = mAddressThree.getText().toString().trim();
		   LogUtils.i(TAG, "mAddressThree   " + trim);
		   if (trim.length() > 0) {
			String branchId = mGoneTwoType.getText().toString().trim();
			if (!trim.equals(mTrim1)) {
			   getHospDept(trim, branchId, mAddressThree, mGoneThreeType, 3);
			}
		   }
		}
		@Override
		public void afterTextChanged(Editable s) {
		}
	   });
	   mAddressFour.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		   if (UIUtils.isFastDoubleClick(v.getId())) {
			return;
		   } else {
			String trim = mGoneThreeType.getText().toString().trim();
			LogUtils.i(TAG, "mGoneThreeType   " + trim);
			getHospBydept(trim, mAddressFour, mGoneFourType, 4);
		   }
		}
	   });
	   mDialogCloss.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
		   dialog.dismiss();
		}
	   });
	   mDialogLeft.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
		   mLeftBtn.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
		}
	   });
	   mDialogRight.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
		   if (UIUtils.isFastDoubleClick(v.getId())) {
			return;
		   } else {
			String mAddressTwos = mAddressTwo.getText().toString().trim();
			String mAddressThrees = mAddressThree.getText().toString().trim();
			String mAddressFours = mAddressFour.getText().toString().trim();
			if ( mAddressTwos.length() > 1 &&
			    mAddressThrees.length() > 1 && (mAddressFours.length() > 1)) {

			   if (myListener != null) {
				String deptId = mGoneThreeType.getText().toString().trim();
				String storehouseCode = mGoneFourType.getText().toString().trim();
				String branchCode = mGoneTwoType.getText().toString().trim();
				String deptName = mAddressThree.getText().toString().trim();
				myListener.getDialogDate(deptName, branchCode, deptId, storehouseCode, dialog);
			   }
			} else {
			   ToastUtils.showShort("请先填写完整信息！");
			}
		   }
		}
	   });
	   return dialog;
	}

	private SettingListener myListener = null;

	public interface SettingListener {

	   public void getDialogDate(
               String deptName, String branchCode, String deptId, String storehouseCode,
               Dialog dialog);
	}

	public void setOnSettingListener(SettingListener listener) {
	   myListener = listener;
	}

	/**
	 * 给列表赋值
	 *
	 * @param hospNameBean
	 * @param textview
	 * @param goneview
	 */
	private void setAdapterDate(
		HospNameBean hospNameBean, TextView textview, TextView goneview, int type) {
	   if (hospNameBean != null) {
		mMhospWindow = new hospitalPopupWindow(mContext, hospNameBean, type);
		mMhospWindow.setFocusable(false);
		mMhospWindow.showPopupWindow(textview);
		adapterOnClick(mMhospWindow, textview, goneview, type);
	   } else {
		mMhospWindow.dismiss();
		mAddressOne.clearFocus();
	   }
	}

	/**
	 * adapter点击
	 *
	 * @param mMhospWindow
	 * @param goneview
	 * @param type
	 */
	private void adapterOnClick(
		hospitalPopupWindow mMhospWindow, TextView textview, TextView goneview, int type) {
	   if (type == 2) {
		mMhospWindow.mHospPopTwoAdapter.setOnItemClickListener(
			new BaseQuickAdapter.OnItemClickListener() {
			   @Override
			   public void onItemClick(
                       BaseQuickAdapter adapter, View view, int position) {
				TextView textView = (TextView) view.findViewById(R.id.item_meal);
				TextView mGoneMeal = (TextView) view.findViewById(R.id.gone_meal);
				String trim = textView.getText().toString().trim();
				mAddressTwo.setText(trim);
				String mGoneText = mGoneMeal.getText().toString().trim();
				goneview.setText(mGoneText);
				mAddressThree.setText("");
				mGoneThreeType.setText("");
				mAddressFour.setText("");
				mGoneFourType.setText("");
				mMhospWindow.dismiss();
			   }
			});
	   } else if (type == 3) {
		mMhospWindow.mThreeAdapter.setOnItemClickListener(
			new BaseQuickAdapter.OnItemClickListener() {
			   @Override
			   public void onItemClick(
                       BaseQuickAdapter adapter, View view, int position) {
				TextView textView = (TextView) view.findViewById(R.id.item_meal);
				TextView mGoneMeal = (TextView) view.findViewById(R.id.gone_meal);
				mTrim1 = textView.getText().toString().trim();
				mAddressThree.setText(mTrim1);
				String mGoneText = mGoneMeal.getText().toString().trim();
				goneview.setText(mGoneText);
				mAddressFour.setText("");
				mGoneFourType.setText("");
				mMhospWindow.dismiss();
				mAddressThree.clearFocus();

			   }
			});
	   } else if (type == 4) {
		mMhospWindow.mFourAdapter.setOnItemClickListener(
			new BaseQuickAdapter.OnItemClickListener() {
			   @Override
			   public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

				TextView mGoneMeal = (TextView) view.findViewById(R.id.gone_meal);
				TextView textView = (TextView) view.findViewById(R.id.item_meal);
				String trim = textView.getText().toString().trim();
				String mGoneText = mGoneMeal.getText().toString().trim();
				goneview.setText(mGoneText);
				mAddressFour.setText(trim);
				mMhospWindow.dismiss();
			   }
			});

	   }

	}

	/**
	 * 查询院区信息
	 */
	private void getHospBranch(TextView textview, TextView goneview, int type) {
	   NetRequest.getInstance().getHospBranch( mActivity, new BaseResult() {
		@Override
		public void onSucceed(String result) {
		   LogUtils.i(TAG, "result getHospBranch  " + result);
		   Gson gson = new Gson();
		   HospNameBean hospNameBean = gson.fromJson(result, HospNameBean.class);
		   setAdapterDate(hospNameBean, textview, goneview, type);
		}
	   });
	}

	/**
	 * 根据院区编码查询科室信息
	 */
	private void getHospDept(
		String deptNamePinYin, String branchId, TextView textview, TextView goneview,
		int type) {
	   NetRequest.getInstance()
		   .getHospDept(deptNamePinYin, branchId, mActivity, new BaseResult() {
			@Override
			public void onSucceed(String result) {
			   Gson gson = new Gson();

			   mHospDept = gson.fromJson(result, HospNameBean.class);
			   setAdapterDate(mHospDept, textview, goneview, type);

			   LogUtils.i(TAG, "result getHospDept   " + result);
			   if (mHospDept.getHospitalBranchs() == null || mHospDept.getHospitalBranchs().size() == 0) {
				return;
			   }

			   //		   if (type==3)
			   //		   if (hospNameBean.getTbaseHospitals().size() == 3 &&
			   //			 !hospNameBean.getTbaseHospitals().get(0).getHospName().equals("")) {
			   //			setAdapterDate(hospNameBean, textview, goneview, type);
			   //
			   //		   }
			}
		   });
	}

	/**
	 * 根据科室查询库房情况
	 */
	private void getHospBydept(String deptId, TextView textview, TextView goneview, int type) {
	   NetRequest.getInstance().getHospBydept(deptId, mActivity, new BaseResult() {
		@Override
		public void onSucceed(String result) {
		   LogUtils.i(TAG, "result getHospBydept   " + result);
		   Gson gson = new Gson();
		   HospNameBean hospNameBean = gson.fromJson(result, HospNameBean.class);
		   setAdapterDate(hospNameBean, textview, goneview, type);
		}
	   });
	}
   }
}
