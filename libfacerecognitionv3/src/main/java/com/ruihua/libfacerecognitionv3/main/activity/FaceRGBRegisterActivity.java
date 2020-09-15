package com.ruihua.libfacerecognitionv3.main.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.baidu.idl.main.facesdk.FaceInfo;
import com.baidu.idl.main.facesdk.model.BDFaceImageInstance;
import com.baidu.idl.main.facesdk.model.BDFaceSDKCommon;
import com.ruihua.face.recognition.R;
import com.ruihua.libfacerecognitionv3.main.api.FaceApi;
import com.ruihua.libfacerecognitionv3.main.callback.CameraDataCallback;
import com.ruihua.libfacerecognitionv3.main.callback.FaceDetectCallBack;
import com.ruihua.libfacerecognitionv3.main.callback.FaceFeatureCallBack;
import com.ruihua.libfacerecognitionv3.main.camera.AutoTexturePreviewView;
import com.ruihua.libfacerecognitionv3.main.camera.CameraPreviewManager;
import com.ruihua.libfacerecognitionv3.main.manager.FaceSDKManager;
import com.ruihua.libfacerecognitionv3.main.manager.FaceTrackManager;
import com.ruihua.libfacerecognitionv3.main.model.GlobalSet;
import com.ruihua.libfacerecognitionv3.main.model.LivenessModel;
import com.ruihua.libfacerecognitionv3.main.model.SingleBaseConfig;
import com.ruihua.libfacerecognitionv3.main.presenter.FaceManager;
import com.ruihua.libfacerecognitionv3.main.ui.IFaceRegister;
import com.ruihua.libfacerecognitionv3.main.utils.BitmapUtils;
import com.ruihua.libfacerecognitionv3.main.utils.FileUtils;
import com.ruihua.libfacerecognitionv3.main.utils.ImageUtils;
import com.ruihua.libfacerecognitionv3.main.utils.T;
import com.ruihua.libfacerecognitionv3.main.view.CircleImageView;

import java.io.File;

import static com.ruihua.libfacerecognitionv3.main.presenter.FaceManager.CODE_SUCCESS;


/**
 * @Time: 2019/12/24
 * @Author: liaoboyu
 * @Description: 注册的采集人脸页面 - RGB
 */

public class FaceRGBRegisterActivity extends BaseActivity {

//    private Button backButton;
//    private Button setButton;
    private AutoTexturePreviewView mPreviewView;
//    private ImageView testImageview;
    public static final String PATH = "path";


    // 注册的 提示 view
    private TextView mTrackText;
    private TextView mDetectText;
    private CircleImageView mDetectImage;

    // RGB摄像头图像宽和高
    private static final int mWidth = 640;
    private static final int mHeight = 480;
    private boolean qualityControl;
    private String username = null;
    private String userId = null;
    private String groupId = null;
    private String userInfo = null;

