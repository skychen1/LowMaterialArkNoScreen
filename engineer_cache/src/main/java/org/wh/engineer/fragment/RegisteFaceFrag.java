package org.wh.engineer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.baidu.idl.main.facesdk.FaceAuth;
import com.baidu.idl.main.facesdk.utils.PreferencesUtil;
import com.ruihua.libfacerecognitionv3.main.activity.FaceRGBCloseDebugSearchActivity;
import com.ruihua.libfacerecognitionv3.main.camera.CameraPreviewManager;
import com.ruihua.libfacerecognitionv3.main.listener.SimpleSdkInitListener;
import com.ruihua.libfacerecognitionv3.main.manager.FaceSDKManager;
import com.ruihua.libfacerecognitionv3.main.presenter.FaceManager;

import org.wh.engineer.R;
import org.wh.engineer.R2;
import org.wh.engineer.base.SimpleFragment;
import org.wh.engineer.cont.Constants;
import org.wh.engineer.utils.FaceTask;
import org.wh.engineer.utils.LogUtils;
import org.wh.engineer.utils.SPUtils;
import org.wh.engineer.utils.ToastUtils;
import org.wh.engineer.utils.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;
import static org.wh.engineer.cont.Constants.FACE_UPDATE_TIME;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:       chenyanling
 * 创建时间:    2019/6/12
 * 描述:        人脸识别初始化:授权码激活  人脸底库初始化同步
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class RegisteFaceFrag extends SimpleFragment {

    @BindView(R2.id.fragment_btn_active)
    TextView fragmentBtnActive;
    @BindView(R2.id.fragment_btn_init)
    TextView fragmentBtnInit;
    @BindView(R2.id.switch_btn)
    Switch switchBtn;
    @BindView(R2.id.text)
    TextView text;
    private boolean hasModelInit;
    public static RegisteFaceFrag newInstance() {
        Bundle args = new Bundle();
        RegisteFaceFrag fragment = new RegisteFaceFrag();
        //	args.putInt(TYPE_SIZE, param);
        //	args.putString(TYPE_PAGE, type);
        //	fragment.setArguments(args);
        return fragment;

    }

    @Override
    public int getLayoutId() {
        return R.layout.frg_face_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        hasModelInit = FaceManager.getManager().hasModelInit();
        final String licenseOfflineKey = PreferencesUtil.getString("activate_offline_key", "");
        final String licenseOnlineKey = PreferencesUtil.getString("activate_online_key", "");
        text.setText("设备硬件指纹： " + new FaceAuth().getDeviceId(mContext) + "\n激活码：" + licenseOfflineKey + "\n" + licenseOnlineKey);
        Log.i("rreADE", "licenseOfflineKey:   " + licenseOfflineKey);
        Log.i("rreADE","licenseOnlineKey:   "+licenseOnlineKey);
    }


    @Override
    public void onBindViewBefore(View view) {

    }


    @OnClick(R2.id.fragment_btn_init)
    public void onInitClicked() {
        if (!UIUtils.isFastDoubleClick(R.id.fragment_btn_init)) {
            if (FaceManager.getManager().hasModelInit()) {
                initFaceList();
            } else {
                initFace();
            }
        }
    }
    private void initFace() {
        FaceManager.getManager().init(getActivity(), "", Constants.FACE_GROUP, true, CameraPreviewManager.CAMERA_FACING_FRONT, CameraPreviewManager.ORIENTATION_PORTRAIT, new SimpleSdkInitListener() {
            @Override
            public void initLicenseSuccess() {
                //激活成功
            }

            @Override
            public void initLicenseFail(int errorCode, String msg) {
                //激活失败
                fragmentBtnActive.post(() -> ToastUtils.showShortToast("人脸识别SDK激活失败：：errorCode = " + errorCode + ":::msg：" + msg));

            }

            @Override
            public void initModelSuccess() {
                //初始化成功
                fragmentBtnActive.post(() -> {
                    ToastUtils.showShortToast("人脸识别SDK初始化成功 "  );
                    hasModelInit = true;
                    initFaceList();
                });
            }

            @Override
            public void initModelFail(int errorCode, String msg) {
                //初始化失败
                fragmentBtnActive.post(() -> ToastUtils.showShortToast("人脸识别SDK初始化失败：：errorCode = " + errorCode + ":::msg：" + msg));
            }
        });
    }
    private void initFaceList() {
        SPUtils.putString(UIUtils.getContext(), FACE_UPDATE_TIME, "");
        //从服务器更新人脸底库并注册至本地
        FaceTask faceTask = new FaceTask(_mActivity);
        faceTask.setCallBack((hasRegister, msg) -> {
            LogUtils.d("faceTask", "initListener: " + msg);
            if (msg != null) {
                UIUtils.runInUIThread(() -> ToastUtils.showShortToast(msg));
            }
        });
        faceTask.getAllFaceAndRegister();
    }

    @OnClick(R2.id.fragment_btn_recongize)
    void onTest(View v) {
        if (!UIUtils.isFastDoubleClick(R.id.fragment_btn_recongize)) {
            int initStatus = FaceSDKManager.initStatus;
            if (initStatus == FaceSDKManager.SDK_UNACTIVATION) {
                ToastUtils.showShortToast("SDK还未激活，请先激活");
            } else if (initStatus == FaceSDKManager.SDK_INIT_FAIL) {
                ToastUtils.showShortToast("SDK初始化失败，请重新激活初始化");
            } else if (initStatus == FaceSDKManager.SDK_INIT_SUCCESS) {
                ToastUtils.showShortToast("SDK正在加载模型，请稍后再试");
            } else if (initStatus == FaceSDKManager.SDK_MODEL_LOAD_SUCCESS) {
                //在开启人脸识别以前需要获取人脸照片数量，
                //如果为0表示没有底库，是不能开启识别的；
                FaceManager.getManager().queryAllUserCount(count -> {
                    if (count > 0) {
                        //跳转到识别页面
                        startActivity(new Intent(getActivity(), FaceRGBCloseDebugSearchActivity.class));
                    } else {
			     runOnUiThread(() ->ToastUtils.showShortToast("人脸识别底库无数据，请先初始化人脸底库"));
                    }
                });
            }
        }
    }
}
