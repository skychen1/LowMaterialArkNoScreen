package org.wh.engineer.bean;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;
import org.wh.engineer.dbmodel.AccountVosBean;
import org.wh.engineer.dbmodel.ChildrenBeanX;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6.3
 * 创建者:      DanMing
 * 创建时间:    2018/11/23 13:51
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class HomeAuthorityMenuBean extends LitePalSupport implements Serializable{

   /**
    * path : ''
    * title : 耗材操作
    * icon : null
    * parentSeq : null
    * seq : 102
    * menu : null
    * children : [{"path":null,"title":"选择操作","icon":null,"parentSeq":102,"seq":107,"menu":null,"children":[{"path":null,"title":"入库","icon":null,"parentSeq":107,"seq":109,"menu":null,"children":null,"selected":false},{"path":null,"title":"调拨","icon":null,"parentSeq":107,"seq":111,"menu":null,"children":null,"selected":false},{"path":null,"title":"退回","icon":null,"parentSeq":107,"seq":113,"menu":null,"children":null,"selected":false},{"path":null,"title":"退货","icon":null,"parentSeq":107,"seq":114,"menu":null,"children":null,"selected":false}],"selected":false}]
    * selected : false
    */
   private AccountVosBean accountVosbean;
   private String              title;
   private String                      accountName;
   private List<ChildrenBeanX> children = new ArrayList<>();

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


   public String getTitle() { return title;}

   public void setTitle(String title) { this.title = title;}


   public List<ChildrenBeanX> getChildren() { return children;}

   public void setChildren(List<ChildrenBeanX> children) { this.children = children;}
   public List<ChildrenBeanX> getChildrenXbean(String name) {
      return LitePal.where("accountname = ? ", name).find(ChildrenBeanX.class);
   }

}
