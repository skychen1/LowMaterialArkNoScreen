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
        public final static int OPEN_DOOR=1;//柜门已开
        public final static int LOGIN_ERROR=2;//登陆失败
        public final static int PLEASE_CLOSSDOOR = 16;//请关闭柜门

    }

    private SoundPool mSp;
    private Map<Integer, Integer> sSpMap;

    private MusicPlayer(Context context) {
        mContext = context;
        sSpMap = new TreeMap<Integer, Integer>();
        mSp = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
        sSpMap.put(Type.OPEN_DOOR, mSp.load(mContext, R.raw.door_open, 1));
        sSpMap.put(Type.LOGIN_ERROR, mSp.load(mContext, R.raw.login_error, 1));
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