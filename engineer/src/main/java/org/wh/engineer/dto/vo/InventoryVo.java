package org.wh.engineer.dto.vo;

import org.litepal.crud.LitePalSupport;
import org.wh.engineer.dto.InventoryDto;

import java.io.Serializable;

public class InventoryVo extends LitePalSupport implements Serializable {

   private InventoryDto inventoryDto;
   private String       cstName;
   private String       epc;
   private String       cstSpec;
   private String       expirationText;
   private String       expiryDate;
   private String       deviceName;
   private String       status;
   private String       tempPatientId;
   private String       stockMin;
   private boolean      isCreate;

   // 0 已过期
   private Integer expireStatus = -1;
   private String sthId;
   private String deviceId;
   private String cstId;
   private String operation;
   private String storehouseRemark;
   private String remark;
   private int    countStock; // 库存情况  账面
   private int    countActual; // 扫描出来的库存

   private int    count;
   private String updateTime;
   private String userName;
   // 前端状态显示字段

   private String statusStr;
   private String patientName;
   private String patientId;
   private int    size;
   private int    stockNum;

   private int    isErrorOperation;
   private int    deleteCount;//移除次数 0表示从未移除过，大于1 代表移除次数 置灰
   private String type;
   private int    operationStatus;
   private String batchNumber;
   private int    deletetatus;//0不移除    1移除

   private String  updator;//名字
   private String  name;//名字
   private String  idCard;//身份证
   private String  deptId;//科室ID
   private String  operatingRoomNo;//手术间ID
   private String  operatingRoomName;//手术间名字
   private String  surgeryTime;//手术时间
   private String  sex;//性别
   private boolean isSelected;
   private String  bindType;
   private String  medicalId;
   private String  surgeryId;
   private String  hisPatientId;
   private String  accountId;
   private String  renewTime;
   private String  updateDateTime;
   private boolean dateNetType;//判断是否是本地的还是(查询出来的false)
   private String  barcode;
   private String noConfirmCount;

   public String getUpdator() {
	return updator;
   }

   public void setUpdator(String updator) {
	this.updator = updator;
   }

   public String getStockMin() {
	return stockMin;
   }

   public void setStockMin(String stockMin) {
	this.stockMin = stockMin;
   }

   public int getStockNum() {
	return stockNum;
   }

   public void setStockNum(int stockNum) {
	this.stockNum = stockNum;
   }

   public String getNoConfirmCount() {
	return noConfirmCount;
   }

   public void setNoConfirmCount(String noConfirmCount) {
	this.noConfirmCount = noConfirmCount;
   }

   public String getBarcode() {
	return barcode;
   }

   public void setBarcode(String barcode) {
	this.barcode = barcode;
   }

   public boolean isDateNetType() {
	return dateNetType;
   }

   public void setDateNetType(boolean dateNetType) {
	this.dateNetType = dateNetType;
   }

   public String getUpdateDateTime() {
	return updateDateTime;
   }

   public void setUpdateDateTime(String updateDateTime) {
	this.updateDateTime = updateDateTime;
   }

   public String getRenewTime() {
	return renewTime;
   }

   public void setRenewTime(String renewTime) {
	this.renewTime = renewTime;
   }

   public String getAccountId() {
	return accountId;
   }

   public void setAccountId(String accountId) {
	this.accountId = accountId;
   }

   public String getHisPatientId() {
	return hisPatientId;
   }

   public void setHisPatientId(String hisPatientId) {
	this.hisPatientId = hisPatientId;
   }

   public InventoryDto getInventoryDto() {
	return inventoryDto;
   }

   public void setInventoryDto(InventoryDto inventoryDto) {
	this.inventoryDto = inventoryDto;
   }

   public String getSurgeryId() {
	return surgeryId;
   }

   public void setSurgeryId(String surgeryId) {
	this.surgeryId = surgeryId;
   }

   public String getMedicalId() {
	return medicalId;
   }

   public void setMedicalId(String medicalId) {
	this.medicalId = medicalId;
   }

   public String getBindType() {
	return bindType;
   }

   public void setBindType(String bindType) {
	this.bindType = bindType;
   }

   public String getTempPatientId() {
	return tempPatientId;
   }

   public void setTempPatientId(String tempPatientId) {
	this.tempPatientId = tempPatientId;
   }

   public boolean isCreate() {
	return isCreate;
   }

   public void setCreate(boolean create) {
	isCreate = create;
   }

   public int getDeleteCount() {
	return deleteCount;
   }

   public void setDeleteCount(int deleteCount) {
	this.deleteCount = deleteCount;
   }

   public String getBatchNumber() {
	return batchNumber;
   }

   public void setBatchNumber(String batchNumber) {
	this.batchNumber = batchNumber;
   }

   public int getIsErrorOperation() {
	return isErrorOperation;
   }

   public void setIsErrorOperation(int isErrorOperation) {
	this.isErrorOperation = isErrorOperation;
   }

   public boolean isSelected() {
	return isSelected;
   }

   public void setSelected(boolean selected) {
	isSelected = selected;
   }

   public String getOperatingRoomName() {
	return operatingRoomName;
   }

   public void setOperatingRoomName(String operatingRoomName) {
	this.operatingRoomName = operatingRoomName;
   }

   public String getCstId() {
	return cstId;
   }

   public void setCstId(String cstId) {
	this.cstId = cstId;
   }

   public String getIdCard() {
	return idCard;
   }

   public void setIdCard(String idCard) {
	this.idCard = idCard;
   }

   public String getDeptId() {
	return deptId;
   }

   public void setDeptId(String deptId) {
	this.deptId = deptId;
   }

   public String getOperatingRoomNo() {
	return operatingRoomNo;
   }

