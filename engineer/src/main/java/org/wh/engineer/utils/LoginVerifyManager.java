package org.wh.engineer.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.wh.engineer.bean.ConfigBean;
import org.wh.engineer.http.BaseResult;
import org.wh.engineer.http.NetRequest;

import static org.wh.engineer.base.App.mTitleConn;
import static org.wh.engineer.cont.Constants.SAVE_CONFIG_STRING;
import static org.wh.engineer.cont.Constants.SAVE_SEVER_IP;
import static org.wh.engineer.cont.Constants.THING_CODE;
import static org.wh.engineer.service.DeviceService.TAG;

/**
 * created by wh on 2020/8/7
 * desc
 */
public class LoginVerifyManager {

    /**
     * @param configType 0:默认，1：IC卡 2：指纹
     * @param value      不同类型下获取的值 指纹、卡号
     *                   获取配置数据
     */
    public static void getConfigData(Context context, ConfigCallback callback) {
        if (SPUtils.getString(UIUtils.getContext(), THING_CODE) == null
                || TextUtils.isEmpty(SPUtils.getString(UIUtils.getContext(), THING_CODE))) {
            LogUtils.i(TAG, "请先预注册设备");
            return;
        }
        if (!mTitleConn) {
            if (SPUtils.getString(context, SAVE_SEVER_IP) != null) {
                String string = SPUtils.getString(UIUtils.getContext(), SAVE_CONFIG_STRING);
                callConfigBean(string, callback);
            } else {
                LogUtils.i(TAG, "未填写服务器地址");
            }
        } else {
            NetRequest.getInstance().findThingConfigDate(UIUtils.getContext(), new BaseResult() {
                @Override
                public void onSucceed(String result) {

                    LogUtils.i(TAG, "findThingConfigDate result   " + result);
                    SPUtils.putString(UIUtils.getContext(), SAVE_CONFIG_STRING, result);
                    callConfigBean(result, callback);
                }

                @Override
                public void onError(String result) {
                    LogUtils.i(TAG, "findThingConfigDate failed result:   " + result);
                    if (SPUtils.getString(context, SAVE_SEVER_IP) != null && result.equals("-1")) {
                        String string = SPUtils.getString(UIUtils.getContext(), SAVE_CONFIG_STRING);
                        callConfigBean(string, callback);
                    }else {
                        LogUtils.i(TAG, "SPUtils.getString(context, SAVE_SEVER_IP) == null || result.equals(\"-1\")");
                    }
                }
            });
        }
    }

    /**
     * 得到配置项后的操作
     *
     * @param result
     */
    private static void callConfigBean(String result, ConfigCallback callback) {
        Gson mGson = new Gson();
        ConfigBean configBean = mGson.fromJson(result, ConfigBean.class);
        if (configBean == null) {
            LogUtils.i(TAG, "请先进行设备配置");
        }else{
            if (callback != null) {
                callback.getConfig(configBean);
            }
        }
    }

    public static boolean canLogin(ConfigBean bean, int configType) {
        if (LoginUtils.getConfigTrue(bean.getConfigVos())) { //设备被禁用
            if (configType == 0) {
                LogUtils.i(TAG, "账号密码无效，请使用其他登录方式");
            } else if (configType == 1) {
                LogUtils.i(TAG, "IC卡登录无效，请使用指纹登录");
            } else if (configType == 2) {
                LogUtils.i(TAG, "指纹登录无效，请使用IC卡登录");
            }
            return false;
        } else {
            return true;
        }

    }

    /**
     * 是否禁止使用
     *
     * @param configType
     * @param loginType
     */
/*    private void loginEnjoin(
            boolean canDevice, int configType, String loginType) {
        if (!canDevice) {//禁止
            if (configType == 0) {//禁止密码登录

            } else if (configType == 1) {//禁止IC卡登录
                LogUtils.i(TAG, "IC卡登录无效，请使用指纹登录");
            } else if (configType == 2) {//禁止指纹登录
                LogUtils.i(TAG, "指纹登录无效，请使用IC卡登录");
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
                    LogUtils.i(TAG, "指纹离线登录失败，请检查网络！");
                }
            }
        }
    }*/

    public interface ConfigCallback {
        void getConfig(ConfigBean bean);
    }
}
