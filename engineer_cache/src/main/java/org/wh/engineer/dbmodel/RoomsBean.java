package org.wh.engineer.dbmodel;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6.5New
 * 创建者:      DanMing
 * 创建时间:    2019/1/14 16:58
 * 描述:        本地手术间
 * 包名:        high.rivamed.myapplication.dbmodel
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class RoomsBean extends LitePalSupport implements Serializable {

   /**
    * operateSuccess : true
    * id : 0
    * opFlg : 200
    * pageNo : 1
    * pageSize : 20
    * thingId : 402882946827569c0168277df65f0006
    * operationRooms : [{"roomNo":"11","optRoomId":"1","status":1,"roomName":"手术间1","accountId":"10000000000000000000000000000001","updateTime":"2019-01-09","depts":[]},{"roomNo":"22","optRoomId":"2","status":1,"roomName":"手术间2","accountId":"10000000000000000000000000000001","updateTime":"2019-01-09","depts":[]}]
    */


   private String                   thingId;
   private List<OperationRoomsBean> operationRooms = new ArrayList<>();

   public String getThingId() { return thingId;}

   public void setThingId(String thingId) { this.thingId = thingId;}

   public List<OperationRoomsBean> getOperationRooms() { return operationRooms;}

   public void setOperationRooms(
	   List<OperationRoomsBean> operationRooms) { this.operationRooms = operationRooms;}


}
