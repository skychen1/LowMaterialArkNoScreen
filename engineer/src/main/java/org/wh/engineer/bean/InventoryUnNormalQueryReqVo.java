package org.wh.engineer.bean;

import java.io.Serializable;

/**
 * 项目名称:    Android_PV_2.6.6_514C
 * 创建者:      DanMing
 * 创建时间:    2019/6/24 14:26
 * 描述:        异常处理
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class InventoryUnNormalQueryReqVo implements Serializable {

   /**
    * deviceId :
    * cstNameOrEpcOrAccountName :
    * startDate :
    * endDate :
    * unNormalSource :
    */

   private String deviceId;
   private String cstNameOrEpcOrAccountName;
   private String startDate;
   private String endDate;
   private String unNormalSource;
   private String thingId;

   public String getThingId() {
      return thingId;
   }

   public void setThingId(String thingId) {
      this.thingId = thingId;
   }

   public String getDeviceId() { return deviceId;}

   public void setDeviceId(String deviceId) { this.deviceId = deviceId;}

   public String getCstNameOrEpcOrAccountName() { return cstNameOrEpcOrAccountName;}

   public void setCstNameOrEpcOrAccountName(
	   String cstNameOrEpcOrAccountName) { this.cstNameOrEpcOrAccountName = cstNameOrEpcOrAccountName;}

   public String getStartDate() { return startDate;}

   public void setStartDate(String startDate) { this.startDate = startDate;}

   public String getEndDate() { return endDate;}

   public void setEndDate(String endDate) { this.endDate = endDate;}

   public String getUnNormalSource() { return unNormalSource;}

   public void setUnNormalSource(String unNormalSource) { this.unNormalSource = unNormalSource;}
}
