package org.wh.engineer.utils;

import android.content.Context;

import com.google.gson.Gson;

import org.litepal.LitePal;
import org.wh.engineer.bean.LoginResultBean;
import org.wh.engineer.dbmodel.UserFeatureInfosBean;
import org.wh.engineer.dto.FingerLoginDto;
import org.wh.engineer.dto.IdCardLoginDto;
import org.wh.engineer.http.BaseResult;
import org.wh.engineer.http.NetRequest;
import org.wh.engineer.service.DeviceService;

import java.util.List;

import static org.wh.engineer.base.App.SYSTEMTYPE;
import static org.wh.engineer.base.App.mTitleConn;
import static org.wh.engineer.cont.Constants.FINGER_VERSION;
import static org.wh.engineer.cont.Constants.LOGIN_TYPE_FINGER3;
import static org.wh.engineer.cont.Constants.LOGIN_TYPE_IC;
import static org.wh.engineer.cont.Constants.SAVE_DEPT_CODE;
import static org.wh.engineer.cont.Constants.THING_CODE;

/**
 * created by wh on 2020/8/20
 * desc 登录并开门
 */
public class LoginAndOpenDoor {
    /*    public void login(int type, String value) {
              LoginVerifyManager.getConfigData(this, new LoginVerifyManager.ConfigCallback() {
                  @Override
                  public void getConfig(ConfigBean bean) {
                      if (LoginVerifyManager.canLogin(bean, type)) {
                          doLogin(type, value);
                      }
                  }
              });
          }
      */
    public static void doLogin(Context context,int configType, String value) {
        if (configType == 1) {//IC卡登录
            if (mTitleConn) {
                validateLoginIdCard(context,value);
            } else {
                uNNetvalidateLoginIdCard(context,value);
            }
        } else if (configType == 2) { //指纹登陆
            if (mTitleConn) {
                validateLoginFinger(context,value);
            } else {
                LogUtils.i(DeviceService.TAG, "指纹离线登录失败，请检查网络！");
            }
        }
    }

    /**
     * 断网后IC卡登陆
     *  @param context
     * @param idCard
     */
    public static void uNNetvalidateLoginIdCard(Context context, String idCard) {

        List<UserFeatureInfosBean> beans = LitePal.where("data = ? ", idCard)
                .find(UserFeatureInfosBean.class);
        if (beans != null && beans.size() > 0 && beans.get(0).getData().equals(idCard)) {
            LogUtils.i(DeviceService.TAG, "IC卡离线登录成功！");
           openDoor(true);
        } else {
            LogUtils.i(DeviceService.TAG, "IC卡登录失败，暂无登录信息！");
        }
    }

    public static void validateLoginIdCard(Context context, String idCard) {

        NetRequest.getInstance().validateLoginIdCard(
                idCardRequestParams(context,idCard),
                context, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i(DeviceService.TAG, "validateLoginIdCard  result   " + result);
                openDoor(verifyUser(result));
            }

            @Override
            public void onError(String result) {
                uNNetvalidateLoginIdCard(context, idCard);
            }
        });
    }

    public static void validateLoginFinger(Context context, String fingerFea) {

        NetRequest.getInstance().validateLoginFinger(
               fingerRequestParams(context, fingerFea),
                context, new BaseResult() {
            @Override
            public void onSucceed(String result) {
                LogUtils.i(DeviceService.TAG, "validateLoginFinger   result   " + result);
                openDoor(verifyUser(result));
            }

            @Override
            public void onError(String result) {
                LogUtils.i(DeviceService.TAG, "登录失败，请检查网络！");
            }
        });

    }

    public static void openDoor(boolean loginSuccess) {

        //DeviceOperator.openDoor();
        DeviceService.getInstance().openDoor(loginSuccess);
    }

    private static boolean verifyUser(String result){
        Gson gson = new Gson();
        LoginResultBean loginResultBean = gson.fromJson(result, LoginResultBean.class);
        if (loginResultBean != null && loginResultBean.isOperateSuccess()) {
            return true;
        }
        return false;
    }

    /**
     * 指纹网络登录请求参数
     */
    public static String fingerRequestParams(Context context, String fingerFea) {
        Gson mGson = new Gson();
        String thingCode = SPUtils.getString(context, THING_CODE);
        FingerLoginDto data = new FingerLoginDto();
        FingerLoginDto.UserFeatureInfoBean bean = new FingerLoginDto.UserFeatureInfoBean();
        bean.setData(fingerFea);
        data.setUserFeatureInfo(bean);
        data.setDeviceType(FINGER_VERSION);//3.0柜子需要传2   2.1传1
        data.setThingId(thingCode);
        data.setSystemType(SYSTEMTYPE);
        data.setLoginType(LOGIN_TYPE_FINGER3);
        data.setDeptId(SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
        LogUtils.i("Login", "THING_CODE validateLoginFinger  " + mGson.toJson(data));
        return mGson.toJson(data);
    }

    /**
     * IC,ID卡网络登录请求参数
     */
    public static String idCardRequestParams(Context context, String idCard) {
        Gson mGson = new Gson();
        IdCardLoginDto data = new IdCardLoginDto();
        IdCardLoginDto.UserFeatureInfoBean bean = new IdCardLoginDto.UserFeatureInfoBean();
        bean.setData(idCard);
        bean.setType("2");
        data.setUserFeatureInfo(bean);
        data.setThingId(SPUtils.getString(context, THING_CODE));
        data.setDeptId(SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
        data.setSystemType(SYSTEMTYPE);
        data.setLoginType(LOGIN_TYPE_IC);
        LogUtils.i("Login", "mGson.toJson(data)   " + mGson.toJson(data));
        return mGson.toJson(data);
    }

}
