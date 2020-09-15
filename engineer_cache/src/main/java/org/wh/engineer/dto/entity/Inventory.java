package org.wh.engineer.dto.entity;

import java.io.Serializable;


/**
 * The persistent class for the t_cst_inventory database table.
 * 
 */

/**
* <p>标题: Inventory.java</p>
* <p>业务描述:耗材库存信息 </p>
* <p>公司:北京瑞华康源科技有限公司</p>
* <p>版权:rivamed-2018</p>
* @author 魏小波
* @date 2018年7月17日
* @version V1.0 
*/

public class Inventory implements Serializable {


	private String id;

	//条码号
	private String barcode;

	//生产批号

	private String batchNumber;

	//耗材编码
	private String cstId;

	//耗材所在设备部件编码
	private String deviceId;

	private String epc;

	//有效期
	private String expirationTime;

	//生产日期
	private String productionDate;

	//单号
	private String sheetId;

	private String status;

	//库房编码
	private String sthId;
	
	private String createDate;
	
	private String accountId;
	
	private String userName;
	
	private String lastUpdateDate;
	
	private String remark;
	
	private String patientId;



   public String getId() {
	return id;
   }

   public void setId(String id) {
	this.id = id;
   }

   public String getBarcode() {
	return barcode;
   }

   public void setBarcode(String barcode) {
	this.barcode = barcode;
   }

   public String getBatchNumber() {
	return batchNumber;
   }

   public void setBatchNumber(String batchNumber) {
	this.batchNumber = batchNumber;
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

   public String getEpc() {
	return epc;
   }

   public void setEpc(String epc) {
	this.epc = epc;
   }

   public String getExpirationTime() {
	return expirationTime;
   }

   public void setExpirationTime(String expirationTime) {
	this.expirationTime = expirationTime;
   }

   public String getProductionDate() {
	return productionDate;
   }

   public void setProductionDate(String productionDate) {
	this.productionDate = productionDate;
   }

   public String getSheetId() {
	return sheetId;
   }

   public void setSheetId(String sheetId) {
	this.sheetId = sheetId;
   }

   public String getStatus() {
	return status;
   }

   public void setStatus(String status) {
	this.status = status;
   }

   public String getSthId() {
	return sthId;
   }

   public void setSthId(String sthId) {
	this.sthId = sthId;
   }

   public String getCreateDate() {
	return createDate;
   }

   public void setCreateDate(String createDate) {
	this.createDate = createDate;
   }

   public String getAccountId() {
	return accountId;
   }

   public void setAccountId(String accountId) {
	this.accountId = accountId;
   }

   public String getUserName() {
	return userName;
   }

   public void setUserName(String userName) {
	this.userName = userName;
   }

   public String getLastUpdateDate() {
	return lastUpdateDate;
   }

   public void setLastUpdateDate(String lastUpdateDate) {
	this.lastUpdateDate = lastUpdateDate;
   }

   public String getRemark() {
	return remark;
   }

   public void setRemark(String remark) {
	this.remark = remark;
   }

   public String getPatientId() {
	return patientId;
   }

   public void setPatientId(String patientId) {
	this.patientId = patientId;
   }

   public String getStorehouseRemark() {
	return storehouseRemark;
   }

   public void setStorehouseRemark(String storehouseRemark) {
	this.storehouseRemark = storehouseRemark;
   }

   //移出目标库房
	private String storehouseRemark;
	
	

}