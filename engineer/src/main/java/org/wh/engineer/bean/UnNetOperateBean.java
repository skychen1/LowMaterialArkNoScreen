package org.wh.engineer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6.5New
 * 创建者:      DanMing
 * 创建时间:    2019/1/24 18:27
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class UnNetOperateBean implements Serializable{

   /**
    * operateSuccess : true
    * id : 0
    * msg : 领用成功！
    * opFlg : 200
    * pageNo : 1
    * pageSize : 20
    * account : 0
    * thingId : 402882946856085801685621ea1b0033
    * operation : 0
    * add : 0
    * reduce : 0
    * countTwoin : 0
    * countMoveIn : 0
    * countBack : 0
    * countTempopary : 0
    * inTypeCount : 0
    * outTypeCount : 0
    * inTotalCount : 0
    * totalCount : 0
    * outTotalCount : 0
    * notInStoreCstEpcs : []
    */

   private boolean operateSuccess;
   private int     id;
   private String  msg;
   private String  opFlg;
   private int     pageNo;
   private int     pageSize;
   private int     account;
   private String  thingId;
   private int     operation;
   private int     add;
   private int     reduce;
   private int     countTwoin;
   private int     countMoveIn;
   private int     countBack;
   private int     countTempopary;
   private int     inTypeCount;
   private int     outTypeCount;
   private int     inTotalCount;
   private int     totalCount;
   private int     outTotalCount;
   private List<?> notInStoreCstEpcs;

   public boolean isOperateSuccess() { return operateSuccess;}

   public void setOperateSuccess(boolean operateSuccess) { this.operateSuccess = operateSuccess;}

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public String getMsg() { return msg;}

   public void setMsg(String msg) { this.msg = msg;}

   public String getOpFlg() { return opFlg;}

   public void setOpFlg(String opFlg) { this.opFlg = opFlg;}

   public int getPageNo() { return pageNo;}

   public void setPageNo(int pageNo) { this.pageNo = pageNo;}

   public int getPageSize() { return pageSize;}

   public void setPageSize(int pageSize) { this.pageSize = pageSize;}

   public int getAccount() { return account;}

   public void setAccount(int account) { this.account = account;}

   public String getThingId() { return thingId;}

   public void setThingId(String thingId) { this.thingId = thingId;}

   public int getOperation() { return operation;}

   public void setOperation(int operation) { this.operation = operation;}

   public int getAdd() { return add;}

   public void setAdd(int add) { this.add = add;}

   public int getReduce() { return reduce;}

   public void setReduce(int reduce) { this.reduce = reduce;}

   public int getCountTwoin() { return countTwoin;}

   public void setCountTwoin(int countTwoin) { this.countTwoin = countTwoin;}

   public int getCountMoveIn() { return countMoveIn;}

   public void setCountMoveIn(int countMoveIn) { this.countMoveIn = countMoveIn;}

   public int getCountBack() { return countBack;}

   public void setCountBack(int countBack) { this.countBack = countBack;}

   public int getCountTempopary() { return countTempopary;}

   public void setCountTempopary(int countTempopary) { this.countTempopary = countTempopary;}

   public int getInTypeCount() { return inTypeCount;}

   public void setInTypeCount(int inTypeCount) { this.inTypeCount = inTypeCount;}

   public int getOutTypeCount() { return outTypeCount;}

   public void setOutTypeCount(int outTypeCount) { this.outTypeCount = outTypeCount;}

   public int getInTotalCount() { return inTotalCount;}

   public void setInTotalCount(int inTotalCount) { this.inTotalCount = inTotalCount;}

   public int getTotalCount() { return totalCount;}

   public void setTotalCount(int totalCount) { this.totalCount = totalCount;}

   public int getOutTotalCount() { return outTotalCount;}

   public void setOutTotalCount(int outTotalCount) { this.outTotalCount = outTotalCount;}

   public List<?> getNotInStoreCstEpcs() { return notInStoreCstEpcs;}

   public void setNotInStoreCstEpcs(
	   List<?> notInStoreCstEpcs) { this.notInStoreCstEpcs = notInStoreCstEpcs;}
}
