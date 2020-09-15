package org.wh.engineer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/28 19:22
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class HospNameBean extends Movie implements Serializable {

   /**
    * id : 0
    * hospIds : []
    * hospitalBranchs : [{"branchName":"院区1","branchId":"111"},{"branchName":"院区2","branchId":"222"}]
    */

   private int                       id;
   private List<String>              hospIds;
   private List<HospitalBranchsBean> hospitalBranchs;
   private List<DeptVosBean>         deptVos;
   private List<StoreHousesBean>     storehouses;
   public List<DeptVosBean> getDeptVos() {
	return deptVos;
   }

   public void setDeptVos(
	   List<DeptVosBean> deptVos) {
	this.deptVos = deptVos;
   }
   public HospNameBean(String one) {
	super(one);
   }

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public List<String> getHospIds() { return hospIds;}

   public void setHospIds(List<String> hospIds) { this.hospIds = hospIds;}

   public List<HospitalBranchsBean> getHospitalBranchs() { return hospitalBranchs;}

   public void setHospitalBranchs(List<HospitalBranchsBean> hospitalBranchs) { this.hospitalBranchs = hospitalBranchs;}

   public static class DeptVosBean {

	/**
	 * branchName : 院区1
	 * branchId : 111
	 * deptName:
	 * deptId:
	 */

	private String deptName;
	private String deptId;

	public String getDeptName() {
	   return deptName;
	}

	public void setDeptName(String deptName) {
	   this.deptName = deptName;
	}

	public String getDeptId() {
	   return deptId;
	}

	public void setDeptId(String deptId) {
	   this.deptId = deptId;
	}

   }

   public static class HospitalBranchsBean {

	/**
	 * branchName : 院区1
	 * branchId : 111
	 * deptName:
	 * deptId:
	 */

	private String branchName;
	private String branchId;
	private String deptName;
	private String deptId;

	public String getDeptName() {
	   return deptName;
	}

	public void setDeptName(String deptName) {
	   this.deptName = deptName;
	}

	public String getDeptId() {
	   return deptId;
	}

	public void setDeptId(String deptId) {
	   this.deptId = deptId;
	}

	public String getBranchName() { return branchName;}

	public void setBranchName(String branchName) { this.branchName = branchName;}

	public String getBranchId() { return branchId;}

	public void setBranchId(String branchId) { this.branchId = branchId;}
   }



   public List<StoreHousesBean> getStorehouses() {
	return storehouses;
   }

   public void setStorehouses(
	   List<StoreHousesBean> storehouses) {
	this.storehouses = storehouses;
   }

   public String getDeptId() {
	return deptId;
   }

   public void setDeptId(String deptId) {
	this.deptId = deptId;
   }

   public static class StoreHousesBean {

	/**
	 * sthId : 40288b5f6710d706016710f3a65b0001
	 * sthLevel : 2
	 * sthName : 高值库房
	 */

	private String sthId;
	private String sthLevel;
	private String sthName;

	public String getSthId() { return sthId;}

	public void setSthId(String sthId) { this.sthId = sthId;}

	public String getSthLevel() { return sthLevel;}

	public void setSthLevel(String sthLevel) { this.sthLevel = sthLevel;}

	public String getSthName() { return sthName;}

	public void setSthName(String sthName) { this.sthName = sthName;}
   }




   //------------------------------------------------------------------------------------------
   /**
    * id : 0
    * deptId : 22
    * operationRooms : [{"createDateTime":"2018-07-22 11:12:59","roomNo":"1","status":0,"roomName":"骨科手术间1","updateDateTime":"2018-07-22 11:13:02","accountId":null,"deptNamesStr":null,"depts":[],"accountName":null},{"createDateTime":"2018-07-22 11:14:20","roomNo":"2","status":0,"roomName":"骨科手术间2","updateDateTime":"2018-07-22 11:14:23","accountId":null,"deptNamesStr":null,"depts":[],"accountName":null},{"createDateTime":"2018-07-22 11:14:46","roomNo":"3","status":0,"roomName":"脑科","updateDateTime":"2018-07-25 17:25:04","accountId":null,"deptNamesStr":null,"depts":[],"accountName":null}]
    */

   private String               deptId;
   private List<OperationRooms> operationRooms;


   public String getDeptCode() { return deptId;}

   public void setDeptCode(String deptId) { this.deptId = deptId;}

   public List<OperationRooms> getOperationRooms() { return operationRooms;}

   public void setOperationRooms(
	   List<OperationRooms> operationRooms) { this.operationRooms = operationRooms;}

   public static class OperationRooms extends Movie{

	/**
	 * createDateTime : 2018-07-22 11:12:59
	 * roomNo : 1
	 * status : 0
	 * roomName : 骨科手术间1
	 * updateDateTime : 2018-07-22 11:13:02
	 * accountId : null
	 * deptNamesStr : null
	 * depts : []
	 * accountName : null
	 */

	private String createDateTime;
	private String roomNo;
	private int    status;
	private String roomName;
	private String optRoomId;

	public String getOptRoomId() {
	   return optRoomId;
	}

	public void setOptRoomId(String optRoomId) {
	   this.optRoomId = optRoomId;
	}

	public OperationRooms(String one) {
	   super(one);
	}

	public String getCreateDateTime() { return createDateTime;}

	public void setCreateDateTime(String createDateTime) { this.createDateTime = createDateTime;}

	public String getRoomNo() { return roomNo;}

	public void setRoomNo(String roomNo) { this.roomNo = roomNo;}

	public int getStatus() { return status;}

	public void setStatus(int status) { this.status = status;}

	public String getRoomName() { return roomName;}

	public void setRoomName(String roomName) { this.roomName = roomName;}
   }

   /**
    * id : 0
    * deptId : 22
    * tcstBaseStorehouses : [{"storehouseCode":"25","deptNamesStr":null,"name":"物资1库房","stopFlag":1,"storeType":1,"depts":[],"accountName":null,"lastupdateTime":null,"accountId":null},{"storehouseCode":"23","deptNamesStr":null,"name":"内科一库房","stopFlag":1,"storeType":1,"depts":[],"accountName":null,"lastupdateTime":null,"accountId":null},{"storehouseCode":"24","deptNamesStr":null,"name":"胸外科库房","stopFlag":1,"storeType":1,"depts":[],"accountName":null,"lastupdateTime":null,"accountId":null}]
    */

   private List<TcstBaseStorehousesBean> tcstBaseStorehouses;


   public List<TcstBaseStorehousesBean> getTcstBaseStorehouses() { return tcstBaseStorehouses;}

   public void setTcstBaseStorehouses(
	   List<TcstBaseStorehousesBean> tcstBaseStorehouses) { this.tcstBaseStorehouses = tcstBaseStorehouses;}

   public static class TcstBaseStorehousesBean extends Movie{

	/**
	 * storehouseCode : 25
	 * deptNamesStr : null
	 * name : 物资1库房
	 * stopFlag : 1
	 * storeType : 1
	 * depts : []
	 * accountName : null
	 * lastupdateTime : null
	 * accountId : null
	 */

	private String storehouseCode;
	private String name;

	public TcstBaseStorehousesBean(String one) {
	   super(one);
	}

	public String getStorehouseCode() { return storehouseCode;}

	public void setStorehouseCode(String storehouseCode) { this.storehouseCode = storehouseCode;}

	public String getName() { return name;}

	public void setName(String name) { this.name = name;}
   }




}
