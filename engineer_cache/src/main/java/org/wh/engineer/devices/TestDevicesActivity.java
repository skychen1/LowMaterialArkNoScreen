package org.wh.engineer.devices;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.rivamed.libdevicesbase.base.DeviceInfo;
import com.ruihua.libconsumables.ConsumableManager;
import com.ruihua.libfacerecognitionv3.main.activity.MainActivity;
import com.ruihua.reader.ReaderCallback;
import com.ruihua.reader.ReaderManager;
import com.ruihua.reader.bean.AntInfo;
import com.ruihua.reader.bean.EpcInfo;

import org.wh.engineer.R;
import org.wh.engineer.base.SimpleActivity;
import org.wh.engineer.utils.FaceTask;
import org.wh.engineer.utils.LogUtils;
import org.wh.engineer.utils.StringUtils;
import org.wh.engineer.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.wh.engineer.activity.SplashActivity.mIntentService;

public class TestDevicesActivity extends SimpleActivity {

    private TextView mTvLog;
    private Button mBtnStartScan;
    private Button mBtnFingerReg;
    private Button mBtnOpenDoor;
    private Button mBtnCheckState;
    private Button mBtnReaderFrequency;
    private Button mBtnGetPower;
    private Button mBtnSetPower;
    private Button mBtnReaderReset;
    private Button mBtnClean;
    private Button mBtnStopScan;
    private EditText mEtScanTime;
    private Button mBtnOpenGpio;
    private Button mBtnCloseGpio;
    private Button mBtnOpenGpioLight;
    private Button mBtnCloseGpioLight;
    private Button mBtnLightState;
    private Button mBtnLockState;
    private Button mBtnFingerRegisterNew;
    private Button mBtnFace;
    private long startScanTime;
    private EditText mEtPower;
    private Button mBtnQueryConnDev;
    private Button mBtnBack;
    private ScrollView mSvLog;
    String eth002DeviceId = "";
    String fingerData;
    String fingerTemplate;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
    private String readerId;
    private String idCardId = "";
    private String fingerId = "";
    private int scanTime = 2 * 1000;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test_devices_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        stopService(mIntentService);
        mTvLog = findViewById(R.id.txt_log);
        mBtnStartScan = findViewById(R.id.bt_startscan);
        mBtnFingerReg = findViewById(R.id.bt_fingerreg);
        mBtnOpenDoor = findViewById(R.id.bt_opendoor);
        mBtnCheckState = findViewById(R.id.bt_checkState);
        mBtnReaderFrequency = findViewById(R.id.bt_reader_frequency);
        mBtnGetPower = findViewById(R.id.bt_getpower);
        mBtnSetPower = findViewById(R.id.bt_setpower);
        mEtPower = findViewById(R.id.txt_power);
        mBtnStopScan = findViewById(R.id.btn_stop_scan);
        mBtnQueryConnDev = findViewById(R.id.bt_queryConnDev);
        mBtnReaderReset = findViewById(R.id.bt_uhf_reset);
        mBtnBack = findViewById(R.id.bt_back);
        mBtnClean = findViewById(R.id.bt_clear);
        mSvLog = findViewById(R.id.scroll_log);
        mEtScanTime = findViewById(R.id.txt_scantime);
        //create by 孙朝阳 on 2018-12-10
        mBtnOpenGpio = findViewById(R.id.bt_opengpio);
        mBtnCloseGpio = findViewById(R.id.bt_closegpio);
        mBtnOpenGpioLight = findViewById(R.id.bt_opengpiolight);
        mBtnCloseGpioLight = findViewById(R.id.bt_closegpiolight);
        mBtnLightState = findViewById(R.id.bt_light_state);
        mBtnLockState = findViewById(R.id.bt_lockstate);
        mBtnFingerRegisterNew = findViewById(R.id.bt_fingerregister);
        mBtnFace = findViewById(R.id.btn_face);
        initListener();

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            appendLog("获取有效屏幕分辨率:X=" + dm.widthPixels + ";Y=" + dm.heightPixels);
            appendLog("程序已启动");
        }
        mBtnQueryConnDev.performClick();
        initReader();