   public void setOperatingRoomNo(String operatingRoomNo) {
	this.operatingRoomNo = operatingRoomNo;
   }

   public String getSurgeryTime() {
	return surgeryTime;
   }

   public void setSurgeryTime(String surgeryTime) {
	this.surgeryTime = surgeryTime;
   }

   public String getSex() {
	return sex;
   }

   public void setSex(String sex) {
	this.sex = sex;
   }

   public String getOperationScheduleId() {
	return operationScheduleId;
   }

   public void setOperationScheduleId(String operationScheduleId) {
	this.operationScheduleId = operationScheduleId;
   }

   private String operationScheduleId;

   public int getDeletetatus() {
	return deletetatus;
   }

   public void setDeletetatus(int deletetatus) {
	this.deletetatus = deletetatus;
   }

   public boolean isDelete() {
	return isDelete;
   }

   public void setDelete(boolean delete) {
	isDelete = delete;
   }

   public boolean isDelete;

   public int getOperationStatus() {
	return operationStatus;
   }

   public void setOperationStatus(int operationStatus) {
	this.operationStatus = operationStatus;
   }

   public String getPatientName() {
	return patientName;
   }

   public void setPatientName(String patientName) {
	this.patientName = patientName;
   }

   public String getPatientId() {
	return patientId;
   }

   public void setPatientId(String patientId) {
	this.patientId = patientId;
   }

   public int getSize() {
	return size;
   }

   public void setSize(int size) {
	this.size = size;
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

   public String getCstName() {
	return cstName;
   }

   public void setCstName(String cstName) {
	this.cstName = cstName;
   }

   public String getEpc() {
	return epc;
   }

   public void setEpc(String epc) {
	this.epc = epc;
   }

   public String getCstSpec() {
	return cstSpec;
   }

   public void setCstSpec(String cstSpec) {
	this.cstSpec = cstSpec;
   }

   public String getExpirationText() {
	return expirationText;
   }

   public void setExpirationText(String expirationText) {
	this.expirationText = expirationText;
   }

   public String getDeviceName() {
	return deviceName;
   }

   public void setDeviceName(String deviceName) {
	this.deviceName = deviceName;
   }

   public int getCount() {
	return count;
   }

   public void setCount(int count) {
	this.count = count;
   }

   public String getUpdateTime() {
	return updateTime;
   }

   public void setUpdateTime(String updateTime) {
	this.updateTime = updateTime;
   }

   public String getUserName() {
	return userName;
   }

   public void setUserName(String userName) {
	this.userName = userName;
   }

   public String getStatusStr() {
	return statusStr;
   }

   public void setStatusStr(String statusStr) {
	this.statusStr = statusStr;
   }

   //
   //	public InventoryVo(String cstName, String epc, String cstSpec, Date expirationText, String deviceName,
   //			String status) {
   //		super();
   //		this.cstName = cstName;
   //		this.epc = epc;
   //		this.cstSpec = cstSpec;
   //		this.expirationText = expirationText;
   //		this.deviceName = deviceName;
   //		this.status = status;
   //	}

   @Override
   public boolean equals(Object obj) {
	if (this == obj) {
	   return true;
	}
	if (obj == null) {
	   return false;
	}
	if (getClass() != obj.getClass()) {
	   return false;
	}
	InventoryVo inventoryVo = (InventoryVo) obj;
	if (cstId == null) {
	   if (inventoryVo.cstId != null) {
		return false;
	   }
	} else if (!cstId.equals(inventoryVo.cstId)) {
	   return false;
	}
	if (cstName == null) {
	   if (inventoryVo.cstName != null) {
		return false;
	   }
	} else if (!cstName.equals(inventoryVo.cstName)) {
	   return false;
	}
	if (cstSpec == null) {
	   if (inventoryVo.cstSpec != null) {
		return false;
	   }
	} else if (!cstSpec.equals(inventoryVo.cstSpec)) {
	   return false;
	}
	if (epc == null) {
	   if (inventoryVo.epc != null) {
		return false;
	   }
	} else if (!epc.equals(inventoryVo.epc)) {
	   return false;
	}
	return true;
   }

   public String getStatus() {
	return status;
   }

   public void setStatus(String status) {
	this.status = status;
   }

   public String getExpiryDate() {
	return expiryDate;
   }

   public void setExpiryDate(String expiryDate) {
	this.expiryDate = expiryDate;
   }

   public Integer getExpireStatus() {
	return expireStatus;
   }

   public void setExpireStatus(Integer expireStatus) {
	this.expireStatus = expireStatus;
   }

   public String getSthId() {
	return sthId;
   }

   public void setSthId(String sthId) {
	this.sthId = sthId;
   }

   public String getDeviceId() {
	return deviceId;
   }

   public void setDeviceId(String deviceId) {
	this.deviceId = deviceId;
   }

   public String getCstCode() {
	return cstId;
   }

   public void setCstCode(String cstId) {
	this.cstId = cstId;
   }

   public String getOperation() {
	return operation;
   }

   public void setOperation(String operation) {
	this.operation = operation;
   }

   public String getStorehouseRemark() {
	return storehouseRemark;
   }

   public void setStorehouseRemark(String storehouseRemark) {
	this.storehouseRemark = storehouseRemark;
   }

   public String getRemark() {
	return remark;
   }

   public void setRemark(String remark) {
	this.remark = remark;
   }

   public int getCountStock() {
	return countStock;
   }

   public void setCountStock(int countStock) {
	this.countStock = countStock;
   }

   public int getCountActual() {
	return countActual;
   }

   public void setCountActual(int countActual) {
	this.countActual = countActual;
   }

}
