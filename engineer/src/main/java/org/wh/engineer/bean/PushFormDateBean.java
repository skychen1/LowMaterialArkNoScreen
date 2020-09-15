package org.wh.engineer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6.3
 * 创建者:      DanMing
 * 创建时间:    2018/12/24 15:08
 * 描述:        上传请领单的数据
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class PushFormDateBean implements Serializable {

   /**
    * orders : [{"orderId":1},{"orderId":2}]
    * thingId : 1
    */

   private String thingId;
   private List<OrdersBean> orders;

   public String getThingId() { return thingId;}

   public void setThingId(String thingId) { this.thingId = thingId;}

   public List<OrdersBean> getOrders() { return orders;}

   public void setOrders(List<OrdersBean> orders) { this.orders = orders;}

   public static class OrdersBean {

	/**
	 * orderId : 1
	 */

	private String orderId;

	public String getOrderId() { return orderId;}

	public void setOrderId(String orderId) { this.orderId = orderId;}
   }
}
