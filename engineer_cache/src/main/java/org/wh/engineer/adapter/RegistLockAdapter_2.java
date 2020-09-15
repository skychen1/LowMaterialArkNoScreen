package org.wh.engineer.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.wh.engineer.R;
import org.wh.engineer.bean.Event;
import org.wh.engineer.utils.EventBusUtils;

import java.util.List;

import cn.rivamed.Eth002Manager;

/**
 * 项目名称:    Android_PV_2.6.6_416D
 * 创建者:      DanMing
 * 创建时间:    2019/4/22 17:54
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class RegistLockAdapter_2 extends BaseQuickAdapter<String, BaseViewHolder> {

   public RegistLockAdapter_2(int layoutResId, @Nullable List<String> data) {
      super(layoutResId, data);
   }
   private TextView     mIdText;
   private TextView     mItemSettingOpen;
   private TextView     mItemSettingCloss;
   private TextView     mItemSettingStats;
   private TextView     mItemRegisteFinger;



   @Override
   protected void convert(BaseViewHolder helper, String item) {
      mIdText = (TextView) helper.getView(R.id.id_text);
      mItemSettingOpen = (TextView) helper.getView(R.id.item_setting_open);
      mItemSettingStats = (TextView) helper.getView(R.id.item_setting_stats);
      mItemRegisteFinger = (TextView) helper.getView(R.id.item_registe_finger);
      mIdText.setText(item);
      mItemSettingOpen.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            int ret = Eth002Manager.getEth002Manager().openDoor(item);
            EventBusUtils.post(new Event.lockType(1, ret, item));

         }
      });
      mItemSettingStats.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            int ret = Eth002Manager.getEth002Manager().checkDoorState(item);
            EventBusUtils.post(new Event.lockType(2, ret, item));

         }
      });
      mItemRegisteFinger.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            int ret =  Eth002Manager.getEth002Manager().fingerReg(item);
            EventBusUtils.post(new Event.lockType(3, ret, item));

         }
      });

   }
}
