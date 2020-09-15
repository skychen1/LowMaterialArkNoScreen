package org.wh.engineer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rivamed.FingerCallback;
import com.rivamed.FingerManager;
import com.rivamed.libidcard.IdCardCallBack;
import com.rivamed.libidcard.IdCardManager;
import com.ruihua.libconsumables.ConsumableManager;
import com.ruihua.libconsumables.callback.ConsumableCallBack;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;
import org.wh.engineer.bean.BoxSizeBean;
import org.wh.engineer.bean.ConfigBean;
import org.wh.engineer.bean.Event;
import org.wh.engineer.dbmodel.UserFeatureInfosBean;
import org.wh.engineer.devices.AllDeviceCallBack;
import org.wh.engineer.dto.FingerLoginDto;
import org.wh.engineer.dto.IdCardLoginDto;
import org.wh.engineer.http.BaseResult;
import org.wh.engineer.http.NetRequest;
import org.wh.engineer.utils.EventBusUtils;
import org.wh.engineer.utils.LogUtils;
import org.wh.engineer.utils.LoginUtils;
import org.wh.engineer.utils.SPUtils;
import org.wh.engineer.utils.ToastUtils;
import org.wh.engineer.utils.UIUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.wh.engineer.base.App.SYSTEMTYPE;
import static org.wh.engineer.base.App.mAppContext;
import static org.wh.engineer.base.App.mTitleConn;
import static org.wh.engineer.cont.Constants.BOX_SIZE_DATE;
import static org.wh.engineer.cont.Constants.FINGER_VERSION;
import static org.wh.engineer.cont.Constants.LOGIN_TYPE_FINGER;
import static org.wh.engineer.cont.Constants.LOGIN_TYPE_IC;
import static org.wh.engineer.cont.Constants.SAVE_CONFIG_STRING;
import static org.wh.engineer.cont.Constants.SAVE_DEPT_CODE;
import static org.wh.engineer.cont.Constants.SAVE_SEVER_IP;
import static org.wh.engineer.cont.Constants.THING_CODE;

/**
 * created by wh on 2020/7/31
 * desc
 */
public class LoginService extends Service {
    private static String TAG = LoginService.class.getSimpleName();
    private Gson mGson;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mGson = new Gson();
        EventBusUtils.register(this);
        initIC();
        initFinger();
        initBom();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    /**
     * 设备网络连接状态
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTitleConnEvent(Event.XmmppConnect event) {
        mTitleConn = event.connect;
    }


    /**
     * @param configType 0:默认，1：IC卡 2：指纹
     * @param value      不同类型下获取的值
     *                   获取配置项
     */
    public void getConfigDate(int configType, String value) {
        LogUtils.i(TAG, "getConfigDate   ");
        if (SPUtils.getString(UIUtils.getContext(), THING_CODE) == null
                || TextUtils.isEmpty(SPUtils.getString(UIUtils.getContext(), THING_CODE))) {
            ToastUtils.showShortToast("设备未激活");
            return;
        }
        if (!mTitleConn) {
            if (SPUtils.getString(LoginService.this, SAVE_SEVER_IP) != null) {
                String string = SPUtils.getString(UIUtils.getContext(), SAVE_CONFIG_STRING);
                setConfigBean(string, configType, value);
            } else {
                ToastUtils.showShortToast("未填写服务器地址");
            }
        } else {
            NetRequest.getInstance().findThingConfigDate(UIUtils.getContext(), new BaseResult() {
                @Override
                public void onSucceed(String result) {
                    LogUtils.i(TAG, "result   " + result);
                    SPUtils.putString(UIUtils.getContext(), SAVE_CONFIG_STRING, result);
                    setConfigBean(result, configType, value);
                }

                @Override
                public void onError(String result) {
                    if (SPUtils.getString(LoginService.this, SAVE_SEVER_IP) != null && result.equals("-1")) {
                        String string = SPUtils.getString(UIUtils.getContext(), SAVE_CONFIG_STRING);
                        setConfigBean(string, configType, value);
                    }
                }
            });
        }
    }

    /**
     * 得到配置项后的操作
     *
     * @param result
     * @param configType
     * @param loginType
     */
    private void setConfigBean(String result, int configType, String loginType) {
        ConfigBean configBean = mGson.fromJson(result, ConfigBean.class);
        if (configBean == null) {
            ToastUtils.showShortToast("未存入配置项，请检查网络");
            return;
        }
        loginEnjoin(!LoginUtils.getConfigTrue(configBean.getConfigVos()), configType, loginType);
    }


