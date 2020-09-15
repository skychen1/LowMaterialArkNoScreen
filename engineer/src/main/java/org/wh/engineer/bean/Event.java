package org.wh.engineer.bean;

import android.app.Dialog;

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

	public static class  EventLoginErr{
		String errMsg;

		public String getErrMsg() {
			return errMsg;
		}

		public void setErrMsg(String errMsg) {
			this.errMsg = errMsg;
		}

		public EventLoginErr(String errMsg) {
			this.errMsg = errMsg;
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
    * 关灯的倒计时
    */
   public static class EventLightCloss {

	public boolean b;

	public EventLightCloss(boolean b) {
	   this.b = b;
	}
   }

   public static class EventLoading {

	public boolean loading;

	public EventLoading(boolean loading) {
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
}