    private Bitmap rgbBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_reg_detect);

        // 获取页面类型 pageType =1 注册；
        Intent intent = getIntent();
        if (intent != null) {
            username = intent.getStringExtra("user_name");
            userId = intent.getStringExtra("user_id");
            groupId = intent.getStringExtra("group_id");
            userInfo = intent.getStringExtra("user_info");
        }
        initView();

        qualityControl = SingleBaseConfig.getBaseConfig().isQualityControl();
        // 注册默认开启质量检测
        SingleBaseConfig.getBaseConfig().setQualityControl(true);
        FaceSDKManager.getInstance().initConfig();
    }

    private void initView() {

//        backButton = findViewById(R.id.id_regcapture_back);
//        setButton = findViewById(R.id.id_regcapture_setting);
//        backButton.setOnClickListener(this);
//        setButton.setOnClickListener(this);
        // RGB预览
        mPreviewView = findViewById(R.id.auto_camera_preview_view);
        mPreviewView.setScaleX(-1);
//        testImageview = findViewById(R.id.test_imgView);
        // 图像预览
        mTrackText = findViewById(R.id.track_txt);
        mDetectText = findViewById(R.id.detect_reg_text);
        mDetectImage = findViewById(R.id.detect_reg_image_item);

        // 注册页面 只支持 固定区域检测
        // 遮罩
//        FaceRoundView rectView = findViewById(R.id.rect_view);
//        rectView.setVisibility(View.VISIBLE);

        // 需要调整预览 大小
        DisplayMetrics dm = new DisplayMetrics();
        Display display = this.getWindowManager().getDefaultDisplay();
        display.getMetrics(dm);
        // 显示Size
        int mDisplayWidth = dm.widthPixels;
        int mDisplayHeight = dm.heightPixels;
        int w = mDisplayWidth;
        int h = mDisplayHeight;
        FrameLayout.LayoutParams cameraFL = new FrameLayout.LayoutParams(
                (int) (w * GlobalSet.SURFACE_RATIO), (int) (h * GlobalSet.SURFACE_RATIO),
                Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        mPreviewView.setLayoutParams(cameraFL);

    }


//    @Override
//    public void onClick(View view) {
//
//        if (view == backButton) {
//            FaceRGBRegisterActivity.this.finish();
//        } else if (view == setButton) {
//            Intent intent = new Intent(this, SettingMainActivity.class);
//            intent.putExtra("page_type", "register");
//            startActivityForResult(intent, PAGE_TYPE);
//            finish();
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        // 摄像头图像预览
        FaceManager.getManager().faceStartRGBRegister(this, userId, username, mPreviewView, new IFaceRegister() {
            @Override
            public void registerResult(int code, String msg) {

                if (code == CODE_SUCCESS) {
                    Log.e("Face", "registerResult path: "+msg );
                    //上传至服务器，删除本地照片
                    mPreviewView.post(() -> {
                        Intent intent = new Intent();
                        intent.putExtra(PATH, msg);
                        setResult(RESULT_OK, intent);
                        FaceRGBRegisterActivity.this.finish();
                    });
                }else {
                    displayTip(msg);
                }
            }
        });
//        startCameraPreview();
        Log.e("qing", "start camera");
    }

    /**
     * 摄像头图像预览
     */
    private void startCameraPreview() {
        // 设置前置摄像头
        CameraPreviewManager.getInstance().setDisplayOrientation(CameraPreviewManager.ORIENTATION_HORIZONTAL);
        CameraPreviewManager.getInstance().setCameraFacing(CameraPreviewManager.CAMERA_FACING_FRONT);
        // 设置后置摄像头
//         CameraPreviewManager.getInstance().setCameraFacing(CameraPreviewManager.CAMERA_FACING_BACK);
        // 设置USB摄像头
//        CameraPreviewManager.getInstance().setCameraFacing(CameraPreviewManager.CAMERA_USB);

        // TODO 在得力设备和部分手机上出现过 CameraPreviewManager 崩溃的问题

        CameraPreviewManager.getInstance().startPreview(this, mPreviewView, mWidth, mHeight, new CameraDataCallback() {
            @Override
            public void onGetCameraData(byte[] data, Camera camera, int width, int height) {
//                T.d(FaceRGBRegisterActivity.class, "摄像机onGetCameraData");

                // 调试模式打开 显示实际送检图片的方向，SDK只检测人脸朝上的图
                boolean isRGBDisplay = SingleBaseConfig.getBaseConfig().getDisplay();
                if (isRGBDisplay) {
//                    showDetectImage(data);
                }

                // 拿到相机帧数
                faceDetect(data, width, height);


            }
        });
    }

    /**
     * 摄像头数据处理
     *
     * @param data
     * @param width
     * @param height
     */
    private void faceDetect(byte[] data, final int width, final int height) {

        // 摄像头预览数据进行人脸检测
        int liveType = SingleBaseConfig.getBaseConfig().getType();
        if (liveType == 1) { // 无活体检测
            FaceTrackManager.getInstance().setAliving(false);
        } else if (Integer.valueOf(liveType) == 2) { // 活体检测
            FaceTrackManager.getInstance().setAliving(true);
        }

        FaceTrackManager.getInstance().faceTrack(data, width, height, new FaceDetectCallBack() {
            @Override
            public void onFaceDetectCallback(LivenessModel livenessModel) {
                // 做过滤
                boolean isFilterSuccess = faceSizeFilter(livenessModel.getFaceInfo(), width, height);
                if (isFilterSuccess) {
                    // 展示model
                    checkResult(livenessModel);
                }
            }

            @Override
            public void onTip(int code, final String msg) {
                displayTip(msg);
            }

            @Override
            public void onFaceDetectDarwCallback(LivenessModel livenessModel) {

            }
        });


    }


