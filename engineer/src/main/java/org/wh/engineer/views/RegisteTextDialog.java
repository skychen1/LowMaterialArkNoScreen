package org.wh.engineer.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import org.wh.engineer.R;

import java.lang.ref.WeakReference;


/**
 * 项目名称:    WE_TG
 * 创建者:      LiangDanMing
 * 创建时间:    2017/3/28 9:22
 * 描述:        无按钮的dialog
 * 包名:        wetg.p5w.net.views
 * <p/>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class RegisteTextDialog extends Dialog {

   public RegisteTextDialog(Context context) {
	super(context);
   }

   public RegisteTextDialog(Context context, int theme) {
	super(context, theme);
   }

   public static class Builder {

	private Activity         mContext;

	private TextView                mContentText;
	private TextView                mDialogBtn;
	public  RegisteTextDialog       mDialog;
	private WeakReference<Activity> activitySRF = null;
	public Builder(Activity context) {
	   this.mContext = context;
	}

	public RegisteTextDialog create() {
	   activitySRF = new WeakReference<Activity>(mContext);
	   LayoutInflater inflater = (LayoutInflater) activitySRF.get().getSystemService(
		   Context.LAYOUT_INFLATER_SERVICE);
	   mDialog = new RegisteTextDialog(activitySRF.get(), R.style.Dialog);
	   mDialog.setCancelable(false);
	   View layout = inflater.inflate(R.layout.dialog_registetext_layout, null);
	   mDialog.addContentView(layout,
					  new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
									    ViewGroup.LayoutParams.WRAP_CONTENT));
	   mContentText = (TextView) layout.findViewById(R.id.content);
	   mDialogBtn = (TextView) layout.findViewById(R.id.dialog_btn);
	   mContentText.setText(R.string.regist_text);
	   final WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
	   params.width = (int)mContext.getResources().getDimension(R.dimen.x1500);
	   mDialog.getWindow().setAttributes(params);
	   mDialogBtn.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View view) {
		   mDialog.dismiss();
		}
	   });
	   return mDialog;
	}
   }

}
