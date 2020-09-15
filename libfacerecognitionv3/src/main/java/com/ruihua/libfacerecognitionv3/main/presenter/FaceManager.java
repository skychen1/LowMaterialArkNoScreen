package com.ruihua.libfacerecognitionv3.main.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.hardware.Camera;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;

import com.baidu.idl.main.facesdk.FaceAuth;
import com.baidu.idl.main.facesdk.FaceInfo;
import com.baidu.idl.main.facesdk.callback.Callback;
import com.baidu.idl.main.facesdk.model.BDFaceImageInstance;
import com.baidu.idl.main.facesdk.model.BDFaceSDKCommon;
import com.baidu.idl.main.facesdk.model.Feature;
import com.baidu.idl.main.facesdk.utils.PreferencesUtil;
import com.ruihua.libfacerecognitionv3.main.activity.FaceRGBRegisterActivity;
import com.ruihua.libfacerecognitionv3.main.api.FaceApi;
import com.ruihua.libfacerecognitionv3.main.callback.CameraDataCallback;
import com.ruihua.libfacerecognitionv3.main.callback.FaceDetectCallBack;
import com.ruihua.libfacerecognitionv3.main.callback.FaceFeatureCallBack;
import com.ruihua.libfacerecognitionv3.main.camera.AutoTexturePreviewView;
import com.ruihua.libfacerecognitionv3.main.camera.CameraPreviewManager;
import com.ruihua.libfacerecognitionv3.main.listener.SdkInitListener;
import com.ruihua.libfacerecognitionv3.main.manager.FaceSDKManager;
import com.ruihua.libfacerecognitionv3.main.manager.FaceTrackManager;
import com.ruihua.libfacerecognitionv3.main.manager.UserInfoManager;
import com.ruihua.libfacerecognitionv3.main.model.BaseConfig;
import com.ruihua.libfacerecognitionv3.main.model.LivenessModel;
import com.ruihua.libfacerecognitionv3.main.model.SingleBaseConfig;
import com.ruihua.libfacerecognitionv3.main.model.User;
import com.ruihua.libfacerecognitionv3.main.ui.IFaceInport;
import com.ruihua.libfacerecognitionv3.main.ui.IFaceLicense;
import com.ruihua.libfacerecognitionv3.main.ui.IFaceQueryUser;
import com.ruihua.libfacerecognitionv3.main.ui.IFaceRegister;
import com.ruihua.libfacerecognitionv3.main.ui.IFaceRegisterCount;
import com.ruihua.libfacerecognitionv3.main.ui.IFaceSearch;
import com.ruihua.libfacerecognitionv3.main.utils.BitmapUtils;
import com.ruihua.libfacerecognitionv3.main.utils.FaceOnDrawTexturViewUtil;
import com.ruihua.libfacerecognitionv3.main.utils.FileUtils;
import com.ruihua.libfacerecognitionv3.main.utils.ImageUtils;
import com.ruihua.libfacerecognitionv3.main.utils.T;
import com.ruihua.libfacerecognitionv3.main.view.FaceAuthDialog;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;
import static com.ruihua.libfacerecognitionv3.main.manager.FaceSDKManager.SDK_INIT_FAIL;
import static com.ruihua.libfacerecognitionv3.main.manager.FaceSDKManager.SDK_INIT_SUCCESS;
import static com.ruihua.libfacerecognitionv3.main.manager.FaceSDKManager.SDK_MODEL_LOAD_SUCCESS;
import static com.ruihua.libfacerecognitionv3.main.manager.FaceSDKManager.initStatus;

/**
 * describe ：
 * 使用说明 1. sdk激活: 可以在线激活和离线激活，只用激活一次，之后初始化直接调用 initLicense() 方法即可，
 * 在线激活initLicenseOnline(),
 * 离线激活 initLicenseOffLine()
 * 判断sdk是否初始完成 hasLicense
 * <p>
 * 弹出框激活方式，lib 中内置 FaceAuthDialog 类，用于弹出是否激活框，也可以根据需求自己定义界面
 * <p>
 * <p>
 * 2. 人脸注册：startActivityFaceRegister 提供跳转界面使用，注册完成后返回的图片路径，
 * 需要在activity 的onActivityResult 方法中调用onActivityResultIn，回调接口用同一个即可
 * 注意（用户名是唯一的，不能重名）
 * <p>
 * <p>
 * 3. 人脸图片导入功能：inportFaceImage() 直接传入图片路径，用户名和图片名字，即可，注意相同名字不可重复注册
 * <p>
 * 4. 查询本地人脸数据库中注册的用户数据，可以通过queryAllUser()，queryUser(),后续还有需要可以继续添加新的方法
 * <p>
 * 5. 人脸搜索匹配，直接调用 faceStartRGBSearch 需要传入一个AutoTexturePreviewView，
 * 如果需要显示人脸线框，需要在 faceStartRGBSearch 之前调用 initShowFrame 初始化线框绘制TextureView，
 * 注意退出界面后需要调用 stopPreviewAndRelease 用于释放界面和停止相机预览
 * 注意最好在onResume()中调用 faceStartRGBSearch， 在onPause() 中调用stopPreviewAndRelease。
 * <p>
 * 6. 部分参数设置：
 * setMirrorRGB(); 是否设置为镜像，默认非镜像
 *
 * @author : boyu
 * date: 2019/12/25
 */
