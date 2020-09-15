package org.wh.engineer.devices;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruihua.libconsumables.ConsumableManager;

import org.litepal.LitePal;
import org.wh.engineer.bean.BoxSizeBean;
import org.wh.engineer.dbmodel.BoxIdBean;
import org.wh.engineer.utils.DevicesUtils;
import org.wh.engineer.utils.LogUtils;
import org.wh.engineer.utils.SPUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.wh.engineer.base.App.SYSTEMTYPE;
import static org.wh.engineer.base.App.mAppContext;
import static org.wh.engineer.cont.Constants.BOX_SIZE_DATE;
import static org.wh.engineer.cont.Constants.CONSUMABLE_TYPE;
import static org.wh.engineer.service.DeviceService.TAG;

/**
 * created by wh on 2020/8/7
 * desc 设备操作类
 */
public class DeviceOperator {

    public static void openDoor() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                openDoorImpl();
            }
        }).start();
    }

    public static boolean openDoorImpl() {

        List<String> mBomDoorDeviceIdList = DevicesUtils.getBomDeviceId(SYSTEMTYPE);//获取主控板下的所有门锁设备
        if (mBomDoorDeviceIdList == null || mBomDoorDeviceIdList.size() == 0) { //主控板没有检测到门锁设备
            LogUtils.i(TAG, "控制板未检测到门锁设备");
            return false;
        }
        List<BoxSizeBean.DevicesBean> cabinets = getDeviceBeans(); //获取绑定的所有大柜体（上柜，单柜，下柜，目前只有一个（单柜））
        if (cabinets == null || cabinets.size() == 0) { //主控板没有检测到门锁设备
            LogUtils.i(TAG, "控制板未检测到门锁设备");
            return false;
        }
        for (int i = 0; i < cabinets.size(); i++) {
            String cabinetId = cabinets.get(i).getDeviceId();
            List<BoxIdBean> boxIdBeans = LitePal.where("box_id = ? and name = ?", cabinetId,
                    CONSUMABLE_TYPE).find(BoxIdBean.class);//获取单个柜体下的所有小柜子（一个小柜子，一个门锁）
            if (boxIdBeans == null || boxIdBeans.size() == 0) {
                LogUtils.i(TAG, cabinetId + " : 该设备下没有可开的柜体");
                return false;
            }
            for (int index = 0; index < boxIdBeans.size(); index++) {
                String boxDeviceId = boxIdBeans.get(index).getDevice_id();
                if (mBomDoorDeviceIdList.contains(boxDeviceId)) { //如果控制板检测的门锁信息包含当前待开柜的锁
                   int result= ConsumableManager.getManager().openDoor(boxDeviceId, 0);//上柜或者单柜   J24
                  /* if (result==0){
                       openSingleLightStart(boxDeviceId);
                   }*/
                    //ConsumableManager.getManager().openDoor(boxDeviceId, 1);//J25

                    return result==0;
                } else {
                    LogUtils.i(TAG, boxDeviceId + " : 待开门锁设备在设备列表中不存在");
                }
            }
        }
        return false;
    }

    /**
     * 获取柜体列表(单柜，上柜，下柜等组合)
     */
    public static List<BoxSizeBean.DevicesBean> getDeviceBeans() {
        Gson mGson = new Gson();
        String devicesBeanJson = SPUtils.getString(mAppContext, BOX_SIZE_DATE);
        Type type = new TypeToken<ArrayList<BoxSizeBean.DevicesBean>>() {
        }.getType();
        List<BoxSizeBean.DevicesBean> devicesBeans = mGson.fromJson(devicesBeanJson, type);
        return devicesBeans;
    }

    /**
     * 开灯
     */
    public static void openLightStart() {
        List<String> bomDeviceId = DevicesUtils.getBomDeviceId(SYSTEMTYPE);
        for (String s : bomDeviceId) {
            int i = ConsumableManager.getManager().openLight(s);
            Log.i("appSatus", "openLight.()+  " + i);
        }
    }
    /**
     * 开灯
     */
    public static void openSingleLightStart(String bomDeviceId) {
        int i = ConsumableManager.getManager().openLight(bomDeviceId);
        Log.i(TAG, "openSingleLightStart.()+  " + i);
    }

    /**
     * 关灯
     */
    public static void closeLightStart() {
        List<String> bomDeviceId = DevicesUtils.getBomDeviceId(SYSTEMTYPE);
        for (String s : bomDeviceId) {
            int i = ConsumableManager.getManager().closeLight(s);
            Log.i("appSatus", "closeLight.()+  " + i);
        }
    }
    /**
     * 关灯
     */
    public static void closeSingleLightStart(String bomDeviceId) {
        int i = ConsumableManager.getManager().closeLight(bomDeviceId);
        Log.i(TAG, "closeSingleLightStart.()+  " + i);
    }

}
