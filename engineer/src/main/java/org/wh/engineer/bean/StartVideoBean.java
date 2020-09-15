package org.wh.engineer.bean;

import java.io.Serializable;

/**
 * 项目名称:    Android_PV_2.6.14
 * 创建者:      DanMing
 * 创建时间:    2020/3/11 0011 11:21
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class StartVideoBean implements Serializable {

   /**
    * operateSuccess : true
    * id : 0
    * startTime : 2020-03-11 11:18:43
    * operatorName : admin
    * thingId : 4028ef956e258e19016e25a36ea70084
    * thingType : 001
    * departCode : d92b2702d13645129a0c64d1489c9653
    * departName : 手术室
    * operateType : opendoor
    * businessNo : 202003111118641eab4138b400d2f52b
    * sceneCode : 001
    */

   private boolean operateSuccess;
   private int    id;
   private String startTime;
   private String operatorName;
   private String thingId;
   private String thingType;
   private String departCode;
   private String departName;
   private String operateType;
   private String businessNo;
   private String sceneCode;

   public boolean isOperateSuccess() { return operateSuccess;}

   public void setOperateSuccess(boolean operateSuccess) { this.operateSuccess = operateSuccess;}

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public String getStartTime() { return startTime;}

   public void setStartTime(String startTime) { this.startTime = startTime;}

   public String getOperatorName() { return operatorName;}

   public void setOperatorName(String operatorName) { this.operatorName = operatorName;}

   public String getThingId() { return thingId;}

   public void setThingId(String thingId) { this.thingId = thingId;}

   public String getThingType() { return thingType;}

   public void setThingType(String thingType) { this.thingType = thingType;}

   public String getDepartCode() { return departCode;}

   public void setDepartCode(String departCode) { this.departCode = departCode;}

   public String getDepartName() { return departName;}

   public void setDepartName(String departName) { this.departName = departName;}

   public String getOperateType() { return operateType;}

   public void setOperateType(String operateType) { this.operateType = operateType;}

   public String getBusinessNo() { return businessNo;}

   public void setBusinessNo(String businessNo) { this.businessNo = businessNo;}

   public String getSceneCode() { return sceneCode;}

   public void setSceneCode(String sceneCode) { this.sceneCode = sceneCode;}
}
