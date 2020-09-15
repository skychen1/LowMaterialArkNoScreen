package org.wh.engineer.devices;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.rivamed.FingerCallback;
import com.rivamed.FingerManager;
import com.rivamed.libidcard.IdCardCallBack;
import com.rivamed.libidcard.IdCardManager;
import com.ruihua.libconsumables.ConsumableManager;
import com.ruihua.libconsumables.callback.ConsumableCallBack;
import com.ruihua.reader.ReaderManager;

import org.litepal.LitePal;
import org.wh.engineer.base.App;
import org.wh.engineer.bean.BoxSizeBean;
import org.wh.engineer.bean.Event;
import org.wh.engineer.bean.StartVideoBean;
import org.wh.engineer.dbmodel.BoxIdBean;
import org.wh.engineer.http.BaseResult;
import org.wh.engineer.http.NetRequest;
import org.wh.engineer.service.ScanService;
import org.wh.engineer.utils.DevicesUtils;
import org.wh.engineer.utils.EventBusUtils;
import org.wh.engineer.utils.LogUtils;
import org.wh.engineer.utils.UIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import cn.rivamed.Eth002Manager;
import cn.rivamed.callback.Eth002CallBack;

import static org.wh.engineer.base.App.READER_TIME;
import static org.wh.engineer.base.App.SYSTEMTYPE;
import static org.wh.engineer.base.App.mAppContext;
import static org.wh.engineer.cont.Constants.CONFIG_059;
import static org.wh.engineer.cont.Constants.CONSUMABLE_TYPE;
import static org.wh.engineer.cont.Constants.READER_TYPE;
import static org.wh.engineer.cont.Constants.SYSTEMTYPES_3;
import static org.wh.engineer.utils.DevicesUtils.getDoorStatus;
import static org.wh.engineer.utils.LyDateUtils.stopScan;
import static org.wh.engineer.utils.StringUtils.getStringType;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      LiangDanMing
 * 创建时间:    2018/8/22 12:17
 * 描述:        硬件回调
 * 包名:        high.rivamed.myapplication.devices
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class AllDeviceCallBack {

    private static final String TAG = "AllDeviceCallBack";
    // 设置本类为单例模式
    private static AllDeviceCallBack instances;
    public static ArrayList<String> mEthDeviceIdBack;
    public static ArrayList<String> mEthDeviceIdBack2;
    public static ArrayList<String> mEthDeviceIdBack3;
    public static List<String> mReaderIdList;
    public static List<String> mReaderDeviceId;
    public static List<String> mBomDoorDeviceIdList;
    public static Gson mGson;
    private static Map<String, String> sMap;
    private Timer mTimer;
    private boolean mForceinType = false;
    public boolean mOpenDoorType = true;

    public static AllDeviceCallBack getInstance() {
        //	sReaderType = UIUtils.getConfigReaderType(UIUtils.getContext(), CONFIG_000);
        if (instances == null) {
            synchronized (NetRequest.class) {
                if (instances == null) {
                    mGson = new Gson();
                    instances = new AllDeviceCallBack();
                    mReaderDeviceId = DevicesUtils.getReaderDeviceId();
                    Log.i(TAG, "mReaderDeviceId    " + mReaderDeviceId.size());
                    mBomDoorDeviceIdList = DevicesUtils.getBomDeviceId(SYSTEMTYPE);
                    mEthDeviceIdBack = new ArrayList<>();
                    mEthDeviceIdBack3 = new ArrayList<>();
                    mEthDeviceIdBack2 = new ArrayList<>();
                    sMap = new HashMap<>();
                }
            }
        }
        return instances;
    }

    public void setReaderList(String mDeviceCode) {
        if (mDeviceCode == null) {
            mReaderDeviceId = DevicesUtils.getReaderDeviceId();
            for (int i = 0; i < mReaderDeviceId.size(); i++) {
                String DeviceId = (String) mReaderDeviceId.get(i);
                mReaderIdList.add(DeviceId);
            }
        } else {
            List<BoxIdBean> boxIdBeans = LitePal.where("box_id = ? and name = ?", mDeviceCode,
                    READER_TYPE).find(BoxIdBean.class);

            for (BoxIdBean boxIdBean : boxIdBeans) {
                String device_id = boxIdBean.getDevice_id();
                LogUtils.i(TAG, "device_id   " + device_id);
                LogUtils.i(TAG, "mReaderDeviceId.size   " + mReaderDeviceId.size());
                for (int i = 0; i < mReaderDeviceId.size(); i++) {
                    LogUtils.i(TAG, "mReaderDeviceId.get(i)   " + mReaderDeviceId.get(i));
                    if (mReaderDeviceId.get(i).equals(device_id)) {
                        mReaderIdList.add(device_id);
                        LogUtils.i(TAG, " mBomDoorDeviceIdList.size   " + mBomDoorDeviceIdList.size());
                    }
                }
            }
        }
    }

    /**
     * 开柜,开柜子获得reader的标识
     *
     * @param deviceId
     * @param mTbaseDevices
     */
    public void openDoor(
            String deviceId, List<BoxSizeBean.DevicesBean> mTbaseDevices) {
        mBomDoorDeviceIdList = DevicesUtils.getBomDeviceId(SYSTEMTYPE);
        mReaderDeviceId = DevicesUtils.getReaderDeviceId();
        stopScan();
        if (mReaderIdList != null) {
            mReaderIdList.clear();
        } else {
            mReaderIdList = new ArrayList<>();
        }
        LogUtils.i(TAG, " mBomDoorDeviceIdList.size   " + mBomDoorDeviceIdList.size());
        setReaderList(deviceId);

        if (mTbaseDevices.size() > 1 && mBomDoorDeviceIdList.size() >= 1) {
            if (deviceId.equals("")) {//第一个为全部开柜
                //		initCallBack();
                for (int i = 0; i < mBomDoorDeviceIdList.size(); i++) {
                    LogUtils.i(TAG,
                            " mBomDoorDeviceIdList.get(i)   " + (String) mBomDoorDeviceIdList.get(i));
                    List<BoxIdBean> boxIdBeans = LitePal.where("device_id = ? and name = ?",
                            mBomDoorDeviceIdList.get(i),
                            CONSUMABLE_TYPE).find(BoxIdBean.class);
                    if (SYSTEMTYPE.equals(SYSTEMTYPES_3)) {
                        if (boxIdBeans.size() > 1) {
                            ConsumableManager.getManager().openDoor((String) mBomDoorDeviceIdList.get(i));
                            Log.i("ssffff", "开门全部ddddd");
                        } else {
                            ConsumableManager.getManager().openDoor((String) mBomDoorDeviceIdList.get(i), 0);
                            Log.i("ssffff", "开门1个");
                        }
                    } else {
                        Eth002Manager.getEth002Manager().openDoor((String) mBomDoorDeviceIdList.get(i));
                    }
                }
            } else {
                queryDoorId(deviceId);
            }
        } else {
            queryDoorId(deviceId);
        }

    }

    /**
     * 获取设备门锁ID，并开柜
     */
    private void queryDoorId(String mDeviceCode) {
        Log.i("ffaer", " mDeviceCode  " + mDeviceCode);
        LogUtils.i(TAG, " mBomDoorDeviceIdList.size  " + mBomDoorDeviceIdList.size());
        for (int i = 0; i < mBomDoorDeviceIdList.size(); i++) {
            List<BoxIdBean> boxIdBeans = LitePal.where("box_id = ? and name = ?", mDeviceCode,
                    CONSUMABLE_TYPE).find(BoxIdBean.class);
            Log.i("ffaer", " boxIdBeans  " + boxIdBeans);
            for (BoxIdBean boxIdBean : boxIdBeans) {
                String device_id = boxIdBean.getDevice_id();
                Log.i("ffaer", " device_id  " + device_id);
                String id = mBomDoorDeviceIdList.get(i);
                if (device_id.equals(id)) {
                    Log.i("ffaer", " mBomDoorDeviceIdList.get(i)  " + id);
                    if (boxIdBean.getCabinetType() != null) {
                        if (boxIdBean.getCabinetType().equals("1") ||
                                boxIdBean.getCabinetType().equals("0")) {//上柜或者单柜   J24
                            if (SYSTEMTYPE.equals(SYSTEMTYPES_3)) {
                                ConsumableManager.getManager().openDoor(id, 0);
                            } else {
                                Eth002Manager.getEth002Manager().openDoor(mBomDoorDeviceIdList.get(i));
                            }
                        } else if (boxIdBean.getCabinetType()
                                .equals("2")) {                                                  //J25
                            ConsumableManager.getManager().openDoor(id, 1);
                        }
                    }
                }
            }
        }
    }

    /**
     * 开灯
     */
    public void openLightStart() {
        List<String> bomDeviceId = DevicesUtils.getBomDeviceId(SYSTEMTYPE);
        for (String s : bomDeviceId) {
            int i = ConsumableManager.getManager().openLight(s);
            Log.i("appSatus", "openLight.()+  " + i);
        }
    }

    /**
     * 关灯
     */
    public void closeLightStart() {
        List<String> bomDeviceId = DevicesUtils.getBomDeviceId(SYSTEMTYPE);
        for (String s : bomDeviceId) {
            int i = ConsumableManager.getManager().closeLight(s);
            Log.i("appSatus", "closeLight.()+  " + i);
        }
    }

    /**
     * 检测灯
     */
    public void StateLightStart() {
        List<String> bomDeviceId = DevicesUtils.getBomDeviceId(SYSTEMTYPE);
        for (String s : bomDeviceId) {
            ConsumableManager.getManager().checkLightState(s);
        }
    }

    /**
     * 正式开始扫描
     *
     * @param deviceIndentify
     */
    public void startScan(String deviceIndentify, int which) {
        LogUtils.i(TAG, "deviceIndentify    " + deviceIndentify + "    ");
        String cabinetType = "";
        List<BoxIdBean> boxIdBeans = LitePal.where("device_id = ? and name = ?", deviceIndentify,
                CONSUMABLE_TYPE).find(BoxIdBean.class);
        if (boxIdBeans.size() == 2) {
            if (which == 0) {
                cabinetType = "1";
            } else if (which == 1) {
                cabinetType = "2";
            }
        } else if (boxIdBeans.size() == 1) {
            cabinetType = "0";
        }

        for (BoxIdBean boxIdBean : boxIdBeans) {
            if (cabinetType.equals(boxIdBean.getCabinetType())) {
                String box_id = boxIdBean.getBox_id();
                BoxIdBean deviceBean = LitePal.where("box_id = ? and name = ?", box_id, READER_TYPE)
                        .findFirst(BoxIdBean.class);
                String device_id = deviceBean.getDevice_id();
                int i = ReaderManager.getManager().startScan(device_id, READER_TIME);
                if (i == 1) {
                    ReaderManager.getManager().restDevice(device_id);
                    mReaderDeviceId = DevicesUtils.getReaderDeviceId();
                    ReaderManager.getManager().startScan(device_id, READER_TIME);
                }

                LogUtils.i(TAG, "开始扫描了状态    " + i + "    " + device_id);

            }
        }

    }

    public void initCallBack() {
        if (SYSTEMTYPE.equals(SYSTEMTYPES_3)) {
            initBom();
            initFinger();
            initIC();
        } else {
            initEth002();
        }
    }

    /**
     * IC卡
     */
    private void initIC() {
        IdCardManager.getIdCardManager().registerCallBack(new IdCardCallBack() {
            @Override
            public void onConnectState(String deviceId, boolean isConnect) {
                Log.i("appSatus", "initIC   " + isConnect);
                if (isConnect) {
                    IdCardManager.getIdCardManager().startReadCard(deviceId);
                }
            }

            @Override
            public void onReceiveCardNum(String cardNo) {
                int appSatus = App.getInstance().getAppSatus();
                Log.i("appSatus", "appSatus   " + appSatus);
                if (appSatus != 3) {
                    if (!UIUtils.isFastDoubleClick()) {
                        //mConfigType = 1;//IC卡
                        EventBusUtils.post(new Event.EventICAndFinger(cardNo, 1));
                    }
                } else {
                    Intent intent = new Intent(mAppContext, ScanService.class);
                    mAppContext.stopService(intent);
                }
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
                Log.i("appSatus", "isConnectdeviceId     " + isConnect + deviceId);
                if (isConnect) {
                    FingerManager.getManager().startReadFinger(deviceId);
                }
            }

            @Override
            public void onFingerFeatures(String deviceId, String features) {
                int appSatus = App.getInstance().getAppSatus();
                Log.i("appSatus", "appSatus   " + appSatus);
                if (appSatus != 3) {
                    if (!UIUtils.isFastDoubleClick()) {
                        //mConfigType = 2;//指纹登录
                        EventBusUtils.post(new Event.EventICAndFinger(features, 2));
                    }
                } else {
                    Intent intent = new Intent(mAppContext, ScanService.class);
                    mAppContext.stopService(intent);
                }
            }

            @Override
            public void onRegisterResult(
                    String deviceId, int code, String features, List<String> fingerPicPath, String msg) {
                Log.i("appSatus", "deviceId   " + deviceId);
                EventBusUtils.post(new Event.EventFingerRegEnter(code, features, msg));
                //		appendLog("设备：：" + deviceId + "注册结果码是：：" + code + "\n>>>>>>>" + msg
                //			    + "\n指纹照片数据：：" + (fingerPicPath == null ? 0 : fingerPicPath.size()) + "\n特征值是：：：" + features);
                //收到注册结果标识注册完成就开启读取

            }

            @Override
            public void onFingerUp(String deviceId) {
                Log.i("appSatus", "请抬起手指   " + deviceId);
                //		appendLog("设备：：" + deviceId + "请抬起手指：");
                EventBusUtils.post(new Event.EventFingerReg(0));
            }

            @Override
            public void onRegisterTimeLeft(String deviceId, long time) {
                Log.i("appSatus", "请抬起手指   " + time);
                //		setLog("设备：：" + deviceId + "剩余注册时间：：" + time + "\n");
                EventBusUtils.post(new Event.EventFingerTime(time));
                EventBusUtils.post(new Event.EventFingerReg(1));
            }
        });
    }

    /**
     * 主板回调
     */
    private void initBom() {
        ConsumableManager.getManager().registerCallback(new ConsumableCallBack() {
            @Override
            public void onConnectState(String deviceId, boolean isConnect) {

            }

            @Override
            public void onOpenDoor(String deviceId, int which, boolean isSuccess) {
                //		appendLog("设备：：" + deviceId + "开锁：：：" + which + ":::的结果是：：：" + isSuccess);
                Log.i("onDoorState",
                        "onOpenDoor 开锁：：" + deviceId + "状态：：：" + which + ":::的结果是：：：" + isSuccess);
//		String string = SPUtils.getString(mAppContext, THING_MODEL);
//		if (string.equals("1")) {//嵌入式
//		   //强开的可能出现第二次开门 .mForceinType=true标识强开
//		   if (!mForceinType) {
//			if (isSuccess && which == 0) {
//			   Log.i("outtccc", "柜门已开    ");
//			   EventBusUtils.post(
//				   new Event.PopupEvent(true, mOpenDoorType, "柜门已开", deviceId + which));
//			   if (UIUtils.getConfigType(mAppContext, CONFIG_059) && mOpenDoorType) {
//				startVideo("opendoor", deviceId);
//			   }
//			   mOpenDoorType = true;
//			   if (mEthDeviceIdBack.size() > 0) {
//				if (!getStringType(mEthDeviceIdBack, deviceId + which)) {
//				   mEthDeviceIdBack.add(deviceId + which);
//				}
//			   } else {
//				mEthDeviceIdBack.add(deviceId + which);
//			   }
//			   mEthDeviceIdBack2.clear();
//			   mEthDeviceIdBack2.addAll(mEthDeviceIdBack);
//			   mEthDeviceIdBack3.addAll(mEthDeviceIdBack);
//			}
//		   } else {
//			Log.i("onDoorState", "我是强开后未检测到门状态，第2次   开门    ");
//		   }
//		} else {
                if (isSuccess) {
                    Log.i("outtccc", "柜门已开    ");

                    EventBusUtils.post(new Event.PopupEvent(true, true, "柜门已开", deviceId + which));
                    if (UIUtils.getConfigType(mAppContext, CONFIG_059)) {
                        startVideo("opendoor", deviceId);
                    }
                }
                if (mEthDeviceIdBack.size() > 0) {
                    if (!getStringType(mEthDeviceIdBack, deviceId + which)) {
                        mEthDeviceIdBack.add(deviceId + which);
                    }
                } else {
                    mEthDeviceIdBack.add(deviceId + which);
                }
                mEthDeviceIdBack2.clear();
                mEthDeviceIdBack2.addAll(mEthDeviceIdBack);
                mEthDeviceIdBack3.addAll(mEthDeviceIdBack);
                getDoorStatus(SYSTEMTYPE);
//		}

            }

            @Override
            public void onCloseDoor(String deviceId, int which, boolean isSuccess) {
                Log.i("onDoorState",
                        "onCloseDoor设备：：" + deviceId + "状态：：：" + which + ":::的结果是：：：" + isSuccess);
//		String string = SPUtils.getString(mAppContext, THING_MODEL);
//		if (string.equals("1")) {//嵌入式
//		   if (which == 0) {
//			if (mEthDeviceIdBack2.size() == 0 && mEthDeviceIdBack.size() == 0) {
//			   //todo 我是强开
//			   Log.i("onDoorState", "我是强开的第一次 关门，开始检测右门门状态    ");
//			   mForceinType = true;
//			}
//			int i = ConsumableManager.getManager().checkDoorState(deviceId, 10);
//			Log.i("onDoorState", "嵌入式关门开始检测  ： ：  " + i);
//		   }

//		} else {//标准柜体
                EventBusUtils.post(new Event.PopupEvent(false, "关闭", deviceId + which));
                LogUtils.i("onDoorState", "onDoorClosed  " + mEthDeviceIdBack2.size() + "   " +
                        mEthDeviceIdBack.size());
                if (mEthDeviceIdBack2.size() == 0 && mEthDeviceIdBack.size() == 0) {//强开
                    if (UIUtils.getConfigType(mAppContext, CONFIG_059)) {
                        startVideo("forcein", deviceId);
                    }
                    startScan(deviceId, which);
                } else {//正常开门
                    for (int i = 0; i < mEthDeviceIdBack2.size(); i++) {
                        if (mEthDeviceIdBack2.get(i).equals(deviceId + which)) {
                            mEthDeviceIdBack2.remove(i);
                            if (UIUtils.getConfigType(mAppContext, CONFIG_059)) {
                                stopVideo(deviceId);
                            }
                        }
                    }
                }
                getDoorStatus(SYSTEMTYPE);
//		}
            }

            @Override
            public void onDoorState(String deviceId, int which, boolean state) {
//		String string = SPUtils.getString(mAppContext, THING_MODEL);
                Log.i("onDoorState",
                        "onDoorState设备：：" + deviceId + "检测锁的状态：：：" + which + ":::的结果是：：：" + state);
//		if (string.equals(SYSTEMTYPES_3)) {//标准柜子
                EventBusUtils.postSticky(new Event.EventDoorStatus(deviceId + which, state));
//		}
//		else if (string.equals("1")) {//嵌入式
//		   if (which == 10) {
//			if (state) {
//			   mOpenDoorType = false;
//			   int i = ConsumableManager.getManager().openDoor(deviceId, 0);
//			   Log.i("onDoorState", "右门门状态是开，强开的第2次 开门    " + i);
//			} else {
//			   //			   EventBusUtils.post(new Event.PopupEvent(false, "关闭", deviceId+which));
//			   int i1 = ConsumableManager.getManager().closeLight(deviceId, 11);
//			   Log.i("onDoorState", "closeLight 关电磁锁：：    " + i1);
//			   getDoorStatus();
//			}
//		   } else if (which == 0) {
//			if (!state) {
//			   EventBusUtils.post(new Event.PopupEvent(false, "关闭", deviceId + which));
//			   if (mEthDeviceIdBack2.size() == 0 && mEthDeviceIdBack.size() == 0) {//强开
//				Log.i("onDoorState", "我是强开的关闭逻辑  开始扫描了    ");
//				if (UIUtils.getConfigType(mAppContext, CONFIG_059)) {
//				   startVideo("forcein", deviceId);
//				}
//				startScan(deviceId, which);
//			   } else {//正常开门
//				for (int i = 0; i < mEthDeviceIdBack2.size(); i++) {
//				   if (mEthDeviceIdBack2.get(i).equals(deviceId + which)) {
//					mEthDeviceIdBack2.remove(i);
//					if (UIUtils.getConfigType(mAppContext, CONFIG_059)) {
//					   stopVideo(deviceId);
//					}
//				   }
//				}
//			   }
//			}
//			EventBusUtils.post(new Event.EventDoorStatus(deviceId + which, state));
//		   }
//		}
            }

            @Override
            public void onOpenLight(String deviceId, int which, boolean isSuccess) {
                Log.i("onDoorState", "设备：：" + deviceId + "开灯：：：" + which + ":::的结果是：：：" + isSuccess);
            }

            @Override
            public void onCloseLight(String deviceId, int which, boolean isSuccess) {
                Log.i("onDoorState", "设备：：" + deviceId + "关灯：：：" + which + ":::的结果是：：：" + isSuccess);
            }

            @Override
            public void onLightState(String deviceId, int which, boolean state) {
                if (state) {
                    EventBusUtils.post(new Event.EventLightCloss(true));
                }
                Log.i("onDoorState", "设备：：" + deviceId + "检测灯：：：" + which + ":::的结果是：：：" + state);
            }

            @Override
            public void onFirmwareVersion(String deviceId, String version) {
                //		appendLog("设备：：" + deviceId + "获取到版本号：：：" + version);
            }

            @Override
            public void onNeedUpdateFile(String deviceId) {
                //		appendLog("设备：：" + deviceId + "需要升级文件！！！！");
                //		String filePath = FilesUtils.getFilePath(mContext) + "/boot.bin";
                //		com.rivamed.libdevicesbase.utils.LogUtils.e("文件路径是：：：" + filePath);
                //		int i = ConsumableManager.getManager().sendUpdateFile(deviceId, filePath);
                //		if (i != FunctionCode.SUCCESS) {
                //		   com.rivamed.libdevicesbase.utils.LogUtils.e("发送升级文件出错" + i);
                //		}
            }

            @Override
            public void onUpdateProgress(String deviceId, int percent) {
                //		appendLog("设备：：" + deviceId + "已升级的进度：：：" + percent + "%");
            }

            @Override
            public void onUpdateResult(String deviceId, boolean isSuccess) {
                //		appendLog("设备：：" + deviceId + "升级" + (isSuccess ? "成功！！！！！" : "失败！！！！"));
            }
        });
    }

    private void stopVideo(String deviceIndentify) {
        String number = null;
        for (Map.Entry<String, String> entry : sMap.entrySet()) {
            if (entry.getKey().equals(deviceIndentify)) {
                number = entry.getValue();
            }
        }
        if (number != null) {
            NetRequest.getInstance().stopRecordVideo(number, this, new BaseResult() {
                @Override
                public void onSucceed(String result) {
                    Log.i("videod", " sMap1       " + sMap.size());
                    sMap.entrySet().remove(deviceIndentify);
                    Log.i("videod", " sMap2       " + sMap.size());
                }

                @Override
                public void onError(String result) {

                }
            });
        }

    }

    /**
     * 开始录像  opendoor,正常开门  ； forcein 强开
     */
    private void startVideo(String type, String deviceId) {
        BoxIdBean first = LitePal.where("device_id = ?", deviceId).findFirst(BoxIdBean.class);
        String box_id = first.getBox_id();
        if (type.equals("opendoor")) {
            NetRequest.getInstance().startRecordVideo(box_id, this, new BaseResult() {
                @Override
                public void onSucceed(String result) {
                    StartVideoBean startVideoBean = mGson.fromJson(result, StartVideoBean.class);
                    if (startVideoBean.isOperateSuccess()) {
                        sMap.put(deviceId, startVideoBean.getBusinessNo());
                    }
                }

                @Override
                public void onError(String result) {

                }
            });
        } else {
            NetRequest.getInstance().startForceRecordVideo(box_id, this, new BaseResult() {
                @Override
                public void onSucceed(String result) {

                }

                @Override
                public void onError(String result) {

                }
            });
        }
    }

    public void initEth002() {
        Eth002Manager.getEth002Manager().registerCallBack(new Eth002CallBack() {
            @Override
            public void onConnectState(String deviceId, boolean isConnect) {

            }

            @Override
            public void onFingerFea(String deviceId, String fingerFea) {
                int appSatus = App.getInstance().getAppSatus();
                Log.i("appSatus", "appSatus   " + appSatus);
                if (appSatus != 3) {
                    if (!UIUtils.isFastDoubleClick()) {
                        //mConfigType = 2;//指纹登录
                        EventBusUtils.post(new Event.EventICAndFinger(fingerFea, 2));
                    }
                } else {
                    Intent intent = new Intent(mAppContext, ScanService.class);
                    mAppContext.stopService(intent);
                }
            }

            @Override
            public void onFingerRegExcuted(String deviceId, boolean success) {

            }

            @Override
            public void onFingerRegisterRet(String deviceId, boolean success, String fingerData) {

            }

            @Override
            public void onIDCard(String deviceId, String idCard) {
                int appSatus = App.getInstance().getAppSatus();
                Log.i("appSatus", "appSatus   " + appSatus);
                if (appSatus != 3) {
                    if (!UIUtils.isFastDoubleClick()) {
                        //mConfigType = 1;//IC卡
                        EventBusUtils.post(new Event.EventICAndFinger(idCard, 1));
                    }
                } else {
                    Intent intent = new Intent(mAppContext, ScanService.class);
                    mAppContext.stopService(intent);
                }

            }

            @Override
            public void onDoorOpened(String deviceIndentify, boolean success) {
                //目前设备监控开门success有可能出现错误   都设置成true
                getDoorStatus(SYSTEMTYPE);
                if (success) {
                    Log.i("outtccc", "柜门已开    ");
                    EventBusUtils.post(new Event.PopupEvent(true, "柜门已开", deviceIndentify));
                    if (UIUtils.getConfigType(mAppContext, CONFIG_059)) {
                        startVideo("opendoor", deviceIndentify);
                    }
                }
                if (mEthDeviceIdBack.size() > 0) {
                    if (!getStringType(mEthDeviceIdBack, deviceIndentify)) {
                        mEthDeviceIdBack.add(deviceIndentify);
                    }
                } else {
                    mEthDeviceIdBack.add(deviceIndentify);
                }
                mEthDeviceIdBack2.clear();
                mEthDeviceIdBack2.addAll(mEthDeviceIdBack);
                mEthDeviceIdBack3.addAll(mEthDeviceIdBack);
                Log.i("ffererers", "mEthDeviceIdBack2  开门      " + mEthDeviceIdBack2.size());
            }

            @Override
            public void onDoorClosed(String deviceIndentify, boolean success) {

                Log.i("outtccc", "柜门已关闭    ");
                EventBusUtils.post(new Event.PopupEvent(false, "关闭", deviceIndentify));
                LogUtils.i(TAG, "onDoorClosed  " + mEthDeviceIdBack2.size() + "   " +
                        mEthDeviceIdBack.size());
                if (mEthDeviceIdBack2.size() == 0 && mEthDeviceIdBack.size() == 0) {//强开
                    if (UIUtils.getConfigType(mAppContext, CONFIG_059)) {
                        startVideo("forcein", deviceIndentify);
                    }
                    startScan(deviceIndentify, -1);
                } else {//正常开门
                    for (int i = 0; i < mEthDeviceIdBack2.size(); i++) {
                        if (mEthDeviceIdBack2.get(i).equals(deviceIndentify)) {
                            mEthDeviceIdBack2.remove(i);
                            if (UIUtils.getConfigType(mAppContext, CONFIG_059)) {
                                stopVideo(deviceIndentify); //todo  3.0只需要上柜和单柜的deviceid ，下柜传上柜的deviceid
                            }
                        }
                    }
                }
                getDoorStatus(SYSTEMTYPE);
                Log.i("ffererers", "mEthDeviceIdBack2   关门   " + mEthDeviceIdBack2.size());
            }

            @Override
            public void onDoorCheckedState(String deviceIndentify, boolean opened) {
                Log.i("ffererers", "onDoorCheckedState   检测一下   " + opened);
                EventBusUtils.postSticky(new Event.EventDoorStatus(deviceIndentify, opened));
            }
        });
    }

}
