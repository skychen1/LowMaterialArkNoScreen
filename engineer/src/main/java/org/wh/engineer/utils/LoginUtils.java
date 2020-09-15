package org.wh.engineer.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.text.format.Formatter;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import org.litepal.LitePal;
import org.wh.engineer.R;
import org.wh.engineer.bean.ConfigBean;
import org.wh.engineer.bean.HomeAuthorityMenuBean;
import org.wh.engineer.bean.VersionBean;
import org.wh.engineer.dbmodel.AccountVosBean;
import org.wh.engineer.dbmodel.ChildrenBean;
import org.wh.engineer.dbmodel.ChildrenBeanX;
import org.wh.engineer.devices.AllDeviceCallBack;
import org.wh.engineer.http.BaseResult;
import org.wh.engineer.http.NetRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.wh.engineer.base.App.SYSTEMTYPE;
import static org.wh.engineer.base.App.mTitleConn;
import static org.wh.engineer.cont.Constants.CONFIG_013;
import static org.wh.engineer.cont.Constants.KEY_ACCOUNT_ID;
import static org.wh.engineer.cont.Constants.KEY_ACCOUNT_NAME;
import static org.wh.engineer.cont.Constants.KEY_ACCOUNT_s_NAME;
import static org.wh.engineer.cont.Constants.KEY_USER_NAME;
import static org.wh.engineer.cont.Constants.KEY_USER_SEX;
import static org.wh.engineer.cont.Constants.SAVE_CONFIG_STRING;
import static org.wh.engineer.cont.Constants.SAVE_MENU_LEFT_TYPE;
import static org.wh.engineer.cont.Constants.SAVE_SEVER_IP;
import static org.wh.engineer.cont.Constants.THING_CODE;

/**
 * 项目名称：高值
 * 创建者：chenyanling
 * 创建时间：2019/6/12
 * 描述：登录相关操作
 * getConfigTrue=true 设备禁用 显示遮罩
 *  不可登录
 * getConfigTrue=false 设备可用 隐藏遮罩
 *  有网登录
 *  离线登录
 */
public class LoginUtils {
    /**
     * 获取配置项
     */
    public static void getConfigDate(Activity mContext, LoginCallback callback) {
        if (mTitleConn) {
            if (SPUtils.getString(UIUtils.getContext(), THING_CODE) != null) {
                NetRequest.getInstance().findThingConfigDate(mContext, new BaseResult() {
                    @Override
                    public void onSucceed(String result) {
                        SPUtils.putString(UIUtils.getContext(), SAVE_CONFIG_STRING, result);
                        ConfigBean configBean = new Gson().fromJson(result, ConfigBean.class);
                        List<ConfigBean.ConfigVosBean> tCstConfigVos = configBean.getConfigVos();
//                    if (tCstConfigVos.size()!=0){
                        loginEnjoin(tCstConfigVos, true, callback);
//                        getUpDateVer(mContext, tCstConfigVos, callback);
//                    }else {
//                        ToastUtils.showShortToast("请先在管理端对配置项进行设置，后进行登录！");
//                    }
                    }

                    @Override
                    public void onError(String result) {
                        if (SPUtils.getString(UIUtils.getContext(), SAVE_SEVER_IP) != null && result.equals("-1")) {
                            String string = SPUtils.getString(UIUtils.getContext(), SAVE_CONFIG_STRING);
                            LogUtils.i("LoginA", "string   " + string);
                            ConfigBean configBean = new Gson().fromJson(string, ConfigBean.class);
                            List<ConfigBean.ConfigVosBean> tCstConfigVos = configBean.getConfigVos();
                            callback.onLogin(true, !getConfigTrue(tCstConfigVos), false);
                        } else {
                            callback.onLogin(false, false, false);
                        }
                    }
                });
            } else {
                callback.onLogin(false, false, false);
            }
        }else {
            if (SPUtils.getString(UIUtils.getContext(), SAVE_SEVER_IP) != null ) {
                String string = SPUtils.getString(UIUtils.getContext(), SAVE_CONFIG_STRING);
                LogUtils.i("LoginA", "string   " + string);
                ConfigBean configBean = new Gson().fromJson(string, ConfigBean.class);
                if (string!=null){
                    List<ConfigBean.ConfigVosBean> tCstConfigVos = configBean.getConfigVos();
                    callback.onLogin(true, !getConfigTrue(tCstConfigVos), false);
                }else {
                    ToastUtils.showShortToast("请到管理端开启配置项！");
                }

            } else {
                callback.onLogin(false, false, false);
            }
        }
    }

