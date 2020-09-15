package org.wh.engineer.dto.vo;

import java.io.Serializable;
import java.util.Date;


public class TCstInventoryJournalVo implements Serializable {
	/**
	 * 字段: 字段名称
	 *
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = 1L;
	private String status;
	private String cstName;
	private String EPC;
	private String cstSpec;
	private Date expirationTime;
	private String expiration;

   public static long getSerialVersionUID() {
	return serialVersionUID;
   }

   public String getStatus() {
	return status;
   }

   public void setStatus(String status) {
	this.status = status;
   }

   public String getCstName() {
	return cstName;
   }

   public void setCstName(String cstName) {
	this.cstName = cstName;
   }

   public String getEPC() {
	return EPC;
   }

   public void setEPC(String EPC) {
	this.EPC = EPC;
   }

   public String getCstSpec() {
	return cstSpec;
   }

   public void setCstSpec(String cstSpec) {
	this.cstSpec = cstSpec;
   }

   public Date getExpirationTime() {
	return expirationTime;
   }

   public void setExpirationTime(Date expirationTime) {
	this.expirationTime = expirationTime;
   }

   public String getExpiration() {
	return expiration;
   }

   public void setExpiration(String expiration) {
	this.expiration = expiration;
   }

   public String getDeviceName() {
	return deviceName;
   }

   public void setDeviceName(String deviceName) {
	this.deviceName = deviceName;
   }

   public String getDeviceCode() {
	return deviceCode;
   }

   public void setDeviceCode(String deviceCode) {
	this.deviceCode = deviceCode;
   }

   public Date getOptionDate() {
	return optionDate;
   }

   public void setOptionDate(Date optionDate) {
	this.optionDate = optionDate;
   }

   public String getOptionName() {
	return optionName;
   }

   public void setOptionName(String optionName) {
	this.optionName = optionName;
   }

   public Integer getStopFlag() {
	return stopFlag;
   }

   public void setStopFlag(Integer stopFlag) {
	this.stopFlag = stopFlag;
   }

   public Integer getCount() {
	return count;
   }

   public void setCount(Integer count) {
	this.count = count;
   }

   private String  deviceName;
   private String  deviceCode;
   private Date    optionDate;
   private String  optionName;
   private Integer stopFlag;
   private Integer count;

	public TCstInventoryJournalVo(String status, String cstName, String ePC, String cstSpec, Date expirationTime,
			String deviceName, String deviceCode, Date optionDate, String optionName) {
		super();
		this.status = status;
		this.cstName = cstName;
		EPC = ePC;
		this.cstSpec = cstSpec;
		this.expirationTime = expirationTime;
		this.deviceName = deviceName;
		this.deviceCode = deviceCode;
		this.optionDate = optionDate;
		this.optionName = optionName;
	}

	public TCstInventoryJournalVo(Integer count) {
		super();
		this.count = count;
	}

}
