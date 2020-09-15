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
 * 创建时间:    2019/1/11 15:48
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.dbmodel
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public  class ChildrenBeanX extends LitePalSupport implements Serializable {

   /**
    * path : null
    * title : 选择操作
    * icon : null
    * parentSeq : 102
    * seq : 107
    * menu : null
    * children : [{"path":null,"title":"入库","icon":null,"parentSeq":107,"seq":109,"menu":null,"children":null,"selected":false},{"path":null,"title":"调拨","icon":null,"parentSeq":107,"seq":111,"menu":null,"children":null,"selected":false},{"path":null,"title":"退回","icon":null,"parentSeq":107,"seq":113,"menu":null,"children":null,"selected":false},{"path":null,"title":"退货","icon":null,"parentSeq":107,"seq":114,"menu":null,"children":null,"selected":false}]
    * selected : false
    */

   private HomeAuthorityMenuBean homeauthoritymenubean;
   private String                title;
   private String                      accountName;
   private List<ChildrenBean> children = new ArrayList<>();

   public String getAccountName() {
	return accountName;
   }

   public void setAccountName(String accountName) {
	this.accountName = accountName;
   }

   public HomeAuthorityMenuBean getHomeAuthorityMenuBean() {
	return homeauthoritymenubean;
   }

   public void setHomeAuthorityMenuBean(
	   HomeAuthorityMenuBean homeAuthorityMenuBean) {
	homeauthoritymenubean = homeAuthorityMenuBean;
   }

   public String getTitle() { return title;}

   public void setTitle(String title) { this.title = title;}

   public List<ChildrenBean> getChildren() { return children;}

   public void setChildren(List<ChildrenBean> children) { this.children = children;}
   public List<ChildrenBean> getChildrenbean(String name) {
	return LitePal.where("accountname = ? ", name).find(ChildrenBean.class);
   }
}