    /**
     * 检测版本更新
     */
    public static void getUpDateVer(Context mContext) {
        if (mTitleConn) {
            NetRequest.getInstance().checkVer(mContext, new BaseResult() {
                @Override
                public void onSucceed(String result) {
                    VersionBean versionBean = new Gson().fromJson(result, VersionBean.class);
                    if (versionBean.isOperateSuccess()) {
                        // 本地版本号
                        String localVersion = PackageUtils.getVersionName(UIUtils.getContext());
                        // 网络版本
                        String netVersion = versionBean.getSystemVersion().getVersion();
                        if (netVersion != null && 1 == StringUtils.compareVersion(netVersion, localVersion)) {
                            String mDesc = versionBean.getSystemVersion().getDescription();
                            showUpdateDialog(mContext,mDesc,versionBean.getApkFtpUrl());
                        } else {
                            // 不需要更新
//                            loginEnjoin(tCstConfigVos, true);
                        }
                    } else {
//                        loginEnjoin(tCstConfigVos, true);
                    }
                }

                @Override
                public void onError(String result) {
//                    loginEnjoin(tCstConfigVos, true);
                }
            });
        }else {
//            loginEnjoin(tCstConfigVos, false, callback);
        }
    }


    /**
     * 离线状态：1.登录数据保存，2.获取本地缓存菜单数据，3.跳转首页
     */
    @NonNull
    public static List<HomeAuthorityMenuBean> setUnNetSPdate(String accountName, Gson mGson) {
        AccountVosBean beanss = LitePal.where("accountname = ? ", accountName)
                .findFirst(AccountVosBean.class);
        SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_s_NAME, beanss.getAccountId());
        SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_NAME, beanss.getAccountName());
        SPUtils.putString(UIUtils.getContext(), KEY_USER_NAME, beanss.getUserName());
        SPUtils.putString(UIUtils.getContext(), KEY_ACCOUNT_ID, beanss.getAccountId());
        SPUtils.putString(UIUtils.getContext(), KEY_USER_SEX, beanss.getSex());
        List<HomeAuthorityMenuBean> menusList = beanss.getMenusList(accountName);
        if (menusList.size() > 0 && menusList.get(0).getTitle().equals("耗材操作")) {
            List<ChildrenBeanX> childrenXbean = menusList.get(0).getChildrenXbean(accountName);
            menusList.get(0).setChildren(childrenXbean);
            if (childrenXbean.size() > 0 && childrenXbean.get(0).getTitle().equals("选择操作")) {
                List<ChildrenBean> childrenbean = childrenXbean.get(0).getChildrenbean(accountName);
                childrenXbean.get(0).setChildren(childrenbean);
            }
        }
        List<HomeAuthorityMenuBean> fromJson = new ArrayList<>();
        fromJson.addAll(menusList);
        SPUtils.putString(UIUtils.getContext(), SAVE_MENU_LEFT_TYPE, mGson.toJson(fromJson));
        return fromJson;
    }

    /**
     * 登录检测回调：检测1.是否能够登录 2.设备是否禁用 3.登录模式：有网还是离线
     */
    private static void loginEnjoin(List<ConfigBean.ConfigVosBean> tCstConfigVos, boolean hasNet, LoginCallback callback) {
        callback.onLogin(true, !getConfigTrue(tCstConfigVos), hasNet);
    }

    /**
     * 检测设备是否禁用
     */
    public static boolean getConfigTrue(List<ConfigBean.ConfigVosBean> tCstConfigVos) {
        if (tCstConfigVos==null||tCstConfigVos.size() == 0)
            return false;
        Iterator<ConfigBean.ConfigVosBean> iterator = tCstConfigVos.iterator();
        while (iterator.hasNext()){
            ConfigBean.ConfigVosBean next = iterator.next();
            if (next.getCode().equals(CONFIG_013)) {
                return true;
            }
        }
//        for (ConfigBean.ConfigVosBean s : tCstConfigVos) {
//            if (s.getCode().equals(CONFIG_013)) {
//                return true;
//            }
//        }
        return false;
    }

    /**
     * 检测epc过滤的问题
     * @param tCstConfigVos
     * @return
     */
    public static ConfigBean.ConfigVosBean getEpcFilte(List<ConfigBean.ConfigVosBean> tCstConfigVos, String CONFIG_x) {
        if (tCstConfigVos==null||tCstConfigVos.size() == 0)
            return null;
        Iterator<ConfigBean.ConfigVosBean> iterator = tCstConfigVos.iterator();
        while (iterator.hasNext()){
            ConfigBean.ConfigVosBean next = iterator.next();
            if (next.getCode().equals(CONFIG_x)) {
                return next;
            }
        }
//        for (ConfigBean.ConfigVosBean s : tCstConfigVos) {
//            if (s.getCode().equals(CONFIG_x)) {
//                return s;
//            }
//        }
        return null;
    }

    /**
     * apk更新的dialog
     */
    private static void showUpdateDialog(Context mContext, String mDesc,String downUrl) {
        Log.d("-------",downUrl);
        //更新
        downloadNewVersion(mContext,downUrl);//未下载就开始下载
    }

    /**
     * apk下载进度条
     */
    private static void downloadNewVersion(Context mContext,String downUrl) {
        // 1.显示进度的dialog
        ProgressDialog mDialog = new ProgressDialog(mContext, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.setCancelable(false);
        mDialog.setMax(100);
        mDialog.show();

        loadUpDataVersion(mContext, mDialog,downUrl);
    }

    /**
     * apk下载
     */
    private static void loadUpDataVersion(Context mContext, final ProgressDialog mDialog,String dowmUrl) {
        OkGo.<File>get(dowmUrl).tag(mContext)//
                .params("systemType", SYSTEMTYPE)
                .execute(new FileCallback(FileUtils.getDiskCacheDir(mContext), "RivamedPV.apk") {  //文件下载时，需要指定下载的文件目录和文件名
                    @Override
                    public void onSuccess(Response<File> response) {
                        mDialog.dismiss();
                        upActivity(mContext, response.body());
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        String downloadLength = Formatter.formatFileSize(mContext, progress.currentSize);
                        String totalLength = Formatter.formatFileSize(mContext, progress.totalSize);
                        mDialog.setProgress((int) (progress.fraction * 100));
                        mDialog.setProgressNumberFormat(downloadLength + "/" + totalLength);
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        Log.i("responses",""+response.message());
                        Log.i("responses",""+response.code());
                        Log.i("responses",""+response.body());
                        ToastUtils.showShort(R.string.connection_fails);
                        mDialog.dismiss();
//                        loginEnjoin(tCstConfigVos, true);
                    }
                });

    }

    /**
     * apk安装
     */
    private static void upActivity(Context mContext, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0+以上版本
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            LogUtils.i("Login", "apkUri " + Uri.fromFile(file));
        }
        mContext.startActivity(intent);
//        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /* 定义一个倒计时的内部类 */
    public static class LightTimeCount extends CountDownTimer {

        public LightTimeCount(
              long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {// 计时完毕时触发
            Log.i("onDoorState", "onFinish     " );
            AllDeviceCallBack.getInstance().closeLightStart();
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            Log.i("onDoorState", "millisUntilFinished     " + millisUntilFinished);
        }
    }
    /**
     * 登录检测回调
     */
    public interface LoginCallback {
        /**
         * @param canLogin  是否能登录：设备未注册激活或离线时没有本地数据，则无法登录
         * @param canDevice 设备是否禁用 通过配置项判断
         * @param hasNet    登录模式：有网还是离线
         */
        void onLogin(boolean canLogin, boolean canDevice, boolean hasNet);
    }

    /**
     * 权限菜单检测回调
     */
    public interface MenuCallback {
        /**
         * 这里是已经登录成功了之后的操作
         *
         * @param hasMenu 是否有权限菜单：有权限菜单，则跳转至首页；否则，提示并重新走登录流程
         */
        void onMenu(boolean hasMenu);
    }
}
