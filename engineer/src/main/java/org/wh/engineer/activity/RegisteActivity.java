package org.wh.engineer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import net.lucode.hackware.magicindicator.MagicIndicator;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.wh.engineer.R;
import org.wh.engineer.base.SimpleActivity;
import org.wh.engineer.bean.Event;
import org.wh.engineer.fragment.RegisteFrag;
import org.wh.engineer.fragment.RegisteLockFrag;
import org.wh.engineer.fragment.RegisteRecoverFrag;
import org.wh.engineer.service.ScanService;
import org.wh.engineer.utils.UIUtils;
import org.wh.engineer.utils.WifiUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static org.wh.engineer.activity.SplashActivity.mIntentService;
import static org.wh.engineer.base.App.mTitleConn;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/11 10:59
 * 描述:        工程模式
 * 包名:        high.rivamed.myapplication.activity
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class RegisteActivity extends SimpleActivity {

    private static final String TAG = "RegisteActivity";

    @BindView(R.id.base_tab_tv_title)
    TextView mBaseTabTvTitle;
    @BindView(R.id.base_tab_tv_name)
    TextView mBaseTabTvName;
    @BindView(R.id.base_tab_icon_right)
    de.hdodenhof.circleimageview.CircleImageView mBaseTabIconRight;
    @BindView(R.id.base_tab_tv_outlogin)
    ImageView mBaseTabOutLogin;
    @BindView(R.id.base_tab_btn_msg)
    ImageView mBaseTabBtnMsg;

    @BindView(R.id.registe_tl)
    MagicIndicator mRegisteTl;
    public ImageView mBaseTabBtnConn;
    public static ViewPager mRegisteViewpager;
    //private String[] mKeys = {"设备注册/激活", "数据恢复","Reader设置","锁/灯/IC卡/指纹仪","人脸设置"};
    private String[] mKeys = {"设备注册/激活", "数据恢复", "锁/灯/IC卡/指纹仪"};
    private RegistePagerAdapter mPagerAdapter;

    /**
     * 设备title连接状态
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTitleConnEvent(Event.XmmppConnect event) {
        mTitleConn = event.connect;
        selTitleIcon();
        hasNetWork(mTitleConn, event.net);
    }

    public void selTitleIcon() {
        if (mTitleConn) {
            if (mBaseTabBtnConn != null) {
                mBaseTabBtnConn.setEnabled(true);
            }
        } else {
            if (mBaseTabBtnConn != null) {
                mBaseTabBtnConn.setEnabled(false);
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_registe_layout;
    }

    @Override
    public void initDataAndEvent(Bundle savedInstanceState) {
        mRegisteViewpager = findViewById(R.id.registe_viewpager);
        mBaseTabBtnConn = (ImageView) findViewById(R.id.base_tab_conn);
        mBaseTabTvTitle.setVisibility(View.VISIBLE);
        mBaseTabIconRight.setVisibility(View.GONE);
        mBaseTabTvName.setVisibility(View.GONE);
        mBaseTabBtnMsg.setVisibility(View.GONE);
        mBaseTabTvTitle.setText("工程模式");
        if (mIntentService != null) {
            stopService(mIntentService);
        } else {
            mIntentService = new Intent(this, ScanService.class);
            stopService(mIntentService);
        }


        if (WifiUtils.isWifi(mContext) == 0) {
            hasNetWork(false, false);
            mBaseTabBtnConn.setEnabled(false);
        }
        mPagerAdapter = new RegistePagerAdapter(getSupportFragmentManager());
        mRegisteViewpager.setAdapter(mPagerAdapter);
        mRegisteViewpager.setCurrentItem(0);
        mRegisteViewpager.setOffscreenPageLimit(3);
//	mRegisteTl.setViewPager(mRegisteViewpager, mKeys);
        UIUtils.initPvTabLayouts(mKeys, mRegisteViewpager, mRegisteTl);

    }

    @Override
    public void onBindViewBefore() {

    }

    @Override
    public Object newP() {
        return null;
    }

    @OnClick({R.id.base_tab_tv_outlogin})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.base_tab_tv_outlogin:
                startActivity(new Intent(this, EntranceActivity.class));
                finish();
                break;
        }
    }

    private class RegistePagerAdapter extends FragmentStatePagerAdapter {

        public RegistePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return RegisteFrag.newInstance();
            } else if (position == 1) {
                return RegisteRecoverFrag.newInstance();
            } else if (position == 2) {
                return RegisteLockFrag.newInstance();
            }
            return null;
         /*   else if (position == 3) {
               // return RegisteReaderFrag.newInstance();
            } else {
                //return RegisteFaceFrag.newInstance();
            }*/
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mKeys[position];
        }

        @Override
        public int getCount() {
            return mKeys == null ? 0 : mKeys.length;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRegisteViewpager = null;
//	ReaderManager.getManager().unRegisterCallback();
//	Eth002Manager.getEth002Manager().unRegisterCallBack();
    }
}
