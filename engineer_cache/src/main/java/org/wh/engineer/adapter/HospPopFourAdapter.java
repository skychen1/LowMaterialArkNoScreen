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
 * 描述:        激活设备选择的位置 库房
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class HospPopFourAdapter extends BaseQuickAdapter<HospNameBean.StoreHousesBean, BaseViewHolder> {

   TextView mMeal;
   TextView mGoneMeal;

   public HospPopFourAdapter(int layout, List<HospNameBean.StoreHousesBean> data) {
      super(layout, data);
   }

   @Override
   protected void convert(BaseViewHolder helper, HospNameBean.StoreHousesBean item) {
      findId(helper);
      mMeal.setText(item.getSthName());
      mGoneMeal.setText(item.getSthId());
   }

   private void findId(BaseViewHolder helper) {

      mMeal = ((TextView) helper.getView(R.id.item_meal));
      mGoneMeal = ((TextView) helper.getView(R.id.gone_meal));

   }
}
