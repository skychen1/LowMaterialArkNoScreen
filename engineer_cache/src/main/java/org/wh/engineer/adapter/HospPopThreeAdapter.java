package org.wh.engineer.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.wh.engineer.R;
import org.wh.engineer.bean.HospNameBean;

import java.util.List;


/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/30 15:26
 * 描述:        激活设备选择的位置 科室
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class HospPopThreeAdapter extends BaseQuickAdapter<HospNameBean.DeptVosBean, BaseViewHolder> {

   TextView mMeal;
   TextView mGoneMeal;

   public HospPopThreeAdapter(int layout, List<HospNameBean.DeptVosBean> data) {
      super(layout, data);
   }

   @Override
   protected void convert(BaseViewHolder helper, HospNameBean.DeptVosBean item) {
      findId(helper);
      mMeal.setText(item.getDeptName());
      mGoneMeal.setText(item.getDeptId());
   }

   private void findId(BaseViewHolder helper) {

      mMeal = ((TextView) helper.getView(R.id.item_meal));
      mGoneMeal = ((TextView) helper.getView(R.id.gone_meal));

   }
}
