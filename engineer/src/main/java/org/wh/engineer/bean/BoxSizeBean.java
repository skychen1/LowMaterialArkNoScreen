package org.wh.engineer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/22 15:57
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class BoxSizeBean implements Serializable {

   /**
    * operateSuccess : true
    * id : 0
    * opFlg : 200
    * pageNo : 1
    * pageSize : 20
    * thingId : 4028efd06ea12378016ea1d02914000d
    * devices : [{"deviceId":"4028efd06ea12378016ea1d02937000f","deviceName":"高值柜(上)","identification":null,"ip":null,"parent":"4028efd06ea12378016ea1d02928000e","remark":null,"status":0,"thingId":"4028efd06ea12378016ea1d02914000d","operationTime":"2019-11-25","dictId":"ff80818166c7b5d00166c8fbcbf50016","deviceType":"10","cabinetType":"1","cabinetNum":"192.168.100.221"},{"deviceId":"4028efd06ea12378016ea1d0294f0014","deviceName":"高值柜(下)","identification":null,"ip":null,"parent":"4028efd06ea12378016ea1d02928000e","remark":null,"status":0,"thingId":"4028efd06ea12378016ea1d02914000d","operationTime":"2019-11-25","dictId":"ff80818166c7b5d00166c8fbcbf50016","deviceType":"10","cabinetType":"-1","cabinetNum":"192.168.100.221"},{"deviceId":"4028efd06ea12378016ea1d029610017","deviceName":"高值柜(单)","identification":null,"ip":null,"parent":"4028efd06ea12378016ea1d02928000e","remark":null,"status":0,"thingId":"4028efd06ea12378016ea1d02914000d","operationTime":"2019-11-25","dictId":"ff80818166c7b5d00166c8fbcbf50016","deviceType":"10","cabinetType":"0","cabinetNum":"192.168.100.223"}]
    * deviceTypeVo : {"deviceVos":[{"devices":[{"deviceId":null,"deviceName":"全部","identification":null,"ip":null,"parent":null,"remark":null,"status":0,"thingId":null,"operationTime":null,"dictId":null,"deviceType":null,"cabinetType":null,"cabinetNum":null}],"deviceName":"高值柜(上)"},{"devices":[{"deviceId":"4028efd06ea12378016ea1d029610017","deviceName":"高值柜(单)","identification":null,"ip":null,"parent":"4028efd06ea12378016ea1d02928000e","remark":null,"status":0,"thingId":"4028efd06ea12378016ea1d02914000d","operationTime":"2019-11-25","dictId":"ff80818166c7b5d00166c8fbcbf50016","deviceType":"10","cabinetType":"0","cabinetNum":"192.168.100.223"}],"deviceName":"高值柜(上)"},{"devices":[{"deviceId":"4028efd06ea12378016ea1d02937000f","deviceName":"高值柜(上)","identification":null,"ip":null,"parent":"4028efd06ea12378016ea1d02928000e","remark":null,"status":0,"thingId":"4028efd06ea12378016ea1d02914000d","operationTime":"2019-11-25","dictId":"ff80818166c7b5d00166c8fbcbf50016","deviceType":"10","cabinetType":"1","cabinetNum":"192.168.100.221"},{"deviceId":"4028efd06ea12378016ea1d0294f0014","deviceName":"高值柜(下)","identification":null,"ip":null,"parent":"4028efd06ea12378016ea1d02928000e","remark":null,"status":0,"thingId":"4028efd06ea12378016ea1d02914000d","operationTime":"2019-11-25","dictId":"ff80818166c7b5d00166c8fbcbf50016","deviceType":"10","cabinetType":"-1","cabinetNum":"192.168.100.221"}],"deviceName":"高值柜(上)"}]}
    */

   private boolean operateSuccess;
   private int                id;
   private String             opFlg;
   private int                pageNo;
   private int                pageSize;
   private String             thingId;
   private DeviceTypeVoBean   deviceTypeVo;
   private List<DevicesBean> devices;

   public boolean isOperateSuccess() { return operateSuccess;}

   public void setOperateSuccess(boolean operateSuccess) { this.operateSuccess = operateSuccess;}

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public String getOpFlg() { return opFlg;}

   public void setOpFlg(String opFlg) { this.opFlg = opFlg;}

   public int getPageNo() { return pageNo;}

   public void setPageNo(int pageNo) { this.pageNo = pageNo;}

   public int getPageSize() { return pageSize;}

   public void setPageSize(int pageSize) { this.pageSize = pageSize;}

   public String getThingId() { return thingId;}

   public void setThingId(String thingId) { this.thingId = thingId;}

   public DeviceTypeVoBean getDeviceTypeVo() { return deviceTypeVo;}

   public void setDeviceTypeVo(DeviceTypeVoBean deviceTypeVo) { this.deviceTypeVo = deviceTypeVo;}

   public List<DevicesBean> getDevices() { return devices;}

   public void setDevices(List<DevicesBean> devices) { this.devices = devices;}

   public static class DeviceTypeVoBean  implements Serializable{

	private List<DeviceVosBean> deviceVos;

	public List<DeviceVosBean> getDeviceVos() { return deviceVos;}

	public void setDeviceVos(List<DeviceVosBean> deviceVos) { this.deviceVos = deviceVos;}

	public static class DeviceVosBean  implements Serializable{

	   /**
	    * devices : [{"deviceId":null,"deviceName":"全部","identification":null,"ip":null,"parent":null,"remark":null,"status":0,"thingId":null,"operationTime":null,"dictId":null,"deviceType":null,"cabinetType":null,"cabinetNum":null}]
	    * deviceName : 高值柜(上)
	    */

	   private String deviceName;
	   private List<DevicesBeanX> devices;

	   public String getDeviceName() { return deviceName;}

	   public void setDeviceName(String deviceName) { this.deviceName = deviceName;}

	   public List<DevicesBeanX> getDevices() { return devices;}

	   public void setDevices(List<DevicesBeanX> devices) { this.devices = devices;}

	   public static class DevicesBeanX  implements Serializable{

		/**
		 * deviceId : null
		 * deviceName : 全部
		 * identification : null
		 * ip : null
		 * parent : null
		 * remark : null
		 * status : 0
		 * thingId : null
		 * operationTime : null
		 * dictId : null
		 * deviceType : null
		 * cabinetType : null
		 * cabinetNum : null
		 */

		private String deviceId;
		private String deviceName;
		private String identification;
		private String ip;
		private String parent;
		private String remark;
		private int    status;
		private String thingId;
		private String operationTime;
		private String dictId;
		private String deviceType;
		private String cabinetType;
		private String cabinetNum;

		public String getDeviceId() { return deviceId;}

		public void setDeviceId(String deviceId) { this.deviceId = deviceId;}

		public String getDeviceName() { return deviceName;}

		public void setDeviceName(String deviceName) { this.deviceName = deviceName;}

		public String getIdentification() { return identification;}

		public void setIdentification(
			String identification) { this.identification = identification;}

		public String getIp() { return ip;}

		public void setIp(String ip) { this.ip = ip;}

		public String getParent() { return parent;}

		public void setParent(String parent) { this.parent = parent;}

		public String getRemark() { return remark;}

		public void setRemark(String remark) { this.remark = remark;}

		public int getStatus() { return status;}

		public void setStatus(int status) { this.status = status;}

		public String getThingId() { return thingId;}

		public void setThingId(String thingId) { this.thingId = thingId;}

		public String getOperationTime() { return operationTime;}

		public void setOperationTime(
			String operationTime) { this.operationTime = operationTime;}

		public String getDictId() { return dictId;}

		public void setDictId(String dictId) { this.dictId = dictId;}

		public String getDeviceType() { return deviceType;}

		public void setDeviceType(String deviceType) { this.deviceType = deviceType;}

		public String getCabinetType() { return cabinetType;}

		public void setCabinetType(String cabinetType) { this.cabinetType = cabinetType;}

		public String getCabinetNum() { return cabinetNum;}

		public void setCabinetNum(String cabinetNum) { this.cabinetNum = cabinetNum;}
	   }
	}
   }

   public static class DevicesBean  implements Serializable{

	/**
	 * deviceId : 4028efd06ea12378016ea1d02937000f
	 * deviceName : 高值柜(上)
	 * identification : null
	 * ip : null
	 * parent : 4028efd06ea12378016ea1d02928000e
	 * remark : null
	 * status : 0
	 * thingId : 4028efd06ea12378016ea1d02914000d
	 * operationTime : 2019-11-25
	 * dictId : ff80818166c7b5d00166c8fbcbf50016
	 * deviceType : 10
	 * cabinetType : 1
	 * cabinetNum : 192.168.100.221
	 */

	private String deviceId;
	private String deviceName;
	private String identification;
	private String ip;
	private String parent;
	private String remark;
	private int    status;
	private String thingId;
	private String operationTime;
	private String dictId;
	private String deviceType;
	private String cabinetType;
	private String cabinetNum;

	public String getDeviceId() { return deviceId;}

	public void setDeviceId(String deviceId) { this.deviceId = deviceId;}

	public String getDeviceName() { return deviceName;}

	public void setDeviceName(String deviceName) { this.deviceName = deviceName;}

	public String getIdentification() { return identification;}

	public void setIdentification(String identification) { this.identification = identification;}

	public String getIp() { return ip;}

	public void setIp(String ip) { this.ip = ip;}

	public String getParent() { return parent;}

	public void setParent(String parent) { this.parent = parent;}

	public String getRemark() { return remark;}

	public void setRemark(String remark) { this.remark = remark;}

	public int getStatus() { return status;}

	public void setStatus(int status) { this.status = status;}

	public String getThingId() { return thingId;}

	public void setThingId(String thingId) { this.thingId = thingId;}

	public String getOperationTime() { return operationTime;}

	public void setOperationTime(String operationTime) { this.operationTime = operationTime;}

	public String getDictId() { return dictId;}

	public void setDictId(String dictId) { this.dictId = dictId;}

	public String getDeviceType() { return deviceType;}

	public void setDeviceType(String deviceType) { this.deviceType = deviceType;}

	public String getCabinetType() { return cabinetType;}

	public void setCabinetType(String cabinetType) { this.cabinetType = cabinetType;}

	public String getCabinetNum() { return cabinetNum;}

	public void setCabinetNum(String cabinetNum) { this.cabinetNum = cabinetNum;}
   }
}
