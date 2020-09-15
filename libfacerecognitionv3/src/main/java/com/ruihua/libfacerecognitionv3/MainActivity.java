package com.ruihua.libfacerecognitionv3;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;

import com.ruihua.face.recognition.R;
import com.ruihua.libfacerecognitionv3.main.activity.BaseActivity;
import com.ruihua.libfacerecognitionv3.main.camera.AutoTexturePreviewView;
import com.ruihua.libfacerecognitionv3.main.camera.CameraPreviewManager;
import com.ruihua.libfacerecognitionv3.main.model.GlobalSet;
import com.ruihua.libfacerecognitionv3.main.model.User;
import com.ruihua.libfacerecognitionv3.main.presenter.FaceManager;
import com.ruihua.libfacerecognitionv3.main.ui.IFaceLicense;
import com.ruihua.libfacerecognitionv3.main.ui.IFaceRegister;
import com.ruihua.libfacerecognitionv3.main.ui.IFaceSearch;
import com.ruihua.libfacerecognitionv3.main.utils.T;
import com.ruihua.libfacerecognitionv3.main.view.FaceAuthDialog;


import java.util.List;

public class MainActivity extends BaseActivity {

    AutoTexturePreviewView autotexturePreviewView;

    FaceAuthDialog mFaceAuthDialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        mFaceAuthDialog = new FaceAuthDialog(this);
        autotexturePreviewView = findViewById(R.id.autotexturePreviewView);
        autotexturePreviewView.setVisibility(View.VISIBLE);

//        // 需要调整预览 大小
//        DisplayMetrics dm = new DisplayMetrics();
//        Display display = this.getWindowManager().getDefaultDisplay();
//        display.getMetrics(dm);
//        // 显示Size
//        int mDisplayWidth = dm.widthPixels;
//        int mDisplayHeight = dm.heightPixels;
//        int w = mDisplayWidth;
//        int h = mDisplayHeight;
//        FrameLayout.LayoutParams cameraFL = new FrameLayout.LayoutParams(
//                (int) (w * GlobalSet.SURFACE_RATIO), (int) (h * GlobalSet.SURFACE_RATIO),
//                Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
//        autotexturePreviewView.setLayoutParams(cameraFL);


        FaceManager.getManager().initLicense(this, new IFaceLicense() {
            @Override
            public void initLicenseSuccess() {
                T.d(MainActivity.class, "initLicenseSuccess");

            }

            @Override
            public void initLicenseFail(int errorCode, String msg) {
                T.d(MainActivity.class, "onTip code:" + errorCode + " msg:" + msg);
            }

            @Override
            public void notFindLicense() {

            }
        });
        T.d(MainActivity.class, "haslicense:" + FaceManager.getManager().hasLicense());

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.d(MainActivity.class, "onClick");

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

//        FaceManager.getManager().faceStartRGBRegister(
//                MainActivity.this, "hahah", null, null,
//                autotexturePreviewView, new IFaceRegister() {
//                    @Override
//                    public void registerResult(int code, String msg) {
//                        T.d(MainActivity.class, "registerResult code:" + code + " msg:" + msg);
//                    }
//                }, CameraPreviewManager.CAMERA_FACING_FRONT, CameraPreviewManager.ORIENTATION_HORIZONTAL);

//        FaceManager.getManager().initShowFrame(texttureView);
        T.d(MainActivity.class, "onresume");
//        FaceManager.getManager().faceStartRGBSearch(this, autotexturePreviewView, new IFaceSearch() {
//            @Override
//            public void onTip(int code, String msg, User user) {
//                T.d(MainActivity.class, "onTip code:" + code + " msg:" + msg + " user:" + user);
//            }
//        }, CameraPreviewManager.CAMERA_FACING_FRONT, CameraPreviewManager.ORIENTATION_HORIZONTAL);
    }

    @Override
    protected void onPause() {
        T.d(MainActivity.class, "onPause");
        FaceManager.getManager().stopPreviewAndRelease();
        super.onPause();
//        FaceManager.getManager().releaseRegister();

    }
}
