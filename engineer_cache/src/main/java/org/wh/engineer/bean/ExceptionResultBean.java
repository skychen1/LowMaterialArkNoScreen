package org.wh.engineer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6.10
 * 创建者:      DanMing
 * 创建时间:    2019/12/26 0026 16:16
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class ExceptionResultBean implements Serializable {

   /**
    * operateSuccess : true
    * id : 0
    * opFlg : 200
    * pageNo : 1
    * pageSize : 20
    * account : 0
    * epcs : ["0000000000000019"]
    * inInventoryVos : []
    * outInventoryVos : []
    * inTypeCount : 0
    * outTypeCount : 0
    * inTotalCount : 0
    * outTotalCount : 0
    * errorEpcs : []
    * notInStoreCstEpcs : []
    * isCharge : true
    * isRefundFee : false
    * isCanRefundFee : false
    * isBindPatient : true
    * isRelatedCstAndStore : false
    * isAnyReturn : false
    * inventoryUnNormalHandleVos : [{"unNormalId":"4028efd06f41b3fb016f41ca385a0007","operationStatue":3,"operatorName":null,"operatorId":null,"outStatue":null,"targetSthId":null,"returnReason":null,"patientId":"02628b8ea5d741e4b18595bf119db6c2","patientName":"张雷","medicalId":"b6031bbfa4454358aa16c8bf3585e52b","surgeryId":"00f23fbfcebb4ebc9f710d1584dfc855","deptId":null,"thingId":"4028ef956e258e19016e25a36e210082","removeStatue":null}]
    * fuWai : false
    */

   private boolean operateSuccess;
   private int                                  id;
   private String                               opFlg;
   private int                                  pageNo;
   private int                                  pageSize;
   private int                                  account;
   private int                                  inTypeCount;
   private int                                  outTypeCount;
   private int                                  inTotalCount;
   private int                                  outTotalCount;
   private boolean                              isCharge;
   private boolean                              isRefundFee;
   private boolean                              isCanRefundFee;
   private boolean                              isBindPatient;
   private boolean                              isRelatedCstAndStore;
   private boolean                              isAnyReturn;
   private boolean                              fuWai;
   private List<String>                         epcs;
   private List<?>                              inInventoryVos;
   private List<?>                              outInventoryVos;
   private List<?>                              errorEpcs;
   private List<?>                              notInStoreCstEpcs;
   private List<InventoryUnNormalHandleVosBean> inventoryUnNormalHandleVos;

   public boolean isOperateSuccess() { return operateSuccess;}

   public void setOperateSuccess(boolean operateSuccess) { this.operateSuccess = operateSuccess;}

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public String getOpFlg() { return opFlg;}

   public void setOpFlg(String opFlg) { this.opFlg = opFlg;}

   public int getPageNo() { return pageNo;}

   public void setPageNo(int pageNo) { this.pageNo = pageNo;}

   public int getPageSize() { return pageSize;}

   public void setPageSize(int pageSize) { this.pageSize = pageSize;}

   public int getAccount() { return account;}

   public void setAccount(int account) { this.account = account;}

   public int getInTypeCount() { return inTypeCount;}

   public void setInTypeCount(int inTypeCount) { this.inTypeCount = inTypeCount;}

   public int getOutTypeCount() { return outTypeCount;}

   public void setOutTypeCount(int outTypeCount) { this.outTypeCount = outTypeCount;}

   public int getInTotalCount() { return inTotalCount;}

   public void setInTotalCount(int inTotalCount) { this.inTotalCount = inTotalCount;}

   public int getOutTotalCount() { return outTotalCount;}

   public void setOutTotalCount(int outTotalCount) { this.outTotalCount = outTotalCount;}

   public boolean isIsCharge() { return isCharge;}

   public void setIsCharge(boolean isCharge) { this.isCharge = isCharge;}

   public boolean isIsRefundFee() { return isRefundFee;}

   public void setIsRefundFee(boolean isRefundFee) { this.isRefundFee = isRefundFee;}

   public boolean isIsCanRefundFee() { return isCanRefundFee;}

   public void setIsCanRefundFee(boolean isCanRefundFee) { this.isCanRefundFee = isCanRefundFee;}

   public boolean isIsBindPatient() { return isBindPatient;}

   public void setIsBindPatient(boolean isBindPatient) { this.isBindPatient = isBindPatient;}

   public boolean isIsRelatedCstAndStore() { return isRelatedCstAndStore;}

   public void setIsRelatedCstAndStore(
	   boolean isRelatedCstAndStore) { this.isRelatedCstAndStore = isRelatedCstAndStore;}

   public boolean isIsAnyReturn() { return isAnyReturn;}

   public void setIsAnyReturn(boolean isAnyReturn) { this.isAnyReturn = isAnyReturn;}

   public boolean isFuWai() { return fuWai;}

   public void setFuWai(boolean fuWai) { this.fuWai = fuWai;}

   public List<String> getEpcs() { return epcs;}

   public void setEpcs(List<String> epcs) { this.epcs = epcs;}

   public List<?> getInInventoryVos() { return inInventoryVos;}

   public void setInInventoryVos(List<?> inInventoryVos) { this.inInventoryVos = inInventoryVos;}

   public List<?> getOutInventoryVos() { return outInventoryVos;}

   public void setOutInventoryVos(
	   List<?> outInventoryVos) { this.outInventoryVos = outInventoryVos;}

   public List<?> getErrorEpcs() { return errorEpcs;}

   public void setErrorEpcs(List<?> errorEpcs) { this.errorEpcs = errorEpcs;}

   public List<?> getNotInStoreCstEpcs() { return notInStoreCstEpcs;}

   public void setNotInStoreCstEpcs(
	   List<?> notInStoreCstEpcs) { this.notInStoreCstEpcs = notInStoreCstEpcs;}

   public List<InventoryUnNormalHandleVosBean> getInventoryUnNormalHandleVos() { return inventoryUnNormalHandleVos;}

   public void setInventoryUnNormalHandleVos(
	   List<InventoryUnNormalHandleVosBean> inventoryUnNormalHandleVos) { this.inventoryUnNormalHandleVos = inventoryUnNormalHandleVos;}

   public static class InventoryUnNormalHandleVosBean {

	/**
	 * unNormalId : 4028efd06f41b3fb016f41ca385a0007
	 * operationStatue : 3
	 * operatorName : null
	 * operatorId : null
	 * outStatue : null
	 * targetSthId : null
	 * returnReason : null
	 * patientId : 02628b8ea5d741e4b18595bf119db6c2
	 * patientName : 张雷
	 * medicalId : b6031bbfa4454358aa16c8bf3585e52b
	 * surgeryId : 00f23fbfcebb4ebc9f710d1584dfc855
	 * deptId : null
	 * thingId : 4028ef956e258e19016e25a36e210082
	 * removeStatue : null
	 */

	private String unNormalId;
	private int    operationStatue;
	private Object operatorName;
	private Object operatorId;
	private Object outStatue;
	private Object targetSthId;
	private Object returnReason;
	private String patientId;
	private String patientName;
	private String medicalId;
	private String surgeryId;
	private Object deptId;
	private String thingId;
	private Object removeStatue;

	public String getUnNormalId() { return unNormalId;}

	public void setUnNormalId(String unNormalId) { this.unNormalId = unNormalId;}

	public int getOperationStatue() { return operationStatue;}

	public void setOperationStatue(int operationStatue) { this.operationStatue = operationStatue;}

	public Object getOperatorName() { return operatorName;}

	public void setOperatorName(Object operatorName) { this.operatorName = operatorName;}

	public Object getOperatorId() { return operatorId;}

	public void setOperatorId(Object operatorId) { this.operatorId = operatorId;}

	public Object getOutStatue() { return outStatue;}

	public void setOutStatue(Object outStatue) { this.outStatue = outStatue;}

	public Object getTargetSthId() { return targetSthId;}

	public void setTargetSthId(Object targetSthId) { this.targetSthId = targetSthId;}

	public Object getReturnReason() { return returnReason;}

	public void setReturnReason(Object returnReason) { this.returnReason = returnReason;}

	public String getPatientId() { return patientId;}

	public void setPatientId(String patientId) { this.patientId = patientId;}

	public String getPatientName() { return patientName;}

	public void setPatientName(String patientName) { this.patientName = patientName;}

	public String getMedicalId() { return medicalId;}

	public void setMedicalId(String medicalId) { this.medicalId = medicalId;}

	public String getSurgeryId() { return surgeryId;}

	public void setSurgeryId(String surgeryId) { this.surgeryId = surgeryId;}

	public Object getDeptId() { return deptId;}

	public void setDeptId(Object deptId) { this.deptId = deptId;}

	public String getThingId() { return thingId;}

	public void setThingId(String thingId) { this.thingId = thingId;}

	public Object getRemoveStatue() { return removeStatue;}

	public void setRemoveStatue(Object removeStatue) { this.removeStatue = removeStatue;}
   }
}
