package org.wh.engineer.adapter;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ruihua.libconsumables.ConsumableManager;

import org.wh.engineer.R;
import org.wh.engineer.bean.Event;
import org.wh.engineer.utils.EventBusUtils;

import java.util.List;

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
public class RegistLockAdapter_3 extends BaseQuickAdapter<String, BaseViewHolder> {

   public RegistLockAdapter_3(int layoutResId, @Nullable List<String> data) {
      super(layoutResId, data);
   }
   private TextView mIdText;
   private TextView mItemDoorOpen0;
   private TextView mItemDoorOpen1;
   private TextView mItemSettingCloss;
   private TextView mItemSettingStats;
   private TextView mItemLightOpen2;
   private TextView mItemLightOpen3;
   private TextView mItemLightCloss;
   private TextView mItemLED3Open;
   private TextView mItemLED3Closs;
   private TextView mItemLED3Check;
   private TextView mItemLock3Open;



   @Override
   protected void convert(BaseViewHolder helper, String item) {
      mIdText = (TextView) helper.getView(R.id.id_text);
      mItemDoorOpen0 = (TextView) helper.getView(R.id.item_door_open0);
      mItemDoorOpen1 = (TextView) helper.getView(R.id.item_door_open1);
      mItemSettingStats = (TextView) helper.getView(R.id.item_setting_stats);
      mItemLightOpen3 = (TextView) helper.getView(R.id.item_light_open3);
      mItemLightOpen2 = (TextView) helper.getView(R.id.item_light_open2);
      mItemLightCloss = (TextView) helper.getView(R.id.item_light_closs);
      mItemLED3Open = (TextView) helper.getView(R.id.item_light_open4);
      mItemLED3Closs = (TextView) helper.getView(R.id.item_light_closs1);
      mItemLED3Check = (TextView) helper.getView(R.id.item_light_check1);
      mItemLock3Open = (TextView) helper.getView(R.id.item_door_open2);
      mIdText.setText(item);
      /**
       * j24
       */
      mItemDoorOpen0.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            int ret = ConsumableManager.getManager().openDoor(item,0);
            EventBusUtils.post(new Event.lockType(1,ret,item,1));

         }
      });
      /**
       * j25
       */
      mItemDoorOpen1.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            int ret = ConsumableManager.getManager().openDoor(item,1);
            EventBusUtils.post(new Event.lockType(1,ret,item,1));

         }
      });
      /**
       * 门检测
       */
      mItemSettingStats.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
//            int i = ConsumableManager.getManager().checkDoorState(item);
            int i = ConsumableManager.getManager().checkDoorState(item, 10);
            Log.i("ffaer", " checkDoorState  " + item+"   "+i);
                        EventBusUtils.post(new Event.lockType(2,i,item));

         }
      });
      /**
       * j16
       */
      mItemLightOpen2.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            int i = ConsumableManager.getManager().openLight(item, 2);
            EventBusUtils.post(new Event.lockType(3,i,item,2));
         }
      });
      /**
       * j17
       */
      mItemLightOpen3.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            int i = ConsumableManager.getManager().openLight(item, 3);
            EventBusUtils.post(new Event.lockType(3,i,item,3));
         }
      });
      /**
       * led3关
       */
      mItemLED3Closs.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            int i = ConsumableManager.getManager().closeLight(item, 11);
            Log.i("3342", "item   " + item + "   int   " + i);
            EventBusUtils.post(new Event.lockType(9,i,item,11));
         }
      });
      /**
       * led3开
       */
      mItemLED3Open.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

            int i = ConsumableManager.getManager().openLight(item, 11);

            Log.i("3342", "item   " + item + "   int   " + i);
            EventBusUtils.post(new Event.lockType(10,i,item,11));
         }
      });
      mItemLED3Check.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            int i = ConsumableManager.getManager().checkLightState(item, 11);
            Log.i("3342", "item   " + item + "   int   " + i);
            EventBusUtils.post(new Event.lockType(11,i,item,11));
         }
      });
      mItemLightCloss.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            int i = ConsumableManager.getManager().closeLight(item);

            EventBusUtils.post(new Event.lockType(4,i,item));
         }
      });
   }
}
