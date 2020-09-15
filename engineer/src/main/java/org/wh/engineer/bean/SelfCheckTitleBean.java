package org.wh.engineer.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import static org.wh.engineer.adapter.SelfCheckAdapter.TYPE_CONTEXT;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/18 16:08
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class SelfCheckTitleBean implements MultiItemEntity {

   public String name;
   public String type;
   public String mac;
   public String ip;
   public int    check;

   public SelfCheckTitleBean(String name, String type, String mac, String ip, int check) {
	this.name = name;
	this.type = type;
	this.mac = mac;
	this.ip = ip;
	this.check = check;
   }


   public String getName() {
	return name;
   }

   public void setName(String name) {
	this.name = name;
   }

   public String getType() {
	return type;
   }

   public void setType(String type) {
	this.type = type;
   }

   public String getMac() {
	return mac;
   }

   public void setMac(String mac) {
	this.mac = mac;
   }

   public String getIp() {
	return ip;
   }

   public void setIp(String ip) {
	this.ip = ip;
   }

   public int getCheck() {
	return check;
   }

   public void setCheck(int check) {
	this.check = check;
   }

   @Override
   public int getItemType() {
	return TYPE_CONTEXT;
   }
}
