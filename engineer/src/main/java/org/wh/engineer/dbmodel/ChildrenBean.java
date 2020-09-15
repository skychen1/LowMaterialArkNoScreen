package org.wh.engineer.dbmodel;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * 项目名称:    Android_PV_2.6.5New
 * 创建者:      DanMing
 * 创建时间:    2019/1/11 15:51
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.dbmodel
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class ChildrenBean extends LitePalSupport implements Serializable {
   /**
    * path : null
    * title : 入库
    * icon : null
    * parentSeq : 107
    * seq : 109
    * menu : null
    * children : null
    * selected : false
    */

   private ChildrenBeanX childrenbeanx;
   private String                title;
   private String                      accountName;

   public String getAccountName() {
      return accountName;
   }

   public void setAccountName(String accountName) {
      this.accountName = accountName;
   }

   public ChildrenBeanX getChildrenbeanx() {
	return childrenbeanx;
   }

   public void setChildrenbeanx(ChildrenBeanX childrenbeanx) {
	this.childrenbeanx = childrenbeanx;
   }

   public String getTitle() { return title;}

   public void setTitle(String title) { this.title = title;}

}
