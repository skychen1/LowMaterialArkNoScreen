package org.wh.engineer.dto;

import org.litepal.crud.LitePalSupport;
import org.wh.engineer.dto.entity.Inventory;
import org.wh.engineer.dto.vo.CstExpirationVo;
import org.wh.engineer.dto.vo.DeviceInventoryVo;
import org.wh.engineer.dto.vo.InventoryGroupVos;
import org.wh.engineer.dto.vo.InventoryVo;
import org.wh.engineer.dto.vo.OrderVos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述: TODO<br/>
 * 公司: 北京瑞华康源科技有限公司<br/>
 * 版权: rivamed2018<br/>
 *
 * @author 魏小波
 * @version V1.0
 * @date 2018-07-12 12:14:19
 */
public class InventoryDto extends LitePalSupport implements Serializable {

    int id;
    private Inventory               inventory;
    private List<Inventory>         inventorys;
   private List<String>            orderIds;
    private List<DeviceInventoryVo> deviceInventoryVos;
    private List<InventoryVo>       inventoryVos = new ArrayList<>();
    private List<InventoryVo>       inInventoryVos;
    private List<InventoryVo>       outInventoryVos;

    //效期数量监控
    private List<CstExpirationVo>   cstExpirationVos;
    private String                  configPatientCollar;
    private String                  thingId;
    private String                  deviceId;
   private int    cstKinds;//下限
    private int    operation;
    private String requestResult;
    private int    type;    //0 放入 1取出
    private String cstSpec;
    private int    account;
    //名称及规格查询条件
    private String nameOrSpecQueryCon;
    private int    add;    // 库存情况
    private int    reduce;    // 扫描出来的库存
    private String cstId;
    private String remark;    //退货备注
    private String toSthId;    //移出备注
    private String sthId;        //调拨库房
    private int    stopFlag;        //效期情况 0过期 1-3近效期 4正常
    private int    countTwoin;
    private int    countMoveIn;
    private int    countBack;
    private int    countTempopary;
    private String epcName;
    private String patientId;
    private String patientName;
    private String accountId;
    private String operationScheduleId;
    private String bindType;
    private String       configType;
    private List<String> errorEpcs; //扫描到的系统里没有数据的epc

    private boolean operateSuccess;
    private boolean isSelected;
    private String  surgeryTime;//手术时间
    private String  operationSurgeonName;//医生名字
    private String  operatingRoomNoName;//手术间
    private String  deptName;//科室
    private String  operationBeginDateTime;
    private String  updateTime;
    private String        deptId;//科室ID
    private String        operatingRoomNo;//手术间ID
    private String        idNo;//身份证
    private String        sex;//性别
    private String        tempPatientId;
    private String        msg;
    private boolean       isCreate;
    private String        opFlg;
    private String        medicalId;
    private String        surgeryId;
    private String        hisPatientId        ;
    private int           totalCount;
   private List<String>   unNetMoreEpcs;
   private List<String>   epcs;
   private List<String>   deviceIds;
   private String            checkType;
   private String            checkFrom;
   private List<OrderVos> orderVos;
   /**
    * pageNo : 1
    * pageSize : 20
    * inInventoryVos : []
    * outInventoryVos : []
    * inTypeCount : 0
    * outTypeCount : 0
    * inTotalCount : 0
    * outTotalCount : 0
    * errorEpcs : []
    * notInStoreCstEpcs : []
    * isCanRefundFee : true
    * isRelatedCstAndStore : false
    * cstSum : 1
    * storageCstCountVo : [{"stockNum":2,"cstName":"麻醉咽喉镜窥视片","cstSpec":"中号"}]
    * fuWai : false
    */

    private int           pageNo;
   private int            pageSize;
   private int                     inTypeCount;
   private int                     cstSum;
   private List<InventoryVo>       storageCstCountVo;
   private List<InventoryGroupVos> inventoryGroupVos;

   public String getCheckType() {
      return checkType;
   }

   public void setCheckType(String checkType) {
      this.checkType = checkType;
   }

   public String getCheckFrom() {
      return checkFrom;
   }

   public void setCheckFrom(String checkFrom) {
      this.checkFrom = checkFrom;
   }
   public int getCstKinds() {
      return cstKinds;
   }

   public void setCstKinds(int cstKinds) {
      this.cstKinds = cstKinds;
   }

   public List<OrderVos> getOrderVos() {
      return orderVos;
   }

   public void setOrderVos(List<OrderVos> orderVos) {
      this.orderVos = orderVos;
   }

   public List<String> getOrderIds() {
      return orderIds;
   }

   public void setOrderIds(List<String> orderIds) {
      this.orderIds = orderIds;
   }

   public List<InventoryGroupVos> getInventoryGroupVos() {
      return inventoryGroupVos;
   }