//        initEth002();
    }


    /**
     * 初始化罗丹贝尔回调
     */
    private void initReader() {
        appendLog("initReader" );
        //设置回调
        ReaderManager.getManager().registerCallback(new ReaderCallback() {
            @Override
            public void onConnectState(String deviceId, boolean isConnect) {
                if (isConnect) {
                    // TODO: 2019/3/26
                    readerId = deviceId;
                    appendLog("Reader设备已连接:::ID=" + deviceId);
                } else {
                    readerId = "";
                    appendLog("Reader设备已断开:::ID" + deviceId);
                }
            }

            @Override
            public void onScanResult(String deviceId, Map<String, List<EpcInfo>> result) {
                StringBuilder stringBuilder = new StringBuilder();
                long myTime = System.currentTimeMillis() - startScanTime;
                stringBuilder.append("Reader (" + deviceId + ")扫描完成。总共耗时：：：：" + myTime + "ms\n");
                stringBuilder.append("\t EPC数量" + result.size() + "个:\n");
                for (Map.Entry<String, List<EpcInfo>> v : result.entrySet()) {
                    stringBuilder.append(v.getKey() + ";");
                }
                //                appendLog(stringBuilder.toString());
                setLog(stringBuilder.toString());
                Log.i("ddddda",stringBuilder.toString());
                // TODO: 2019/1/28 循环测试
                mBtnStartScan.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBtnStartScan.performClick();
                    }
                }, 2000);
            }

            @Override
            public void onScanNewEpc(String deviceId, String epc, int ant) {
                String string;
                string = ("\t EPC数量:1" + "个:" + epc + ", 被第" + ant + "根天线扫到");
                appendLog(string);
            }

            @Override
            public void onSetPower(String deviceId, boolean success) {
                appendLog("设备：：" + deviceId + "设置功率成功状态::" + success);
            }

            @Override
            public void onGetPower(String deviceId, int power) {
                appendLog("设备：：" + deviceId + "的功率值是::" + power);
            }

            @Override
            public void onGetPower(String deviceId, int[] power) {

            }

            @Override
            public void onGetFrequency(String deviceId, String frequency) {
                appendLog("设备：：" + deviceId + "的频率值是::" + frequency);
            }

            @Override
            public void onCheckAnt(String deviceId, List<AntInfo> ant) {
                //nothing（暂未完成）
            }

            @Override
            public void onLockOpen(String deviceId, boolean isSuccess) {
                appendLog("设备：：" + deviceId + "开锁是否成功::" + isSuccess);
            }

            @Override
            public void onLockClose(String deviceId, boolean isSuccess) {
                appendLog("设备：：" + deviceId + "关锁是否成功::" + isSuccess);
            }

            @Override
            public void onLightOpen(String deviceId, boolean isSuccess) {
                appendLog("设备：：" + deviceId + "开灯是否成功::" + isSuccess);
            }

            @Override
            public void onLightClose(String deviceId, boolean isSuccess) {
                appendLog("设备：：" + deviceId + "关灯是否成功::" + isSuccess);
            }

            @Override
            public void onLockState(String deviceId, boolean isOpened) {
                appendLog("设备：：" + deviceId + "检测所的状态::" + isOpened);
            }

            @Override
            public void onLightState(String deviceId, boolean isOpened) {
                appendLog("设备：：" + deviceId + "检测灯的状态::" + isOpened);
            }
        });
    }



    //    private void initFinger() {
    //        FingerManager.getFingerManager().registerCallBack(new FingerCallBack() {
    //            @Override
    //            public void onConnectState(String deviceId, boolean isConnect) {
    //                if (isConnect) {
    //                    fingerId = deviceId;
    //                    appendLog("Finger设备已连接：ID=" + deviceId);
    //                } else {
    //                    fingerId = "";
    //                    appendLog("Finger设备已连接：ID=" + deviceId);
    //                }
    //            }
    //
    //            @Override
    //            public void OnFingerReg(String deviceId, boolean success) {
    //                appendLog("三能指纹注册命令已执行：" + deviceId + ":::success=" + success);
    //            }
    //
    //            @Override
    //            public void OnFingerRegRet(String deviceId, boolean success, String fingerData) {
    //                appendLog("三能接收到指纹注册结果：" + deviceId + ":::success=" + success + ":::FingerData=" + fingerData);
    //                if (success) {
    //                    fingerTemplate = fingerData;
    //                }
    //            }
    //
    //            @Override
    //            public void OnFingerMayMatch(String deviceId, boolean isCan) {
    //                if (isCan) {
    //                    appendLog("指纹已经采集到，可以发送模板数据进行比对：" + deviceId);
    //                    int ret = FingerManager.getFingerManager().FingerMatch(deviceId, fingerTemplate);
    //                }
    //            }
    //
    //            @Override
    //            public void OnFingerMatchRet(String deviceId, boolean matchSuccess, String reason) {
    //                if (matchSuccess) {
    //                    appendLog("指纹成功匹配：" + deviceId);
    //                } else {
    //                    appendLog("指纹匹配失败，请继续：" + deviceId);
    //                }
    //            }
    //        });
    //    }

    //           /**
    //     * 初始化id卡
    //     */
    //    private void initIdCard() {
    //        IdCardManager.getIdCardManager().registerCallBack(new IdCardCallBack() {
    //            @Override
    //            public void onConnectState(String deviceId, boolean isConnect) {
    //                if (isConnect) {
    //                    idCardId = deviceId;
    //                    appendLog("IdCardISO14443A设备已连接：ID=" + deviceId);
    //                } else {
    //                    idCardId = "";
    //                    appendLog("IdCardISO14443A设备已断开:ID=" + deviceId);
    //                }
    //            }
    //
    //            @Override
    //            public void onReceiveIDCard(String cardNo) {
    //                String string;
    //                string = ("\t读到的卡号：" + cardNo);
    //                appendLog(string);
    //            }
    //        });
    //    }

    private void appendLog(String msg) {
        TestDevicesActivity.this.mTvLog.post(new Runnable() {
            @Override
            public void run() {
                Date time = new Date();
                String s = ">>>>" + simpleDateFormat.format(time) + "  " + msg + "\n";
                TestDevicesActivity.this.mTvLog.append(s);

                int offset = mTvLog.getMeasuredHeight() - mSvLog.getMeasuredHeight();
                if (offset < 0) {
                    offset = 0;
                }
                mSvLog.scrollTo(0, offset);
                Log.i("fff", msg);
            }
        });
    }

    private void setLog(String sss) {
        TestDevicesActivity.this.mTvLog.post(new Runnable() {
            @Override
            public void run() {
                TestDevicesActivity.this.mTvLog.setText(sss);
            }
        });
    }


    private void initListener() {
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestDevicesActivity.this.finish();
            }
        });

        /**
         *  modify by 孙朝阳 on 2019-1-5
         */
        mBtnStartScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //默认时间为1秒
                if (TextUtils.isEmpty(mEtScanTime.getText().toString())) {
                    scanTime = 2000;
                } else {
                    scanTime = Integer.parseInt(mEtScanTime.getText().toString());
                    if (scanTime < 1000 || scanTime > 10 * 1000) {
                        ToastUtils.showShort("数据时间应在1000-10000范围类");
                        return;
                    }
                }
                startScanTime = System.currentTimeMillis();
                // TODO: 2019/3/27  本地测试
                int ret = ReaderManager.getManager().startScan(readerId, scanTime);
                appendLog("启动持续扫描，扫描时间为" + ";RET=" + ret);
                //                int i = RaylinksManager.getManager().startScan();
                //                appendLog("启动持续扫描，扫描时间为" + ";RET=" + i);
            }
        });

        mBtnStopScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = ReaderManager.getManager().stopScan(readerId);
                appendLog("停止持续扫描了：" + ";RET=" + i);
            }
        });

        mBtnFingerReg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                int ret = Eth002Manager.getEth002Manager().fingerReg(eth002DeviceId);
                //                int ret = FingerManager.getFingerManager().FingerRegister(fingerId);
