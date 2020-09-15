package org.wh.engineer.dbmodel;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * 项目名称:    Android_PV_2.6.5New
 * 创建者:      DanMing
 * 创建时间:    2019/1/10 15:56
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.dbmodel
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class UserFeatureInfosBean extends LitePalSupport implements Serializable {
   /**
    * featureId : 40288293677e276f01677e2c92850005
    * userId : 10000000000000000000000000000001
    * type : 2
    * data : DA40BDC4
    */

   private AccountVosBean accountVosbean;
   private String featureId;
   private String userId;
   private String type;
   private String data;
   private String                      accountName;

   public String getAccountName() {
      return accountName;
   }

   public void setAccountName(String accountName) {
      this.accountName = accountName;
   }

   public AccountVosBean getAccountVosbean() {
      return accountVosbean;
   }

   public void setAccountVosbean(AccountVosBean accountVosbean) {
      this.accountVosbean = accountVosbean;
   }

   public String getFeatureId() { return featureId;}

   public void setFeatureId(String featureId) { this.featureId = featureId;}

   public String getUserId() { return userId;}

   public void setUserId(String userId) { this.userId = userId;}

   public String getType() { return type;}

   public void setType(String type) { this.type = type;}

   public String getData() { return data;}

   public void setData(String data) { this.data = data;}
}
