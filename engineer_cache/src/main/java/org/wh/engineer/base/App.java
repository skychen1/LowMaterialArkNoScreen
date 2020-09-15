package org.wh.engineer.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import androidx.multidex.MultiDex;

import com.baidu.idl.main.facesdk.utils.PreferencesUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.rivamed.libdevicesbase.utils.LogUtils;
import com.rivamed.libdevicesbase.utils.ToastUtils;
import com.rivamed.libidcard.IdCardManager;
import com.ruihua.libconsumables.ConsumableManager;
import com.ruihua.reader.ReaderManager;
import com.ruihua.reader.ReaderProducerType;

import org.litepal.LitePal;
import org.wh.engineer.bean.PushFormDateBean;
import org.wh.engineer.cont.Constants;
import org.wh.engineer.http.MyHttpLoggingInterceptor;
import org.wh.engineer.utils.ACache;
import org.wh.engineer.utils.CrashHandler;
import org.wh.engineer.utils.SPUtils;
import org.wh.engineer.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import cn.rivamed.Eth002Manager;
import okhttp3.OkHttpClient;

import static com.rivamed.libidcard.IdCardProducerType.TYPE_NET_AN_DE;
import static org.wh.engineer.cont.Constants.READER_NAME;
import static org.wh.engineer.cont.Constants.READER_NAME_COLU;
import static org.wh.engineer.cont.Constants.READER_NAME_RODINBELL;
import static org.wh.engineer.cont.Constants.SAVE_SYSTEMTYPE;
import static org.wh.engineer.cont.Constants.SYSTEMTYPES_2;
import static org.wh.engineer.cont.Constants.SYSTEMTYPES_3;

//import com.ayvytr.okhttploginterceptor.LoggingLevel;

public class App extends Application {

   public static final String                            TAG                    = "BaseApplication";
   public static       int                               READER_TIME            = 3000;     //扫描时间
   public static       int                               ANIMATION_TIME         = 1000;     //动画延时时间
   public static       int                               COUNTDOWN_TIME         = 20000;         //无操作退出时间
   public static       int                               CLOSSLIGHT_TIME        = 30000;         //无操作关灯
   public static       int                               HOME_COUNTDOWN_TIME    = 60000;         //无操作退出时间
   public static       int                               REMOVE_LOGFILE_TIME    = 30;         //天
   public static       int                               NOEPC_LOGINOUT_TIME    = 20000;         //未扫描到耗材退出
   public static       int                               VOICE_NOCLOSSDOOR_TIME = 600000;         //没关门提示
   private static      App                               instance;
   public static PushFormDateBean mPushFormDateBean      = new PushFormDateBean();
   public static       List<PushFormDateBean.OrdersBean> mPushFormOrders        = new ArrayList<>();

   /**
    * 缓存
    */
   private static ACache mAppCache;
   public static String         SYSTEMTYPE =null ;
   public static String         MAIN_URL   = null;
   public static boolean        mTitleConn = false;
   public static boolean        mTitleMsg  = false;
   public static Context        mAppContext;
   public static DisplayMetrics mDm;

   public static synchronized App getInstance() {
	return instance;
   }

   /**
    * 获取系统上下文：用于ToastUtil类
    */
   public static Context getAppContext() {
	return mAppContext;
   }

   @Override
   protected void attachBaseContext(Context base) {
	super.attachBaseContext(base);
	MultiDex.install(this);
   }

   @Override
   public void onCreate() {
	super.onCreate();
//	if (BuildConfig.DEBUG) {
//	   StrictMode.setThreadPolicy(
//		   (new StrictMode.ThreadPolicy.Builder()).detectAll().penaltyLog().build());
//	   StrictMode.setVmPolicy(
//		   (new android.os.StrictMode.VmPolicy.Builder()).detectAll().penaltyLog().build());
//	   LeakCanary.install(this);
//	}

	mAppContext = getApplicationContext();
	mPushFormDateBean.setOrders(mPushFormOrders);
	LitePal.initialize(mAppContext);//数据库初始化

	instance = this;
	PreferencesUtil.initPrefs(mAppContext);
	initOkGo();
	Log.i("ererere","  SYSTEMTYPE 111     "+SYSTEMTYPE+"    "+SPUtils.getString(UIUtils.getContext(), SAVE_SYSTEMTYPE));
	if (SPUtils.getString(UIUtils.getContext(), SAVE_SYSTEMTYPE)!=null){
	   SYSTEMTYPE = SPUtils.getString(UIUtils.getContext(), SAVE_SYSTEMTYPE);
	}else {
	   SYSTEMTYPE = SYSTEMTYPES_2;
	}
	registDevice(SYSTEMTYPE);//注册硬件
	Log.i("ererere","  SYSTEMTYPE 222     "+SYSTEMTYPE);
	LogUtils.setDebugMode(false);
	//设备基础module中有使用，需要注册初始化
	ToastUtils.register(this);
	CrashHandler crashHandler = CrashHandler.getInstance();
	crashHandler.init(this);

	WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
	if (wm != null) {
	   mDm = new DisplayMetrics();
	   wm.getDefaultDisplay().getMetrics(mDm);
	}
   }

