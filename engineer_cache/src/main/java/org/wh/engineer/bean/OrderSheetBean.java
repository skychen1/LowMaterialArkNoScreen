package org.wh.engineer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: new2.6.3
 * @Package: high.rivamed.myapplication.bean
 * @ClassName: OrderSheetBean
 * @Description: 医嘱单领用-顶部医嘱单列表
 * @Author: Amos_Bo
 * @CreateDate: 2018/10/26 13:43
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/10/26 13:43
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class OrderSheetBean implements Serializable {
    /**
     * pageNo : 1
     * pageSize : 5
     * rows : [{"id":"40288293667aa53e01667aa5fc610000","creatorName":"adminUM","accountId":null,"patientId":null,"patientName":"573076/马羊焕","operationScheduleId":null,"operationRoomNo":null,"operationRoomNoName":null,"createTime":"2018-10-16 10:13:10"}]
     * total : 1
     */

    private int pageNo;
    private int pageSize;
    private int total;
    private List<RowsBean> rows;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean implements Serializable {
        /**
         * id : 40288293667aa53e01667aa5fc610000
         * creatorName : adminUM
         * accountId : null
         * patientId : null
         * patientName : 573076/马羊焕
         * operationScheduleId : null
         * operationRoomNo : null
         * operationRoomNoName : null
         * createTime : 2018-10-16 10:13:10
         */

        private String suiteId;
        private String orderId;
        private String creatorName;
        private String accountId;
        private String patientId;
        private String patientName;
        private String operationScheduleId;
        private String operationRoomNo = "";
        private String roomName        = "";
        private String createTime      = "";
        public String  cstType         = "";
        public String  cstNumber       = "";
        public String  surgeryId       = "";
        public String  medicalId       = "";

        public String getCstType() {
            return cstType;
        }

        public void setCstType(String cstType) {
            this.cstType = cstType;
        }

        public String getCstNumber() {
            return cstNumber;
        }

        public void setCstNumber(String cstNumber) {
            this.cstNumber = cstNumber;
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

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getSuiteId() {
            return suiteId;
        }

        public void setSuiteId(String suiteId) {
            this.suiteId = suiteId;
        }

        public String getCreatorName() {
            return creatorName;
        }

        public void setCreatorName(String creatorName) {
            this.creatorName = creatorName;
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

        public String getOperationScheduleId() {
            return operationScheduleId;
        }

        public void setOperationScheduleId(String operationScheduleId) {
            this.operationScheduleId = operationScheduleId;
        }

        public String getOperationRoomNo() {
            return operationRoomNo;
        }

        public void setOperationRoomNo(String operationRoomNo) {
            this.operationRoomNo = operationRoomNo;
        }

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
