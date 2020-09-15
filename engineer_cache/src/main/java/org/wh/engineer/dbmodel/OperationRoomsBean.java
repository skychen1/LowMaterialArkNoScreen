package org.wh.engineer.dbmodel;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * 项目名称:    Android_PV_2.6.5New
 * 创建者:      DanMing
 * 创建时间:    2019/1/14 17:20
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.dbmodel
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class OperationRoomsBean extends LitePalSupport implements Serializable {
   /**
    * roomNo : 11
    * optRoomId : 1
    * status : 1
    * roomName : 手术间1
    * accountId : 10000000000000000000000000000001
    * updateTime : 2019-01-09
    * depts : []
    */

   private RoomsBean  roomsbean;
   private String  optRoomId;
   private String  roomName;

   public RoomsBean getRoomsbean() {
      return roomsbean;
   }

   public void setRoomsbean(RoomsBean roomsbean) {
      this.roomsbean = roomsbean;
   }

   public String getOptRoomId() { return optRoomId;}

   public void setOptRoomId(String optRoomId) { this.optRoomId = optRoomId;}

   public String getRoomName() { return roomName;}

   public void setRoomName(String roomName) { this.roomName = roomName;}

}
