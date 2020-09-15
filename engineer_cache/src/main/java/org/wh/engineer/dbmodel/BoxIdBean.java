package org.wh.engineer.dbmodel;

import org.litepal.crud.LitePalSupport;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/22 10:09
 * 描述:        柜子和柜子内部的id绑定的类
 * 包名:        high.rivamed.myapplication.dbmodel
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class BoxIdBean extends LitePalSupport {
   private String name;//柜子或者部件的类型
   private String device_id;//柜子部件的id
   private String box_id;//柜子id
   private String            cabinetNum;//柜子编号：同个编号表示同一个柜子
   private String            cabinetType;//柜子类型：1：上柜 2：下柜 0：单柜

   public String getCabinetNum() {
	return cabinetNum;
   }

   public void setCabinetNum(String cabinetNum) {
	this.cabinetNum = cabinetNum;
   }

   public String getCabinetType() {
	return cabinetType;
   }

   public void setCabinetType(String cabinetType) {
	this.cabinetType = cabinetType;
   }


   public String getDevice_id() {
	return device_id;
   }

   public void setDevice_id(String device_id) {
	this.device_id = device_id;
   }

   public String getName() {
	return name;
   }

   public void setName(String name) {
	this.name = name;
   }

   public String getBox_id() {
	return box_id;
   }

   public void setBox_id(String box_id) {
	this.box_id = box_id;
   }
}
