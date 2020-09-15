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
import org.wh.engineer.utils.UnNetCstUtils;
import org.wh.engineer.views.LoadingDialog;

import java.io.File;
import java.util.HashMap;
import java.util.List;
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
import static org.wh.engineer.cont.Constants.SAVE_DEPT_NAME;
import static org.wh.engineer.cont.Constants.SAVE_STOREHOUSE_CODE;
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
    * 获取耗材流水
    */
   public void loadRunWate(
	   String page, String size, String deviceCode, String term, String startTime, String endTime,
	   String status, Object tag, NetResult netResult) {

	String urls = MAIN_URL + NetApi.URL_HOME_RUNWATE;
	Map<String, String> map = new HashMap<>();
	map.put("thingId", sThingCode);
	map.put("pageNo", page);
	map.put("pageSize", size);
	map.put("startTime", startTime);
	map.put("endTime", endTime);
	map.put("status", status);
	map.put("deviceId", deviceCode);
	map.put("term", term);
	String json = mGson.toJson(map);
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
    * 耗材效期监控
    */
   public void materialControl(Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_STOCKSTATUS_TOP;
	Map<String, String> map = new HashMap<>();
	map.put("thingId", sThingCode);
	GetRequest(urls, map, tag, netResult);
   }

   /**
    * 库存详情
    */
   public void getStockMiddleDetails(
	   String nameOrSpecQueryCon, String deviceCode, int mStopFlag, Object tag,
	   NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_STOCKSTATUS_DETAILS;
	Map<String, String> map = new HashMap<>();
	map.put("thingId", sThingCode);
	map.put("nameOrSpecQueryCon", nameOrSpecQueryCon);
	map.put("deviceId", deviceCode);
	map.put("expireStatus", mStopFlag + "");
	GetRequest(urls, map, tag, netResult);
   }
   /**
    * 库存监控
    */
   public void getStockLeftDown(String deviceCode, Object tag,
	   NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_STOCKSTATUS_MONITORING;
	Map<String, String> map = new HashMap<>();
	map.put("thingId", sThingCode);
	map.put("deviceId", deviceCode);
	GetRequest(urls, map, tag, netResult);
   }
   /**
    * 未确认耗材
    */
   public void getRightUnconfDate(
	   String deviceCode, String mTrim, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_STOCKUNCON_RIGHT;
	Map<String, String> map = new HashMap<>();
	map.put("thingId", sThingCode);
	map.put("sthId", SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
	map.put("deviceId", deviceCode);
	map.put("nameOrSpecQueryCon", mTrim);
	GetRequest(urls, map, tag, netResult);
   }

   /**
    * 查询单个耗材
    */
   public void getStockDetailDate(
	   int mExpireStatus, String deviceCode, String cstId, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_STOCK_DETAIL;
	Map<String, String> map = new HashMap<>();
	map.put("cstId", cstId);
	map.put("expireStatus", mExpireStatus + "");
	map.put("deviceId", deviceCode);
	map.put("thingId", sThingCode);
	GetRequest(urls, map, tag, netResult);
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
    * 耗材操作开柜扫描提交数据(快速开柜出柜)
    */
   public void putOutAndInEPCDate(
	   String deviceInventoryVos, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_QUERY_OUTANDIN;
	PostTokenRequest(urls, deviceInventoryVos, tag, netResult);
   }

   /**
    * 耗材操作开柜扫描查询数据
    */
   public void putEPCDate(String deviceInventoryVos, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_OPERATE_QUERY;
	PostTokenRequest(urls, deviceInventoryVos, tag, netResult);
   }

   /**
    * 耗材操作确认操作
    */
   public void putOperateYes(String operateTCstInventory, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_OPERATE_INOUTBOX_YES;
	PostTokenRequest(urls, operateTCstInventory, tag, netResult);
   }

   /**
    * 耗材操作开柜扫描查询数据(领用退回)
    */
   public void putEPCLyThDate(String deviceInventoryVos, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_OPERATE_QUERY_LYTH;
	PostTokenRequest(urls, deviceInventoryVos, tag, netResult);
   }

   /**
    * 耗材操作确认操作(领用退回)
    */
   public void putOperateLyThYes(String operateTCstInventory, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_OPERATE_INOUTBOX_YES_LYTH;
	PostTokenRequest(urls, operateTCstInventory, tag, netResult);
   }

   /**
    * 耗材操作确认操作(快速开柜)
    */
   public void putAllOperateYes(String operateTCstInventory, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_QUERY_ALL_YES;
	PostTokenRequest(urls, operateTCstInventory, tag, netResult);
   }

   /**
    * 查询非本科室的库房    调拨查
    */
   public void getOperateDbDialog(
	   String deptId, String branchCode, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_OPERATE_DB_YES;
	Map<String, String> map = new HashMap<>();
	map.put("deptId", deptId);
	map.put("branchId", branchCode);
	GetTokenRequest(urls, map, tag, netResult);
   }

   /**
    * 盘点
    */
   public void startTimelyScan(String tCstInventoryDto, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_TIMELY_ONE;
	PostTokenRequest(urls, tCstInventoryDto, tag, netResult);
   }

   /**
    * 盘亏
    */
   public void getLossesDate(
	   String tCstInventoryDto, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_TIMELY_LOSSES;
	PostTokenRequest(urls, tCstInventoryDto, tag, netResult);
   }

   /**
    * 盘盈
    */
   public void getProfitDate(
	   String tCstInventoryDto, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_TIMELY_PROFIT;
	PostTokenRequest(urls, tCstInventoryDto, tag, netResult);
   }

   /**
    * 盘点后的详情
    */
   public void getDetailDate(
	   String tCstInventoryDto, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_TIMELY_DETAIL;
	PostTokenRequest(urls, tCstInventoryDto, tag, netResult);

   }

   /**
    * 查询患者信息（包含临时患者）
    */
   public void findSchedulesDate(
	   String optienNameOrId, String deptName, int pageNo, int pageSize, Object tag,
	   NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_PATIENTS_FIND;
	Map<String, String> map = new HashMap<>();
	map.put("deptId", SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
	map.put("thingId", sThingCode);
	map.put("patientNameOrId", optienNameOrId);
	map.put("operationOrDept", deptName);
	map.put("pageNo", pageNo + "");
	map.put("pageSize", pageSize + "");
	GetTokenRequest(urls, map, tag, netResult);
   }

   /**
    * 查询所有的未绑定临时患者
    */
   public void findTempPatients(
	   String optienNameOrId, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_FIND_TEMP_PATIENTS;
	Map<String, String> map = new HashMap<>();
	map.put("deptId", SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
	map.put("patientNameOrId", optienNameOrId);
	PostTokenRequest(urls, mGson.toJson(map), tag, netResult);
   }

   /**
    * 临时患者与在院患者进行关联
    */
   public void tempPatientConnPatient(
	   String json, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_TEMP_PATIENT_CONN_PATIENT;
	PostTokenRequest(urls, json, tag, netResult);
   }

   /**
    * 查询所有在院患者信息
    */
   public void findInPatientPage(
	   String patientNameOrId, int pageNo, int pageSize, Object tag, LoadingDialog.Builder dialog,
	   NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_FIND_IN_PATIENT_PAGE;
	Map<String, String> map = new HashMap<>();
	map.put("patientNameOrId", patientNameOrId);
	map.put("deptId", SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
	map.put("pageNo", pageNo + "");
	map.put("pageSize", pageSize + "");
	GetTokenRequest(urls, map, tag, netResult);
   }

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

   /**
    * 使用记录的患者列表
    */
   public void getFindPatientDate(
	   String string,String startTime, String endTime, int page, int rows, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_FIND_PATIENT;
	Map<String, String> map = new HashMap<>();
	map.put("patientNameOrId", string);
	map.put("startTime", startTime);
	map.put("endTime", endTime);
	map.put("deptId", SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
	map.put("pageSize", rows + "");
	map.put("pageNo", page + "");
	map.put("thingId", sThingCode);
	PostTokenRequest(urls, mGson.toJson(map), tag, netResult);
   }

   /**
    * 使用记录详情
    */
   public void getFindEpcDetails(String patientId,String mHisPatientId , int status, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_FIND_EPC_DETAILS;
	Map<String, String> map = new HashMap<>();
	map.put("patientindexid", patientId);
	map.put("hisPatientId", mHisPatientId);
	map.put("status", status + "");
	map.put("thingId", sThingCode);

	GetTokenRequest(urls, map, tag, netResult);
   }

   /**
    * 医嘱单领用-顶部医嘱单列表
    */
   public void findPatientOrderSheetDate(
	   String orderId, String pageNo, String pageSize, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_RECEIVEORDER_LISTALL;
	Map<String, String> map = new HashMap<>();
	map.put("orderId", orderId);
	map.put("pageNo", pageNo);
	map.put("pageSize", pageSize);
	GetTokenRequest(urls, map, tag, netResult);
   }

   /**
    * 医嘱单领用-根据医嘱单ID查询单柜耗材的库存
    */
   public void findStockByOrderId(
	   String Id, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_RECEIVEORDER_FINDBYORDERID;
	Map<String, String> map = new HashMap<>();
	map.put("orderId", Id);
	map.put("thingId", SPUtils.getString(UIUtils.getContext(), THING_CODE));
	GetTokenRequest(urls, map, tag, netResult);
   }

   /**
    * 医嘱单领用-根据EPC获取耗材
    */
   public void findBillStockByEpc(
	   String json, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_RECEIVEORDER_CONTAINORDERCST;
	PostTokenRequest(urls, json, tag, netResult);

   }

   /**
    * 医嘱单领用-确认领用耗材
    */
   public void sureReceiveOrder(
	   String json, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_RECEIVEORDER_TWOOUTBYRECEIVEORDER;
	PostTokenRequest(urls, json, tag, netResult);
   }

   /**
    * 套组领用-套组列表
    */
   public void findOrderCstPlanDate(Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_CSTPLAN_LISTFORPAD;
	Map<String, String> map = new HashMap<>();
	map.put("deptId", SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
	GetTokenRequest(urls, map, tag, netResult);
   }

   /**
    * 套组领用-查询单个套组的耗材详情
    */
   public void findOrderCstListById(
	   String cstPlanId, String thingId, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_CSTPLAN_FINDCSTLIST;
	Map<String, String> map = new HashMap<>();
	map.put("suiteId", cstPlanId);
	map.put("thingId", thingId);
	GetTokenRequest(urls, map, tag, netResult);
   }

   /**
    * 套组领用-根据EPC查询耗材详情
    */
   public void findOrderCstListByEpc(
		   String json, Object tag, LoadingDialog.Builder dialog, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_CSTPLAN_FINDCSTANDCOMPARETOCSTPLAN;
	PostTokenRequest(urls, json, tag, netResult);
   }

   /**
    * 消息-查询待办任务列表
    */
   public void getPendingTaskList(Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_FIND_MESSAGE_BY_ACCOUNTID;
	GetTokenRequest(urls, null, tag, netResult);
   }

   /**
    * 消息-删除待办任务
    */
   public void deleteMessageById(String id, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_DELETE_MESSAGE_BY_ID;
	Map<String, String> map = new HashMap<>();
	map.put("message.messageId", id);
	GetTokenRequest(urls, map, tag, netResult);
   }

   /**
    * 套组领用-领用套组
    */
   public void useOrderCst(String json, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_CSTPLAN_OPERATETCSTINVENTORY;
	PostTokenRequest(urls, json, tag, netResult);
   }

   /**
    * 请领单领用-在退出应用前提交套组信息生成消息
    */
   public void submitOrderCstInfo(String json, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_RECEIVEORDER_SAVERECEIVEORDERMSG;
	PostTokenRequest(urls, json, tag, netResult);
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

   /**
    * 无网后来网的登录
    *
    * @param tag
    * @param netResult
    */
   public void putUnNetCstDate(String json, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_UNENT_CST_OFFLINE;
	PostRequest(urls, json, tag, netResult);
   }

   /**
    * 召唤机器人
    */
   public void CallRobot(
	   Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_ROBOT;
	Map<String, String> map = new HashMap<>();
	map.put("sthId", SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
	GetTokenRequest(urls, map, tag, netResult);
   }

   /**
    * 获取所有人脸照
    */
   public void getAllFace(String startDate,  Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_FACE_GET_ALL;
	Map<String, String> map = new HashMap<>();
	map.put("systemType", SYSTEMTYPE);
	map.put("startDate", startDate);
//	map.put("endDate", endDate);
	GetRequest(urls, map, tag, netResult);
   }

   /**
    * 绑定人脸
    */
   public void bindFace(String faceBase64, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_FACE_BIND;
	Map<String, String> map = new HashMap<>();
	map.put("faceBase64", faceBase64);
	String json = mGson.toJson(map);
	PostTokenRequest(urls, json, tag, netResult);
   }

   /**
    * 异常处理界面
    */
   public void getExceptionLeftDate(String json , Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_EXCEPTION_LEFT;
	PostTokenRequest(urls, json, tag, netResult);
   }
   /**
    * 异常记录界面
    */
   public void getExceptionRightDate(String json , Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_EXCEPTION_RIGHT;
	PostTokenRequest(urls, json, tag, netResult);
   }

   /**
    * 异常确认关联操作人
    */
   public void getExceptionOperateUnknow(String json , Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_EXCEPTION_OOERATEUNKNOW;
	PostTokenRequest(urls, json, tag, netResult);
   }
   /**
    * 异常确认关联
    */
   public void getExceptionRelevance(String json , Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_EXCEPTION_RELEVANCE;
	PostTokenRequest(urls, json, tag, netResult);
   }
   /**
    * 查询需要关联的用户信息
    */
   public void getExceptionOperator(int page,int size,String name , Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_EXCEPTION_OPERATOR;
	Map<String, String> map = new HashMap<>();
	map.put("term", name);
	map.put("pageNo", page+"");
	map.put("pageSize", size+"");
	map.put("deptId", SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
	GetTokenRequest(urls, map, tag, netResult);
   }

   /**
    * 查询待入库信息
    */
   public void getLoginCstMsg(int page,int size,Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_LOGIN_CSTMSG;
	Map<String, String> map = new HashMap<>();
	map.put("pageNo", page+"");
	map.put("pageSize", size+"");
	map.put("sthId", SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
	GetRequest(urls, map, tag, netResult);
   }

   /**
    * 盘点单提交
    */
   public void putSavePadPdDate(String json , Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_TIMELY_PUTSAVEPADPD;
	PostTokenRequest(urls, json, tag, netResult);
   }
   /**
    * 入库统计
    */
   public void putInboxCountDate(String json , Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_OPERATE_STORAGECST_COUNT;
	PostTokenRequest(urls, json, tag, netResult);
   }

   /**
    * 开始录像（正常）
    forcein 强开，opendoor  正常开门
    * @param deviceId
    * @param tag
    * @param netResult
    */
   public void startRecordVideo(String deviceId, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_RECORDVIDEO;
	Map<String, String> map = new HashMap<>();
	map.put("thingType", "001");//高值柜
	map.put("sceneCode", "001");//高值柜
	map.put("operateType", "opendoor");
	map.put("departName", SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_NAME));
	map.put("departCode",  SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
	map.put("thingId", deviceId);
	PostTokenRequest(urls, mGson.toJson(map), tag, netResult);
   }
   /**
    * 开始录像（强开）
    forcein 强开，opendoor  正常开门
    * @param deviceId
    * @param tag
    * @param netResult
    */
   public void startForceRecordVideo(String deviceId, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_RECORDVIDEO;
	Map<String, String> map = new HashMap<>();
	map.put("thingType", "001");//高值柜
	map.put("sceneCode", "001");//高值柜
	map.put("operateType", "forcein");
	map.put("departName", SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_NAME));
	map.put("departCode",  SPUtils.getString(UIUtils.getContext(), SAVE_DEPT_CODE));
	map.put("thingId", deviceId);
	PostRequest(urls, mGson.toJson(map), tag, netResult);
   }

   /**
    * 结束录像
    * @param businessNo
    * @param tag
    * @param netResult
    */
   public void stopRecordVideo(String  businessNo, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_STOPVIDEO;
	Map<String, String> map = new HashMap<>();
	map.put("sceneCode", "001");//高值柜
	map.put("businessNo", businessNo);
	map.put("thingType", "001");
	PostRequest(urls, mGson.toJson(map), tag, netResult);
   }
   /**
    * 查询入库单
    */
   public void putInboxOrderDate(int page, int rows,String text,Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_OPERATE_STORAGECST_ORDER;
	Map<String, String> map = new HashMap<>();
	map.put("sthId", SPUtils.getString(UIUtils.getContext(), SAVE_STOREHOUSE_CODE));
	map.put("filterStr", text);
	map.put("pageSize", rows + "");
	map.put("pageNo", page + "");
	PostTokenRequest(urls, mGson.toJson(map), tag, netResult);
   }
   /**
    * 修改入库单状态
    */
   public void putOrderStatus(List<String> orderids, Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_ISORDER_STATUS;
	Map<String, List<String>> map = new HashMap<>();
	map.put("orderIds", orderids);
	PostTokenRequest(urls, mGson.toJson(map), tag, netResult);
   }
   /**
    * 获取低于下限数量
    */
   public void getFloorList(Object tag, NetResult netResult) {
	String urls = MAIN_URL + NetApi.URL_FLOORLIST;
	Map<String,String> map = new HashMap<>();
	map.put("thingId", sThingCode);
	PostRequest(urls, mGson.toJson(map), tag, netResult);
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
	public void onSuccess(Response<String> response) {
	   if (!mTitleConn) {
		EventBusUtils.post(new Event.XmmppConnect(true));
	   }
	   UnNetCstUtils.putUnNetOperateYes(tag);//提交离线耗材和重新获取在库耗材数据

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
			UIUtils.putOrderId(tag);
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
			   UIUtils.putOrderId(tag);
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
		EventBusUtils.post(new Event.XmmppConnect(true));
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
		EventBusUtils.post(new Event.XmmppConnect(true));
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
			UIUtils.putOrderId(tag);
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
			   UIUtils.putOrderId(tag);
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
