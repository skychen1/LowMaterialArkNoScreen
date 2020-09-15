package org.wh.engineer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/20 16:55
 * 描述:        预注册和激活的封装对象
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class TBaseThingDto implements Serializable {

   private TBaseThing     tBaseThing;
   private HospitalInfoVo hospitalInfoVo;

   private List<TBaseDeviceVo> tBaseDeviceVos;//柜子LIST

   public TBaseThing gettBaseThing() {
	return tBaseThing;
   }

   public void settBaseThing(TBaseThing tBaseThing) {
	this.tBaseThing = tBaseThing;
   }

   public List<TBaseDeviceVo> gettBaseDeviceVos() {
	return tBaseDeviceVos;
   }

   public void settBaseDeviceVos(
	   List<TBaseDeviceVo> tBaseDeviceVos) {
	this.tBaseDeviceVos = tBaseDeviceVos;
   }

   public HospitalInfoVo getHospitalInfoVo() {
	return hospitalInfoVo;
   }

   public void setHospitalInfoVo(
	   HospitalInfoVo hospitalInfoVo) {
	this.hospitalInfoVo = hospitalInfoVo;
   }

   public static class HospitalInfoVo {//激活的时候医院信息
	private String         storehouseCode;
	private String         deptId;
	private String         deptName;
	private String         operationRoomNo;
	private String         branchCode;

	public String getDeptName() {
	   return deptName;
	}

	public void setDeptName(String deptName) {
	   this.deptName = deptName;
	}

	public String getBranchCode() {
	   return branchCode;
	}

	public void setBranchCode(String branchCode) {
	   this.branchCode = branchCode;
	}

	public String getStorehouseCode() {
	   return storehouseCode;
	}

	public void setStorehouseCode(String storehouseCode) {
	   this.storehouseCode = storehouseCode;
	}

	public String getDeptCode() {
	   return deptId;
	}

	public void setDeptCode(String deptId) {
	   this.deptId = deptId;
	}

	public String getOperationRoomNo() {
	   return operationRoomNo;
	}

	public void setOperationRoomNo(String operationRoomNo) {
	   this.operationRoomNo = operationRoomNo;
	}
   }

   public static class TBaseThing {//设备信息
	private String thingName;      //设备名称
	private String thingType;      //设备型号
	private String sn;            //设备序列号
	private String localIp;       //设备IP
	private String serverIp;      //服务器IP
	private String portNumber;
	private String thingCode;

	public String getThingCode() {
	   return thingCode;
	}

	public void setThingCode(String thingCode) {
	   this.thingCode = thingCode;
	}

	public String getPortNumber() {
	   return portNumber;
	}

	public void setPortNumber(String portNumber) {
	   this.portNumber = portNumber;
	}

	public String getThingName() {
	   return thingName;
	}

	public void setThingName(String thingName) {
	   this.thingName = thingName;
	}

	public String getThingType() {
	   return thingType;
	}

	public void setThingType(String thingType) {
	   this.thingType = thingType;
	}

	public String getSn() {
	   return sn;
	}

	public void setSn(String sn) {
	   this.sn = sn;
	}

	public String getLocalIp() {
	   return localIp;
	}

	public void setLocalIp(String localIp) {
	   this.localIp = localIp;
	}

	public String getServerIp() {
	   return serverIp;
	}

	public void setServerIp(String serverIp) {
	   this.serverIp = serverIp;
	}
   }

   public static class TBaseDeviceVo {//柜体信息
	private String            deviceName;     //柜子名称
	private String            deviceCode;      //柜子编码
	private List<TBaseDevice> tBaseDevices;//柜子内部设备

	public String getDeviceName() {
	   return deviceName;
	}

	public void setDeviceName(String deviceName) {
	   this.deviceName = deviceName;
	}

	public String getDeviceCode() {
	   return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
	   this.deviceCode = deviceCode;
	}

	public List<TBaseDevice> gettBaseDevices() {
	   return tBaseDevices;
	}

	public void settBaseDevices(
		List<TBaseDevice> tBaseDevices) {
	   this.tBaseDevices = tBaseDevices;
	}

	public static class TBaseDevice {//柜子内部设备信息
	   private String deviceName;      //设备名称
	   private String identification;      //设备编码
	   private String ip;      //设备
	   private String dictId;      //设备配置ID
	   private String deviceType;      //类型
	   private String deviceCode;      //code

	   public String getDeviceCode() {
		return deviceCode;
	   }

	   public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	   }

	   public String getDictId() {
		return dictId;
	   }

	   public void setDictId(String dictId) {
		this.dictId = dictId;
	   }

	   public String getDeviceType() {
		return deviceType;
	   }

	   public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	   }

	   public String getIdentification() {
		return identification;
	   }

	   public void setIdentification(String identification) {
		this.identification = identification;
	   }

	   public String getDeviceName() {
		return deviceName;
	   }

	   public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	   }

	   public String getIp() {
		return ip;
	   }

	   public void setIp(String ip) {
		this.ip = ip;
	   }
	}
   }
}
