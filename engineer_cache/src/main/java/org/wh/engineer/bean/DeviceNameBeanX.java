package org.wh.engineer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/28 13:38
 * 描述:        预注册  通过类型得到名字和ID
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class DeviceNameBeanX implements Serializable{

   /**
    * id : 0
    * deviceTypes : ["UHFREADER","Eth002"]
    * deviceDictVos : [{"dictId":"14","name":"罗丹贝尔-reader","deviceType":"UHFREADER"},{"dictId":"15","name":"红陆-reader","deviceType":"UHFREADER"},{"dictId":"16","name":"串口服务器","deviceType":"Eth002"}]
    */

   private int                 id;
   private List<String>        deviceTypes;
   private List<DeviceDictVos> deviceDictVos;

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public List<String> getDeviceTypes() { return deviceTypes;}

   public void setDeviceTypes(List<String> deviceTypes) { this.deviceTypes = deviceTypes;}

   public List<DeviceDictVos> getDeviceDictVos() { return deviceDictVos;}

   public void setDeviceDictVos(
	   List<DeviceDictVos> tBaseDeviceDictVos) { this.deviceDictVos = tBaseDeviceDictVos;}

   public static class DeviceDictVos {

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
