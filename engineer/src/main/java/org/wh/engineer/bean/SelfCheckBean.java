package org.wh.engineer.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import static org.wh.engineer.adapter.SelfCheckAdapter.TYPE_TITLE;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/13 10:13
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class SelfCheckBean extends AbstractExpandableItem<SelfCheckTitleBean> implements MultiItemEntity {

   public String nametitle;
   public String typetitle;
   public String mactitle;
   public String iptitle;
   public int    checktitle;

   public SelfCheckBean(String nametitle, String typetitle, String mactitle, String iptitle, int checktitle) {
	this.nametitle = nametitle;
	this.typetitle = typetitle;
	this.mactitle = mactitle;
	this.iptitle = iptitle;
	this.checktitle = checktitle;
   }
   public String getNametitle() {
	return nametitle;
   }

   public void setNametitle(String nametitle) {
	this.nametitle = nametitle;
   }

   public String getTypetitle() {
	return typetitle;
   }

   public void setTypetitle(String typetitle) {
	this.typetitle = typetitle;
   }

   public String getMactitle() {
	return mactitle;
   }

   public void setMactitle(String mactitle) {
	this.mactitle = mactitle;
   }

   public String getIptitle() {
	return iptitle;
   }

   public void setIptitle(String iptitle) {
	this.iptitle = iptitle;
   }

   public int getChecktitle() {
	return checktitle;
   }

   public void setChecktitle(int checktitle) {
	this.checktitle = checktitle;
   }



   @Override
   public int getLevel() {
	return 0;
   }

   @Override
   public int getItemType() {
	return TYPE_TITLE;
   }


}
