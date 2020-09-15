package org.wh.engineer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.rivamed.FingerCallback;
import com.rivamed.FingerManager;
import com.rivamed.libidcard.IdCardCallBack;
import com.rivamed.libidcard.IdCardManager;
import com.ruihua.libconsumables.ConsumableManager;
import com.ruihua.libconsumables.callback.ConsumableCallBack;

import org.wh.engineer.base.App;
import org.wh.engineer.devices.DeviceOperator;
import org.wh.engineer.receiver.NetWorkReceiver;
import org.wh.engineer.utils.LogUtils;
import org.wh.engineer.utils.LoginAndOpenDoor;
import org.wh.engineer.utils.MusicPlayer;

import java.util.List;

import static com.rivamed.libidcard.IdCardProducerType.TYPE_NET_AN_DE;
import static org.wh.engineer.base.App.mAppContext;
import static org.wh.engineer.cont.Constants.FINGER_TYPE;
import static org.wh.engineer.cont.Constants.IC_TYPE;
import static org.wh.engineer.receiver.NetWorkReceiver.initReceiver;

/**
 * created by wh on 2020/7/31
 * desc
 */
public class DeviceService extends Service {
    public static String TAG = DeviceService.class.getSimpleName();
    private static DeviceService mInstance;
    private NetWorkReceiver mWorkReceiver;
    private boolean mIsDoorOpen;//柜门是否已经打开
    private boolean mIsLogining = false;//正在处理登录和开门过程中
    private long mLastLoginTime = 0;//上一次登录结束时间

    public static DeviceService getInstance() {
        if (mInstance == null) {
            synchronized (DeviceService.class) {
                if (mInstance == null) {
                    mInstance = new DeviceService();
                }
            }
        }
        return mInstance;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mWorkReceiver = initReceiver(this);
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWorkReceiver != null) {
            unregisterReceiver(mWorkReceiver);
            mWorkReceiver = null;
        }
        App.appRestart();
    }

    /**
     * 执行登录
     */
    public void login(int configType, String value) {
        if (!mIsLogining) {
            mIsLogining = true;
            LoginAndOpenDoor.doLogin(DeviceService.this, configType, value);
        }
    }

    /**
     * 根据登录状态进行开门和语音提示
     */
    public void openDoor(boolean loginSuccess) {
        mLastLoginTime = System.currentTimeMillis();

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (loginSuccess) {
                    if (!mIsDoorOpen) {
                        if (DeviceOperator.openDoorImpl()) {//开门成功
                            MusicPlayer.getInstance().play(MusicPlayer.Type.OPEN_DOOR);
                        } else {//开门失败
                            MusicPlayer.getInstance().play(MusicPlayer.Type.LOGIN_ERROR);
                        }
                        // DeviceOperator.openDoor();
                    } else {
                        MusicPlayer.getInstance().play(MusicPlayer.Type.OPEN_DOOR);
                    }
                } else { //登陆失败
                    MusicPlayer.getInstance().play(MusicPlayer.Type.LOGIN_ERROR);
                }
                mIsLogining = false;
            }
        }).start();

    }


    public void init() {
        new Thread(() -> {
            initState();
            initBom();
            initIC();
            initFinger();

        }).start();
    }

    /**
     * IC卡
     */
    private void initIC() {
        IdCardManager.getIdCardManager().registerCallBack(new IdCardCallBack() {
            @Override
            public void onConnectState(String deviceId, boolean isConnect) {
                LogUtils.i(TAG, "IC读卡连接：设备ID:   " + deviceId + "   ;   isConnect = " + isConnect);
                initState();
                if (isConnect) {
                    IdCardManager.getIdCardManager().startReadCard(deviceId);
                }else {
                    /**
                     * 2020.9.10
                     * 处理板子断电重新上电后的重连。
                     * */
                    IdCardManager.getIdCardManager().destroyIdCard();
                    IdCardManager.getIdCardManager().connectIdCard(mAppContext, TYPE_NET_AN_DE);
                }
            }

            @Override
            public void onReceiveCardNum(String cardNo) {
                LogUtils.i(TAG, "接收到刷卡信息：设备ID:     ID = " + cardNo);
                if (App.mDeviceId.contains(IC_TYPE)) {
                    login(1, cardNo);
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
                LogUtils.i(TAG, "指纹设备，deviceId= " + deviceId + ", isConnect = " + isConnect);
                initState();
                if (isConnect) {
                    FingerManager.getManager().startReadFinger(deviceId);
                }
            }

            @Override
            public void onFingerFeatures(String deviceId, String features) {
                Log.d(TAG, "接收到指纹采集信息：设备ID:   " + deviceId + "   ;   FingerData = " + features);

                if (App.mDeviceId.contains(FINGER_TYPE)) {
                    login(2, features);
                }

            }

            @Override
            public void onRegisterResult(
                    String deviceId, int code, String features, List<String> fingerPicPath, String msg) {
                Log.d(TAG, "设备：：" + deviceId + "注册结果码是：：" + code + "\n>>>>>>>" + msg + "\n指纹照片数据：：" +
                        (fingerPicPath == null ? 0 : fingerPicPath.size()) + "\n特征值是：：：" + features);
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

    /**
     * 主控板
     */
    private void initBom() {
        ConsumableManager.getManager().registerCallback(new ConsumableCallBack() {
            @Override
            public void onConnectState(String deviceId, boolean isConnect) {
                initState();
                LogUtils.i(TAG, "主控板连接：设备ID:   " + deviceId + ", isConnect = " + isConnect);
                if (isConnect) {
                    /**
                     * 2020.9.10
                     * 处理板子断电重新上电后的重连。
                     * */
                    IdCardManager.getIdCardManager().destroyIdCard();
                    IdCardManager.getIdCardManager().connectIdCard(mAppContext, TYPE_NET_AN_DE);
                }
            }

            @Override
            public void onOpenDoor(String deviceId, int which, boolean isSuccess) {
                if (isSuccess) {
                    LogUtils.i(TAG, "开门成功");
                    if (!mIsDoorOpen) {
                        DeviceOperator.openSingleLightStart(deviceId);
                        mIsDoorOpen = true;
                    }
                } else {
                    LogUtils.i(TAG, "开门失败");
                }
            }

            @Override
            public void onCloseDoor(String deviceId, int which, boolean isSuccess) {
                if (mIsDoorOpen) {
                    DeviceOperator.closeSingleLightStart(deviceId);
                    mIsDoorOpen = false;
                }
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

    private void initState() {
        mIsDoorOpen = false;
        mIsLogining = false;
    }
}
