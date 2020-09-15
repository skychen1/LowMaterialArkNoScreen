package org.wh.engineer.adapter;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rivamed.libdevicesbase.base.DeviceInfo;
import com.ruihua.reader.ReaderManager;

import org.wh.engineer.R;
import org.wh.engineer.bean.Event;
import org.wh.engineer.utils.EventBusUtils;

import java.util.List;
/**
 * 项目名称:    Android_PV_2.6.1
 * 创建者:      DanMing
 * 创建时间:    2018/11/27 19:10
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.adapter
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class RegistReaderAdapter extends BaseQuickAdapter<String, BaseViewHolder>{

   public RegistReaderAdapter(int layoutResId, @Nullable List<String> data) {
      super(layoutResId, data);
   }

   @Override
   protected void convert(BaseViewHolder helper, String item) {
      TextView   id = ((TextView) helper.getView(R.id.id_text));
      EditText   powe_text = ((EditText) helper.getView(R.id.item_powe_text));
      TextView   setting_powe = ((TextView) helper.getView(R.id.item_setting_powe));
      TextView   itemScan = ((TextView) helper.getView(R.id.item_scan));
      TextView   itemStopScan = ((TextView) helper.getView(R.id.item_stopscan));

      id.setText(item);

      setting_powe.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            try {
               byte powerByte = Byte.parseByte(powe_text.getText().toString());
               if (powerByte < 0 || powerByte > 30) {
                  throw new Exception();
               }
               ReaderManager.getManager().setPower(item, powerByte);
               EventBusUtils.post(new Event.EventTestIdAndPower(null,"设置RFID reader功率为："+powerByte));
            } catch (Exception ex) {
               EventBusUtils.post(new Event.EventTestIdAndPower(null,"请输入有效的功率数值: 1-30"));
            }
         }
      });
      itemStopScan.setOnClickListener(view->{

         List<DeviceInfo> connectedDevices = ReaderManager.getManager().getConnectedReader();
         String ss = "";
         for (DeviceInfo de : connectedDevices) {
            String  mDiviceId = de.getIdentification();
            EventBusUtils.post(new Event.EventTestStopScan(mDiviceId));

         }

      });

      itemScan.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            EventBusUtils.post(new Event.EventTestIdAndPower(id.getText().toString().trim(),null));
         }
      });
   }
}
