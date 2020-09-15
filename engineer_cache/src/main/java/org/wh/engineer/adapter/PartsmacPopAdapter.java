package org.wh.engineer.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rivamed.libdevicesbase.base.DeviceInfo;

import org.wh.engineer.R;

import java.util.List;


/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/30 15:26
 * 描述:        硬件标识
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class PartsmacPopAdapter extends BaseQuickAdapter<DeviceInfo, BaseViewHolder> {

   TextView mMeal;

   public PartsmacPopAdapter(int layout, List<DeviceInfo> data) {
      super(layout, data);
   }

   @Override
   protected void convert(BaseViewHolder helper, DeviceInfo item) {
      findId(helper);
      mMeal.setText(item.getIdentification());
   }

   private void findId(BaseViewHolder helper) {

      mMeal = ((TextView) helper.getView(R.id.item_meal));

   }
}
