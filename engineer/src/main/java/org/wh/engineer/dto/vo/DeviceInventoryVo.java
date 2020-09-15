package org.wh.engineer.dto.vo;

import org.wh.engineer.dto.entity.Inventory;

import java.util.List;

public class DeviceInventoryVo {

	//盘点 耗材详情 cstId
	private String          cstId;
	private String          deviceId;
	private String          deviceName;
	private List<Inventory> inventories;
	
	private List<InventoryVo> inventoryVos;
   private int                  add;	// 库存情况
   private int                  reduce;	// 扫描出来的库存

   public int getAdd() {
	return add;
   }

   public void setAdd(int add) {
	this.add = add;
   }

   public int getReduce() {
	return reduce;
   }

   public void setReduce(int reduce) {
	this.reduce = reduce;
   }

   public String getCstCode() {
	return cstId;
   }

   public void setCstCode(String cstId) {
	this.cstId = cstId;
   }

   public String getDeviceId() {
	return deviceId;
   }

   public void setDeviceId(String deviceId) {
	this.deviceId = deviceId;
   }

   public String getDeviceName() {
	return deviceName;
   }

   public void setDeviceName(String deviceName) {
	this.deviceName = deviceName;
   }

   public List<Inventory> getInventories() {
	return inventories;
   }

   public void setInventories(
	   List<Inventory> inventories) {
	this.inventories = inventories;
   }

   public List<InventoryVo> getInventoryVos() {
	return inventoryVos;
   }

   public void setInventoryVos(
	   List<InventoryVo> inventoryVos) {
	this.inventoryVos = inventoryVos;
   }
}