public class FaceManager {

    private FaceAuth mFaceAuth;

    // 正则只支持数字与字符
    private static final Pattern pattern = Pattern.compile("^[A-Za-z0-9]+$");
    private static final int TEXT_LENGTH = 20;
    private static final int USERINFO_LENGTH = 100;
    public static final int CODE_ERROR = -1;
    public static final int CODE_SUCCESS = 200;
    private static final int REQ = 20010;
    private static final String DEFAULT = "default"; //默认用户组标识

    private FaceManager() {
        mFaceAuth = new FaceAuth();
    }

    public static FaceManager getManager() {
        return SingleFceManager.SINGLE;
    }

    public boolean hasModelInit() {
        return initStatus == SDK_MODEL_LOAD_SUCCESS;
    }

    private static class SingleFceManager {
        private static FaceManager SINGLE = new FaceManager();
    }

    /**
     *
     * ================================================sdk初始化=========================================
     */

    /**
     * 检测是否激活
     *
     * @return
     */
    public boolean hasLicense() {
        if (FaceSDKManager.initStatus != SDK_MODEL_LOAD_SUCCESS) {
            return false;
        }
        return true;
    }

    private static final String DEFAULT_GROUP = "rivamed"; //默认用户组标识
    private static final String DEFAULT_FILE = "rivamed_face"; //默认用户组标识
    private String groupName = DEFAULT_GROUP;
    private String fileName = DEFAULT_FILE;
    private FaceAuthDialog authDialog;

    /**
     * 检测是否激活
     *
     * @return
     */
    public boolean hasAction() {
        boolean hasActivate = PreferencesUtil.getBoolean("has_activate", false);
        if (hasActivate)
            FaceSDKManager.initStatus = SDK_INIT_SUCCESS;
        return hasActivate;
    }

    /**
     * @param cameraFacing       摄像头，CameraPreviewManager.CAMERA_FACING_FRONT，
     *                           CameraPreviewManager.CAMERA_USB,
     *                           CameraPreviewManager.CAMERA_FACING_BACK
     * @param displayOrientation 画面方向 CameraPreviewManager.ORIENTATION_HORIZONTAL,
     *                           CameraPreviewManager.ORIENTATION_PORTRAIT
     */
    public void init(final Context context, String fileName, String groupName, boolean isMirror, int cameraFacing, int displayOrientation, final SdkInitListener listener) {
        this.fileName = TextUtils.isEmpty(fileName) ? DEFAULT_FILE : fileName;
        this.groupName = TextUtils.isEmpty(groupName) ? DEFAULT_GROUP : groupName;
        init(context, isMirror, cameraFacing, displayOrientation, listener);
    }

    public void init(final Context context, boolean isMirror, int cameraFacing, int displayOrientation, final SdkInitListener listener) {
        if (context == null) {
            throw new NullPointerException("context 不能为null");
        }
        if (listener == null) {
            throw new NullPointerException("listener 不能为null");
        }
        BaseConfig baseConfig = SingleBaseConfig.getBaseConfig();
        baseConfig.setMirrorRGB(isMirror ? 1 : 0);

        CameraPreviewManager.getInstance().setCameraFacing(cameraFacing);
        CameraPreviewManager.getInstance().setDisplayOrientation(displayOrientation);
        PreferencesUtil.initPrefs(context.getApplicationContext());
        final String licenseOfflineKey = PreferencesUtil.getString("activate_offline_key", "");
        final String licenseOnlineKey = PreferencesUtil.getString("activate_online_key", "");

        // 如果licenseKey 不存在提示授权码为空，并跳转授权页面授权
        if (TextUtils.isEmpty(licenseOfflineKey) && TextUtils.isEmpty(licenseOnlineKey)) {
            authDialog = new FaceAuthDialog(context);
            authDialog.setDialogClickListener(listener);
            authDialog.show();
        } else if (!TextUtils.isEmpty(licenseOfflineKey)) {
            // 离线激活
            initLicenseOffLine(context, listener);
        } else if (!TextUtils.isEmpty(licenseOnlineKey)) {
            initLicenseOnline(licenseOnlineKey, context, listener);
        }
    }

    /**
     * 启动应用程序，如果之前初始过，自动初始化鉴权和模型
     */
    public void initLicense(Context context, IFaceLicense listener) {
        if (context == null) {
            throw new NullPointerException("context 不能为null");
        }
        if (listener == null) {
            throw new NullPointerException("listener 不能为null");
        }
        if (FaceSDKManager.initStatus != SDK_MODEL_LOAD_SUCCESS) {
            FaceSDKManager.getInstance().init(context, new SdkInitListener() {
                @Override
                public void initStart() {

                }

                @Override
                public void initLicenseSuccess() {
                    listener.initLicenseSuccess();
                }

                @Override
                public void initLicenseFail(int errorCode, String msg) {
                    listener.initLicenseFail(errorCode, msg);
                }

                @Override
                public void initModelSuccess() {
                }

                @Override
                public void initModelFail(int errorCode, String msg) {

                }
            });
        }
    }