//                appendLog("指纹注册命令已发送 RET=" + ret + ";请等待指纹注册执行结果");

            }
        });

        mBtnOpenDoor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int ret = ConsumableManager.getManager().openDoor(eth002DeviceId,0);
                //                int ret = ReaderManager.getManager().openLock(readerId);
                appendLog("开门命令已发出 ret=" + ret);
            }
        });

        /**
         *   create by 孙朝阳 on 2018-12-10
         *   开锁按钮
         */
        mBtnOpenGpio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                int ret = ReaderManager.getManager().openLock(readerId);
                //                appendLog("GPIO开锁命令已经发出 ret=" + ret);
            }
        });

        /**
         *   create by 孙朝阳 on 2018-12-11
         *   关锁按钮
         */
        mBtnCloseGpio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                int ret = ReaderManager.getManager().closeLock(readerId);
                //                appendLog("GPIO关锁命令已经发出 ret=" + ret);
            }
        });

        /**
         *   create by 孙朝阳 on 2018-12-12
         *   开灯按钮
         */
        mBtnOpenGpioLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                int ret = ReaderManager.getManager().openLight(readerId);
                //                appendLog("GPIO开灯命令已经发出 ret=" + ret);
            }
        });

        /**
         *   create by 孙朝阳 on 2018-12-12
         *   关灯按钮
         */
        mBtnCloseGpioLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                int ret = ReaderManager.getManager().closeLight(readerId);
                //                appendLog("GPIO关灯命令已经发出 ret=" + ret);
            }
        });

        /**
         *   create by 孙朝阳 on 2018-12-12
         *   检测灯状态按钮
         */
        mBtnLightState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                int ret = ReaderManager.getManager().checkLightState(readerId);
                //                appendLog("GPIO检测门锁状态命令已经发出 ret=" + ret);
            }
        });

        /**
         *   create by 孙朝阳 on 2018-12-12
         *   检测锁状态按钮
         */
        mBtnLockState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                int ret = ReaderManager.getManager().checkLockState(readerId);
                //                appendLog("GPIO检测门锁状态命令已经发出 ret=" + ret);
            }
        });

        /**
         *  create by 孙朝阳 on 2018-12-29
         *  注册指纹（三能指纹仪）
         */
        //        mBtnFingerRegisterNew.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //
        //                int ret1 = FingerManager.getFingerManager().FingerRegister(fingerId);
        //                appendLog("三能指纹注册命令已发送 RET=" + ret1 + ";请等待指纹注册执行结果");
        //            }
        //        });
        mBtnQueryConnDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<DeviceInfo> connectedDevice = ReaderManager.getManager().getConnectedReader();
                String device = "";
                for (DeviceInfo de : connectedDevice) {
                    device += "\t " + de.getProduct() + "  设备id： \t" + de.getIdentification() + "\n";
                    readerId = de.getIdentification();
                }
                appendLog(
                      StringUtils.isEmpty(device) ? "目前暂无RFID设备连接" : ("已连接Reader设备如下：\n" + device));

                //                // TODO: 2019/3/26   2.6ID新版代码测试
                //                List<DeviceInfo> connectedDeviceIdCard = IdCardManager.getIdCardManager().getConnectedDevice();
                //                String devices = "";
                //                for (DeviceInfo de : connectedDeviceIdCard) {
                //                    devices += "\t  IdCardISO14443A 设备id： \t" + de.getIdentification() + "\n";
                //                    idCardId = de.getIdentification();
                //                }
                //                appendLog(
                //			    StringUtils.isEmpty(devices) ? "目前暂无IDCard设备连接" : ("已连接Id卡设备如下：\n" + devices));
                //
                //                // TODO: 2019/3/26   2.6三能指纹仪新代码测试
                //                List<DeviceInfo> connectedDeviceFinger = FingerManager.getFingerManager().getConnectedDevice();
                //                String fingers = "";
                //                for (com.rivamed.libdevicesbase.base.DeviceInfo df : connectedDeviceFinger) {
                //                    fingers += "\t  Finger 设备id： \t" + df.getIdentification() + "\n";
                //                    fingerId = df.getIdentification();
                //                }
                //                appendLog(
                //			    StringUtils.isEmpty(fingers) ? "目前暂无Finger设备连接" : ("已连接指纹设备如下：\n" + fingers));

                // TODO: 2019/3/26  ETH002模块，新代码测试
