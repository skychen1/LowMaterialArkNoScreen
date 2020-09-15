package org.wh.engineer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/23 15:16
 * 描述:        库存监控的耗材效期监控 bean
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class SocketLeftTopBean implements Serializable{

   /**
    * id : 0
    * cstExpirationVos : [{"deviceCode":"324325","deviceName":"二号柜子","expireCount":0,"nearExpireCount":0},{"deviceCode":"4545454","deviceName":"三号柜子","expireCount":2,"nearExpireCount":0},{"deviceCode":"454545678","deviceName":"一号柜子","expireCount":0,"nearExpireCount":0},{"deviceCode":"5454545","deviceName":"四号柜子","expireCount":0,"nearExpireCount":0}]
    * thingCode : 23233
    */

   private int id;
   private String thingId;
   private List<CstExpirationVosBean> cstExpirationVos;

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public String getThingId() { return thingId;}

   public void setThingId(String thingId) { this.thingId = thingId;}

   public List<CstExpirationVosBean> getCstExpirationVos() { return cstExpirationVos;}

   public void setCstExpirationVos(
	   List<CstExpirationVosBean> cstExpirationVos) { this.cstExpirationVos = cstExpirationVos;}

   public static class CstExpirationVosBean {

	/**
	 * deviceCode : 324325
	 * deviceName : 二号柜子
	 * expireCount : 0
	 * nearExpireCount : 0
	 */

	private String deviceId;
	private String deviceName;
	private int    expireCount;
	private int    nearExpireCount;

	public String getDeviceId() { return deviceId;}

	public void setDeviceId(String deviceId) { this.deviceId = deviceId;}

	public String getDeviceName() { return deviceName;}

	public void setDeviceName(String deviceName) { this.deviceName = deviceName;}

	public int getExpireCount() { return expireCount;}

	public void setExpireCount(int expireCount) { this.expireCount = expireCount;}

	public int getNearExpireCount() { return nearExpireCount;}

	public void setNearExpireCount(int nearExpireCount) { this.nearExpireCount = nearExpireCount;}
   }
}
