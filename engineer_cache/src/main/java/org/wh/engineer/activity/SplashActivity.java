package org.wh.engineer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.ruihua.libfacerecognitionv3.main.camera.CameraPreviewManager;
import com.ruihua.libfacerecognitionv3.main.listener.SimpleSdkInitListener;
import com.ruihua.libfacerecognitionv3.main.presenter.FaceManager;

import org.litepal.LitePal;
import org.wh.engineer.R;
import org.wh.engineer.base.App;
import org.wh.engineer.cont.Constants;
import org.wh.engineer.dbmodel.BoxIdBean;
import org.wh.engineer.http.NetApi;
import org.wh.engineer.service.ScanService;
import org.wh.engineer.utils.FaceTask;
import org.wh.engineer.utils.LogUtils;
import org.wh.engineer.utils.LogcatHelper;
import org.wh.engineer.utils.RxPermissionUtils;
import org.wh.engineer.utils.SPUtils;
import org.wh.engineer.utils.ToastUtils;
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
        setDate();//设置默认值
        initLitePal();//数据库

        startAct(launchActivity);//页面跳转
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

    private void startAct(Class launchActivity) {

        new Thread(() -> startService(mIntentService)).start();

        //人脸识别SDK初始化权限申请：存储 相机 这里elo设备点击允许存储权限页面会关闭，原因未知
        RxPermissionUtils.checkCameraReadWritePermission(this, hasPermission -> {
            if (hasPermission && FaceManager.getManager().hasAction()) {
                //		   if ( FaceManager.getManager().hasActivation(SplashActivity.this)) {
                //检测设备是否激活授权码
                //启动页初始化人脸识别sdk
                new Thread(() -> FaceManager.getManager().init(SplashActivity.this, "", Constants.FACE_GROUP, true, CameraPreviewManager.CAMERA_FACING_FRONT, CameraPreviewManager.ORIENTATION_PORTRAIT, new SimpleSdkInitListener() {
                    @Override
                    public void initLicenseSuccess() {
                        //激活成功
                    }

                    @Override
                    public void initLicenseFail(int errorCode, String msg) {
                        //激活失败
                        UIUtils.runInUIThread(() -> ToastUtils.showShortToast("人脸识别SDK激活失败：：errorCode = " + errorCode + ":::msg：" + msg));
                        LogUtils.d("Face", "initFail 1  ");
                        launchLogin(launchActivity);
                    }

                    @Override
                    public void initModelSuccess() {
                        //初始化成功
                        UIUtils.runInUIThread(() -> ToastUtils.showShortToast("人脸识别SDK初始化成功"));
                        //从服务器更新人脸底库并注册至本地
                        FaceTask faceTask = new FaceTask(SplashActivity.this);
                        faceTask.setCallBack((hasRegister, msg) -> {
                            if (msg != null) {
                                UIUtils.runInUIThread(() -> ToastUtils.showShortToast(msg));
                            }
                            //初始化完成后跳转页面
                            launchLogin(launchActivity);
                        });
                        if (MAIN_URL != null) {
                            faceTask.getAllFaceAndRegister();
                        }
                    }

                    @Override
                    public void initModelFail(int errorCode, String msg) {
                        //初始化失败
                        UIUtils.runInUIThread(() -> ToastUtils.showShortToast("人脸识别SDK初始化失败：：errorCode = " + errorCode + ":::msg：" + msg));
                        //初始化完成后跳转页面
                        LogUtils.d("Face", "initFail 2  ");
                        launchLogin(launchActivity);
                    }
                })).start();
            } else {
                new Handler().postDelayed(() -> launchLogin(launchActivity), 2000);
            }
        });
    }

    private void launchLogin(Class launchActivity) {
        startActivity(new Intent(SplashActivity.this, launchActivity));//LoginActivity
        finish();
    }
}
