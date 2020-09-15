package org.wh.engineer.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;

import org.wh.engineer.R;
import org.wh.engineer.base.App;

import java.util.Map;
import java.util.TreeMap;

public class MusicPlayer {
    private Context mContext;
    private static MusicPlayer sInstance;
    private Handler         mHandler = new Handler();

    public static class Type {
        public final static int LOGIN_SUC = 1;//您已登录成功.
        public final static int LOGOUT_SUC = 2;//您已退出登录.
        public final static int PLEASE_CLOSSDOOR = 16;//请关闭柜门
    }

    private SoundPool mSp;
    private Map<Integer, Integer> sSpMap;

    private MusicPlayer(Context context) {
        mContext = context;
        sSpMap = new TreeMap<Integer, Integer>();
        mSp = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
        sSpMap.put(Type.LOGIN_SUC, mSp.load(mContext, R.raw.login_suc, 1));
        sSpMap.put(Type.LOGOUT_SUC, mSp.load(mContext, R.raw.logout_suc, 1));
        sSpMap.put(Type.PLEASE_CLOSSDOOR, mSp.load(mContext, R.raw.clossdoor, 1));

    }

    static {
        sInstance = new MusicPlayer(App.getInstance().getApplicationContext());
    }

    public static MusicPlayer getInstance() {
        return sInstance;
    }

    public void play(int type) {
        if (sSpMap.get(type) == null)
            return;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSp.play(sSpMap.get(type), 1, 1, 0, 0, 1);
            }
        }, 500);
    }
}