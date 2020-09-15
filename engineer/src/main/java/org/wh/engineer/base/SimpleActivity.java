package org.wh.engineer.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.wh.engineer.R;
import org.wh.engineer.base.mvp.IPresent;
import org.wh.engineer.base.mvp.IView;
import org.wh.engineer.base.mvp.KnifeKit;
import org.wh.engineer.base.mvp.VDelegate;
import org.wh.engineer.base.mvp.VDelegateBase;
import org.wh.engineer.bean.Event;
import org.wh.engineer.utils.DevicesUtils;
import org.wh.engineer.utils.EventBusUtils;
import org.wh.engineer.utils.SPUtils;
import org.wh.engineer.utils.UIUtils;

import java.util.List;

import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportActivity;

import static org.wh.engineer.base.App.SYSTEMTYPE;
import static org.wh.engineer.base.App.mTitleConn;
import static org.wh.engineer.cont.Constants.LOGCAT_OPEN;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/14 16:23
 * 描述:       仿照XActivity写的基类没有标题栏
 * 包名:        high.rivamed.myapplication.fragment
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public abstract class SimpleActivity<P extends IPresent> extends SupportActivity
        //	implements IView<P>,NetWorkReceiver.IntAction {
        implements IView<P> {

    private VDelegate vDelegate;
    private P p;
    public Activity mContext;
    public Gson mGson;
    public List<String> mReaderDeviceId;
    public List<String> eth002DeviceIdList;
    private Unbinder unbinder;
    View mTipView;
    View mLogView;
    WindowManager mWindowManager;
    WindowManager mLogWindowManager;
    WindowManager.LayoutParams mLayoutParams;
    WindowManager.LayoutParams mLayoutParamsLog;
    public static Intent mIntent;
    private boolean mNet;

    /**
     * 设备title连接状态
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onTitleConnEvent(Event.XmmppConnect event) {
        mTitleConn = event.connect;
        mNet = event.net;
        hasNetWork(mTitleConn, mNet);
    }

    /**
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogEvent(Event.EventLogType event) {
        hasLogWork(event.type);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusUtils.register(this);
        //	hideBottomUIMenu();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //	   applyNet();
        mContext = this;
        mGson = new Gson();
        eth002DeviceIdList = DevicesUtils.getBomDeviceId(SYSTEMTYPE);
        mReaderDeviceId = DevicesUtils.getReaderDeviceId();
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
            initTipView();
            initLogView();
            onBindViewBefore();
            bindUI(null);
            bindEvent();
        }
        initDataAndEvent(savedInstanceState);

        //App.getInstance().addActivity_(this);
//	App.getInstance().addActivityAll(this);
    }
    //   private void applyNet() {
    //	IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    //	netWorkReceiver = new NetWorkReceiver();
    //	registerReceiver(netWorkReceiver, filter);
    //	netWorkReceiver.setInteractionListener(this);
    //
    //   }
    //   @Override
    //   public void setInt(int k) {
    //	if (k != -1) {
    //	   if (k == 2||k == 1) {
    //		NetRequest.getInstance().getHospBranch(this, new BaseResult());//用来查询是否网连通了
    ////		EventBusUtils.post(new XmppEvent.XmmppConnect(true));
    //	   } else if (k == 0) {
    //		EventBusUtils.post(new XmppEvent.XmmppConnect(false));
    //	   }
    //	}
    //   }

    /**
     * 判断显示网络异常
     *
     * @param has:true
     */
    public void hasNetWork(boolean has, boolean net) {
        if (mWindowManager != null) {
            if (has) {
                if (net) {
                    if (mTipView != null && mTipView.getParent() != null) {
                        mWindowManager.removeView(mTipView);
                    }
                } else {
                    if (mTipView.getParent() == null) {
                        try {
                            mWindowManager.addView(mTipView, mLayoutParams);
                        } catch (Exception e) {

                        }
                    }
                    ((TextView) mTipView.getRootView().findViewById(R.id.tip_text)).setText("网络不可用，系统已切换至离线模式");
                }

            } else {
                if (mTipView.getParent() == null) {
                    try {
                        mWindowManager.addView(mTipView, mLayoutParams);
                    } catch (Exception e) {

                    }

                }
                if (!net) {
                    ((TextView) mTipView.getRootView().findViewById(R.id.tip_text)).setText("未连接到网络，请检查网络设备");
                } else {
                    ((TextView) mTipView.getRootView().findViewById(R.id.tip_text)).setText("网络正常，未连接到服务器！");
                }
            }
        }
    }

    private void initTipView() {
        LayoutInflater inflater = getLayoutInflater();
        mTipView = inflater.inflate(R.layout.layout_network_tip, null); //提示View布局
        mWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        //使用非CENTER时，可以通过设置XY的值来改变View的位置
        mLayoutParams.gravity = Gravity.TOP;
        mLayoutParams.x = 0;
        mLayoutParams.y = getResources().getDimensionPixelSize(R.dimen.y86);
    }

    /**
     * 判断日志开启
     *
     * @param has:true
     */
    public void hasLogWork(boolean has) {
        if (mLogWindowManager != null) {
            if (!has) {
                if (mLogView != null && mLogView.getParent() != null) {
                    mLogWindowManager.removeView(mLogView);
                }
            } else {
                if (mLogView.getParent() == null) {
                    try {
                        mLogWindowManager.addView(mLogView, mLayoutParamsLog);
                    } catch (Exception e) {

                    }

                }
            }
        }
    }

    /**
     * 日志启用显示
     */
    private void initLogView() {
        LayoutInflater inflater = getLayoutInflater();
        mLogView = inflater.inflate(R.layout.layout_log_layout, null); //提示View布局
        mLogWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        mLayoutParamsLog = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        //使用非CENTER时，可以通过设置XY的值来改变View的位置

        mLayoutParamsLog.gravity = Gravity.BOTTOM;
    }

    @Override
    public void bindUI(View rootView) {
        unbinder = KnifeKit.bind(this);
    }

    protected VDelegate getvDelegate() {
        if (vDelegate == null) {
            vDelegate = VDelegateBase.create(mContext);
        }
        return vDelegate;
    }

    protected P getP() {
        if (p == null) {
            p = newP();
            if (p != null) {
                p.attachV(this);
            }
        }
        return p;
    }

    @Override
    public void onStart() {
        super.onStart();
        eth002DeviceIdList = DevicesUtils.getBomDeviceId(SYSTEMTYPE);
        mReaderDeviceId = DevicesUtils.getReaderDeviceId();
        if (SPUtils.getBoolean(UIUtils.getContext(), LOGCAT_OPEN)) {
            hasLogWork(true);
        } else {
            hasLogWork(false);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mTitleConn) {
            hasNetWork(mTitleConn, mNet);
        }
        getvDelegate().resume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTipView != null && mTipView.getParent() != null) {
            mWindowManager.removeView(mTipView);
        }
        getvDelegate().pause();

    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusUtils.unregister(this);
        OkGo.getInstance().cancelTag(this);
        UIUtils.removeAllCallbacks();
        if (mLogWindowManager != null && mLogView != null && mLogView.getParent() != null) {
            mLogWindowManager.removeViewImmediate(mLogView);
        }

        if (getP() != null) {
            getP().detachV();
        }
        getvDelegate().destory();
        p = null;
        vDelegate = null;
        unbinder.unbind();

        //	  unregisterReceiver(netWorkReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getOptionsMenuId() > 0) {
            getMenuInflater().inflate(getOptionsMenuId(), menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public int getOptionsMenuId() {
        return 0;
    }

    @Override
    public void bindEvent() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        Log.i("CCCCC", "系统回收");
        super.onSaveInstanceState(outState, outPersistentState);
    }
}