    /**
     * 是否禁止使用
     *
     * @param configType
     * @param loginType
     */
    private void loginEnjoin(
            boolean canDevice, int configType, String loginType) {
        if (!canDevice) {//禁止
            if (configType == 0) {//禁止密码登录

            } else if (configType == 1) {//禁止IC卡登录

                ToastUtils.showShortToast("正在维护，请到管理端启用");
            } else if (configType == 2) {//禁止指纹登录

                ToastUtils.showShortToast("正在维护，请到管理端启用");
            }
        } else {
            if (configType == 0) {//正常登录密码登录限制

            } else if (configType == 1) {//IC卡登录限制
                if (mTitleConn) {
                    validateLoginIdCard(loginType);
                } else {
                    uNNetvalidateLoginIdCard(loginType);
                }
            } else if (configType == 2) {
                if (mTitleConn) {
                    validateLoginFinger(loginType);
                } else {
                    ToastUtils.showShortToast("指纹登录失败，请检查网络！");
                }
            }
        }
    }

    /**
     * IC卡
     */
    private void initIC() {
        IdCardManager.getIdCardManager().registerCallBack(new IdCardCallBack() {
            @Override
            public void onConnectState(String deviceId, boolean isConnect) {
                Log.d(TAG, "IC读卡连接：设备ID:   " + deviceId + "   ;   isConnect = " + isConnect);
                if (isConnect) {
                    IdCardManager.getIdCardManager().startReadCard(deviceId);
                }else{
                    ToastUtils.showShortToast("设备连接失败，请检查网络！");
                }
            }

            @Override
            public void onReceiveCardNum(String cardNo) {
                Log.d(TAG, "接收到刷卡信息：设备ID:     ID = " + cardNo);

                getConfigDate(1, cardNo);
            }
        });
    }

    /**
     * 指纹仪
     */
    private void initFinger() {
        FingerManager.getManager().registerCallback(new FingerCallback() {
            @Override
            public void onConnectState(String deviceId, boolean isConnect) {
                Log.d(TAG, "指纹连接：设备ID:   " + deviceId + "   ;   isConnect = " + isConnect);
                if (isConnect) {
                    FingerManager.getManager().startReadFinger(deviceId);
                }else{
                    ToastUtils.showShortToast("设备连接失败，请检查网络！");
                }
            }

            @Override
            public void onFingerFeatures(String deviceId, String features) {
                Log.d(TAG, "接收到指纹采集信息：设备ID:   " + deviceId + "   ;   FingerData = " + features);

                getConfigDate(2, features);
            }

            @Override
            public void onRegisterResult(
                    String deviceId, int code, String features, List<String> fingerPicPath, String msg) {
                Log.d(TAG, "设备：：" + deviceId + "注册结果码是：：" + code + "\n>>>>>>>" + msg + "\n指纹照片数据：：" +
                        (fingerPicPath == null ? 0 : fingerPicPath.size()) + "\n特征值是：：：" + features);
                //收到注册结果标识注册完成就开启读取
            }

            @Override
            public void onFingerUp(String deviceId) {
                Log.d(TAG, "设备：：" + deviceId + "请抬起手指：");
            }

            @Override
            public void onRegisterTimeLeft(String deviceId, long time) {
                Log.d(TAG, "设备：：" + deviceId + "剩余注册时间：：" + time + "\n");
            }
        });
    }

    private void initBom() {
        ConsumableManager.getManager().registerCallback(new ConsumableCallBack() {
            @Override
            public void onConnectState(String deviceId, boolean isConnect) {
                if (!isConnect) {
                    Log.d(TAG, "设备已断开：设备ID:   " + deviceId + "；");
                }
            }

            @Override
            public void onOpenDoor(String deviceId, int which, boolean isSuccess) {

            }

            @Override
            public void onCloseDoor(String deviceId, int which, boolean isSuccess) {

            }

            @Override
            public void onDoorState(String deviceId, int which, boolean state) { //门锁状态，true：开，false:关

            }

            @Override
            public void onOpenLight(String deviceId, int which, boolean isSuccess) {//灯(2,3)、电磁锁(11)状态

            }

            @Override
            public void onCloseLight(String deviceId, int which, boolean isSuccess) {

            }

            @Override
            public void onLightState(String deviceId, int which, boolean state) {

            }

            @Override
            public void onFirmwareVersion(String deviceId, String version) {

            }

            @Override
            public void onNeedUpdateFile(String deviceId) {

            }

            @Override
            public void onUpdateProgress(String deviceId, int percent) {

            }

            @Override
            public void onUpdateResult(String deviceId, boolean isSuccess) {

            }

        });
    }

