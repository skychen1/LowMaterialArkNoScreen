package org.wh.engineer.bean;

import android.app.Dialog;
import android.content.DialogInterface;

import org.wh.engineer.dto.InventoryDto;
import org.wh.engineer.dto.vo.InventoryVo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/15 15:50
 * 描述:        EVENTBUS的bean
 * 包名:        high.rivamed.myapplication.bean;
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class Event {

   /**
    * 整单入库的
    */
   public static class OrderVosEvent {

	public ArrayList<String> vos;
	public String          mDeviceId;

	public OrderVosEvent(ArrayList<String> vos, String deviceId) {
	   this.vos = vos;
	   this.mDeviceId = deviceId;
	}
   }
   /**
    * 触发重新连接reader
    */
   public static class ConnectReaderState {
	public boolean type;

	public ConnectReaderState(boolean type) {
	   this.type = type;
	}
   }

   public static class SelectOption {
	public String deviceId;
	public int id;

	public SelectOption(String deviceId,int id) {
	   this.deviceId = deviceId;
	   this.id = id;
	}
   }
   /**
    * 正在扫描:type :true是被占用无法开启扫描  start:true正在扫描
    */
   public static class StartScanType {
	public boolean type;
	public boolean start;

	public StartScanType(boolean type,boolean start) {
	   this.type = type;
	   this.start = start;
	}
   }
   /**
    * 页面顶部的连接状态改变
    */
   public static class EventPushMessageNum {
	public int num;

	public EventPushMessageNum(int num) {
	   this.num = num;
	}
   }
   /**
    * 连接状态
    */
   public static class XmmppConnect {
	public boolean connect;
	public boolean net;

	public XmmppConnect(boolean connect) {
	   this.connect = connect;
	}
	public XmmppConnect(boolean connect, boolean net) {
	   this.connect = connect;
	   this.net = net;
	}
   }
   /**
    * 登录界面触发server
    */
   public static class EventDoorV {

	public boolean mBoolean;

	public EventDoorV(boolean mBoolean) {
	   this.mBoolean = mBoolean;
	}
   }
   /**
    * 登录界面触发server
    */
   public static class EventServer {

	public boolean mBoolean;
	public int type;//1

	public EventServer(boolean mBoolean,int type) {
	   this.type = type;
	   this.mBoolean = mBoolean;
	}
   }
   /**
    * lock的点击日志刷新
    */
   public static class lockType {

	public int type;
	public int ret;
	public int witch;
	public String item;

	public lockType(int type,int ret,String item,int witch) {
	   this.type = type;
	   this.ret = ret;
	   this.item = item;
	   this.witch = witch;
	}
	public lockType(int type,int ret,String item) {
	   this.type = type;
	   this.ret = ret;
	   this.item = item;
	}
   }
   /**
    * FingerReg接收指纹成功
    */
   public static class EventFingerRegEnter {

	public int type;
	public String fingerData;
	public String msg;

	public EventFingerRegEnter(int type,String fingerData,String msg) {
	   this.type = type;
	   this.fingerData = fingerData;
	   this.msg = msg;
	}
   }
   /**
    * FingerReg发起注册
    */
   public static class EventFingerTime {

	public long time;

	public EventFingerTime(long time) {
	   this.time = time;
	}
   }

   /**
    * FingerReg 手指抬起的提示 0抬起，1正常
    */
   public static class EventFingerReg {

	public int msg;

	public EventFingerReg(int msg) {
	   this.msg = msg;
	}
   }
   /**
    * ExceptionDialog
    */
   public static class EventExceptionDialog {

	public boolean type;

	public EventExceptionDialog(boolean type) {
	   this.type = type;
	}
   }
   /**
    * 强开后的扫描 true
    */
   public static class EventStartScan {
	public boolean type;
	public String deviceIndentify;
	public EventStartScan(boolean type,String deviceIndentify) {
	   this.type = type;
	   this.deviceIndentify = deviceIndentify;
	}
   }
   /**
    * 门锁的检测消息 TYPE true没关门，false关门
    */
   public static class EventDoorStatus {

	public boolean type;
	public String  id;

	public EventDoorStatus(String id, boolean type) {
	   this.id = id;
	   this.type = type;
	}
   }

   /*
  快速开柜清理界面数据
   */
   public static class FastOutDataClear {

	public boolean type;

	public FastOutDataClear(boolean type) {
	   this.type = type;
	}
   }

   /*
   首页禁止点击按钮
    */
   public static class EventHomeEnable {

	public boolean type;

	public EventHomeEnable(boolean type) {
	   this.type = type;
	}
   }

   public static class EventLogType {

	public boolean type;

	public EventLogType(boolean type) {
	   this.type = type;
	}
   }

   /**
    * ic卡登陆
    */
   public static class EventICAndFinger {

	public int    type;
	public String date;

	public EventICAndFinger(String date, int type) {
	   this.type = type;

	   this.date = date;
	}
   }

   /**
    * 快速开柜重新扫描
    */
   public static class EventMealType {

	public List<InventoryVo> inventoryVos;

	public EventMealType(List<InventoryVo> inventoryVos) {
	   this.inventoryVos = inventoryVos;
	}
   }

   /**
    * 快速开柜重新扫描
    */
   public static class EventFastTimeStart {

	public boolean b;

	public EventFastTimeStart(boolean b) {
	   this.b = b;
	}
   }

   /**
    * 快速开柜重新扫描
    */
   public static class EventFastMoreScan {

	public boolean b;

	public EventFastMoreScan(boolean b) {
	   this.b = b;
	}
   }

   /**
    * 快速开柜重新开柜
    */
   public static class EventFastOpenDoor {

	public boolean b;

	public EventFastOpenDoor(boolean b) {
	   this.b = b;
	}
   }

   /**
    * 快速入柜的数据
    */
   public static class EventInDto {

	public InventoryDto outDto;
	public InventoryDto inDto;

	public EventInDto(InventoryDto outDto, InventoryDto inDto) {
	   this.outDto = outDto;
	   this.inDto = inDto;
	}
   }

   /**
    * 快速开柜的数据
    */
   public static class EventOutDto {

	public InventoryDto inventoryDto;
	public int          inSize;
	public int          outSize;
	public String       type;

	public EventOutDto(InventoryDto inventoryDto, int inSize, int outSize) {
	   this.inventoryDto = inventoryDto;
	   this.inSize = inSize;
	   this.outSize = outSize;
	}

	public EventOutDto(InventoryDto inventoryDto, int inSize, int outSize, String type) {
	   this.inventoryDto = inventoryDto;
	   this.inSize = inSize;
	   this.outSize = outSize;
	   this.type = type;
	}
   }

   /**
    * 耗材详情
    */
   public static class EventStockDetailVo {

	public InventoryVo vosBean;

	public EventStockDetailVo(InventoryVo dto) {
	   this.vosBean = dto;
	}
   }

   /**
    * 选择操作的数据传递需要绑定患者的
    */
   public static class EventOutBoxBingDto {

	public InventoryDto mInventoryDto;
	public InventoryDto mPatientDto;

	public EventOutBoxBingDto(InventoryDto dto) {
	   this.mInventoryDto = dto;
	}

	public EventOutBoxBingDto(InventoryDto dto, InventoryDto patientdto) {
	   this.mInventoryDto = dto;
	   this.mPatientDto = patientdto;
	}
   }

   /**
    * 选择操作的数据传递
    */
   public static class EventSelInOutBoxDto {

	public InventoryDto mInventoryDto;

	public EventSelInOutBoxDto(InventoryDto dto) {
	   this.mInventoryDto = dto;
	}
   }

   /**
    * 工程模式的
    */
   public static class EventTestIdAndPower {

	public String readerId;
	public String readerPower;

	public EventTestIdAndPower(String readerId, String readerPower) {
	   this.readerId = readerId;
	   this.readerPower = readerPower;
	}
   }
   /**
    * 工程模式的
    */
   public static class EventTestStopScan {

	public String readerId;

	public EventTestStopScan(String readerId) {
	   this.readerId = readerId;
	}
   }

   /**
    * 耗材的明细数据
    */
   public static class EventPatientId {

	public String patientId;
	public String hisPatientId;
	public String patientName;
	public int    status;

	public EventPatientId(String patientId,String hisPatientId,  int status,String patientName) {
	   this.patientId = patientId;
	   this.hisPatientId = hisPatientId;
	   this.status = status;
	   this.patientName = patientName;
	}
   }

   /**
    * 页面顶部的连接状态改变
    */
   public static class EventDoorList {

	public ArrayList<String> doorList;

	public EventDoorList(ArrayList<String> doorList) {
	   this.doorList = doorList;
	}
   }

   /**
    * 页面顶部的连接状态改变
    */
   public static class EventTitleConn {

	public boolean b;

	public EventTitleConn(boolean b) {
	   this.b = b;
	}
   }

   /**
    * 快速开柜数据的替换
    */
   public static class EventDate {

	public boolean b;
	public boolean moreScan;

	public EventDate(boolean b) {
	   this.b = b;
	}

	public EventDate(boolean b, boolean moreScan) {
	   this.b = b;
	   this.moreScan = moreScan;
	}
   }

   /**
    * 隐藏绑定患者的界面按钮
    */
   public static class EventButGone {

	public boolean b;

	public EventButGone(boolean b) {
	   this.b = b;
	}
   }

   /**
    * 快速开柜入柜提示
    */
   public static class EventOutTitleV {

	public boolean b;

	public EventOutTitleV(boolean b) {
	   this.b = b;
	}
   }

   /**
    * 耗材倒计时结束发起
    */
   public static class EventOverPut {

	public boolean b;

	public EventOverPut(boolean b) {
	   this.b = b;
	}
   }
   /**
    * 耗材倒计时结束发起
    */
   public static class EventOverNoEpc {

	public boolean b;

	public EventOverNoEpc(boolean b) {
	   this.b = b;
	}
   }
   /**
    * 主页倒计时结束发起
    */
   public static class EventOverHome {

	public boolean b;

	public EventOverHome(boolean b) {
	   this.b = b;
	}
   }
   /**
    * 关灯的倒计时
    */
   public static class EventLightCloss {

	public boolean b;

	public EventLightCloss(boolean b) {
	   this.b = b;
	}
   }

   /**
    * 检测灯
    */
   public static class EventStateLightType {

	public boolean b;

	public EventStateLightType(boolean b) {
	   this.b = b;
	}
   }
   /**
    * Loss原因
    */
   public static class EventLoss {

	public String string;

	public EventLoss(String string) {
	   this.string = string;
	}
   }

   public static class EventJump {

	public int jump;

	public EventJump(int jump) {
	   this.jump = jump;
	}
   }

   public static class EventLoading {

	public boolean loading;

	public EventLoading(boolean loading) {
	   this.loading = loading;
	}
   }
   public static class EventLoadingX {

	public boolean loading;

	public EventLoadingX(boolean loading) {
	   this.loading = loading;
	}
   }
   /**
    * 触摸
    */
   public static class EventTouch {

	public boolean touch;

	public EventTouch(boolean touch) {
	   this.touch = touch;
	}
   }

   /**
    * 关门 后给按钮设置为可以点击，InoutBoxTwoActivity
    */
   public static class EventGoneBtn {

	public String mGoneBtn;

	public EventGoneBtn(String mGoneBtn) {
	   this.mGoneBtn = mGoneBtn;
	}
   }

   /**
    * Frag的跳转
    */
   public static class EventFrag {

	public String type;

	public EventFrag(String type) {
	   this.type = type;
	}
   }

   public static class EventAct {

	public String mString = "";

	public EventAct(String trim) {
	   this.mString = trim;
	}
   }

   public static class EventClickBack {

	public String mString;

	public EventClickBack(String trim) {
	   this.mString = trim;
	}
   }

   public static class EventCheckbox {

	public InventoryVo                   vo;
//	public String                        mString;
//	public CreatTempPatientBean          creatTempPatientBean;
//	public String                        id;
//	public String                        deptId;
//	public String                        idNo;
//	public String                        scheduleDateTime;
//	public String                        operatingRoomNo;
//	public String                        sex;
//	public String                        operatingRoomNoName;
//	public String                        type;
//	public String                        mTempPatientId;
//	public String                        operationScheduleId;
//	public String                        mMedicalId;
//	public String                        mSurgeryId;
//	public String                        mHisPatientId;
//	public int                           position;
//	public boolean                       create;
//	public List<BoxSizeBean.DevicesBean> mTbaseDevices;

	public EventCheckbox(InventoryVo vo) {
	   this.vo = vo;
	}
//
//	public EventCheckbox(
//		String name, String mId, String idNo, String scheduleDateTime, String operatingRoomNo,
//		String operatingRoomNoName, String sex, String deptId, boolean create, String type,
//		int position, List<BoxSizeBean.DevicesBean> mTbaseDevices, String medicalId) {
//	   this.deptId = deptId;
//	   this.id = mId;
//	   this.mString = name;
//	   this.idNo = idNo;
//	   this.scheduleDateTime = scheduleDateTime;
//	   this.operatingRoomNo = operatingRoomNo;
//	   this.operatingRoomNoName = operatingRoomNoName;
//	   this.sex = sex;
//	   this.type = type;
//	   this.position = position;
//	   this.create = create;
//	   this.mTbaseDevices = mTbaseDevices;
//	   this.mMedicalId = medicalId;
//	}
//
//	public EventCheckbox(
//		String name, String id, String mTempPatientId, String operationScheduleId, String type,
//		int position, List<BoxSizeBean.DevicesBean> mTbaseDevices, String mMedicalId,
//		String mSurgeryId, String mHisPatientId, String mOperatingRoomNo) {
//	   this.mString = name;
//	   this.id = id;
//	   this.mTempPatientId = mTempPatientId;
//	   this.type = type;
//	   this.position = position;
//	   this.mTbaseDevices = mTbaseDevices;
//	   this.operationScheduleId = operationScheduleId;
//	   this.mMedicalId = mMedicalId;
//	   this.mSurgeryId = mSurgeryId;
//	   this.mHisPatientId = mHisPatientId;
//	   this.operatingRoomNo = mOperatingRoomNo;
//	}
   }

   /**
    * 禁止首页的左侧菜单栏
    */
   public static class HomeNoClickEvent {

	public boolean isClick;
	public String  door;

	public HomeNoClickEvent(boolean isClick, String door) {
	   this.isClick = isClick;
	   this.door = door;
	}
   }

   /**
    * 柜门的开关提示
    */
   public static class PopupEvent {

	public boolean isMute;
	public boolean openDoorType;
	public String  mString;
	public String  mEthId;

	public PopupEvent(boolean isMute,boolean openDoorType,String trim, String EthId) {
	   this.isMute = isMute;
	   this.openDoorType = openDoorType;
	   this.mString = trim;
	   this.mEthId = EthId;
	}
	public PopupEvent(boolean isMute,String trim, String EthId) {
	   this.isMute = isMute;
	   this.mString = trim;
	   this.mEthId = EthId;
	}
   }

   public static class activationEvent {

	public boolean         isActivation;
	public DialogInterface dialog;

	public activationEvent(boolean isActivation, DialogInterface dialog) {
	   this.isActivation = isActivation;
	   this.dialog = dialog;
	}
   }

   public static class dialogEvent {

	public Dialog dialog;
	public String deptId;
	public String storehouseCode;
	public String operationRoomNo;
	public String branchCode;
	public String deptName;

	public dialogEvent(
		String deptName, String branchCode, String deptId, String storehouseCode,
		Dialog dialog) {

	   this.deptId = deptId;
	   this.deptName = deptName;
	   this.storehouseCode = storehouseCode;
	   this.dialog = dialog;
	   this.branchCode = branchCode;
	}
   }

   public static class outBoxEvent {

	public Dialog dialog;
	public String type;
	public String context;
	public int    mIntentType;

	public outBoxEvent(String type, String text, Dialog dialog, int mIntentType) {

	   this.dialog = dialog;
	   this.type = type;
	   this.context = text;
	   this.mIntentType = mIntentType;

	}
   }

   public static class timelyDate {

	public String       type;
	public InventoryDto mInventoryDto;

	public timelyDate(String type, InventoryDto inventoryDto) {

	   this.type = type;
	   this.mInventoryDto = inventoryDto;

	}
   }

   public static class tempPatientEvent {

	public Dialog dialog;
	public String userName        = "";
	public String userSex         = "";
	public String idCard          = "";
	public String time            = "";
	public String roomNum         = "";//手术间名字
	public String operatingRoomNo = "";//手术间编号

	public tempPatientEvent(
		String userName, String roomNum, String roomId, String userSex, String idCard,
		String time, Dialog dialog) {

	   this.userName = userName;
	   this.roomNum = roomNum;
	   this.operatingRoomNo = roomId;
	   this.userSex = userSex;
	   this.dialog = dialog;
	   this.idCard = idCard;
	   this.time = time;
	}
   }

   public static class EventString {

	public String mString;

	public EventString(String text) {
	   this.mString = text;
	}
   }

   public static class EventTimelyCode {

	public String mCode;

	public EventTimelyCode(String mCode) {
	   this.mCode = mCode;
	}
   }

   public static class EventBoolean {

	public boolean mBoolean;
	public String  mId;

	public EventBoolean(boolean booleans, String id) {
	   this.mBoolean = booleans;
	   this.mId = id;
	}
   }

   public static class EventToast {

	public String mString;

	public EventToast(String text) {
	   this.mString = text;
	}
   }

   public static class EventRg {

	public String mString;

	public EventRg(String text) {
	   this.mString = text;
	}
   }

   /**
    * 发送是否开门
    */
   public static class EventOppenDoor {

	public String mString;

	public EventOppenDoor(String text) {
	   this.mString = text;
	}
   }

   /**
    * 硬件正常扫描返回
    */
   public static class EventDeviceCallBack {

	public String                     deviceId;
	public List<String>  epcs;

	public EventDeviceCallBack(String deviceId, List<String>  epcs) {
	   this.deviceId = deviceId;
	   this.epcs = epcs;
	}
   }

   /**
    * 硬件正常扫描返回（单个）
    */
   public static class EventOneEpcDeviceCallBack {

	public String deviceId;
	public String epc;

	public EventOneEpcDeviceCallBack(String deviceId, String epc) {
	   this.deviceId = deviceId;
	   this.epc = epc;
	}
   }

   /**
    * 硬件强开扫描返回（单个）
    */
   public static class EventOneEpcStrongOpenDeviceCallBack {

	public String deviceId;
	public String epc;

	public EventOneEpcStrongOpenDeviceCallBack(String deviceId, String epc) {
	   this.deviceId = deviceId;
	   this.epc = epc;
	}
   }

   /**
    * 硬件强开返回
    */
   public static class EventStrongOpenDeviceCallBack {

	public String                     deviceId;
	public List<String> epcs;

	public EventStrongOpenDeviceCallBack(String deviceId,List<String>  epcs) {
	   this.deviceId = deviceId;
	   this.epcs = epcs;
	}
   }

   /**
    * 刷新按钮
    */
   public static class EventButton {

	public boolean type;
	public boolean bing;
	public boolean mRemark;

	public EventButton(boolean type, boolean bing) {
	   this.type = type;
	   this.bing = bing;
	}

	public EventButton(boolean type, boolean bing, boolean mRemark) {
	   this.type = type;
	   this.bing = bing;
	   this.mRemark = mRemark;
	}
   }

   /**
    * 医嘱单领用--确认领用请求所需耗材信息
    */
   public static class EventBillStock implements Serializable {

	public List<BillStockResultBean.OrderDetailVo> transReceiveOrderDetailVosList;
	public OrderSheetBean.RowsBean                 orderSheetBean;
	public List<BoxSizeBean.DevicesBean>           tbaseDevices;

	public EventBillStock(
		OrderSheetBean.RowsBean orderSheetBean,
		List<BillStockResultBean.OrderDetailVo> transReceiveOrderDetailVosList,
		List<BoxSizeBean.DevicesBean> tbaseDevices) {
	   this.transReceiveOrderDetailVosList = transReceiveOrderDetailVosList;
	   this.orderSheetBean = orderSheetBean;
	   this.tbaseDevices = tbaseDevices;
	}
   }

   /**
    * 套组领用-选择套组后传递数据给获取耗材使用
    */
   public static class EventOutMealSuit {

	public OutMealBean.SuitesBean mOutMealSuitBeanResult;
	public boolean                isMute;

	public EventOutMealSuit(boolean isMute, OutMealBean.SuitesBean outMealSuitBeanResult) {
	   this.mOutMealSuitBeanResult = outMealSuitBeanResult;
	   this.isMute = isMute;
	}
   }

   /**
    * 套组领用--确认领用请求所需耗材信息
    */
   public static class EventBillOrder {

	public List<BillStockResultBean.OrderDetailVo> transReceiveOrderDetailVosList;
	public OrderSheetBean.RowsBean                 orderSheetBean;
	public List<BoxSizeBean.DevicesBean>           tbaseDevices;

	public EventBillOrder(
		OrderSheetBean.RowsBean orderSheetBean,
		List<BillStockResultBean.OrderDetailVo> transReceiveOrderDetailVosList,
		List<BoxSizeBean.DevicesBean> tbaseDevices) {
	   this.transReceiveOrderDetailVosList = transReceiveOrderDetailVosList;
	   this.orderSheetBean = orderSheetBean;
	   this.tbaseDevices = tbaseDevices;
	}
   }

   /**
    * 消息--删除消息
    */
   public static class EventMsgDelete {

	public PendingTaskBean.MessagesBean data;

	public EventMsgDelete(PendingTaskBean.MessagesBean data) {
	   this.data = data;
	}
   }
}