   public void setInventoryGroupVos(
         List<InventoryGroupVos> inventoryGroupVos) {
      this.inventoryGroupVos = inventoryGroupVos;
   }

   public List<String> getEpcs() {
      return epcs;
   }

   public void setEpcs(List<String> epcs) {
      this.epcs = epcs;
   }

   public List<String> getDeviceIds() {
      return deviceIds;
   }

   public void setDeviceIds(List<String> deviceIds) {
      this.deviceIds = deviceIds;
   }

   public List<String> getUnNetMoreEpcs() {
      return unNetMoreEpcs;
   }

   public void setUnNetMoreEpcs(List<String> unNetMoreEpcs) {
      this.unNetMoreEpcs = unNetMoreEpcs;
   }

   public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
    private String optRoomId        ;

   public String getOptRoomId() {
	return optRoomId;
   }

   public void setOptRoomId(String optRoomId) {
	this.optRoomId = optRoomId;
   }

    public String getHisPatientId() {
        return hisPatientId;
    }

    public void setHisPatientId(String hisPatientId) {
        this.hisPatientId = hisPatientId;
    }
    public String getSurgeryId() {
        return surgeryId;
    }

    public void setSurgeryId(String surgeryId) {
        this.surgeryId = surgeryId;
    }

    public String getMedicalId() {
        return medicalId;
    }

    public void setMedicalId(String medicalId) {
        this.medicalId = medicalId;
    }
    public String getOpFlg() {
        return opFlg;
    }

