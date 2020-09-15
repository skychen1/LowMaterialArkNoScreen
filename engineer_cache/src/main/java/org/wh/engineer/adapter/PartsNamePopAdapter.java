package org.wh.engineer.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.wh.engineer.R;
import org.wh.engineer.bean.TBaseDevices;

import java.util.List;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/30 15:26
 * 描述:        硬件类型
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class PartsNamePopAdapter extends BaseQuickAdapter<TBaseDevices.tBaseDevices.partsnameBean, BaseViewHolder> {

   TextView mMeal;

   public PartsNamePopAdapter(int layout, List<TBaseDevices.tBaseDevices.partsnameBean> data) {
      super(layout, data);
   }

   @Override
   protected void convert(BaseViewHolder helper, TBaseDevices.tBaseDevices.partsnameBean item) {
      findId(helper);
      mMeal.setText(item.getName());
   }

   private void findId(BaseViewHolder helper) {

      mMeal = ((TextView) helper.getView(R.id.item_meal));

   }
}
