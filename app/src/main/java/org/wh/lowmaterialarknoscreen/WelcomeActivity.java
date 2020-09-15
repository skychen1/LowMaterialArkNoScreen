package org.wh.lowmaterialarknoscreen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.rivamed.FingerManager;

import org.litepal.LitePal;
import org.wh.engineer.base.App;
import org.wh.engineer.dbmodel.BoxIdBean;
import org.wh.engineer.http.NetApi;
import org.wh.engineer.service.ScanService;
import org.wh.engineer.utils.LogcatHelper;
import org.wh.engineer.utils.SPUtils;
import org.wh.engineer.utils.UIUtils;

import static com.rivamed.FingerType.TYPE_NET_ZHI_ANG;
import static org.wh.engineer.activity.SplashActivity.mIntentService;
import static org.wh.engineer.base.App.MAIN_URL;
import static org.wh.engineer.base.App.mAppContext;
import static org.wh.engineer.cont.Constants.SAVE_ANIMATION_TIME;
import static org.wh.engineer.cont.Constants.SAVE_CLOSSLIGHT_TIME;
import static org.wh.engineer.cont.Constants.SAVE_HOME_LOGINOUT_TIME;
import static org.wh.engineer.cont.Constants.SAVE_LOGINOUT_TIME;
import static org.wh.engineer.cont.Constants.SAVE_NOEPC_LOGINOUT_TIME;
import static org.wh.engineer.cont.Constants.SAVE_ONE_REGISTE;
import static org.wh.engineer.cont.Constants.SAVE_READER_TIME;
import static org.wh.engineer.cont.Constants.SAVE_REMOVE_LOGFILE_TIME;
import static org.wh.engineer.cont.Constants.SAVE_SEVER_IP;
import static org.wh.engineer.cont.Constants.SAVE_VOICE_NOCLOSSDOOR_TIME;
import static org.wh.engineer.utils.UIUtils.fullScreenImmersive;

/**
 * created by wh on 2020/8/4
 * desc 根据engineer中的欢迎界面SplashActivity进行重写
 */
public class WelcomeActivity extends FragmentActivity {
    RelativeLayout viewById;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onWindowFocusChanged(true);
        setContentView(org.wh.engineer.R.layout.activity_splash_layout);
        viewById = findViewById(org.wh.engineer.R.id.rl);
        Log.e("版本号：", UIUtils.getVersionName(this) + "_M");
        initData();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        fullScreenImmersive(this.getWindow().getDecorView());
    }

    public void initData() {
        FingerManager.getManager().connectFinger(this, TYPE_NET_ZHI_ANG);
        mIntentService = new Intent(this, ScanService.class);
        Logger.addLogAdapter(new AndroidLogAdapter());
        setDate();//设置默认值
        initLitePal();//数据库
        startService(mIntentService);
        launchLogin(EntranceActivity.class);
    }

    private void setDate() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                LogcatHelper.getInstance(getApplicationContext()).stop();
                MAIN_URL = SPUtils.getString(mAppContext, SAVE_SEVER_IP);
                String urls = MAIN_URL + NetApi.URL_CONNECT;
                Log.i("outtccc", "MAIN_URL     " + MAIN_URL + "  dfdfdfdfdf  ");
                if (MAIN_URL != null) {
                    OkGo.<String>get(urls).tag(this).execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            Log.i("outtccc", "MAIN_URL     " + MAIN_URL + " fffffffffffffffffff  ");
                            App.mTitleConn = true;
                        }

                        @Override
                        public void onError(Response<String> response) {
                            App.mTitleConn = false;
                        }
                    });
                } else {
                    App.mTitleConn = false;
                }
                if (SPUtils.getInt(UIUtils.getContext(), SAVE_ANIMATION_TIME) == -1) {
                    App.ANIMATION_TIME = 1000;
                } else {
                    App.ANIMATION_TIME = SPUtils.getInt(UIUtils.getContext(), SAVE_ANIMATION_TIME);
                }
                if (SPUtils.getInt(UIUtils.getContext(), SAVE_READER_TIME) == -1) {
                    App.READER_TIME = 3000;
                } else {
                    App.READER_TIME = SPUtils.getInt(UIUtils.getContext(), SAVE_READER_TIME);
                }
                if (SPUtils.getInt(UIUtils.getContext(), SAVE_LOGINOUT_TIME) == -1) {
                    App.COUNTDOWN_TIME = 20000;
                } else {
                    App.COUNTDOWN_TIME = SPUtils.getInt(UIUtils.getContext(), SAVE_LOGINOUT_TIME);
                }
                if (SPUtils.getInt(UIUtils.getContext(), SAVE_CLOSSLIGHT_TIME) == -1) {
                    App.CLOSSLIGHT_TIME = 30000;
                } else {
                    App.CLOSSLIGHT_TIME = SPUtils.getInt(UIUtils.getContext(), SAVE_CLOSSLIGHT_TIME);
                }
                if (SPUtils.getInt(UIUtils.getContext(), SAVE_HOME_LOGINOUT_TIME) == -1) {
                    App.HOME_COUNTDOWN_TIME = 60000;
                } else {
                    App.HOME_COUNTDOWN_TIME = SPUtils.getInt(UIUtils.getContext(), SAVE_HOME_LOGINOUT_TIME);
                }
                if (SPUtils.getInt(UIUtils.getContext(), SAVE_REMOVE_LOGFILE_TIME) == -1) {
                    App.REMOVE_LOGFILE_TIME = 30;
                } else {
                    App.REMOVE_LOGFILE_TIME = SPUtils.getInt(UIUtils.getContext(), SAVE_REMOVE_LOGFILE_TIME);
                }
                if (SPUtils.getInt(UIUtils.getContext(), SAVE_NOEPC_LOGINOUT_TIME) == -1) {
                    App.NOEPC_LOGINOUT_TIME = 20000;
                } else {
                    App.NOEPC_LOGINOUT_TIME = SPUtils.getInt(UIUtils.getContext(), SAVE_NOEPC_LOGINOUT_TIME);
                }
                if (SPUtils.getInt(UIUtils.getContext(), SAVE_VOICE_NOCLOSSDOOR_TIME) == -1) {
                    App.VOICE_NOCLOSSDOOR_TIME = 600000;
                } else {
                    App.VOICE_NOCLOSSDOOR_TIME = SPUtils.getInt(UIUtils.getContext(), SAVE_VOICE_NOCLOSSDOOR_TIME);
                }
                LogcatHelper.getInstance(getApplicationContext()).start();
                SPUtils.putString(getApplicationContext(), "TestLoginName", "admin");
                SPUtils.putString(getApplicationContext(), "TestLoginPass", "rivamed");
            }
        }).start();

    }

    private void initLitePal() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //创建数据库表
                LitePal.getDatabase();
                if (!SPUtils.getBoolean(UIUtils.getContext(), SAVE_ONE_REGISTE)) {
                    LitePal.deleteAll(BoxIdBean.class);
                }
            }
        }).start();
    }

    private void launchLogin(Class launchActivity) {
        startActivity(new Intent(this, launchActivity));//LoginActivity
        finish();
    }
}
