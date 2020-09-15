package org.wh.engineer.adapter;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.wh.engineer.R;
import org.wh.engineer.bean.SelfCheckBean;
import org.wh.engineer.bean.SelfCheckTitleBean;

import java.util.List;


/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/18 13:57
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class SelfCheckAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
   public static final int TYPE_TITLE = 0;
   public static final int TYPE_CONTEXT = 1;
   public SelfCheckAdapter(
         List<MultiItemEntity> data) {
      super(data);
      addItemType(TYPE_TITLE, R.layout.item_self_six_layout);
      addItemType(TYPE_CONTEXT, R.layout.item_self_six_layout);
   }

   @Override
   protected void convert(
           final BaseViewHolder helper, final MultiItemEntity item) {
      switch (helper.getItemViewType()) {
         case TYPE_TITLE:
		LinearLayout view = (LinearLayout) helper.getView(R.id.seven_ll);
		view.setBackgroundResource(R.color.bg_f6);
		final SelfCheckBean item1 = (SelfCheckBean) item;
		((TextView) helper.getView(R.id.seven_two)).setText(item1.nametitle);
//		((TextView) helper.getView(R.id.seven_three)).setText(item1.typetitle);
		((TextView) helper.getView(R.id.seven_four)).setText(item1.mactitle);
		((TextView) helper.getView(R.id.seven_five)).setText(item1.iptitle);
		TextView textView = (TextView) helper.getView(R.id.seven_six_text);
		ProgressBar loading = (ProgressBar) helper.getView(R.id.seven_six_loading);
		helper.setImageResource(R.id.seven_one,item1.isExpanded() ? R.mipmap.self_open : R.mipmap.self_closs);

		if (item1.checktitle==0){
		   loading.setVisibility(View.GONE);
		   textView.setText("正常");
		   textView.setTextColor(mContext.getResources().getColor(R.color.color_g));
		   Drawable drawableLeft = mContext.getResources().getDrawable(
			   R.mipmap.hccz_ic_tx_small);
		   textView.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,null,null,null);
		   textView.setCompoundDrawablePadding(10);
		}else if (item1.checktitle==1){
		   loading.setVisibility(View.VISIBLE);
		   textView.setVisibility(View.GONE);
		}else if (item1.checktitle==2){
		   loading.setVisibility(View.GONE);
		   textView.setText("异常");
		   textView.setTextColor(mContext.getResources().getColor(R.color.color_red));
		   Drawable drawableLeft = mContext.getResources().getDrawable(
			   R.mipmap.gcms_ic_yc);
		   textView.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,null,null,null);
		   textView.setCompoundDrawablePadding(10);
		}else {
		   textView.setVisibility(View.GONE);
		   loading.setVisibility(View.GONE);
		}
		helper.itemView.setOnClickListener(new View.OnClickListener() {
		   @Override
		   public void onClick(View v) {
			int pos = helper.getAdapterPosition();
			if (item1.isExpanded()) {
			   collapse(pos,false,true);
			} else {
			   expand(pos,false,true);
			}
		   }
		});
		break;
	   case TYPE_CONTEXT:
		SelfCheckTitleBean item2 = (SelfCheckTitleBean) item;

		((TextView) helper.getView(R.id.seven_two)).setText(item2.getName());
//		((TextView) helper.getView(R.id.seven_three)).setText(item2.getType());
		((TextView) helper.getView(R.id.seven_four)).setText(item2.getMac());
		((TextView) helper.getView(R.id.seven_five)).setText(item2.getIp());
		TextView textView2 = (TextView) helper.getView(R.id.seven_six_text);
		ProgressBar loading2 = (ProgressBar) helper.getView(R.id.seven_six_loading);
		Log.i("ff",helper.getAdapterPosition()+"    "+item2.check);
		if (item2.check==0){
		   loading2.setVisibility(View.GONE);
		   textView2.setText("正常");
		   textView2.setTextColor(mContext.getResources().getColor(R.color.color_g));
		   Drawable drawableLeft = mContext.getResources().getDrawable(
			   R.mipmap.hccz_ic_tx_small);
		   textView2.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,null,null,null);
		   textView2.setCompoundDrawablePadding(10);
		}else if (item2.check==1){
		   loading2.setVisibility(View.VISIBLE);
		   textView2.setVisibility(View.GONE);
		}else if (item2.check==2){
		   loading2.setVisibility(View.GONE);
		   textView2.setText("异常");
		   textView2.setTextColor(mContext.getResources().getColor(R.color.color_red));
		   Drawable drawableLeft = mContext.getResources().getDrawable(
			   R.mipmap.gcms_ic_yc);
		   textView2.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,null,null,null);
		   textView2.setCompoundDrawablePadding(10);
		}else {
		   textView2.setVisibility(View.GONE);
		   loading2.setVisibility(View.GONE);
		}
//		helper.itemView.setOnClickListener(new View.OnClickListener() {
//		   @Override
//		   public void onClick(View v) {
//
//		   }
//		});
		break;
      }

   }
}
