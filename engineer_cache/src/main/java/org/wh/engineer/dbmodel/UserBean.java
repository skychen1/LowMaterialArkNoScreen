package org.wh.engineer.dbmodel;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6.5New
 * 创建者:      DanMing
 * 创建时间:    2019/1/10 15:50
 * 描述:         用户离线数据
 * 包名:        high.rivamed.myapplication.dbmodel
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class UserBean extends LitePalSupport implements Serializable {

   private String deptId;

   /**
    * operateSuccess : true
    * id : 0
    * opFlg : 200
    * pageNo : 1
    * pageSize : 20
    * systemType : HCT
    * accountVos : [{"accountId":"10000000000000000000000000000001","userId":"10000000000000000000000000000001","accountName":"admin","tenantId":"10000000000000000000000000000001","useState":"1","password":"8682b08b939e7d5e0e0db43db3bb39b42ab4453b","salt":"Jvd0G7Pz8nJoYsOu","userFeatureInfos":[{"featureId":"40288293677e276f01677e2c92850005","userId":"10000000000000000000000000000001","type":"2","data":"DA40BDC4"}],"menus":[{"path":"''","title":"耗材操作","icon":null,"parentSeq":null,"seq":102,"menu":null,"children":[{"path":null,"title":"选择操作","icon":null,"parentSeq":102,"seq":107,"menu":null,"children":[{"path":null,"title":"领用","icon":null,"parentSeq":107,"seq":108,"menu":null,"children":null,"selected":false},{"path":null,"title":"入库","icon":null,"parentSeq":107,"seq":109,"menu":null,"children":null,"selected":false},{"path":null,"title":"移出","icon":null,"parentSeq":107,"seq":110,"menu":null,"children":null,"selected":false},{"path":null,"title":"调拨","icon":null,"parentSeq":107,"seq":111,"menu":null,"children":null,"selected":false},{"path":null,"title":"移入","icon":null,"parentSeq":107,"seq":112,"menu":null,"children":null,"selected":false},{"path":null,"title":"退回","icon":null,"parentSeq":107,"seq":113,"menu":null,"children":null,"selected":false},{"path":null,"title":"退货","icon":null,"parentSeq":107,"seq":114,"menu":null,"children":null,"selected":false}],"selected":false}],"selected":false},{"path":"\u2018\u2019","title":"耗材流水","icon":null,"parentSeq":null,"seq":103,"menu":null,"children":null,"selected":false},{"path":"\u2018\u2019","title":"库存状态","icon":null,"parentSeq":null,"seq":104,"menu":null,"children":null,"selected":false},{"path":"\u2018\u2019","title":"实时盘点","icon":null,"parentSeq":null,"seq":105,"menu":null,"children":null,"selected":false},{"path":"\u2018\u2019","title":"使用记录","icon":null,"parentSeq":null,"seq":106,"menu":null,"children":null,"selected":false}]},{"accountId":"4028728167627ebe01676284dca60000","userId":"2","accountName":"123","tenantId":"10000000000000000000000000000001","useState":"1","password":"591449faec999931810e24e7cd6c490297bed310","salt":"lfxZ0WJVD3QFakiQ","userFeatureInfos":[],"menus":[{"path":"''","title":"耗材操作","icon":null,"parentSeq":null,"seq":102,"menu":null,"children":null,"selected":false},{"path":"\u2018\u2019","title":"耗材流水","icon":null,"parentSeq":null,"seq":103,"menu":null,"children":null,"selected":false},{"path":"\u2018\u2019","title":"库存状态","icon":null,"parentSeq":null,"seq":104,"menu":null,"children":null,"selected":false},{"path":"\u2018\u2019","title":"实时盘点","icon":null,"parentSeq":null,"seq":105,"menu":null,"children":null,"selected":false},{"path":"\u2018\u2019","title":"使用记录","icon":null,"parentSeq":null,"seq":106,"menu":null,"children":null,"selected":false}]},{"accountId":"ff80818167e886340167e8fe1c4a0002","userId":"ff80818167e886340167e8fe1c4b0003","accountName":"库管测试","useState":"1","password":"df52091ce049e9340d6c4734cdc086f4234f23f8","salt":"bIiOq4GIanc1vklc","userFeatureInfos":[],"menus":[{"path":"''","title":"耗材操作","icon":null,"parentSeq":null,"seq":102,"menu":null,"children":[{"path":null,"title":"选择操作","icon":null,"parentSeq":102,"seq":107,"menu":null,"children":[{"path":null,"title":"领用","icon":null,"parentSeq":107,"seq":108,"menu":null,"children":null,"selected":false},{"path":null,"title":"入库","icon":null,"parentSeq":107,"seq":109,"menu":null,"children":null,"selected":false},{"path":null,"title":"移出","icon":null,"parentSeq":107,"seq":110,"menu":null,"children":null,"selected":false},{"path":null,"title":"调拨","icon":null,"parentSeq":107,"seq":111,"menu":null,"children":null,"selected":false},{"path":null,"title":"移入","icon":null,"parentSeq":107,"seq":112,"menu":null,"children":null,"selected":false},{"p│ \t\t\t\t\t\t\t\t\t\t\t\t\tath ":null," \t\t\t\t\t\t\t\t\t\t\t\t\ttitle ":" \t\t\t\t\t\t\t\t\t\t\t\t\t退回 "," \t\t\t\t\t\t\t\t\t\t\t\t\ticon ":null," \t\t\t\t\t\t\t\t\t\t\t\t\tparentSeq ":107," \t\t\t\t\t\t\t\t\t\t\t\t\tseq ":113," \t\t\t\t\t\t\t\t\t\t\t\t\tmenu ":null," \t\t\t\t\t\t\t\t\t\t\t\t\tchildren ":null," \t\t\t\t\t\t\t\t\t\t\t\t\tselected ":false},{" \t\t\t\t\t\t\t\t\t\t\t\t\tpath ":null," \t\t\t\t\t\t\t\t\t\t\t\t\ttitle ":" \t\t\t\t\t\t\t\t\t\t\t\t\t退货 "," \t\t\t\t\t\t\t\t\t\t\t\t\ticon ":null," \t\t\t\t\t\t\t\t\t\t\t\t\tparentSeq ":107," \t\t\t\t\t\t\t\t\t\t\t\t\tseq ":114," \t\t\t\t\t\t\t\t\t\t\t\t\tmenu ":null," \t\t\t\t\t\t\t\t\t\t\t\t\tchildren ":null," \t\t\t\t\t\t\t\t\t\t\t\t\tselected ":false}]," \t\t\t\t\t\t\t\t\t\t\t\t\tselected ":false}]," \t\t\t\t\t\t\t\t\t\t\t\t\tselected ":false},{" \t\t\t\t\t\t\t\t\t\t\t\t\tpath ":"\u2018\u2019 \t\t\t\t\t\t\t\t\t\t\t\t\t"," \t\t\t\t\t\t\t\t\t\t\t\t\ttitle ":" \t\t\t\t\t\t\t\t\t\t\t\t\t耗材流水 "," \t\t\t\t\t\t\t\t\t\t\t\t\ticon ":null," \t\t\t\t\t\t\t\t\t\t\t\t\tparentSeq ":null," \t\t\t\t\t\t\t\t\t\t\t\t\tseq ":103," \t\t\t\t\t\t\t\t\t\t\t\t\tmenu ":null," \t\t\t\t\t\t\t\t\t\t\t\t\tchildren ":null," \t\t\t\t\t\t\t\t\t\t\t\t\tselected ":false},{" \t\t\t\t\t\t\t\t\t\t\t\t\tpath ":"\u2018\u2019 \t\t\t\t\t\t\t\t\t\t\t\t\t"," \t\t\t\t\t\t\t\t\t\t\t\t\ttitle ":" \t\t\t\t\t\t\t\t\t\t\t\t\t库存状态 "," \t\t\t\t\t\t\t\t\t\t\t\t\ticon ":null," \t\t\t\t\t\t\t\t\t\t\t\t\tparentSeq ":null," \t\t\t\t\t\t\t\t\t\t\t\t\tseq ":104," \t\t\t\t\t\t\t\t\t\t\t\t\tmenu ":null," \t\t\t\t\t\t\t\t\t\t\t\t\tchildren ":null," \t\t\t\t\t\t\t\t\t\t\t\t\tselected ":false},{" \t\t\t\t\t\t\t\t\t\t\t\t\tpath ":"\u2018\u2019 \t\t\t\t\t\t\t\t\t\t\t\t\t"," \t\t\t\t\t\t\t\t\t\t\t\t\ttitle ":" \t\t\t\t\t\t\t\t\t\t\t\t\t实时盘点 "," \t\t\t\t\t\t\t\t\t\t\t\t\ticon ":null," \t\t\t\t\t\t\t\t\t\t\t\t\tparentSeq ":null," \t\t\t\t\t\t\t\t\t\t\t\t\tseq ":105," \t\t\t\t\t\t\t\t\t\t\t\t\tmenu ":null," \t\t\t\t\t\t\t\t\t\t\t\t\tchildren ":null," \t\t\t\t\t\t\t\t\t\t\t\t\tselected ":false},{" \t\t\t\t\t\t\t\t\t\t\t\t\tpath ":"\u2018\u2019 \t\t\t\t\t\t\t\t\t\t\t\t\t"," \t\t\t\t\t\t\t\t\t\t\t\t\ttitle ":" \t\t\t\t\t\t\t\t\t\t\t\t\t使用记录 "," \t\t\t\t\t\t\t\t\t\t\t\t\ticon ":null," \t\t\t\t\t\t\t\t\t\t\t\t\tparentSeq ":null," \t\t\t\t\t\t\t\t\t\t\t\t\tseq ":106," \t\t\t\t\t\t\t\t\t\t\t\t\tmenu ":null," \t\t\t\t\t\t\t\t\t\t\t\t\tchildren ":null," \t\t\t\t\t\t\t\t\t\t\t\t\tselected ":false}]}]
    */

   private boolean operateSuccess;
   private String  opFlg;
   private List<AccountVosBean> accountVos = new ArrayList<>();

   public String getDeptId() {
	return deptId;
   }

   public void setDeptId(String deptId) {
	this.deptId = deptId;
   }

   public boolean isOperateSuccess() { return operateSuccess;}

   public void setOperateSuccess(boolean operateSuccess) { this.operateSuccess = operateSuccess;}

   public String getOpFlg() { return opFlg;}

   public void setOpFlg(String opFlg) { this.opFlg = opFlg;}

   public List<AccountVosBean> getAccountVos() { return accountVos;}

   public void setAccountVos(List<AccountVosBean> accountVos) { this.accountVos = accountVos;}


}
