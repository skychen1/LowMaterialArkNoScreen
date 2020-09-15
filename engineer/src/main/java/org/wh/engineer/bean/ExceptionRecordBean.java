package org.wh.engineer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称：高值
 * 创建者：chenyanling
 * 创建时间：2019/5/13
 * 描述：异常处理-异常记录
 */
public class ExceptionRecordBean implements Serializable {

    /**
     * pageNo : 1
     * pageSize : 2
     * rows : [{"cstName":"麻醉咽喉镜窥视片","cstSpec":"中号","epc":"00021820180608000023","expiryDate":"2019-11-01","deviceName":"2柜子","operation":"强开出柜","countStock":0,"countActual":0,"count":0,"operationStatus":0,"isErrorOperation":0,"deleteCount":0,"sortNum":30,"unNormalSource":"7","operationTime":"2019-06-19"},{"cstName":"人类免疫缺陷病毒P24抗原检测试剂盒","cstSpec":"1234","epc":"00022120180612000109","expiryDate":"2019-07-13","deviceName":"2柜子","operation":"强开出柜","countStock":0,"countActual":0,"count":0,"operationStatus":0,"isErrorOperation":0,"deleteCount":0,"sortNum":30,"unNormalSource":"7","operationTime":"2019-06-24"}]
     * total : 66
     */
    private boolean operateSuccess;
    private int pageNo;
    private int pageSize;
    private int total;
    private List<RowsBean> rows;

    public boolean isOperateSuccess() {
        return operateSuccess;
    }

    public void setOperateSuccess(boolean operateSuccess) {
        this.operateSuccess = operateSuccess;
    }
    public int getPageNo() { return pageNo;}

    public void setPageNo(int pageNo) { this.pageNo = pageNo;}

    public int getPageSize() { return pageSize;}

    public void setPageSize(int pageSize) { this.pageSize = pageSize;}

    public int getTotal() { return total;}

    public void setTotal(int total) { this.total = total;}

    public List<RowsBean> getRows() { return rows;}

    public void setRows(List<RowsBean> rows) { this.rows = rows;}

    public static class RowsBean implements Serializable{

        /**
         * cstName : 麻醉咽喉镜窥视片
         * cstSpec : 中号
         * epc : 00021820180608000023
         * expiryDate : 2019-11-01
         * deviceName : 2柜子
         * operation : 强开出柜
         * countStock : 0
         * countActual : 0
         * count : 0
         * operationStatus : 0
         * isErrorOperation : 0
         * deleteCount : 0
         * sortNum : 30
         * unNormalSource : 7
         * operationTime : 2019-06-19
         * unNormalOperationTime : 2019-06-19
         * 	"operationResult": "殷力志",
         */

        private String cstName;
        private String cstSpec;
        private String epc;
        private String expiryDate;
        private String deviceName;
        private String operation;//操作
        private int    countStock;
        private int    countActual;
        private int    count;
        private int    operationStatus;
        private int    isErrorOperation;
        private int    deleteCount;
        private int    sortNum;
        private String unNormalSource;//来源
        private String operatorName;
        private String operationTime;
        private String unNormalId;
        private String unNormalContent;
        private String operationResult;
        private String unNormalOperationTime;
        private String userName;
        private String operatorId;
        private boolean isSelected;//是否选中

        public String getOperatorId() {
            return operatorId;
        }

        public void setOperatorId(String operatorId) {
            this.operatorId = operatorId;
        }

        public String getUnNormalContent() {
            return unNormalContent;
        }

        public void setUnNormalContent(String unNormalContent) {
            this.unNormalContent = unNormalContent;
        }

        public String getOperationResult() {
            return operationResult;
        }

        public void setOperationResult(String operationResult) {
            this.operationResult = operationResult;
        }

        public String getUnNormalOperationTime() {
            return unNormalOperationTime;
        }

        public void setUnNormalOperationTime(String unNormalOperationTime) {
            this.unNormalOperationTime = unNormalOperationTime;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUnNormalId() {
            return unNormalId;
        }

        public void setUnNormalId(String unNormalId) {
            this.unNormalId = unNormalId;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getOperatorName() {
            return operatorName;
        }

        public void setOperatorName(String operatorName) {
            this.operatorName = operatorName;
        }

        public String getCstName() { return cstName;}

        public void setCstName(String cstName) { this.cstName = cstName;}

        public String getCstSpec() { return cstSpec;}

        public void setCstSpec(String cstSpec) { this.cstSpec = cstSpec;}

        public String getEpc() { return epc;}

        public void setEpc(String epc) { this.epc = epc;}

        public String getExpiryDate() { return expiryDate;}

        public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate;}

        public String getDeviceName() { return deviceName;}

        public void setDeviceName(String deviceName) { this.deviceName = deviceName;}

        public String getOperation() { return operation;}

        public void setOperation(String operation) { this.operation = operation;}

        public int getCountStock() { return countStock;}

        public void setCountStock(int countStock) { this.countStock = countStock;}

        public int getCountActual() { return countActual;}

        public void setCountActual(int countActual) { this.countActual = countActual;}

        public int getCount() { return count;}

        public void setCount(int count) { this.count = count;}

        public int getOperationStatus() { return operationStatus;}

        public void setOperationStatus(
              int operationStatus) { this.operationStatus = operationStatus;}

        public int getIsErrorOperation() { return isErrorOperation;}

        public void setIsErrorOperation(
              int isErrorOperation) { this.isErrorOperation = isErrorOperation;}

        public int getDeleteCount() { return deleteCount;}

        public void setDeleteCount(int deleteCount) { this.deleteCount = deleteCount;}

        public int getSortNum() { return sortNum;}

        public void setSortNum(int sortNum) { this.sortNum = sortNum;}

        public String getUnNormalSource() { return unNormalSource;}

        public void setUnNormalSource(
              String unNormalSource) { this.unNormalSource = unNormalSource;}

        public String getOperationTime() { return operationTime;}

        public void setOperationTime(String operationTime) { this.operationTime = operationTime;}
    }
}