//                List<DeviceInfo> connectedDeviceEth002 = Eth002Manager.getEth002Manager().getConnectedDevice();
//                String eth002 = "";
//                for (DeviceInfo df : connectedDeviceEth002) {
//                    eth002 += "\t  Eth002 设备id： \t" + df.getIdentification() + "\n";
//                    eth002DeviceId = df.getIdentification();
//                }
//                appendLog(StringUtils.isEmpty(eth002) ? "目前暂无Eth002设备连接" : ("已连接里模块设备如下：\n" + eth002));
            }
        });
        mBtnCheckState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                int ret = Eth002Manager.getEth002Manager().checkDoorState(eth002DeviceId);
//                appendLog("检查门锁命令发送 RET=" + ret);
            }
        });

        mBtnReaderFrequency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                int i = RaylinksManager.getManager().getFrequency();
                //                appendLog("读取频率 RET=" + i);
            }
        });
        mBtnGetPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2019/3/27  本地测试
                int ret = ReaderManager.getManager().getPower(readerId);
                appendLog("获取RFID reader功率指令发送 RET=" + ret);
                //                int i = RaylinksManager.getManager().getPower();
                //                appendLog("获取功率 RET=" + i);
            }
        });

        mBtnSetPower.setOnClickListener(new View.OnClickListener() {

            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mEtPower.getText().toString())) {
                    ToastUtils.showShort("请输入功率值");
                    return;
                }
                int i = Integer.parseInt(mEtPower.getText().toString());
                if (i <= 0 || i > 30) {
                    ToastUtils.showShort("功率值范围应在1-30之间");
                    return;
                }
                // TODO: 2019/3/27  本地测试
                int ret = ReaderManager.getManager().setPower(readerId, i);
                appendLog("设置RFID reader功率指令发送 设备Ｉ=" + readerId + ",功率=" + i + " RET=" + ret);
                //                int ret = RaylinksManager.getManager().setPower(i);
                //                appendLog("设置功率功率 RET=" + ret);
            }
        });
        mBtnReaderReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ret = ReaderManager.getManager().restDevice(readerId);
                appendLog("复位RFID reader功率指令发送 设备Ｉ=" + readerId + " RET=" + ret);
            }
        });

        mBtnClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestDevicesActivity.this.mTvLog.setText("");
            }
        });

        //人脸识别
        mBtnFace.setOnClickListener(view -> startActivity(new Intent(this, MainActivity.class)));
        findViewById(R.id.btn_face_register).setOnClickListener(view -> {
            FaceTask faceTask = new FaceTask(this);
            faceTask.setCallBack((hasRegister, msg) -> {
                if (msg!=null){
                    LogUtils.d("faceTask", "initListener: "+msg );
                }
            });
            faceTask.getAllFaceAndRegister();
        });
    }

    @Override
    public void onBindViewBefore() {

    }

    @Override
    public Object newP() {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReaderManager.getManager().unRegisterCallback();
        //        ReaderManager.getManager().disConnectReader(readerId);
//        Eth002Manager.getEth002Manager().unRegisterCallBack();
        //        RtspManager.getManager().stopEncode("rtsp://192.168.11.96:8554/live");
        //        RtspManager.getManager().stopRtsp("rtsp://192.168.11.96:8554/live");

    }
}