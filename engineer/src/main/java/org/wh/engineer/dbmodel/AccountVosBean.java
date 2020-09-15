package org.wh.engineer.dbmodel;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;
import org.wh.engineer.bean.HomeAuthorityMenuBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6.5New
 * 创建者:      DanMing
 * 创建时间:    2019/1/10 15:59
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.dbmodel
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class AccountVosBean extends LitePalSupport implements Serializable {

   /**
    * accountId : 10000000000000000000000000000001
    * userId : 10000000000000000000000000000001
    * accountName : admin
    * tenantId : 10000000000000000000000000000001
    * useState : 1
    * password : 8682b08b939e7d5e0e0db43db3bb39b42ab4453b
    * salt : Jvd0G7Pz8nJoYsOu
    * userFeatureInfos : [{"featureId":"40288293677e276f01677e2c92850005","userId":"10000000000000000000000000000001","type":"2","data":"DA40BDC4"}]
    * menus : [{"path":"''","title":"耗材操作","icon":null,"parentSeq":null,"seq":102,"menu":null,"children":[{"path":null,"title":"选择操作","icon":null,"parentSeq":102,"seq":107,"menu":null,"children":[{"path":null,"title":"领用","icon":null,"parentSeq":107,"seq":108,"menu":null,"children":null,"selected":false},{"path":null,"title":"入库","icon":null,"parentSeq":107,"seq":109,"menu":null,"children":null,"selected":false},{"path":null,"title":"移出","icon":null,"parentSeq":107,"seq":110,"menu":null,"children":null,"selected":false},{"path":null,"title":"调拨","icon":null,"parentSeq":107,"seq":111,"menu":null,"children":null,"selected":false},{"path":null,"title":"移入","icon":null,"parentSeq":107,"seq":112,"menu":null,"children":null,"selected":false},{"path":null,"title":"退回","icon":null,"parentSeq":107,"seq":113,"menu":null,"children":null,"selected":false},{"path":null,"title":"退货","icon":null,"parentSeq":107,"seq":114,"menu":null,"children":null,"selected":false}],"selected":false}],"selected":false},{"path":"\u2018\u2019","title":"耗材流水","icon":null,"parentSeq":null,"seq":103,"menu":null,"children":null,"selected":false},{"path":"\u2018\u2019","title":"库存状态","icon":null,"parentSeq":null,"seq":104,"menu":null,"children":null,"selected":false},{"path":"\u2018\u2019","title":"实时盘点","icon":null,"parentSeq":null,"seq":105,"menu":null,"children":null,"selected":false},{"path":"\u2018\u2019","title":"使用记录","icon":null,"parentSeq":null,"seq":106,"menu":null,"children":null,"selected":false}]
    */

   private UserBean                      userbean;
   private String                      accountId;
   private String                      faceId;
   private String                      userId;
   private String                      accountName;
   private String                      userName;
   private String                      tenantId;
   private String                      useState;
   private String                      password;
   private String                      salt;
   private String                      sex;
   private List<UserFeatureInfosBean>  userFeatureInfos = new ArrayList<>();
   private List<HomeAuthorityMenuBean> menus= new ArrayList<>();

   public String getFaceId() {
      return faceId;
   }

   public void setFaceId(String faceId) {
      this.faceId = faceId;
   }

   public String getUserName() {
      return userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public String getSex() {
      return sex;
   }

   public void setSex(String sex) {
      this.sex = sex;
   }

   public UserBean getUserbean() {
      return userbean;
   }

   public void setUserbean(UserBean userbean) {
      this.userbean = userbean;
   }

   public String getAccountId() { return accountId;}

   public void setAccountId(String accountId) { this.accountId = accountId;}

   public String getUserId() { return userId;}

   public void setUserId(String userId) { this.userId = userId;}

   public String getAccountName() { return accountName;}

   public void setAccountName(String accountName) { this.accountName = accountName;}

   public String getTenantId() { return tenantId;}

   public void setTenantId(String tenantId) { this.tenantId = tenantId;}

   public String getUseState() { return useState;}

   public void setUseState(String useState) { this.useState = useState;}

   public String getPassword() { return password;}

   public void setPassword(String password) { this.password = password;}

   public String getSalt() { return salt;}

   public void setSalt(String salt) { this.salt = salt;}

   public List<UserFeatureInfosBean> getUserFeatureInfosbean(String name) {
      return LitePal.where("accountname = ? ", name).find(UserFeatureInfosBean.class);
   }
   public List<UserFeatureInfosBean> getUserFeatureInfos() {
      return userFeatureInfos;
   }
   public void setUserFeatureInfos(
         List<UserFeatureInfosBean> userFeatureInfos) {
      this.userFeatureInfos = userFeatureInfos;
   }

   public void setMenus(List<HomeAuthorityMenuBean> menus) {
      this.menus = menus;
   }

   public List<HomeAuthorityMenuBean> getMenusList(String name) {
      return LitePal.where("accountname = ? ", name).find(HomeAuthorityMenuBean.class);
   }
   public HomeAuthorityMenuBean getMenusbean(String name) {
      return LitePal.where("accountname = ? ", name).findFirst(HomeAuthorityMenuBean.class);
   }
   public List<HomeAuthorityMenuBean> getMenus() {
      return menus;
   }

}
