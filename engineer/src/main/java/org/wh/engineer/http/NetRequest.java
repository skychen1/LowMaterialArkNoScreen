package org.wh.engineer.http;

import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import org.litepal.LitePal;
import org.wh.engineer.bean.Event;
import org.wh.engineer.bean.UpDateTokenBean;
import org.wh.engineer.dbmodel.AccountVosBean;
import org.wh.engineer.dto.UserLoginDto;
import org.wh.engineer.utils.EventBusUtils;
import org.wh.engineer.utils.LogUtils;
import org.wh.engineer.utils.SPUtils;
import org.wh.engineer.utils.ToastUtils;
import org.wh.engineer.utils.UIUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.wh.engineer.base.App.MAIN_URL;
import static org.wh.engineer.base.App.SYSTEMTYPE;
import static org.wh.engineer.base.App.mAppContext;
import static org.wh.engineer.base.App.mTitleConn;
import static org.wh.engineer.cont.Constants.ACCESS_TOKEN;
import static org.wh.engineer.cont.Constants.ERROR_1000;
import static org.wh.engineer.cont.Constants.ERROR_1001;
import static org.wh.engineer.cont.Constants.ERROR_1010;
import static org.wh.engineer.cont.Constants.ERROR_200;
import static org.wh.engineer.cont.Constants.FINGER_VERSION;
import static org.wh.engineer.cont.Constants.KEY_ACCOUNT_ID;
import static org.wh.engineer.cont.Constants.REFRESH_TOKEN;
import static org.wh.engineer.cont.Constants.SAVE_DEPT_CODE;
import static org.wh.engineer.cont.Constants.THING_CODE;
import static org.wh.engineer.utils.UIUtils.removeAllAct;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/4 16:51
 * 描述:        网络接口集合类
 * 包名:        high.rivamed.myapplication.http
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class NetRequest {

   private static final String TAG = "NetRequest";

   // 设置本类为单例模式
   private static NetRequest instance;
   private static Gson       mGson;
   public static String     sThingCode;

   public static NetRequest getInstance() {
	sThingCode = SPUtils.getString(UIUtils.getContext(), THING_CODE);
	if (instance == null) {
	   synchronized (NetRequest.class) {
		if (instance == null) {
		   instance = new NetRequest();
		   mGson = new Gson();

		}
	   }
	}
	return instance;
   }

   /**
    * Post请求无token 文件上传
    */
   private void PostRequestWithFile(
	   String url, Map<String, String> map, String fileKey, File file, Object tag,
	   FileUpResult netResult) {

	OkGo.<String>post(url).tag(tag).params(fileKey, file)//待上传文件
		.params(map)//其他参数
		.execute(new FileUpCallBack(url, map, fileKey, file, tag, netResult, false));
   }

   /**
    * Post请求有token 文件上传
    */
   private void PostTokenRequestWithFile(
	   String url, Map<String, String> map, String fileKey, File file, Object tag,
	   FileUpResult netResult) {

	OkGo.<String>post(url).tag(tag)
		.headers("tokenId", SPUtils.getString(UIUtils.getContext(), ACCESS_TOKEN))
		.params(fileKey, file)//待上传文件
		.params(map)//其他参数
		.execute(new FileUpCallBack(url, map, fileKey, file, tag, netResult, true));
   }

   /**
    * Post请求无token
    */
   private void PostRequest(String url, String date, Object tag, NetResult netResult) {

	OkGo.<String>post(url).tag(tag)
		.upJson(date)
		.execute(new MyCallBack(url, date, tag, netResult, false, false));
   }

   /**
    * Post请求有token
    */
   private void PostTokenRequest(String url, String date, Object tag, NetResult netResult) {

	OkGo.<String>post(url).tag(tag)
		.headers("tokenId", SPUtils.getString(UIUtils.getContext(), ACCESS_TOKEN))
		.upJson(date)
		.execute(new MyCallBack(url, date, tag, netResult, false, true));
   }

   /**
    * Get请求有token
    */
   private void GetTokenRequest(
	   String url, Map<String, String> map, Object tag, NetResult netResult) {

	OkGo.<String>get(url).tag(tag)
		.headers("tokenId", SPUtils.getString(UIUtils.getContext(), ACCESS_TOKEN))
		.params(map)
		.execute(new MyCallBack(url, map, tag, netResult, true, true));
   }

   /**
    * Get请求无token
    */
   private void GetRequest(
	   String url, Map<String, String> map, Object tag, NetResult netResult) {

	OkGo.<String>get(url).tag(tag)
		.params(map)
		.execute(new MyCallBack(url, map, tag, netResult, true, false));
   }

   /**
    * 预注册和激活的时候获取部件名称和ID
    */
   public void getDeviceInfosDate(
	   String url,  Object tag, NetResult netResult) {
	OkGo.<String>get(url + NetApi.URL_TEST_FINDDEVICE).tag(tag)
		.execute(new MyCallBack(url, null, tag, netResult, true, false));
   }

   /**
    * 预注册
    */
   public void setSaveRegisteDate(String TBaseThing, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_TEST_REGISTE;
	PostRequest(urls, TBaseThing, tag, netResult);
   }

   /**
    * 激活
    */
   public void setSaveActiveDate(String TBaseThing, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_TEST_ACTIVE;
	PostRequest(urls, TBaseThing, tag, netResult);
   }

   /**
    * 版本检测
    */
   public void checkVer(Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_GET_VER;
	Map<String, String> map = new HashMap<>();
	map.put("systemType", SYSTEMTYPE);
	GetRequest(urls, map, tag, netResult);
   }

   /**
    * 用户登录
    */
   public void userLogin(String account, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_USER_LOGIN;
	PostRequest(urls, account, tag, netResult);
   }

   /**
    * 用户登录:userId
    */
   public void userLoginByUserId(String account, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_USER_LOGIN;
	PostRequest(urls, account, tag, netResult);
   }

   /**
    * 绑定指纹
    */
   public void registerFinger(String json, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_USER_REGISTER_FINGER;
	PostTokenRequest(urls, json, tag, netResult);
   }

   /**
    * 登录设置
    */
   public void userLoginInfo(Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_USER_LOGININFO;
	GetTokenRequest(urls, null, tag, netResult);
   }

   /**
    * 查询个人信息
    */
   public void findUserInfo(Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_USER_FINDUSERINFO;
	GetTokenRequest(urls, null, tag, netResult);
   }
   /**
    * 绑定腕带
    */
   public void registerIdCard(String json, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_USER_REGISTERWAIDAI;
	PostTokenRequest(urls, json, tag, netResult);
   }

   /**
    * 腕带解绑
    */
   public void unRegisterIdCard(String json, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_USER_UNREGISTERWAIDAI;
	PostTokenRequest(urls, json, tag, netResult);
   }

   /**
    * 指纹登录
    */
   public void validateLoginFinger(String json, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_USER_LOGIN;
	PostRequest(urls, json, tag, netResult);
   }

   /**
    * 紧急登录密码修改
    */
   public void emergencySetting(String pwd, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_USER_EMERGENCY_PWD;
	Map<String, String> map = new HashMap<>();
	map.put("account.emergencyPwd", pwd);
	GetTokenRequest(urls, map, tag, netResult);

   }

   /**
    * IdCard登录
    */
   public void validateLoginIdCard(String json, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_USER_LOGIN;
	PostRequest(urls, json, tag, netResult);
   }

   /**
    * 重置密码
    */
   public void resetPassword(String json, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_USER_RESET_PASSWORD;
	PostTokenRequest(urls, json, tag, netResult);
   }

   /**
    * 获取柜子个数
    */
   public void loadBoxSize(Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_HOME_BOXSIZE;
	Map<String, String> map = new HashMap<>();
	map.put("thingId", sThingCode);
	map.put("deviceType", FINGER_VERSION);//3.0柜子2，普通柜子是1
	GetRequest(urls, map, tag, netResult);
   }

   /**
    * 获取logo
    */
   public void loadLogo(Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_LOGO;
	GetRequest(urls, null, tag, netResult);
   }

   /**
    * 数据恢复
    */
   public void getRecoverDate(String sn, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_TEST_SNQUERY;
	Map<String, String> map = new HashMap<>();
	map.put("sn", sn);
	GetRequest(urls, map, tag, netResult);
   }

   /**
    * 查询院区信息
    */
   public void getHospBranch(Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_TEST_FIND_BRANCH;
	GetRequest(urls, null, tag, netResult);
   }

   /**
    * 根据院区编码查询科室信息
    */
   public void getHospDept(
	   String deptNamePinYin, String branchId, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_TEST_FIND_DEPT;
	Map<String, String> map = new HashMap<>();
	map.put("deptNamePinYin", deptNamePinYin);
	map.put("branchId", branchId);
	GetRequest(urls, map, tag, netResult);
   }

   /**
    * 根据科室查询库房情况
    */
   public void getHospBydept(String deptId, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_TEST_FIND_BYDEPT;
	Map<String, String> map = new HashMap<>();
	map.put("deptId", deptId);
	map.put("sthType", "1");
	GetRequest(urls, map, tag, netResult);

   }

   /**
    * 根据科室查询手术室信息
    */
   public void getHospRooms(String deptId, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_TEST_FIND_OPERROOMS;
	Map<String, String> map = new HashMap<>();
	map.put("deptId", deptId);
	GetTokenRequest(urls, map, tag, netResult);
   }

   //   /**
   //    * 耗材操作开柜扫描提交数据(快速开柜入柜)
   //    */
   //   public void putAllInEPCDate(
   //	   String deviceInventoryVos, Object tag, NetResult netResult) {
   //	String urls = MAIN_URL + NetApi.URL_QUERY_ALL_IN;
   //	PostTokenRequest(urls, deviceInventoryVos, tag, netResult);
   //   }

   /**
    * 查询所有的配置项
    */
   public void findThingConfigDate(Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_THING_CONFIG_FIND;
	Map<String, String> map = new HashMap<>();
	map.put("nodeId", sThingCode);//thingid改成nodeid
	map.put("grade", "3");//'配置级别: 0 全局、1:科室、2: 库房、3:设备',
	map.put("systemType", SYSTEMTYPE);//配置项
	GetRequest(urls, map, tag, netResult);
   }


//   /**
//    * 获取账号权限菜单（左侧、选择操作）
//    */
//   public void getAuthorityMenu(Object tag, NetResult netResult) {
//	String urls = MAIN_URL + NetApi.URL_AUTHORITY_MENU;
//	Map<String, String> map = new HashMap<>();
//	map.put("systemType", SYSTEMTYPE);
//	OkGo.<String>get(urls).tag(tag)
//		.headers("tokenId", SPUtils.getString(UIUtils.getContext(), ACCESS_TOKEN))
//		.params(map)
//		.execute(new MyCallBack2(urls, map, tag, netResult, true, true));
//   }

   /**
    * 换新token
    */
   public void updateToken(Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_REFRESH_TOKEN;
	String json =
		"{\"systemType\": \"" + SYSTEMTYPE + "\",\"accessToken\": {\"refreshToken\": \"" +
		SPUtils.getString(UIUtils.getContext(), REFRESH_TOKEN) + "\"}}";
	PostRequest(urls, json, tag, netResult);
   }

   /**
    * 获取设备中所有的耗材
    */
   public void getUnEntCstDate(Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_UNENT_GET_ALLCST;
	Map<String, String> map = new HashMap<>();
	map.put("thingId", sThingCode);
	GetRequest(urls, map, tag, netResult);
   }

   /**
    * 获取设备离线账户信息
    */
   public void getUnNetUseDate(Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_UNENT_GET_LIST_ACCOUNT;
	Map<String, String> map = new HashMap<>();
	map.put("systemType", SYSTEMTYPE);
	map.put("deptId", SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
	GetRequest(urls, map, tag, netResult);
   }

   /**
    * 获取离线手术间信息
    */
   public void getUnEntFindOperation(Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_UNENT_GET_FIND_OPERATIONROOM;
	Map<String, String> map = new HashMap<>();
	map.put("thingId", sThingCode);
	GetRequest(urls, map, tag, netResult);
   }

   /**
    * 无网后来网的登录
    *
    * @param tag
    * @param netResult
    */
   public void getUnNetLogin(String account, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_USER_UNNET_LOGIN;
	PostRequest(urls, account, tag, netResult);
   }

   private class MyCallBack extends StringCallback {

	private String    url;
	private Object    date;
	private Object    tag;
	private NetResult netResult;
	private boolean   isGet;//是否是get请求
	private boolean   isToken;//是否有token

	public MyCallBack(
		String url, Object date, Object tag, NetResult netResult, boolean isGet,
		boolean isToken) {
	   super();

	   this.url = url;
	   this.date = date;
	   this.tag = tag;
	   this.netResult = netResult;
	   this.isGet = isGet;
	   this.isToken = isToken;
	}

	@Override
	public void onError(Response<String> response) {
	   LogUtils.w(TAG, "onError mTitleConn： "  +mTitleConn);
	   if (netResult != null) {
		netResult.onError(response.code() + "");
	   }
	   if (mTitleConn) {
		EventBusUtils.post(new Event.XmmppConnect(false));
	   }
	   if (response.code() == -1) {
//		EventBusUtils.post(new Event.XmmppConnect(false));
	   } else {
		ToastUtils.showShortToast("请求失败  (" + response.code() + ")");
	   }

	   LogUtils.e(TAG, "onError 请求URL： " + url+mTitleConn);
	   LogUtils.e(TAG, "onError 请求URL： " + response.code());
	   LogUtils.e(TAG, "onError 请求Body： " + mGson.toJson(date));
	   LogUtils.e(TAG, "onError 返回Body： " + response.body());
	}

	   @Override
	   public void onFinish() {
		   super.onFinish();
		   LogUtils.i("----","onFinish");
	   }

	   @Override
	public void onSuccess(Response<String> response) {
	   if (!mTitleConn) {
		EventBusUtils.post(new Event.XmmppConnect(true,true));
	   }
	  // UnNetCstUtils.putUnNetOperateYes(tag);//提交离线耗材和重新获取在库耗材数据

		try {
			JSONObject jsonObject = JSON.parseObject(response.body());
			if (null == jsonObject.getString("opFlg") ||
					jsonObject.getString("opFlg").equals(ERROR_200)) {//正常
				if (netResult != null) {
					netResult.onSucceed(response.body());
				}
			} else {
				String opFlg = jsonObject.getString("opFlg");
				if (opFlg.equals(ERROR_1010)) {
					LogUtils.e(TAG, "请求URL： " + url);
					LogUtils.e(TAG, "请求Body： " + mGson.toJson(date));
					LogUtils.e(TAG, "返回Body： " + response.body());
					ToastUtils.showShortToast("后台系统异常，请联系实施人员！");
					if (netResult != null) {
						netResult.onSucceed(response.body());
					}
				} else if (opFlg.equals(ERROR_1000)) {//Token过期
					if (!TextUtils.isEmpty(UIUtils.getRefreshToken())) {
						setUpDateToken(response);//换token
					}
				} else if (opFlg.equals(ERROR_1001)) {//刷新TOKEN过期   需要重新登录
					putUnNetLoginDate();//重新登录获取token
				}else {
					if (netResult != null) {
						netResult.onSucceed(response.body());
					}
				}
			}
		} catch (Exception e) {
			LogUtils.e(TAG, "Exception 请求URL： " + url);
			LogUtils.e(TAG, "Exception 请求Body： " + mGson.toJson(date));
			LogUtils.e(TAG, "Exception 返回Body： " + response.body());
			e.printStackTrace();
		}


	 /*  try {
		JSONObject jsonObject = JSON.parseObject(response.body());
		if (null == jsonObject.getString("opFlg") ||
		    jsonObject.getString("opFlg").equals(ERROR_200)) {//正常
		   if (netResult != null) {
			netResult.onSucceed(response.body());
		   }
		} else {
		   String opFlg = jsonObject.getString("opFlg");
		   if (opFlg.equals(ERROR_1010)) {
			LogUtils.e(TAG, "请求URL： " + url);
			LogUtils.e(TAG, "请求Body： " + mGson.toJson(date));
			LogUtils.e(TAG, "返回Body： " + response.body());
			ToastUtils.showShortToast("后台系统异常，请联系实施人员！");
			if (netResult != null) {
			   netResult.onSucceed(response.body());
			}
		   } else if (opFlg.equals(ERROR_1000)) {//Token过期
			if (!TextUtils.isEmpty(UIUtils.getRefreshToken())) {
			   setUpDateToken(response);//换token
			}
		   } else if (opFlg.equals(ERROR_1001)) {//刷新TOKEN过期   需要重新登录
			putUnNetLoginDate();//重新登录获取token
		   }
		}
	   } catch (Exception e) {
		LogUtils.e(TAG, "Exception 请求URL： " + url);
		LogUtils.e(TAG, "Exception 请求Body： " + mGson.toJson(date));
		LogUtils.e(TAG, "Exception 返回Body： " + response.body());
		e.printStackTrace();
	   }*/
	}

	/**
	 * 离线后来网，重新登录获取token
	 */
	private void putUnNetLoginDate() {
	   String accountId = SPUtils.getString(mAppContext, KEY_ACCOUNT_ID);
	   LogUtils.i("OkGo","putUnNetLoginDate  accountId    "+accountId);
	   AccountVosBean beans = LitePal.where("accountid = ? ", accountId)
		   .findFirst(AccountVosBean.class);
	   LogUtils.i("OkGo","beans     "+mGson.toJson(beans));
	   UserLoginDto userLoginDto = new UserLoginDto();
	   UserLoginDto.AccountBean accountBean = new UserLoginDto.AccountBean();
	   accountBean.setAccountName(beans.getAccountName());
	   accountBean.setPassword(beans.getPassword());
	   accountBean.setSalt(beans.getSalt());
	   userLoginDto.setAccount(accountBean);
	   userLoginDto.setSystemType(SYSTEMTYPE);
	   userLoginDto.setThingId(SPUtils.getString(UIUtils.getContext(), THING_CODE));
	   getUnNetLogin(mGson.toJson(userLoginDto), tag, new BaseResult() {
		@Override
		public void onSucceed(String result) {
		   UpDateTokenBean tokenBean = mGson.fromJson(result, UpDateTokenBean.class);
		   setUnNetToken(tokenBean);
		}

		@Override
		public void onError(String result) {
		   ToastUtils.showShortToast("登录状态已经过期，请重新登录");
		   if (tag instanceof Activity) {
			//UIUtils.putOrderId(tag);
			removeAllAct((Activity) tag);
		   }
		}
	   });
	}

	/**
	 * 更换token后进行重新请求
	 *
	 * @param response
	 */
	private void setUpDateToken(Response<String> response) {
	   updateToken(tag, new BaseResult() {
		@Override
		public void onSucceed(String result) {
		   UpDateTokenBean tokenBean = mGson.fromJson(result, UpDateTokenBean.class);
		   if (tokenBean.getOpFlg().equals("200")) {
			setUnNetToken(tokenBean);//设置token
		   } else {
			ToastUtils.showShortToast("登录状态已经过期，请重新登录");
			if (tag instanceof Activity) {
			 //  UIUtils.putOrderId(tag);
			   removeAllAct((Activity) tag);
			}
		   }
		}
	   });
	}

	/**
	 * 设置token重新请求
	 *
	 * @param tokenBean
	 */
	public void setUnNetToken(UpDateTokenBean tokenBean) {

	   String tokenId = tokenBean.getAccessToken().getTokenId();
	   String refreshToken = tokenBean.getAccessToken().getRefreshToken();
	   SPUtils.putString(UIUtils.getContext(), ACCESS_TOKEN, tokenId);
	   SPUtils.putString(UIUtils.getContext(), REFRESH_TOKEN, refreshToken);
	   if (isGet) {
		if (isToken) {
		   GetTokenRequest(url, (Map<String, String>) date, tag, netResult);
		} else {
		   GetRequest(url, (Map<String, String>) date, tag, netResult);
		}
	   } else {
		if (isToken) {
		   PostTokenRequest(url, (String) date, tag, netResult);
		} else {
		   PostRequest(url, (String) date, tag, netResult);
		}
	   }
	}
   }

   private class MyCallBack2 extends StringCallback {

	private String    url;
	private Object    date;
	private Object    tag;
	private NetResult netResult;
	private boolean   isGet;//是否是get请求
	private boolean   isToken;//是否有token

	public MyCallBack2(
		String url, Object date, Object tag, NetResult netResult, boolean isGet,
		boolean isToken) {
	   super();

	   this.url = url;
	   this.date = date;
	   this.tag = tag;
	   this.netResult = netResult;
	   this.isGet = isGet;
	   this.isToken = isToken;
	}

	@Override
	public void onError(Response<String> response) {
	   if (mTitleConn) {
		EventBusUtils.post(new Event.XmmppConnect(false));
	   }
	   if (netResult != null) {
		netResult.onError(response.code() + "");
	   }
	   LogUtils.w(TAG, "onError 请求URL： " + url);
	   LogUtils.w(TAG, "onError 请求Body： " + mGson.toJson(date));
	   LogUtils.w(TAG, "onError 返回Body： " + response.body());
	}

	@Override
	public void onSuccess(Response<String> response) {
	   if (!mTitleConn) {
		EventBusUtils.post(new Event.XmmppConnect(true,true));
	   }
	   if (netResult != null) {
		netResult.onSucceed(response.body());
		//		LogUtils.w(TAG, "MyCallBack2 请求URL： " + url);
		//		LogUtils.w(TAG, "MyCallBack2 请求Body： " + mGson.toJson(date));
		//		LogUtils.w(TAG, "MyCallBack2 返回Body： " + response.body());
	   }
	}
   }

   private class FileUpCallBack extends StringCallback {

	private String       url;
	private Object       date;
	private String       fileKey;//上传文件
	private File         file;//上传文件
	private Object       tag;
	private FileUpResult netResult;
	private boolean      isToken;//是否有token

	public FileUpCallBack(
		String url, Object date, String fileKey, File file, Object tag, FileUpResult netResult,
		boolean isToken) {
	   super();

	   this.url = url;
	   this.date = date;
	   this.fileKey = fileKey;
	   this.file = file;
	   this.tag = tag;
	   this.netResult = netResult;
	   this.isToken = isToken;
	}

	@Override
	public void uploadProgress(Progress progress) {
	   super.uploadProgress(progress);
	   netResult.uploadProgress(progress);
	}

	@Override
	public void onError(Response<String> response) {

	   if (netResult != null) {
		netResult.onError(response.code() + "");
	   }
	   if (mTitleConn) {
		EventBusUtils.post(new Event.XmmppConnect(false));
	   }
	   if (response.code() == -1) {
		//		ToastUtils.showShortToast("服务器异常，请检查网络！");
	   } else {
		ToastUtils.showShortToast("请求失败  (" + response.code() + ")");
	   }
	   LogUtils.e(TAG, "onError 请求URL： " + url);
	   LogUtils.e(TAG, "onError 请求URL： " + response.code());
	   LogUtils.e(TAG, "onError 请求Body： " + mGson.toJson(date));
	   LogUtils.e(TAG, "onError 请求文件： " + file.getAbsolutePath());
	   LogUtils.e(TAG, "onError 返回Body： " + response.body());
	}

	@Override
	public void onSuccess(Response<String> response) {
	   if (!mTitleConn) {
		EventBusUtils.post(new Event.XmmppConnect(true,true));
	   }
	   try {
		JSONObject jsonObject = JSON.parseObject(response.body());
		if (null == jsonObject.getString("opFlg") ||
		    jsonObject.getString("opFlg").equals(ERROR_200)) {//正常
		   if (netResult != null) {
			netResult.onSucceed(response.body());
		   }
		} else {
		   String opFlg = jsonObject.getString("opFlg");
		   if (opFlg.equals(ERROR_1010)) {
			LogUtils.e(TAG, "请求URL： " + url);
			LogUtils.e(TAG, "请求Body： " + mGson.toJson(date));
			LogUtils.e(TAG, "onError 请求文件： " + file.getAbsolutePath());
			LogUtils.e(TAG, "返回Body： " + response.body());
			ToastUtils.showShortToast("后台系统异常，请联系实施人员！");
			if (netResult != null) {
			   netResult.onSucceed(response.body());
			}
		   } else if (opFlg.equals(ERROR_1000)) {//Token过期
			if (!TextUtils.isEmpty(UIUtils.getRefreshToken())) {
			   setUpDateToken(response);//换token
			}
		   } else if (opFlg.equals(ERROR_1001)) {//刷新TOKEN过期   需要重新登录
			putUnNetLoginDate();//重新登录获取token
		   }
		}
	   } catch (Exception e) {
		LogUtils.e(TAG, "Exception 请求URL： " + url);
		LogUtils.e(TAG, "Exception 请求Body： " + mGson.toJson(date));
		LogUtils.e(TAG, "Exception 返回Body： " + response.body());
		e.printStackTrace();
	   }
	}

	/**
	 * 离线后来网，重新登录获取token
	 */
	private void putUnNetLoginDate() {
	   String accountId = SPUtils.getString(UIUtils.getContext(), KEY_ACCOUNT_ID);

	   AccountVosBean beans = LitePal.where("accountid = ? ", accountId)
		   .findFirst(AccountVosBean.class);
	   UserLoginDto userLoginDto = new UserLoginDto();
	   UserLoginDto.AccountBean accountBean = new UserLoginDto.AccountBean();
	   accountBean.setAccountName(beans.getAccountName());
	   accountBean.setPassword(beans.getPassword());
	   accountBean.setSalt(beans.getSalt());
	   userLoginDto.setAccount(accountBean);
	   userLoginDto.setSystemType(SYSTEMTYPE);
	   userLoginDto.setThingId(SPUtils.getString(UIUtils.getContext(), THING_CODE));
	   getUnNetLogin(mGson.toJson(userLoginDto), tag, new BaseResult() {
		@Override
		public void onSucceed(String result) {
		   UpDateTokenBean tokenBean = mGson.fromJson(result, UpDateTokenBean.class);
		   setUnNetToken(tokenBean);
		}

		@Override
		public void onError(String result) {
		   ToastUtils.showShortToast("登录状态已经过期，请重新登录");
		   if (tag instanceof Activity) {
			//UIUtils.putOrderId(tag);
			removeAllAct((Activity) tag);
		   }
		}
	   });
	}

	/**
	 * 更换token后进行重新请求
	 *
	 * @param response
	 */
	private void setUpDateToken(Response<String> response) {
	   updateToken(tag, new BaseResult() {
		@Override
		public void onSucceed(String result) {
		   UpDateTokenBean tokenBean = mGson.fromJson(result, UpDateTokenBean.class);
		   if (tokenBean.getOpFlg().equals("200")) {
			setUnNetToken(tokenBean);//设置token
		   } else {
			ToastUtils.showShortToast("登录状态已经过期，请重新登录");
			if (tag instanceof Activity) {
			   //UIUtils.putOrderId(tag);
			   removeAllAct((Activity) tag);
			}
		   }
		}
	   });
	}

	/**
	 * 设置token重新请求
	 *
	 * @param tokenBean
	 */
	public void setUnNetToken(UpDateTokenBean tokenBean) {

	   String tokenId = tokenBean.getAccessToken().getTokenId();
	   String refreshToken = tokenBean.getAccessToken().getRefreshToken();
	   SPUtils.putString(UIUtils.getContext(), ACCESS_TOKEN, tokenId);
	   SPUtils.putString(UIUtils.getContext(), REFRESH_TOKEN, refreshToken);
	   if (isToken) {
		PostTokenRequestWithFile(url, (Map<String, String>) date, fileKey, file, tag,
						 netResult);
	   } else {
		PostRequestWithFile(url, (Map<String, String>) date, fileKey, file, tag, netResult);
	   }
	}
   }

   /**
    * 更新本地Token
    */
   private void updateLocalToken(String data) {
	//	LoginBean loginBean = mGson.fromJson(data, LoginBean.class);
	//	UIUtils.getCache().put(ACCESS_TOKEN, loginBean.getAccess_token(), loginBean.getExpires_in());//登录成功的token
	//	UIUtils.getCache().put(REFRESH_TOKEN, loginBean.getRefresh_token(), ACache.TIME_DAY);//登录成功的refresh_token

   }
}