   public void registDevice(final String SYSTEMTYPE) {

	new Thread(new Runnable() {
	   @Override
	   public void run() {
	      if (SYSTEMTYPE.equals(SYSTEMTYPES_3)){
		   int i = ReaderManager.getManager().connectReader(ReaderProducerType.TYPE_NET_RODINBELL);
		   Log.i("dddda", "fdfdfd             " + i);
		   IdCardManager.getIdCardManager().connectIdCard(mAppContext, TYPE_NET_AN_DE);
		   int connect = ConsumableManager.getManager().connect();
		   Log.i("dddda", "connect             " + connect);
		}else {
		   if (SPUtils.getString(mAppContext, READER_NAME) == null ||
			 SPUtils.getString(mAppContext, READER_NAME).equals(READER_NAME_RODINBELL)) {
			ReaderManager.getManager().connectReader(ReaderProducerType.TYPE_NET_RODINBELL);
		   } else if (SPUtils.getString(mAppContext, READER_NAME) != null &&
				  SPUtils.getString(mAppContext, READER_NAME).equals(READER_NAME_COLU)) {
			ReaderManager.getManager().connectReader(ReaderProducerType.TYPE_NET_COLU);
		   }
		   //li模块
		   Eth002Manager.getEth002Manager().startService(8012);
		}


	   }
	}).start();
   }

   /**
    * 加载网络框架
    */
   private void initOkGo() {
	OkHttpClient.Builder builder = new OkHttpClient.Builder();
	//log相关
	MyHttpLoggingInterceptor loggingInterceptor = new MyHttpLoggingInterceptor("OkGo");
		loggingInterceptor.setPrintLevel(
			MyHttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
		loggingInterceptor.setColorLevel(
			Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
		builder.addInterceptor(loggingInterceptor);

//	MyLoggingInterceptor interceptor = new MyLoggingInterceptor(LoggingLevel.BODY);
//	builder.addInterceptor(interceptor);                                 //添加OkGo默认debug日志
	//第三方的开源库，使用通知显示当前请求的log，不过在做文件下载的时候，这个库好像有问题，对文件判断不准确
	//builder.addInterceptor(new ChuckInterceptor(this));

	//超时时间设置，默认60秒
	builder.readTimeout(Constants.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);      //全局的读取超时时间
	builder.writeTimeout(Constants.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);     //全局的写入超时时间
	builder.connectTimeout(Constants.DEFAULT_CONNECTMILLISECONDS,
				     TimeUnit.MILLISECONDS);   //全局的连接超时时间

	//自动管理cookie（或者叫session的保持），以下几种任选其一就行
	//builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));            //使用sp保持cookie，如果cookie不过期，则一直有效
	//	builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));              //使用数据库保持cookie，如果cookie不过期，则一直有效
	//builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));            //使用内存保持cookie，app退出后，cookie消失
	//	builder.cookieJar(new CookieJarImpl(
	//		new DBCookieStore(this)));              //使用数据库保持cookie，如果cookie不过期，则一直有效
	// 其他统一的配置
	// 详细说明看GitHub文档：https://github.com/jeasonlzy/
	OkGo.getInstance().init(this)                           //必须调用初始化
		.setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置会使用默认的
		.setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
		.setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
		.setRetryCount(
			0);                              //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0

   }

   public boolean ifActivityRun(String className) {
	Intent intent = new Intent();
	intent.setClassName(getPackageName(), className);
	Log.d(TAG, "getPackageName" + getPackageName() + "className:" + className);
	if (getPackageManager().resolveActivity(intent, 0) != null) {
	   // 说明系统中不存在这个activity
	   return true;
	}
	return false;
   }

   /**
    * 检测app是否存活
    *
    * @return
    */
   public int getAppSatus() {

	ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
	List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(20);

	//判断程序是否在栈顶
	if (list.get(0).topActivity.getPackageName().equals(getPackageName())) {
	   return 1;
	} else {
	   //判断程序是否在栈里
	   for (ActivityManager.RunningTaskInfo info : list) {
		if (info.topActivity.getPackageName().equals(getPackageName())) {
		   return 2;
		}
	   }
	   return 3;//栈里找不到，返回3
	}
   }

}
