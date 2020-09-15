package org.wh.engineer.dto.vo;

import java.io.Serializable;

/**
 * 项目名称:    Android_PV_2.6.14
 * 创建者:      DanMing
 * 创建时间:    2020/4/7 0007 15:54
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.dto.vo
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class OrderVos implements Serializable {

   /**
    * orderId : ff8081816e9708f8016ea00b8f260080
    * createTime : 2019-11-25 00:52:22
    * orderNo : RK-1911250103
    * inventoryNum : 5
    * inStockNum : 3
    * currentNum : 1
    */

   private String orderId;
   private String createTime;
   private String orderNo;
   private int    inventoryNum;//单据数量
   private int    inStockNum;//累计入库
   private int    currentNum;//本次入数量
   private boolean    isSelected;

   public boolean isSelected() {
      return isSelected;
   }

   public void setSelected(boolean selected) {
      isSelected = selected;
   }

   public String getOrderId() { return orderId;}

   public void setOrderId(String orderId) { this.orderId = orderId;}

   public String getCreateTime() { return createTime;}

   public void setCreateTime(String createTime) { this.createTime = createTime;}

   public String getOrderNo() { return orderNo;}

   public void setOrderNo(String orderNo) { this.orderNo = orderNo;}

   public int getInventoryNum() { return inventoryNum;}

   public void setInventoryNum(int inventoryNum) { this.inventoryNum = inventoryNum;}

   public int getInStockNum() { return inStockNum;}

   public void setInStockNum(int inStockNum) { this.inStockNum = inStockNum;}

   public int getCurrentNum() { return currentNum;}

   public void setCurrentNum(int currentNum) { this.currentNum = currentNum;}
}
