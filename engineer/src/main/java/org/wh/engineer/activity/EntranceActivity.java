package org.wh.engineer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.rivamed.FingerManager;
import com.rivamed.libdevicesbase.base.DeviceInfo;
import com.rivamed.libidcard.IdCardManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;
import org.wh.engineer.R;
import org.wh.engineer.base.App;
import org.wh.engineer.base.SimpleActivity;
import org.wh.engineer.bean.Event;
import org.wh.engineer.dbmodel.BoxIdBean;
import org.wh.engineer.http.NetApi;
import org.wh.engineer.service.DeviceService;
import org.wh.engineer.utils.EventBusUtils;
import org.wh.engineer.utils.LoginUtils;
import org.wh.engineer.utils.SPUtils;

import java.util.List;

import static com.rivamed.FingerType.TYPE_NET_ZHI_ANG;
import static org.wh.engineer.base.App.MAIN_URL;
import static org.wh.engineer.base.App.mAppContext;
import static org.wh.engineer.base.App.mDeviceId;
import static org.wh.engineer.base.App.mTitleConn;
import static org.wh.engineer.cont.Constants.SAVE_SEVER_IP;

/**
 * created by wh on 2020/7/31
 * desc 入口界面
 */
public class EntranceActivity extends SimpleActivity {
    TextView mTvTip;
    Handler mHandler;

    @Override
    public int getLayoutId() {
        return R.layout.activity_entrance_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        EventBusUtils.register(this);
        mHandler = new Handler();
        startRead();
        findViewById(R.id.btnRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EntranceActivity.this, "已进入工程模式", Toast.LENGTH_LONG).show();
                startActivity(new Intent(EntranceActivity.this, RegisteActivity.class));
            }
        });
        findViewById(R.id.tvUpdateVersion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUtils.getUpDateVer(EntranceActivity.this);//检测版本更新
            }
        });
        mTvTip = findViewById(R.id.tvTip);
        Intent intent = new Intent(this, DeviceService.class);
        startService(intent);
    }

    /**
     * 设备网络连接状态
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTitleConnEvent(Event.XmmppConnect event) {
        mTitleConn = event.connect;
        if (!mTitleConn && event.net) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    testServer(event.net);
                }
            }, 1000);
        } else {
            hasNetWork(event.connect, event.net);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<BoxIdBean> boxIdBeans= LitePal.where().find(BoxIdBean.class);
        mDeviceId.clear();
        for (BoxIdBean boxIdBean:boxIdBeans){
            mDeviceId.add(boxIdBean.getName());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, DeviceService.class);
        stopService(intent);
        EventBusUtils.unregister(this);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onBindViewBefore() {

    }

    @Override
    public Object newP() {
        return null;
    }

    private void startRead() {
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

    private void testServer(boolean net) {
        MAIN_URL = SPUtils.getString(mAppContext, SAVE_SEVER_IP);
        String urls = MAIN_URL + NetApi.URL_CONNECT;
        if (MAIN_URL != null) {
            OkGo.<String>get(urls).tag(this).execute(new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {
                    App.mTitleConn = true;
                }

                @Override
                public void onError(Response<String> response) {
                    App.mTitleConn = false;
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    hasNetWork(App.mTitleConn, net);
                }
            });
        }
    }
}
