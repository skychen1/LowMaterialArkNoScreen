package org.wh.engineer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6.3
 * 创建者:      DanMing
 * 创建时间:    2018/12/17 14:36
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class OutMealBean implements Serializable{

   /**
    * id : 0
    * suiteVos : [{"suiteId":"402882a0675e1d4c01675e1ea68d0000","suiteName":"taozu66","deptId":"40287281674eaca301674ebd649d0003","deptName":"产科","updateTime":"2018-12-05","status":1,"inventoryEnough":false},{"suiteId":"402882a06753298a016753dc4bb40005","suiteName":"1","creatorId":"10000000000000000000000000000000","creatorName":"super","createTime":"2018-11-27","deptId":"40287281674eaca301674ebd649d0003","deptName":"产科","updateTime":"2018-11-28","status":1,"inventoryEnough":false},{"suiteId":"402882a06753f0160167543cc169000b","suiteName":"123","deptId":"40287281674eaca301674ebd649d0003","deptName":"产科","updateTime":"2018-11-28","status":1,"inventoryEnough":false},{"suiteId":"402882a06754af25016754b6ae110009","suiteName":"cehsi222","deptId":"40287281674eaca301674ebd649d0003","deptName":"产科","updateTime":"2018-11-28","status":1,"inventoryEnough":false},{"suiteId":"402882a06754af25016754b76650000f","suiteName":"dhdh123","deptId":"40287281674eaca301674ebd649d0003","deptName":"产科","updateTime":"2018-11-28","status":1,"inventoryEnough":false},{"suiteId":"1","suiteName":"ceshi1","deptId":"40287281674eaca301674ebd649d0003","deptName":"产科","updateTime":"2018-11-27","status":1,"inventoryEnough":false}]
    * deptId : 40287281674eaca301674ebd649d0003
    */

   private int              id;
   private String           deptId;
   private List<SuitesBean> suiteVos;

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public String getDeptId() { return deptId;}

   public void setDeptId(String deptId) { this.deptId = deptId;}

   public List<SuitesBean> getSuiteVos() { return suiteVos;}

   public void setSuiteVos(List<SuitesBean> suiteVos) { this.suiteVos = suiteVos;}

   public static class SuitesBean {

	/**
	 * suiteId : 402882a0675e1d4c01675e1ea68d0000
	 * suiteName : taozu66
	 * deptId : 40287281674eaca301674ebd649d0003
	 * deptName : 产科
	 * updateTime : 2018-12-05
	 * status : 1
	 * inventoryEnough : false
	 * creatorId : 10000000000000000000000000000000
	 * creatorName : super
	 * createTime : 2018-11-27
	 */

	private String suiteId;
	private String  suiteName;
	private String  deptId;
	private String  deptName;
	private String  updateTime;
	private int     status;
	private boolean inventoryEnough;
	private String  creatorId;
	private String  creatorName;
	private String  createTime;

	public String getSuiteId() { return suiteId;}

	public void setSuiteId(String suiteId) { this.suiteId = suiteId;}

	public String getSuiteName() { return suiteName;}

	public void setSuiteName(String suiteName) { this.suiteName = suiteName;}

	public String getDeptId() { return deptId;}

	public void setDeptId(String deptId) { this.deptId = deptId;}

	public String getDeptName() { return deptName;}

	public void setDeptName(String deptName) { this.deptName = deptName;}

	public String getUpdateTime() { return updateTime;}

	public void setUpdateTime(String updateTime) { this.updateTime = updateTime;}

	public int getStatus() { return status;}

	public void setStatus(int status) { this.status = status;}

	public boolean isInventoryEnough() { return inventoryEnough;}

	public void setInventoryEnough(
		boolean inventoryEnough) { this.inventoryEnough = inventoryEnough;}

	public String getCreatorId() { return creatorId;}

	public void setCreatorId(String creatorId) { this.creatorId = creatorId;}

	public String getCreatorName() { return creatorName;}

	public void setCreatorName(String creatorName) { this.creatorName = creatorName;}

	public String getCreateTime() { return createTime;}

	public void setCreateTime(String createTime) { this.createTime = createTime;}
   }
}