    /**
     * 在线激活
     *
     * @param key 激活码
     */
    public void initLicenseOnline(String key, Context context, SdkInitListener listener) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        if (context == null) {
            throw new NullPointerException("context 不能为null");
        }
        if (listener == null) {
            throw new NullPointerException("listener 不能为null");
        }
        mFaceAuth.initLicenseOnLine(context, key, new Callback() {
            @Override
            public void onResponse(final int code, final String response) {
                PreferencesUtil.putBoolean("has_activate", code == 0);
                if (null != authDialog && authDialog.isShowing()) {
                    authDialog.dismiss();
                    authDialog = null;
                }
                T.d(FaceManager.class, "initLicenseOnLine code:" + code + " response:" + response);
                if (code == 0) {
                    FaceSDKManager.initStatus = SDK_INIT_SUCCESS;
                    listener.initLicenseSuccess();
                    FaceSDKManager.getInstance().initModel(context, listener);
                } else {
                    FaceSDKManager.initStatus = SDK_INIT_FAIL;
                    PreferencesUtil.putString("activate_offline_key", "");
                    PreferencesUtil.putString("activate_online_key", "");
                    listener.initLicenseFail(code, response);
                }
            }
        });
    }

    /**
     * 请将License.zip放到SD卡根目录下
     * 基于License文件手动激活，设备无需联网
     * 离线激活
     */
    public void initLicenseOffLine(Context context, SdkInitListener listener) {
        if (context == null) {
            throw new NullPointerException("context 不能为null");
        }
        if (listener == null) {
            throw new NullPointerException("listener 不能为null");
        }
        mFaceAuth.initLicenseOffLine(context, new Callback() {
            @Override
            public void onResponse(final int code, final String response) {
                PreferencesUtil.putBoolean("has_activate", code == 0);
                if (null != authDialog && authDialog.isShowing()) {
                    authDialog.dismiss();
                    authDialog = null;
                }
                if (code == 0) {
                    FaceSDKManager.initStatus = SDK_INIT_SUCCESS;
                    listener.initLicenseSuccess();
                    FaceSDKManager.getInstance().initModel(context, listener);
                } else {
                    FaceSDKManager.initStatus = SDK_INIT_FAIL;
                    PreferencesUtil.putString("activate_offline_key", "");
                    PreferencesUtil.putString("activate_online_key", "");
                    listener.initLicenseFail(code, response);
                }
            }
        });
    }

    /**
     *
     * ================================================跳转人脸注册=========================================
     */


    /**
     * 跳转注册界面注册用户, 在onActivityResult 界面调用 onActivityResultIn
     *
     * @param userName
     */
    public void startActivityFaceRegister(Context context, String userId, String userName, IFaceRegister listener) {
        startActivityFaceRegister(context, userId,  userName, groupName, "", listener);
    }

    /**
     * 跳转注册界面注册用户,在onActivityResult 界面调用 onActivityResultIn
     *
     * @param username
     * @param groupId
     * @param userInfo
     */
    public void startActivityFaceRegister(Context context, String userId, String username, String groupId, String userInfo, IFaceRegister listener) {
        if (context == null) {
            throw new NullPointerException("context 不能为null");
        }
        if (listener == null) {
            throw new NullPointerException("listener 不能为null");
        }
        //用户名
        if (TextUtils.isEmpty(userId)) {
            listener.registerResult(CODE_ERROR, "人脸ID不能为空");
            return;
        }
        if (TextUtils.isEmpty(username)) {
            listener.registerResult(CODE_ERROR, "用户名不能为空");
            return;
        }
        Matcher matcher = pattern.matcher(username);
        if (!matcher.matches()) {
            listener.registerResult(CODE_ERROR, "用户名由数字、字母中的一个或者多个组成");
            return;
        }
        if (username.length() > TEXT_LENGTH) {
            listener.registerResult(CODE_ERROR, "用户名输入长度超过限制！");
            return;
        }
        //用户组
        if (TextUtils.isEmpty(groupId)) {
            listener.registerResult(CODE_ERROR, "用户组名不能为空");
            return;
        }
        if (groupId.length() > TEXT_LENGTH) {
            listener.registerResult(CODE_ERROR, "用户组输入长度超过限制！");
            return;
        }
        matcher = pattern.matcher(groupId);
        if (!matcher.matches()) {
            listener.registerResult(CODE_ERROR, "groupId由数字、字母中的一个或者多个组成");
            return;
        }
        // 获取用户
        List<User> listUsers = FaceApi.getInstance().getUserListByUserId(groupName, userId);
        if (listUsers != null && listUsers.size() > 0) {
            //有重名的用户 删除已注册
            boolean success = FaceApi.getInstance().userDeleteByUserId(groupName, userId);
            Log.e("Face", "startActivityFaceRegister delete: " + success);
            if (!success) {
                listener.registerResult(CODE_ERROR, "删除本地已注册人脸照失败");
                return;
            }
        }
        if (!TextUtils.isEmpty(userInfo)) {
            if (userInfo.length() > USERINFO_LENGTH) {
                listener.registerResult(CODE_ERROR, "用户信息输入长度超过限制！");
            }
            return;
        }

        // 判断活体类型
        int liveType = SingleBaseConfig.getBaseConfig().getType();
        if (liveType == 2) { // RGB
            Log.i("faceddddd","跳转    ");
            Intent intent = new Intent(context, FaceRGBRegisterActivity.class);
            intent.putExtra("group_id", groupId);
            intent.putExtra("user_id", userId);
            intent.putExtra("user_name", username);
            if (!TextUtils.isEmpty(userInfo)) {
                intent.putExtra("user_info", userInfo);
            }
            ((Activity) context).startActivityForResult(intent, REQ);
        } else {
            listener.registerResult(CODE_ERROR, "本页面用于RGB活体检测");
        }
    }

    /**
     * 界面有返回时添加到activity 中的onActivityResult中
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResultIn(int requestCode, int resultCode, Intent data, IFaceRegister listener) {
        if (requestCode == REQ && resultCode == RESULT_OK) {
            if (data.getExtras() != null) {
                listener.registerResult(100, data.getExtras().getString("path", ""));
            }
        }
    }

    /**
     *
     * ================================================导入人脸图片=========================================
     */

    /**
     * 导入人脸图片
     *
     * @param userName, 用户名
     */
    public void inportFaceImage(String userId, String userName, String path, IFaceInport listener) {
        inportFaceImage(userId,userName, groupName, path, listener);
    }
    private ExecutorService es = Executors.newSingleThreadExecutor();
    private Future          future;

    /**
     * 数据库发现变化时候，重新把数据库中的人脸信息添加到内存中，id+feature
     */
    public void initDatabases() {
        if (future != null && !future.isDone()) {
            future.cancel(true);
        }
        future = es.submit(() -> {
            ArrayList<Feature> features = new ArrayList<>();
            List<User> listUser = FaceApi.getInstance().getUserList(groupName);
            for (int j = 0; j < listUser.size(); j++) {
                Feature feature = new Feature();
                feature.setId(listUser.get(j).getId());
                feature.setFeature(listUser.get(j).getFeature());
                features.add(feature);
            }
            FaceSDKManager.getInstance().getFaceFeature().featurePush(features);
        });
    }
    /**
     * 导入人脸图片
     *
     * @param userName, 用户名
     * @param groupId   组名
     */
    public void inportFaceImage(String userId,String userName, String groupId, String path, IFaceInport listener) {
        if (listener == null) {
            throw new NullPointerException("listener 不能为null");
        }
        // 获取用户
        // 根据姓名查询数据库与文件中对应的姓名是否相等，如果相等，则先删除旧照，再注册新照

        Log.e("Face", "startActivityFaceRegister delete 0: " );
        List<User> listUsers = FaceApi.getInstance().getUserListByUserId(groupId, userId);
        if (listUsers != null && listUsers.size() > 0) {
            Log.e("Face", "startActivityFaceRegister delete 2: " );
            boolean success = FaceApi.getInstance().userDeleteByUserId(groupId, userId);
            Log.e("Face", "startActivityFaceRegister delete: " + success);
            if (!success) {
                listener.importFaceImageResult(CODE_ERROR, "删除人脸底照失败：：" + userName);
                return;
            }
        }
        // 判断姓名是否有效
        String nameResult = FaceApi.getInstance().isValidName(userName);
        if (!"0".equals(nameResult)) {
            listener.importFaceImageResult(CODE_ERROR, nameResult);
            return;
        }

        //  图片后缀
        String picName = groupId + "-" + userName + ".jpg";
        // 根据图片的路径将图片转成Bitmap
        // 根据图片的路径将图片转成Bitmap
        Bitmap bitmap = BitmapFactory.decodeFile(path);

        if (bitmap != null) {
            byte[] bytes = new byte[512];
            float ret = -1;
            // 走人脸SDK接口，通过人脸检测、特征提取拿到人脸特征值
            ret = FaceApi.getInstance().getFeature(bitmap, bytes,
                    BDFaceSDKCommon.FeatureType.BDFACE_FEATURE_TYPE_LIVE_PHOTO);

            T.d(FaceManager.class, "live_photo = " + ret);

            if (ret == -1) {
                listener.importFaceImageResult(CODE_ERROR, "未检测到人脸，可能原因：人脸太小");
            } else if (ret == 128) {
                // 将用户信息和用户组信息保存到数据库
                boolean importDBSuccess = FaceApi.getInstance().registerUserIntoDBmanager(groupId,
                        userId, userName, picName, null, bytes);
                if (importDBSuccess) {
                    listener.importFaceImageResult(CODE_SUCCESS, userName + "人脸注册成功");
                } else {
                    listener.importFaceImageResult(CODE_ERROR, userName + "人脸注册失败");
                }
            } else {
                listener.importFaceImageResult(CODE_ERROR, "未检测到人脸");
            }

            // 图片回收
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        } else {
            listener.importFaceImageResult(CODE_ERROR, "该图片转成Bitmap失败");
        }

    }

    /**
     *
     * ===============================================人脸库管理=========================================
     */

    /**
     * 查询默认人脸底库注册数量
     *
     * @param groupId 用户组，可以为null，将查找全部
     */
    public void queryAllUserCount(String groupId, IFaceRegisterCount iFaceRegisterCount) {
        if (TextUtils.isEmpty(groupId)) {
            groupId = groupName;
        }
        UserInfoManager.getInstance().getUserListInfoByGroupId(null, groupId,
                new UserInfoManager.UserInfoListener() {
                    @Override
                    public void userListQuerySuccess(List<User> listUserInfo) {
                        iFaceRegisterCount.registerCount(listUserInfo.size());
                    }

                    @Override
                    public void userListQueryFailure(String message) {
                        iFaceRegisterCount.registerCount(0);
                    }
                });
    }

    public void queryAllUserCount(IFaceRegisterCount iFaceRegisterCount) {
        queryAllUserCount(groupName, iFaceRegisterCount);
    }

    /**
     * 查询所有注册用户
     *
     * @param listener
     */
    public void queryAllUser(IFaceQueryUser<User> listener) {
        UserInfoManager.getInstance().getUserListInfoByGroupId(
                null, groupName, new UserInfoManager.UserInfoListener() {

                    @Override
                    public void userListQuerySuccess(List<User> listUserInfo) {
                        listener.uquerySuccess(listUserInfo);
                    }

                    @Override
                    public void userListQueryFailure(String message) {
                        listener.uqueryFailure(message);
                    }
                });
    }

    /**
     * 根据用户名查询用户
     *
     * @param userName
     * @param groupId
     * @param listener
     */
    public void queryUser(String userName, String groupId, IFaceQueryUser<User> listener) {
        UserInfoManager.getInstance().getUserListInfoByGroupId(
                userName, groupId, new UserInfoManager.UserInfoListener() {
                    @Override
                    public void userListQuerySuccess(List<User> listUserInfo) {
                        listener.uquerySuccess(listUserInfo);
                    }

                    @Override
                    public void userListQueryFailure(String message) {
                        listener.uqueryFailure(message);
                    }
                });
    }

    /**
     * ===============================================人脸搜索=========================================
     */
    // 活体阈值
    private float mRgbLiveScore;
    // 图片越大，性能消耗越大，也可以选择640*480， 1280*720
    private static final int PREFER_WIDTH = 640;
    private static final int PERFER_HEIGH = 480;
    WeakReference<Context> mContextWeakReference = null;
    WeakReference<TextureView> mTextureViewWeakReference = null;
    WeakReference<AutoTexturePreviewView> mAutoTexturePreviewViewWeakReference = null;
    private Paint paint;
    private RectF rectF;


    /**
     * 人脸搜索方法,如果需要绘制线框，请先调用初始化 线框界面initShowFrame()方法
     * <p>
     * previewView 用于摄像头捕捉画面后展示人脸数据
     * camerFacing        摄像头，CameraPreviewManager.CAMERA_FACING_FRONT，
     * CameraPreviewManager.CAMERA_USB,
     * CameraPreviewManager.CAMERA_FACING_BACK
     * displayOrientation 画面方向 CameraPreviewManager.ORIENTATION_HORIZONTAL,
     * CameraPreviewManager.ORIENTATION_PORTRAIT
     */
    public void faceStartRGBSearch(Context context, AutoTexturePreviewView previewView, IFaceSearch listener) {
        if (context == null) {
            throw new NullPointerException("context 不能为null");
        }
        mContextWeakReference = new WeakReference<>(context);
        if (previewView == null) {
            throw new NullPointerException("previewView 不能为null");
        }
        mAutoTexturePreviewViewWeakReference = new WeakReference<>(previewView);
        if (listener == null) {
            throw new NullPointerException("listener 不能为null");
        }
        // 活体状态
        final int mLiveType = SingleBaseConfig.getBaseConfig().getType(); //活体状态默认 2是rgb活体
        if (mLiveType != 2) {
            listener.onTip(CODE_ERROR, "必须是Rgb活体检测", null);
            return;
        }
        mRgbLiveScore = SingleBaseConfig.getBaseConfig().getRgbLiveScore();
//        if(SingleBaseConfig.getBaseConfig().getMirrorRGB() == 0){ //无镜像
//            mAutoTexturePreviewViewWeakReference.get().getTextureView().setScaleX(-1); //无镜像
//        }else{
//            mAutoTexturePreviewViewWeakReference.get().getTextureView().setScaleX(0); //镜像
//        }
//

        T.d(FaceManager.class, "startPreview");
        CameraPreviewManager.getInstance().startPreview(context, mAutoTexturePreviewViewWeakReference.get(),
                PREFER_WIDTH, PERFER_HEIGH, new CameraDataCallback() {
                    @Override
                    public void onGetCameraData(byte[] data, Camera camera, int width, int height) {
                        T.d(FaceManager.class, "onGetCameraData");
                        // 摄像头预览数据进行人脸检测
                        FaceSDKManager.getInstance().onDetectCheck(data, null, null,
                                height, width, mLiveType, new FaceDetectCallBack() {
                                    @Override
                                    public void onFaceDetectCallback(LivenessModel livenessModel) {
                                        // 输出结果
                                        T.d(FaceRGBRegisterActivity.class, "onFaceDetectCallback");
                                        checkCloseResult(livenessModel, listener);
                                    }

                                    @Override
                                    public void onTip(int code, String msg) {
                                        listener.onTip(code, msg, null);
                                    }

                                    @Override
                                    public void onFaceDetectDarwCallback(LivenessModel livenessModel) {
                                        if (mTextureViewWeakReference != null) {
                                            showFrame(livenessModel);
                                        }
                                    }
                                });
                    }
                });
    }

    /**
     * 初始化绘制线框，如果不初始化绘制线框就不会绘制
     *
     * @param mtextureView
     */
    public void initShowFrame(TextureView mtextureView) {
        // 画人脸框
        paint = new Paint();
        rectF = new RectF();
        if (mtextureView == null) {
            throw new NullPointerException("mtextureView 不能为null");
        }
        mTextureViewWeakReference = new WeakReference<>(mtextureView);
        mTextureViewWeakReference.get().setOpaque(false);
        mTextureViewWeakReference.get().setKeepScreenOn(true);
    }

    /**
     * 停止捕捉画面和释放绘制view
     */
    public void stopPreviewAndRelease() {
        CameraPreviewManager.getInstance().stopPreview();
        if (mAutoTexturePreviewViewWeakReference != null) {
            mAutoTexturePreviewViewWeakReference.clear();
        }
        if (mTextureViewWeakReference != null) {
            mTextureViewWeakReference.clear();
        }
    }

    /**
     * 绘制人脸框。
     */
    private void showFrame(final LivenessModel model) {
        if (mContextWeakReference.get() == null) {
            return;
        }
        ((Activity) mContextWeakReference.get()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mTextureViewWeakReference.get() == null) {
                    return;
                }
                Canvas canvas = mTextureViewWeakReference.get().lockCanvas();
                if (canvas == null) {
                    mTextureViewWeakReference.get().unlockCanvasAndPost(canvas);
                    return;
                }
                if (model == null) {
                    // 清空canvas
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    mTextureViewWeakReference.get().unlockCanvasAndPost(canvas);
                    return;
                }
                FaceInfo[] faceInfos = model.getTrackFaceInfo();
                if (faceInfos == null || faceInfos.length == 0) {
                    // 清空canvas
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    mTextureViewWeakReference.get().unlockCanvasAndPost(canvas);
                    return;
                }
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                FaceInfo faceInfo = faceInfos[0];

                rectF.set(FaceOnDrawTexturViewUtil.getFaceRectTwo(faceInfo));
                if (mAutoTexturePreviewViewWeakReference.get() == null) {
                    return;
                }
                // 检测图片的坐标和显示的坐标不一样，需要转换。
                FaceOnDrawTexturViewUtil.mapFromOriginalRect(rectF,
                        mAutoTexturePreviewViewWeakReference.get(), model.getBdFaceImageInstance());
                paint.setColor(Color.GREEN);
                paint.setStyle(Paint.Style.STROKE);
                // 绘制框
                canvas.drawRect(rectF, paint);
                mTextureViewWeakReference.get().unlockCanvasAndPost(canvas);

            }
        });
    }

    private void checkCloseResult(final LivenessModel livenessModel, IFaceSearch listener) {
        if (mContextWeakReference.get() == null) {
            return;
        }
        // 当未检测到人脸UI显示
        ((Activity) mContextWeakReference.get()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (livenessModel == null || livenessModel.getFaceInfo() == null) {
                    listener.onTip(CODE_ERROR, "未检测到人脸", null);
                } else {
                    //活体Rgb检测
                    float rgbLivenessScore = livenessModel.getRgbLivenessScore();
                    if (rgbLivenessScore < mRgbLiveScore) {
                        listener.onTip(CODE_ERROR, "识别失败，活体检测未通过", null);
                    } else {
                        User user = livenessModel.getUser();
                        if (user == null) {
                            listener.onTip(CODE_ERROR, "识别失败，搜索不到用户", null);
                        } else {
                            //这边是识别成功后最后的调用
                            String absolutePath = FileUtils.getBatchImportSuccessDirectory(fileName)
                                    + "/" + user.getImageName();
//                            Bitmap bitmap = BitmapFactory.decodeFile(absolutePath);
                            listener.onTip(CODE_SUCCESS, absolutePath, user);
                        }
                    }
                }


            }
        });
    }

    /**
     *
     * ===============================================部分参数设置=========================================
     */

    /**
     * 设置摄像机预览和照片保存是否镜像
     * 0：RGB无镜像，1：有镜像
     *
     * @param rgbmirror
     */
    public void setMirrorRGB(int rgbmirror) {
        SingleBaseConfig.getBaseConfig().setMirrorRGB(rgbmirror);
    }

    /**
     * ===============================================人脸注册=========================================
     */
    // TODO: 2019/12/27 功能模块还需要调试 
    // RGB摄像头图像宽和高
    private static final int mWidth = 640;
    private static final int mHeight = 480;
    private WeakReference<Context> mContextWeakReferenceRegister = null;
    private WeakReference<AutoTexturePreviewView> mAutoTexturePreviewViewWeakReferenceRegister = null;
    private String username = null;
    private String userId = null;
    private String userInfo = null;
    private Bitmap rgbBitmap = null;
    private IFaceRegister mIFaceRegisterlistener;
    private boolean qualityControl;


    /**
     * 人脸识别用户注册
     * <p>
     * username           必填 不能为null
     * groupId            非必填，可以为null
     * userInfo           非必填，可以为null
     * previewView        用于展示摄像头捕捉的画面
     * camerFacing        摄像头，CameraPreviewManager.CAMERA_FACING_FRONT，
     * CameraPreviewManager.CAMERA_USB,
     * CameraPreviewManager.CAMERA_FACING_BACK
     * displayOrientation 画面方向 CameraPreviewManager.ORIENTATION_HORIZONTAL,
     * CameraPreviewManager.ORIENTATION_PORTRAIT
     */
    public void faceStartRGBRegister(Context context, String userId,String username, AutoTexturePreviewView previewView, IFaceRegister listener) {
        faceStartRGBRegister(context, userId,username, groupName, "", previewView, listener);
    }

    public void faceStartRGBRegister(Context context, String userId, String username, String groupId, String userInfo,
                                     AutoTexturePreviewView previewView, IFaceRegister listener) {
        if (context == null) {
            throw new NullPointerException("context 不能为null");
        }
        mContextWeakReferenceRegister = new WeakReference<>(context);
        if (TextUtils.isEmpty(username)) {
            throw new NullPointerException("username 不能为null");
        }
        this.username = username;
        this.userId = userId;
        if (!TextUtils.isEmpty(groupId)) {
            groupName = groupId;
        }
        this.userInfo = userInfo;


        if (previewView == null) {
            throw new NullPointerException("previewView 不能为null");
        }
        mAutoTexturePreviewViewWeakReferenceRegister = new WeakReference<>(previewView);
        if (listener == null) {
            throw new NullPointerException("listener 不能为null");
        }
        mIFaceRegisterlistener = listener;

        qualityControl = SingleBaseConfig.getBaseConfig().isQualityControl();
        // 注册默认开启质量检测
        SingleBaseConfig.getBaseConfig().setQualityControl(true);
        FaceSDKManager.getInstance().initConfig();
        // 活体状态
        final int mLiveType = SingleBaseConfig.getBaseConfig().getType(); //活体状态默认 2是rgb活体
        if (mLiveType != 2) {
            listener.registerResult(CODE_ERROR, "必须是Rgb活体检测");
            return;
        }
        FaceTrackManager.getInstance().setAliving(true);

        // TODO 在得力设备和部分手机上出现过 CameraPreviewManager 崩溃的问题

        CameraPreviewManager.getInstance().startPreview(
                mContextWeakReferenceRegister.get(), mAutoTexturePreviewViewWeakReferenceRegister.get(),
                mWidth, mHeight, new CameraDataCallback() {
                    @Override
                    public void onGetCameraData(byte[] data, Camera camera, int width, int height) {
                        // 拿到相机帧数
                        FaceTrackManager.getInstance().faceTrack(data, width, height, new FaceDetectCallBack() {
                            @Override
                            public void onFaceDetectCallback(LivenessModel livenessModel) {
                                // 做过滤
                                boolean isFilterSuccess = faceSizeFilter(livenessModel.getFaceInfo(), width, height, listener);
                                if (isFilterSuccess) {
                                    // 展示model
                                    boolean livenessSuccess = false;
                                    float rgbLiveThreshold = SingleBaseConfig.getBaseConfig().getRgbLiveScore();
                                    livenessSuccess = (livenessModel.getRgbLivenessScore() > rgbLiveThreshold) ? true : false;
                                    if (livenessSuccess) {
                                        // 注册
                                        register(livenessModel);
                                    } else {
                                        listener.registerResult(CODE_ERROR, "识别失败，活体检测未通过");
                                    }
                                }

                            }

                            @Override
                            public void onTip(int code, final String msg) {
                                listener.registerResult(code, msg);
                            }

                            @Override
                            public void onFaceDetectDarwCallback(LivenessModel livenessModel) {


                            }
                        });
                    }
                });
    }

    /**
     * 重置注册界面状态
     */
    public void releaseRegister() {
        CameraPreviewManager.getInstance().stopPreview();
        // 重置质检状态
        SingleBaseConfig.getBaseConfig().setQualityControl(qualityControl);
        FaceSDKManager.getInstance().initConfig();
    }


    // 人脸大小顾虑
    private boolean faceSizeFilter(FaceInfo faceInfo, int bitMapWidth, int bitMapHeight, IFaceRegister listener) {

        // 判断人脸大小，若人脸超过屏幕二分一，则提示文案“人脸离手机太近，请调整与手机的距离”；
        // 若人脸小于屏幕三分一，则提示“人脸离手机太远，请调整与手机的距离”
        float ratio = (float) faceInfo.width / (float) bitMapHeight;
        if (ratio > 0.6) {

            listener.registerResult(CODE_ERROR, "人脸离屏幕太近，请调整与屏幕的距离");
            return false;
        } else if (ratio < 0.2) {
            listener.registerResult(CODE_ERROR, "人脸离屏幕太远，请调整与屏幕的距离");
            return false;
        } else if (faceInfo.centerX > bitMapWidth * 3 / 4) {
            listener.registerResult(CODE_ERROR, "人脸在屏幕中太靠右");
            return false;
        } else if (faceInfo.centerX < bitMapWidth / 4) {
            listener.registerResult(CODE_ERROR, "人脸在屏幕中太靠左");
            return false;
        } else if (faceInfo.centerY > bitMapHeight * 3 / 4) {
            listener.registerResult(CODE_ERROR, "人脸在屏幕中太靠下");
            return false;
        } else if (faceInfo.centerY < bitMapHeight / 4) {
            listener.registerResult(CODE_ERROR, "人脸在屏幕中太靠上");
            return false;
        }

        return true;
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

        if (username == null) {
            mIFaceRegisterlistener.registerResult(CODE_ERROR, "注册信息缺失");
            return;
        }
       int y = (int)(model.getFaceInfo().centerY+0.5f);
       int x = (int)(model.getFaceInfo().centerX+0.5f);
        BDFaceImageInstance image = model.getBdFaceImageInstance();
        rgbBitmap = BitmapUtils.getInstaceBmp(image);
        rgbBitmap= cropBitmap(rgbBitmap,x,y);
        // 获取选择的特征抽取模型
        int modelType = SingleBaseConfig.getBaseConfig().getActiveModel();
        if (modelType == 1) {
            // 生活照
            FaceSDKManager.getInstance().onFeatureCheck(model.getBdFaceImageInstance(), model.getLandmarks(),
                    BDFaceSDKCommon.FeatureType.BDFACE_FEATURE_TYPE_LIVE_PHOTO, new FaceFeatureCallBack() {
                        @Override
                        public void onFaceFeatureCallBack(float featureSize, byte[] feature) {
                            displayCompareResult(featureSize, feature);
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

    /**
     * 提取人脸特征值后用于在数据库中注册
     *
     * @param ret
     * @param faceFeature
     */
    private void displayCompareResult(float ret, byte[] faceFeature) {

        T.d(FaceRGBRegisterActivity.class, "displayCompareResult 提取特征值用于注册 ret:" + ret);
        // 特征提取成功
        if (ret == 128) {

            String imageName = groupName + "-" + username + ".jpg";
            // 注册到人脸库
            boolean isSuccess = FaceApi.getInstance().registerUserIntoDBmanager(groupName, userId,username, imageName,
                    userInfo, faceFeature);
            if (isSuccess) {
                // 关闭摄像头
                CameraPreviewManager.getInstance().stopPreview();

                // 压缩、保存人脸图片至300 * 300
                File faceDir = FileUtils.getBatchImportSuccessDirectory(fileName);
                File file = new File(faceDir, imageName);
                ImageUtils.resize(rgbBitmap, file, mWidth, mHeight);

                rgbBitmap.recycle();
                rgbBitmap = null;
                if (mContextWeakReferenceRegister != null) {
                    mContextWeakReferenceRegister.clear();
                }
                if (mAutoTexturePreviewViewWeakReferenceRegister != null) {
                    mAutoTexturePreviewViewWeakReferenceRegister.clear();
                }


                // 数据变化，更新内存
                FaceApi.getInstance().initDatabases(true);

                String path = file.getPath();

                mIFaceRegisterlistener.registerResult(CODE_SUCCESS, path);

            } else {
                mIFaceRegisterlistener.registerResult(CODE_ERROR, "特征提取成功，注册失败");
            }

        } else if (ret == -1) {
            mIFaceRegisterlistener.registerResult(CODE_ERROR, "特征提取失败");
        } else {
            mIFaceRegisterlistener.registerResult(CODE_ERROR, "特征提取失败");
        }
    }

    private Bitmap cropBitmap(Bitmap bitmap,int x,int y) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int cropWidth = w >= h ? h : w;// 裁切后所取的正方形区域边长
        cropWidth /= 2;
        return Bitmap.createBitmap(bitmap, x-150, 0, cropWidth, h, null, false);
//        return Bitmap.createBitmap(bitmap, w/3, h/1.5f, cropWidth, cropHeight, null, false);
    }
}
