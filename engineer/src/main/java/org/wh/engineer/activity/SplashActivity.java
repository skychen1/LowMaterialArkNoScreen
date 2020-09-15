package org.wh.engineer.activity;

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
import org.wh.engineer.R;
import org.wh.engineer.base.App;
import org.wh.engineer.dbmodel.BoxIdBean;
import org.wh.engineer.http.NetApi;
import org.wh.engineer.service.ScanService;
import org.wh.engineer.utils.LogcatHelper;
import org.wh.engineer.utils.SPUtils;
import org.wh.engineer.utils.UIUtils;

import static com.rivamed.FingerType.TYPE_NET_ZHI_ANG;
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
 * 项目名称:    Android_PV_2.6.5New
 * 创建者:      DanMing
 * 创建时间:    2019/3/4 17:59
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class SplashActivity extends FragmentActivity {

    public static Intent mIntentService;
    RelativeLayout viewById;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onWindowFocusChanged(true);
        setContentView(R.layout.activity_splash_layout);
        viewById = findViewById(R.id.rl);
        Log.e("版本号：", UIUtils.getVersionName(this) + "_M");
        initData(EntranceActivity.class);
        Log.i("BaseApplication", "SplashActivity onCreate");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        fullScreenImmersive(this.getWindow().getDecorView());
    }

    public void initData(Class launchActivity) {
        FingerManager.getManager().connectFinger(this, TYPE_NET_ZHI_ANG);
        mIntentService = new Intent(SplashActivity.this, ScanService.class);
        Logger.addLogAdapter(new AndroidLogAdapter());
        initLitePal();//数据库
        setDate();//设置默认值
    }

    private void setDate() {

        new Thread(new Runnable() {
            @Override
            public void run() {
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
                SPUtils.putString(getApplicationContext(), "TestLoginName", "admin");
                SPUtils.putString(getApplicationContext(), "TestLoginPass", "rivamed");

                LogcatHelper.getInstance(getApplicationContext()).stop();
                LogcatHelper.getInstance(getApplicationContext()).start();

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

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            launchLogin();
                        }
                    });
                } else {
                    App.mTitleConn = false;
                    launchLogin();
                }

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


    private void launchLogin() {
        startActivity(new Intent(SplashActivity.this, EntranceActivity.class));//LoginActivity
        finish();
    }
}