    /**
     * 断网后IC卡登陆
     *
     * @param idCard
     */
    private void uNNetvalidateLoginIdCard(String idCard) {

        List<UserFeatureInfosBean> beans = LitePal.where("data = ? ", idCard)
                .find(UserFeatureInfosBean.class);
        LogUtils.i(TAG, " beans     " + mGson.toJson(beans));
        if (beans != null && beans.size() > 0 && beans.get(0).getData().equals(idCard)) {
            AllDeviceCallBack.getInstance().openDoor(getDeviceId(), getDeviceBeans());
        } else {
            ToastUtils.showShortToast("登录失败，暂无登录信息！");
        }
    }

    private void validateLoginIdCard(String idCard) {

        NetRequest.getInstance().validateLoginIdCard(idCardRequestParams(idCard), this, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i(TAG, "validateLoginIdCard  result   " + result);
                AllDeviceCallBack.getInstance().openDoor(getDeviceId(), getDeviceBeans());
            }

            @Override
            public void onError(String result) {
                uNNetvalidateLoginIdCard(idCard);
            }
        });

    }

    private void validateLoginFinger(String fingerFea) {

        NetRequest.getInstance().validateLoginFinger(fingerRequestParams(fingerFea), this, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i(TAG, "validateLoginFinger   result   " + result);
                AllDeviceCallBack.getInstance().openDoor(getDeviceId(), getDeviceBeans());
            }

            @Override
            public void onError(String result) {
                EventBusUtils.postSticky(new Event.EventLoading(false));
            }
        });

    }

    /**
     * 指纹网络登录请求参数
     */
    private String fingerRequestParams(String fingerFea) {
        String thingCode = SPUtils.getString(LoginService.this, THING_CODE);
        FingerLoginDto data = new FingerLoginDto();
        FingerLoginDto.UserFeatureInfoBean bean = new FingerLoginDto.UserFeatureInfoBean();
        bean.setData(fingerFea);
        data.setUserFeatureInfo(bean);
        data.setDeviceType(FINGER_VERSION);//3.0柜子需要传2   2.1传1
        data.setThingId(thingCode);
        data.setSystemType(SYSTEMTYPE);
        data.setLoginType(LOGIN_TYPE_FINGER);
        data.setDeptId(SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
        LogUtils.i("Login", "THING_CODE validateLoginFinger  " + mGson.toJson(data));
        return mGson.toJson(data);
    }

    /**
     * IC,ID卡网络登录请求参数
     */
    private String idCardRequestParams(String idCard) {
        IdCardLoginDto data = new IdCardLoginDto();
        IdCardLoginDto.UserFeatureInfoBean bean = new IdCardLoginDto.UserFeatureInfoBean();
        bean.setData(idCard);
        bean.setType("2");
        data.setUserFeatureInfo(bean);
        data.setThingId(SPUtils.getString(LoginService.this, THING_CODE));
        data.setDeptId(SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
        data.setSystemType(SYSTEMTYPE);
        data.setLoginType(LOGIN_TYPE_IC);
        LogUtils.i(TAG, "mGson.toJson(data)   " + mGson.toJson(data));
        return mGson.toJson(data);
    }

    /**
     * 获取柜体列表
     */
    private List<BoxSizeBean.DevicesBean> getDeviceBeans() {
        String devicesBeanJson = SPUtils.getString(mAppContext, BOX_SIZE_DATE);
        Type type = new TypeToken<ArrayList<BoxSizeBean.DevicesBean>>() {
        }.getType();
        List<BoxSizeBean.DevicesBean> devicesBeans = mGson.fromJson(devicesBeanJson, type);
        return devicesBeans;
    }

    private String getDeviceId() {
        List<BoxSizeBean.DevicesBean> devicesBeans = getDeviceBeans();
        if (devicesBeans != null && devicesBeans.size() > 0) {
            BoxSizeBean.DevicesBean devicesBean = devicesBeans.get(0);
            if (devicesBean != null) {
                return devicesBean.getDeviceId();
            }
        }
        return "";
    }
}
