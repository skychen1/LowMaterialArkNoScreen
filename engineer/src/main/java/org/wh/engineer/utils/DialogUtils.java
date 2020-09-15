package org.wh.engineer.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import org.wh.engineer.bean.Event;
import org.wh.engineer.views.RegisteDialog;
import org.wh.engineer.views.RegisteTextDialog;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/28 9:34
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.utils
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class DialogUtils {
    /**
     * 激活医院信息的
     * @param context
     * @param activity
     */
    public static void showRegisteDialog(final Context context, Activity activity) {

        RegisteDialog.Builder builder = new RegisteDialog.Builder(context, activity);
        builder.setLeft("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        builder.setOnSettingListener(new RegisteDialog.Builder.SettingListener() {
            @Override
            public void getDialogDate(
                    String deptName, String branchCode, String deptId, String storehouseCode, Dialog dialog) {
                LogUtils.i("RegisteDialog", "deptName  " + deptName);
                LogUtils.i("RegisteDialog", "branchCode  " + branchCode);
                LogUtils.i("RegisteDialog", "deptId  " + deptId);
                LogUtils.i("RegisteDialog", "storehouseCode  " + storehouseCode);
                EventBusUtils.postSticky(
                        new Event.dialogEvent(deptName, branchCode, deptId, storehouseCode,dialog));
            }
        });

        builder.create().show();
//        builder.create().getWindow().setGravity(Gravity.CENTER);
    }

    public static void showRegisteTextDialog(Activity context) {
        RegisteTextDialog.Builder builder = new RegisteTextDialog.Builder(context);
        builder.create().show();
    }
}
