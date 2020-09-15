package org.wh.engineer.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rivamed.FingerCallback;
import com.rivamed.FingerManager;
import com.rivamed.libdevicesbase.base.DeviceInfo;
import com.rivamed.libidcard.IdCardCallBack;
import com.rivamed.libidcard.IdCardManager;
import com.ruihua.libconsumables.ConsumableManager;
import com.ruihua.libconsumables.callback.ConsumableCallBack;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.wh.engineer.R;
import org.wh.engineer.adapter.RegistLockAdapter_3;
import org.wh.engineer.base.SimpleFragment;
import org.wh.engineer.bean.Event;
import org.wh.engineer.service.DeviceService;
import org.wh.engineer.utils.EventBusUtils;
import org.wh.engineer.utils.SPUtils;
import org.wh.engineer.utils.StringUtils;
import org.wh.engineer.utils.ToastUtils;
import org.wh.engineer.utils.UIUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.widget.GridLayout.VERTICAL;
import static com.rivamed.FingerType.TYPE_NET_ZHI_ANG;
import static org.wh.engineer.base.App.CLOSSLIGHT_TIME;
import static org.wh.engineer.cont.Constants.SAVE_CLOSSLIGHT_TIME;

/**
 * 项目名称:    Android_PV_2.6.6_416D
 * 创建者:      DanMing
 * 创建时间:    2019/4/22 16:40
 * 描述:        工程模式锁-高值3.0
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class RegisteLockFrag extends SimpleFragment {

    @BindView(R.id.frag_closslight_edit)
    EditText mClosslightEdit;
    @BindView(R.id.frag_start)
    TextView mFragStart;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.txt_log)
    TextView mTxtLog;
    @BindView(R.id.scroll_log)
    ScrollView mScrollLog;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
    List<String> mDate = new ArrayList<>();
    private String mDiviceId;
    public String fingerData;
    public String fingerTemplate;
    private RegistLockAdapter_3 mAdapter_3;
    private DisplayMetrics mDm;
    private DividerItemDecoration mDividerItemDecoration;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLockType(Event.lockType event) {
        if (event.type == 1) {
            AppendLog("开门命令已发出 ret=" + event.ret + "      DeviceId   " + event.item);
        } else if (event.type == 2) {
            AppendLog("检查门锁指令已发出 ret=" + event.ret + "   ：设备ID:   " + event.item);
        } else if (event.type == 3) {
            AppendLog("开灯命令已发送 RET=" + event.ret + "   ：设备ID:   " + event.item + "" + event.witch);
        } else if (event.type == 4) {
            AppendLog("关灯命令已发送 RET=" + event.ret + "   ：设备ID:   " + event.item);
        } else if (event.type == 9) {
            AppendLog("电磁锁关闭令已发送 RET=" + event.ret + "   ：设备ID:   " + event.item);
        } else if (event.type == 10) {
            AppendLog("电磁锁开启令已发送 RET=" + event.ret + "   ：设备ID:   " + event.item);
        } else if (event.type == 11) {
            AppendLog("电磁锁检测令已发送 RET=" + event.ret + "   ：设备ID:   " + event.item);
        }
    }

    public static RegisteLockFrag newInstance() {
        RegisteLockFrag fragment = new RegisteLockFrag();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.registe_lock_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        EventBusUtils.register(this);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            mDm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(mDm);
            AppendLog("获取有效屏幕分辨率:X=" + mDm.widthPixels + ";Y=" + mDm.heightPixels);
        }
        initBom();
        initFinger();
        initIC();
    }

    @Override
    public void onBindViewBefore(View view) {

    }

    public void AppendLog(String msg) {
        mTxtLog.post(new Runnable() {
            @Override
            public void run() {
                Date time = new Date();
                String s = ">>" + simpleDateFormat.format(time) + "  " + msg + "\n";
                mTxtLog.append(s);

                int offset = mTxtLog.getMeasuredHeight() - mScrollLog.getMeasuredHeight();
                if (offset < 0) {
                    offset = 0;
                }
                mScrollLog.scrollTo(0, offset);
                Log.i("fff", msg);
            }
        });
    }

    /**
     * IC卡
     */
    private void initIC() {
        IdCardManager.getIdCardManager().registerCallBack(new IdCardCallBack() {
            @Override
            public void onConnectState(String deviceId, boolean isConnect) {
                AppendLog("IC读卡连接：设备ID:   " + deviceId + "   ;   isConnect = " + isConnect);
                if (isConnect) {
                    IdCardManager.getIdCardManager().startReadCard(deviceId);
                }
            }

            @Override
            public void onReceiveCardNum(String cardNo) {
                AppendLog("接收到刷卡信息：设备ID:     ID = " + cardNo);
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
                AppendLog("指纹连接：设备ID:   " + deviceId + "   ;   isConnect = " + isConnect);
                if (isConnect) {
                    FingerManager.getManager().startReadFinger(deviceId);
                }
            }

            @Override
            public void onFingerFeatures(String deviceId, String features) {
                AppendLog("接收到指纹采集信息：设备ID:   " + deviceId + "   ;   FingerData = " + features);
                fingerData = features;
            }

            @Override
            public void onRegisterResult(
                    String deviceId, int code, String features, List<String> fingerPicPath, String msg) {
                AppendLog("设备：：" + deviceId + "注册结果码是：：" + code + "\n>>>>>>>" + msg + "\n指纹照片数据：：" +
                        (fingerPicPath == null ? 0 : fingerPicPath.size()) + "\n特征值是：：：" + features);
                //收到注册结果标识注册完成就开启读取
            }

            @Override
            public void onFingerUp(String deviceId) {
                AppendLog("设备：：" + deviceId + "请抬起手指：");
            }

            @Override
            public void onRegisterTimeLeft(String deviceId, long time) {
                AppendLog("设备：：" + deviceId + "剩余注册时间：：" + time + "\n");
            }
        });
    }

    private void initBom() {
        ConsumableManager.getManager().registerCallback(new ConsumableCallBack() {
            @Override
            public void onConnectState(String deviceId, boolean isConnect) {
                if (!isConnect) {
                    AppendLog("设备已断开：设备ID:   " + deviceId + "；");
                }
            }

            @Override
            public void onOpenDoor(String deviceId, int which, boolean isSuccess) {
                String type;
                String whichs;
                if (isSuccess) {
                    type = "成功";
                } else {
                    type = "失败";
                }
                if (which == 0) {
                    whichs = "0号端口";
                } else {
                    whichs = "1号端口";
                }
                AppendLog("开门结果：设备ID:   " + deviceId + "  端口： " + which + "  which  " + whichs +
                        " ;   开门状态 = " + type);
            }

            @Override
            public void onCloseDoor(String deviceId, int which, boolean isSuccess) {
                String type;
                String whichs;
                if (isSuccess) {
                    type = "成功";
                } else {
                    type = "失败";
                }
                if (which == 0) {
                    whichs = "0号端口";
                } else {
                    whichs = "1号端口";
                }
                AppendLog("门锁已关闭：设备ID:   " + deviceId + "  端口： " + which + "   which   " + whichs +
                        "  ;   关门状态 = " + type);
            }

            @Override
            public void onDoorState(String deviceId, int which, boolean state) {
                String type;
                String whichs;
                if (state) {
                    type = "门锁打开状态";
                } else {
                    type = "门锁关闭状态";
                }
                if (which == 0) {
                    whichs = "0号端口";
                } else {
                    whichs = "1号端口";
                }
                AppendLog("门锁状态检查：设备ID:   " + deviceId + "  端口： " + which + "   which   " + whichs +
                        "    ;   门锁状态 = " + type);
            }

            @Override
            public void onOpenLight(String deviceId, int which, boolean isSuccess) {
                String type;
                String whichs;
                if (isSuccess) {
                    type = "成功";
                } else {
                    type = "失败";
                }
                if (which == 2) {
                    whichs = "2号端口";
                    AppendLog("灯已打开：设备ID:   " + deviceId + "  端口： " + which + "   which   " + whichs +
                            "  ;   灯状态 = " + type);
                } else if (which == 3) {
                    whichs = "3号端口";
                    AppendLog("灯已打开：设备ID:   " + deviceId + "  端口： " + which + "   which   " + whichs +
                            "  ;   灯状态 = " + type);
                } else if (which == 11) {//电磁锁
                    whichs = "11号端口";
                    AppendLog("电磁锁开启状态：设备ID:   " + deviceId + "  端口： " + which + "   which   " + whichs +
                            "  ;   锁状态 = " + type);
                }

            }

            @Override
            public void onCloseLight(String deviceId, int which, boolean isSuccess) {
                String type;
                String whichs;
                if (isSuccess) {
                    type = "成功";
                } else {
                    type = "失败";
                }
                if (which == 2) {
                    whichs = "2号端口";
                    AppendLog("灯已关闭：设备ID:   " + deviceId + "  端口： " + which + "   which   " + whichs +
                            "  ;   灯状态 = " + type);
                } else if (which == 3) {
                    whichs = "3号端口";
                    AppendLog("灯已关闭：设备ID:   " + deviceId + "  端口： " + which + "   which   " + whichs +
                            "  ;   灯状态 = " + type);
                } else if (which == 11) {//电磁锁
                    whichs = "11号端口";
                    AppendLog("电磁锁关闭状态：设备ID:   " + deviceId + "  端口： " + which + "   which   " + whichs +
                            "  ;   锁状态 = " + type);
                } else {
                    whichs = which + "端口";
                    AppendLog("灯已关闭：设备ID:   " + deviceId + "  端口： " + which + "   which   " + whichs +
                            "  ;   灯状态 = " + type);
                }

            }

            @Override
            public void onLightState(String deviceId, int which, boolean state) {
                String type;
                String whichs;
                if (state) {
                    type = "开";
                } else {
                    type = "关";
                }
                if (which == 2) {
                    whichs = "2号端口";
                    AppendLog("灯已关闭：设备ID:   " + deviceId + "  端口： " + which + "   which   " + whichs +
                            "  ;   灯状态 = " + type);
                } else if (which == 3) {
                    whichs = "3号端口";
                    AppendLog("灯已关闭：设备ID:   " + deviceId + "  端口： " + which + "   which   " + whichs +
                            "  ;   灯状态 = " + type);
                } else if (which == 11) {
                    AppendLog("电磁锁状态：设备ID:   " + deviceId + "  端口： " + which + "  ;   灯状态 = " + type);
                }

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

    @OnClick({R.id.frag_start, R.id.frag_closslight_btn})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.frag_start) {
            AppendLog("获取有效屏幕分辨率:X=" + mDm.widthPixels + ";Y=" + mDm.heightPixels);
            if (mDate != null) {
                mDate.clear();
                mTxtLog.setText("");
            }
            getConnectedDevice();
        } else if (view.getId() == R.id.frag_start) {
            try {
                int time = (Integer.parseInt(mClosslightEdit.getText().toString().trim()) * 1000);
                if (time >= 10000) {
                    SPUtils.putInt(UIUtils.getContext(), SAVE_CLOSSLIGHT_TIME, time);
                    CLOSSLIGHT_TIME = time;
                    // BaseSimpleActivity.mLightTimeCount = null;
                    ToastUtils.showShortToast(
                            "设置成功！登录界面无操作后 " + CLOSSLIGHT_TIME / 1000 + " s后自动关灯！");
                } else {
                    ToastUtils.showShortToast("设置失败，时间必须大于等于10秒，请重新设置！");
                }
            } catch (Exception ex) {
                ToastUtils.showShortToast("设置失败，请填写时间！");
            }
        }
    }

    private void getConnectedDevice() {
        List<DeviceInfo> deviceInfos = ConsumableManager.getManager().getConnectedDevice();
        String s = "";
        for (DeviceInfo d : deviceInfos) {
            mDiviceId = d.getIdentification();
            mDate.add(mDiviceId);
            s += "\t  设备类型 \t" + d.getProduct() + ";\t\t设备ID \t" + d.getIdentification() +
                    "\n";
        }
        AppendLog(StringUtils.isEmpty(s) ? "目前暂无连接" : ("已连接设备如下：\n" + s));
        FingerManager.getManager().connectFinger(mContext, TYPE_NET_ZHI_ANG);
        List<DeviceInfo> deviceFingerInfos = FingerManager.getManager().getConnectedFinger();
        for (DeviceInfo info : deviceFingerInfos) {
            int i = FingerManager.getManager().startReadFinger(info.getIdentification());
            Log.i("appSatus", "info.FingerManager()     " + info.getIdentification() +
                    "  FingerManager    " + i);
        }
        List<DeviceInfo> connectedDevice = IdCardManager.getIdCardManager()
                .getConnectedDevice();
        for (DeviceInfo info : connectedDevice) {
            int i = IdCardManager.getIdCardManager().startReadCard(info.getIdentification());
            Log.i("appSatus", "info.IdCardManager()     " + info.getIdentification() +
                    "  IdCardManager    " + i);
        }
        if (mAdapter_3 != null) {
            mAdapter_3.notifyDataSetChanged();
        } else {
            int mLayout = R.layout.item_lock_layout_3;
            mAdapter_3 = new RegistLockAdapter_3(mLayout, mDate);
            setRAdapterLayout();
            mRecyclerview.setAdapter(mAdapter_3);
        }

    }

    private void setRAdapterLayout() {
        if (mDividerItemDecoration != null) {
            mRecyclerview.removeItemDecoration(mDividerItemDecoration);
        }
        mDividerItemDecoration = new DividerItemDecoration(mContext, VERTICAL);
        mRecyclerview.addItemDecoration(mDividerItemDecoration);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        mRefreshLayout.setEnableAutoLoadMore(false);
        mRefreshLayout.setEnableRefresh(false);//是否启用下拉刷新功能
        mRefreshLayout.setEnableLoadMore(false);//是否启用上拉加载功能
    }

    @Override
    public void onDestroyView() {
        FingerManager.getManager().unRegisterCallback();
        IdCardManager.getIdCardManager().unRegisterCallBack();
        ConsumableManager.getManager().unRegisterCallback();
        // AllDeviceCallBack.getInstance().initCallBack();
        DeviceService.getInstance().init();
        super.onDestroyView();
    }
}