    public void setOpFlg(String opFlg) {
        this.opFlg = opFlg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isOperateSuccess() {
        return operateSuccess;
    }

    public void setOperateSuccess(boolean operateSuccess) {
        this.operateSuccess = operateSuccess;
    }

    public List<InventoryVo> getInInventoryVos() {
        return inInventoryVos;
    }

    public void setInInventoryVos(
          List<InventoryVo> inInventoryVos) {
        this.inInventoryVos = inInventoryVos;
    }

    public List<InventoryVo> getOutInventoryVos() {
        return outInventoryVos;
    }

    public void setOutInventoryVos(
          List<InventoryVo> outInventoryVos) {
        this.outInventoryVos = outInventoryVos;
    }

    public String getCstId() {
        return cstId;
    }

    public void setCstId(String cstId) {
        this.cstId = cstId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getSurgeryTime() {
        return surgeryTime;
    }

    public void setSurgeryTime(String surgeryTime) {
        this.surgeryTime = surgeryTime;
    }

    public String getOperationSurgeonName() {
        return operationSurgeonName;
    }

    public void setOperationSurgeonName(String operationSurgeonName) {
        this.operationSurgeonName = operationSurgeonName;
    }

    public String getOperatingRoomNoName() {
        return operatingRoomNoName;
    }

    public void setOperatingRoomNoName(String operatingRoomNoName) {
        this.operatingRoomNoName = operatingRoomNoName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getOperationBeginDateTime() {
        return operationBeginDateTime;
    }

    public void setOperationBeginDateTime(String operationBeginDateTime) {
        this.operationBeginDateTime = operationBeginDateTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getOperatingRoomNo() {
        return operatingRoomNo;
    }

    public void setOperatingRoomNo(String operatingRoomNo) {
        this.operatingRoomNo = operatingRoomNo;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTempPatientId() {
        return tempPatientId;
    }

    public void setTempPatientId(String tempPatientId) {
        this.tempPatientId = tempPatientId;
    }

    public boolean isCreate() {
        return isCreate;
    }

    public void setCreate(boolean create) {
        isCreate = create;
    }

    public List<String> getErrorEpcs() {
        return errorEpcs;
    }

    public String getOperationScheduleId() {
        return operationScheduleId;
    }

    public void setOperationScheduleId(String operationScheduleId) {
        this.operationScheduleId = operationScheduleId;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public void setErrorEpcs(List<String> errorEpcs) {
        this.errorEpcs = errorEpcs;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBindType() {
        return bindType;
    }

    public void setBindType(String bindType) {
        this.bindType = bindType;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }


    public String getEpcName() {
        return epcName;
    }

    public void setEpcName(String epcName) {
        this.epcName = epcName;
    }

    public String getConfigPatientCollar() {
        return configPatientCollar;
    }

    public void setConfigPatientCollar(String configPatientCollar) {
        this.configPatientCollar = configPatientCollar;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCountTwoin() {
        return countTwoin;
    }

    public void setCountTwoin(int countTwoin) {
        this.countTwoin = countTwoin;
    }

    public int getCountMoveIn() {
        return countMoveIn;
    }

    public void setCountMoveIn(int countMoveIn) {
        this.countMoveIn = countMoveIn;
    }

    public int getCountBack() {
        return countBack;
    }

    public void setCountBack(int countBack) {
        this.countBack = countBack;
    }

    public int getCountTempopary() {
        return countTempopary;
    }

    public void setCountTempopary(int countTempopary) {
        this.countTempopary = countTempopary;
    }


    public String getToSthId() {
        return toSthId;
    }

    public void setToSthId(String toSthId) {
        this.toSthId = toSthId;
    }

    public String getSthId() {
        return sthId;
    }

    public void setSthId(String sthId) {
        this.sthId = sthId;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public Inventory getTCstInventory() {
        return inventory;
    }

    public void setTCstInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public List<Inventory> getTCstInventorys() {
        return inventorys;
    }

    public void setTCstInventorys(List<Inventory> inventories) {
        this.inventorys = inventories;
    }

    public String getThingId() {
        return this.thingId;
    }

    public void setThingId(String thingId) {
        this.thingId = thingId;
    }

    public List<InventoryVo> getInventoryVos() {
        return inventoryVos;
    }

    public void setInventoryVos(List<InventoryVo> inventoryVos) {
        this.inventoryVos = inventoryVos;
    }

    public String getRequestResult() {
        return requestResult;
    }

    public void setRequestResult(String requestResult) {
        this.requestResult = requestResult;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }


    public String getNameOrSpecQueryCon() {
        return nameOrSpecQueryCon;
    }

    public List<CstExpirationVo> getCstExpirationVos() {
        return cstExpirationVos;
    }

    public void setCstExpirationVos(List<CstExpirationVo> cstExpirationVos) {
        this.cstExpirationVos = cstExpirationVos;
    }

    public void setNameOrSpecQueryCon(String nameOrSpecQueryCon) {
        this.nameOrSpecQueryCon = nameOrSpecQueryCon;
    }

    public String getCstSpec() {
        return cstSpec;
    }

    public void setCstSpec(String cstSpec) {
        this.cstSpec = cstSpec;
    }

    public int getAdd() {
        return add;
    }

    public void setAdd(int add) {
        this.add = add;
    }

    public int getReduce() {
        return reduce;
    }

    public void setReduce(int reduce) {
        this.reduce = reduce;
    }

    public List<DeviceInventoryVo> getDeviceInventoryVos() {
        return deviceInventoryVos;
    }

    public void setDeviceInventoryVos(List<DeviceInventoryVo> deviceInventoryVos) {
        this.deviceInventoryVos = deviceInventoryVos;
    }

    public List<Inventory> getInventorys() {
        return inventorys;
    }

    public void setInventorys(List<Inventory> inventorys) {
        this.inventorys = inventorys;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCstCode() {
        return cstId;
    }

    public void setCstCode(String cstId) {
        this.cstId = cstId;
    }

    public int getStopFlag() {
        return stopFlag;
    }

    public void setStopFlag(int stopFlag) {
        this.stopFlag = stopFlag;
    }

   public int getPageNo() { return pageNo;}

   public void setPageNo(int pageNo) { this.pageNo = pageNo;}

   public int getPageSize() { return pageSize;}

   public void setPageSize(int pageSize) { this.pageSize = pageSize;}

   public int getInTypeCount() { return inTypeCount;}

   public void setInTypeCount(int inTypeCount) { this.inTypeCount = inTypeCount;}

   public int getCstSum() { return cstSum;}

   public void setCstSum(int cstSum) { this.cstSum = cstSum;}

   public List<InventoryVo> getStorageCstCountVo() { return storageCstCountVo;}

   public void setStorageCstCountVo(
         List<InventoryVo> storageCstCountVo) { this.storageCstCountVo = storageCstCountVo;}

   public static class InventorysBean {

        /**
         * epc : 00020820180613000014
         * cstName : 弯型和直型腔内吻合器
         * cstSpec : 支
         * expiration : 已过期
         * deviceName : 1号柜
         * stopFlag : 0
         */

        private String epc;
        private String cstName;
        private String cstSpec;
        private String expiration;
        private String deviceName;
        private String stopFlag;
        private String countStock;
        private String countActual;

        public String getCountStock() {
            return countStock;
        }

        public void setCountStock(String countStock) {
            this.countStock = countStock;
        }

        public String getCountActual() {
            return countActual;
        }

        public void setCountActual(String countActual) {
            this.countActual = countActual;
        }

        public String getEpc() {
            return epc;
        }

        public void setEpc(String epc) {
            this.epc = epc;
        }

        public String getCstName() {
            return cstName;
        }

        public void setCstName(String cstName) {
            this.cstName = cstName;
        }

        public String getCstSpec() {
            return cstSpec;
        }

        public void setCstSpec(String cstSpec) {
            this.cstSpec = cstSpec;
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

        public String getStopFlag() {
            return stopFlag;
        }

        public void setStopFlag(String stopFlag) {
            this.stopFlag = stopFlag;
        }
    }
}
