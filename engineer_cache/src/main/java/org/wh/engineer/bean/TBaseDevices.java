package org.wh.engineer.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/13 10:13
 * 描述:        柜子
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class TBaseDevices extends AbstractExpandableItem<TBaseDevices.tBaseDevices>
	implements MultiItemEntity {

   private List<tBaseDevices>     list;//柜子list信息
   public  String                 deviceName;//柜子名字
   public  String                 deviceId;//柜子名字
   private List<RegisteTitleBean> list2;//柜子list信息
   private String            cabinetNum;//柜子编号：同个编号表示同一个柜子
   private String            cabinetType;//柜子类型：1：上柜 -1：下柜 0：单柜

   public String getCabinetNum() {
	return cabinetNum;
   }

   public void setCabinetNum(String cabinetNum) {
	this.cabinetNum = cabinetNum;
   }

   public String getCabinetType() {
	return cabinetType;
   }

   public void setCabinetType(String cabinetType) {
	this.cabinetType = cabinetType;
   }

   public String getDeviceId() {
	return deviceId;
   }

   public void setDeviceId(String deviceId) {
	this.deviceId = deviceId;
   }

   public List<RegisteTitleBean> getList2() {
	return list2;
   }

   public void setList2(List<RegisteTitleBean> list2) {
	this.list2 = list2;
   }



   public List<tBaseDevices> getList() {
	return list;
   }

   public void setList(List<tBaseDevices> list) {
	this.list = list;
   }

   public String getDeviceName() {
	return deviceName;
   }

   public void setDeviceName(String deviceName) {
	this.deviceName = deviceName;
   }

   public static class RegisteTitleBean extends tBaseDevices implements MultiItemEntity {

	public RegisteTitleBean(String name, String mac, String ip, String type) {
	   this.mName = name;
	   this.mMac = mac;
	   this.mIp = ip;
	   this.mType = type;
	}

	public String getName() {
	   return mName;
	}

	public void setName(String name) {
	   mName = name;
	}

	public String getMac() {
	   return mMac;
	}

	public void setMac(String mac) {
	   mMac = mac;
	}

	public String getIp() {
	   return mIp;
	}

	public void setIp(String ip) {
	   mIp = ip;
	}

	public String getType() {
	   return mType;
	}

	public void setType(String type) {
	   mType = type;
	}

	public String mName;
	public String mMac;
	public String mIp;
	public String mType;

	@Override
	public int getItemType() {
	   return 0;
	}


   }

   @Override
   public int getLevel() {
	return 0;
   }

   @Override
   public int getItemType() {
	return 0;
   }

   public static class tBaseDevices implements MultiItemEntity {
	private String             dictId;
	private String             dictName;
	private String             deviceType;
	public String              partsname;
	public String              partip;
	public List<partsmacBean>  partsmac;
	public List<partsnameBean> partsmacName;
	public String              partsmactext;
	public String              partmac;
	public String              deviceId;

	public String getDeviceId() {
	   return deviceId;
	}

	public void setDeviceId(String deviceId) {
	   this.deviceId = deviceId;
	}

	public String getDictId() {
	   return dictId;
	}

	public void setDictId(String dictId) {
	   this.dictId = dictId;
	}

	public String getDictName() {
	   return dictName;
	}

	public void setDictName(String dictName) {
	   this.dictName = dictName;
	}

	public String getDeviceType() {
	   return deviceType;
	}

	public void setDeviceType(String deviceType) {
	   this.deviceType = deviceType;
	}

	public List<partsnameBean> getPartsmacName() {
	   return partsmacName;
	}

	public void setPartsmacName(
		List<partsnameBean> partsmacName) {
	   this.partsmacName = partsmacName;
	}
	public String getPartmac() {
	   return partmac;
	}

	public void setPartmac(String partmac) {
	   this.partmac = partmac;
	}


	public String getPartsname() {
	   return partsname;
	}

	public void setPartsname(String partsname) {
	   this.partsname = partsname;
	}

	public String getPartip() {
	   return partip;
	}

	public void setPartip(String partip) {
	   this.partip = partip;
	}

	public List<partsmacBean> getPartsmac() {
	   return partsmac;
	}

	public void setPartsmac(
		List<partsmacBean> partsmac) {
	   this.partsmac = partsmac;
	}

	public String getPartsmactext() {
	   return partsmactext;
	}

	public void setPartsmactext(String partsmactext) {
	   this.partsmactext = partsmactext;
	}



	@Override
	public int getItemType() {
	   return 2;
	}

	public static class partsmacBean {

	   public String getPartsmacnumber() {
		return partsmacnumber;
	   }

	   public void setPartsmacnumber(String partsmacnumber) {
		this.partsmacnumber = partsmacnumber;
	   }
	   public String partsIp;
	   public String partName;
	   public String partsmacnumber;

	   public String getPartName() {
		return partName;
	   }

	   public void setPartName(String partName) {
		this.partName = partName;
	   }


	   public String getPartsIp() {
		return partsIp;
	   }

	   public void setPartsIp(String partsIp) {
		this.partsIp = partsIp;
	   }



	}
	public static class partsnameBean {

	   /**
	    * dictId : 14
	    * name : 罗丹贝尔-reader
	    * deviceType : UHFREADER
	    */

	   private String dictId;
	   private String name;
	   private String deviceType;

	   public String getDictId() { return dictId;}

	   public void setDictId(String dictId) { this.dictId = dictId;}

	   public String getName() { return name;}

	   public void setName(String name) { this.name = name;}

	   public String getDeviceType() { return deviceType;}

	   public void setDeviceType(String deviceType) { this.deviceType = deviceType;}
	}
   }
}