//    /**
//     * 显示检测的图片。用于调试，如果人脸sdk检测的人脸需要朝上，可以通过该图片判断。实际应用中可注释掉
//     *
//     * @param rgb
//     */
//    private void showDetectImage(byte[] rgb) {
//        if (rgb == null) {
//            return;
//        }
//        BDFaceImageInstance rgbInstance = new BDFaceImageInstance(rgb, mHeight,
//                mWidth, BDFaceSDKCommon.BDFaceImageType.BDFACE_IMAGE_TYPE_YUV_420,
//                SingleBaseConfig.getBaseConfig().getDetectDirection(),
//                SingleBaseConfig.getBaseConfig().getMirrorRGB());
//        BDFaceImageInstance imageInstance = rgbInstance.getImage();
//        final Bitmap bitmap = BitmapUtils.getInstaceBmp(imageInstance);
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                testImageview.setVisibility(View.VISIBLE);
//                testImageview.setImageBitmap(bitmap);
//            }
//        });
//        // 流程结束销毁图片，开始下一帧图片检测，否则内存泄露
//        rgbInstance.destory();
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        CameraPreviewManager.getInstance().stopPreview();
        // 重置质检状态
        SingleBaseConfig.getBaseConfig().setQualityControl(qualityControl);
        FaceSDKManager.getInstance().initConfig();
    }

    // 检测结果输出
    private void checkResult(LivenessModel model) {
        if (model == null) {
            clearTip();
            return;
        }
        int liveType = SingleBaseConfig.getBaseConfig().getType();
        // 无活体
        if (Integer.valueOf(liveType) == 1) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTrackText.setVisibility(View.VISIBLE);
                    mDetectText.setText("人脸采集成功");
                }
            });
            displayResult(model, null);
            // 注册
            register(model);

        } else if (Integer.valueOf(liveType) == 2) { // RGB活体检测
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTrackText.setVisibility(View.VISIBLE);
                    mDetectText.setText("活体检测判断中...");
                }
            });

            displayResult(model, "livess");
            boolean livenessSuccess = false;
            float rgbLiveThreshold = SingleBaseConfig.getBaseConfig().getRgbLiveScore();
            livenessSuccess = (model.getRgbLivenessScore() > rgbLiveThreshold) ? true : false;
            if (livenessSuccess) {
                // 注册
                register(model);
            }

        }
    }

    private void displayResult(final LivenessModel livenessModel, final String livess) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (livess != null && livess.equals("livess")) {
                    float rgbLivenessScore = livenessModel.getRgbLivenessScore();
                    float liveThreadHold = SingleBaseConfig.getBaseConfig().getRgbLiveScore();
                    if (rgbLivenessScore < liveThreadHold) {
                        mTrackText.setVisibility(View.VISIBLE);
                        mTrackText.setText("识别失败");
                        mTrackText.setBackgroundColor(Color.RED);
                        mDetectText.setText("活体检测未通过");
                    }

                }
            }

        });


    }


    private void clearTip() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDetectText.setText("未检测到人脸");
                mTrackText.setVisibility(View.VISIBLE);
            }
        });

    }


    private void displayTip(final String status) {
Log.i("faceddddd","status    "+status);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTrackText.setVisibility(View.VISIBLE);
                mDetectText.setText(status);
            }
        });
    }


    /**
     * 注册到人脸库
     *
     * @param model 人脸数据
     */

    private void register(LivenessModel model) {

        if (model == null) {
            return;
        }

        if (userId == null ||username == null || groupId == null) {
            displayTip("注册信息缺失");
            return;
        }


        BDFaceImageInstance image = model.getBdFaceImageInstance();
        rgbBitmap = BitmapUtils.getInstaceBmp(image);
        // 获取选择的特征抽取模型
        int modelType = SingleBaseConfig.getBaseConfig().getActiveModel();
        if (modelType == 1) {
            // 生活照
            FaceSDKManager.getInstance().onFeatureCheck(model.getBdFaceImageInstance(), model.getLandmarks(),
                    BDFaceSDKCommon.FeatureType.BDFACE_FEATURE_TYPE_LIVE_PHOTO, new FaceFeatureCallBack() {
                        @Override
                        public void onFaceFeatureCallBack(float featureSize, byte[] feature) {
                            displayCompareResult(featureSize, feature);
                            Log.e("qing", String.valueOf(feature.length));
                        }

                    });

        } else if (Integer.valueOf(modelType) == 2) {
            // 证件照
            FaceSDKManager.getInstance().onFeatureCheck(model.getBdFaceImageInstance(), model.getLandmarks(),
                    BDFaceSDKCommon.FeatureType.BDFACE_FEATURE_TYPE_ID_PHOTO, new FaceFeatureCallBack() {
                        @Override
                        public void onFaceFeatureCallBack(float featureSize, byte[] feature) {
                            displayCompareResult(featureSize, feature);
                        }
                    });
        }


    }


    // 根据特征抽取的结果 注册人脸

    /**
     * 提取人脸特征值后用于在数据库中注册
     * @param ret
     * @param faceFeature
     */
    private void displayCompareResult(float ret, byte[] faceFeature) {

        T.d(FaceRGBRegisterActivity.class, "displayCompareResult 提取特征值用于注册 ret:" + ret);
        // 特征提取成功
        if (ret == 128) {

            String imageName = groupId + "-" + username + ".jpg";
            // 注册到人脸库
            boolean isSuccess = FaceApi.getInstance().registerUserIntoDBmanager(groupId, userId, username, imageName,
                    userInfo, faceFeature);
            if (isSuccess) {
                // 关闭摄像头
                CameraPreviewManager.getInstance().stopPreview();
                Log.e("qing", "注册成功");

                // 压缩、保存人脸图片至300 * 300
                File faceDir = FileUtils.getBatchImportSuccessDirectory();
                final File file = new File(faceDir, imageName);
                ImageUtils.resize(rgbBitmap, file, 300, 300);

                // 数据变化，更新内存
                FaceApi.getInstance().initDatabases(true);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTrackText.setVisibility(View.VISIBLE);
                        mTrackText.setText("注册完毕");
                        mTrackText.setBackgroundColor(Color.rgb(66, 147, 136));
                        mDetectText.setText("用户" + username + "," + "已经注册完毕");
                        mDetectImage.setImageBitmap(rgbBitmap);
                        // 防止重复注册
                        username = null;
                        groupId = null;
                        // 做延时 finish
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                String path = file.getPath();
                                T.d(FaceRGBRegisterActivity.class, "返回图片的路径path:" + path);
                                Intent intent = new Intent();
                                intent.putExtra(PATH, path);
                                setResult(RESULT_OK, intent);
                                FaceRGBRegisterActivity.this.finish();
                            }
                        }, 3000);

                    }
                });

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTrackText.setVisibility(View.VISIBLE);
                        mTrackText.setText("注册失败");
                        mTrackText.setBackgroundColor(Color.RED);
                        mDetectText.setText("特征提取成功");
                    }
                });
            }

        } else if (ret == -1) {
            displayTip("特征提取失败");
        } else {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTrackText.setVisibility(View.VISIBLE);
                    mTrackText.setText("特征提取失败");
                    mTrackText.setBackgroundColor(Color.RED);
                    mDetectText.setText("特征提取失败");
                }
            });
        }
    }

    // 人脸大小顾虑
    public boolean faceSizeFilter(FaceInfo faceInfo, int bitMapWidth, int bitMapHeight) {

        // 判断人脸大小，若人脸超过屏幕二分一，则提示文案“人脸离手机太近，请调整与手机的距离”；
        // 若人脸小于屏幕三分一，则提示“人脸离手机太远，请调整与手机的距离”
        float ratio = (float) faceInfo.width / (float) bitMapHeight;
        if (ratio > 0.6) {
            displayTip("人脸离屏幕太近，请调整与屏幕的距离");
            return false;
        } else if (ratio < 0.2) {
            displayTip("人脸离屏幕太远，请调整与屏幕的距离");
            return false;
        } else if (faceInfo.centerX > bitMapWidth * 3 / 4) {
            displayTip("人脸在屏幕中太靠右");
            return false;
        } else if (faceInfo.centerX < bitMapWidth / 4) {
            displayTip("人脸在屏幕中太靠左");
            Log.e("qing", "--------屏幕中太靠左--------");
            return false;
        } else if (faceInfo.centerY > bitMapHeight * 3 / 4) {
            displayTip("人脸在屏幕中太靠下");
            return false;
        } else if (faceInfo.centerY < bitMapHeight / 4) {
            displayTip("人脸在屏幕中太靠上");
            return false;
        }

        return true;
    }

}
