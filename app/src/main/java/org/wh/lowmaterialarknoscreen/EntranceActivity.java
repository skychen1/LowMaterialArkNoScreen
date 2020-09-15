package org.wh.lowmaterialarknoscreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * created by wh on 2020/7/31
 * desc 入口界面
 */
public class EntranceActivity extends SimpleActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_entrance_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        org.wh.engineer.utils.LogUtils.i("EntranceActivity", "EntranceActivity initDataAndEvent");
        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EntranceActivity.this, "已进入工程模式", Toast.LENGTH_LONG).show();
                startActivity(new Intent(EntranceActivity.this, TestLoginActivity.class));
            }
        });
        Intent intent = new Intent(this, LoginService.class);
        startService(intent);

        FingerManager.getManager().connectFinger(this, TYPE_NET_ZHI_ANG);
        List<DeviceInfo> deviceFingerInfos = FingerManager.getManager().getConnectedFinger();
        for (DeviceInfo info : deviceFingerInfos) {
            int i = FingerManager.getManager().startReadFinger(info.getIdentification());
            Log.i("appSatus", "info.FingerManager()     " + info.getIdentification() +
                    "  FingerManager    " + i);
        }
        List<DeviceInfo> connectedDevice = IdCardManager.getIdCardManager()
                .getConnectedDevice();
        for (DeviceInfo info : connectedDevice) {
            int i = IdCardManager.getIdCardManager().startReadCard(info.getIdentification());
            Log.i("appSatus", "info.IdCardManager()     " + info.getIdentification() +
                    "  IdCardManager    " + i);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, LoginService.class);
        stopService(intent);
        org.wh.engineer.utils.LogUtils.i("EntranceActivity", "EntranceActivity onDestroy");
    }

    @Override
    public void onBindViewBefore() {

    }

    @Override
    public Object newP() {
        return null;
    }
}
