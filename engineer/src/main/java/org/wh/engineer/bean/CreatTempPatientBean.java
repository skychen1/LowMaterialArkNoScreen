package org.wh.engineer.bean;

/**
 * @作者: 钱凯
 * @创建时间: 2018/8/26
 * @功能描述:	创建临时患者信息上传模型
 */
public class CreatTempPatientBean {


    /**
     * tTransOperationSchedule : {"idNo":"133232324444","name":"刘得到","operatingDeptCode":"245","operatingRoomNo":"1号","surgeryTime":"2018-08-26","sex":"男"}
     */

    private TTransOperationScheduleBean tTransOperationSchedule;

    public TTransOperationScheduleBean getTTransOperationSchedule() {
        return tTransOperationSchedule;
    }

    public void setTTransOperationSchedule(TTransOperationScheduleBean tTransOperationSchedule) {
        this.tTransOperationSchedule = tTransOperationSchedule;
    }

    public static class TTransOperationScheduleBean {
        /**
         * idNo : 133232324444
         * name : 刘得到
         * operatingDeptCode : 245
         * operatingRoomNo : 1号
         * surgeryTime : 2018-08-26
         * sex : 男
         */

        private String  idNo;
        private String  name;
        private String  deptId;
        private String  operatingRoomNo;
        private String  operatingRoomNoName;
        private String  surgeryTime;
        private String  sex;
        private boolean isCreate;
        private String medicalId;
        private String deptType;

        public String getDeptType() {
            return deptType;
        }

        public void setDeptType(String deptType) {
            this.deptType = deptType;
        }

        public String getMedicalId() {
            return medicalId;
        }

        public void setMedicalId(String medicalId) {
            this.medicalId = medicalId;
        }
        public String getDeptId() {
            return deptId;
        }

        public void setDeptId(String deptId) {
            this.deptId = deptId;
        }
        public boolean isCreate() {
            return isCreate;
        }

        public void setCreate(boolean create) {
            isCreate = create;
        }

        public String getOperatingRoomNoName() {
            return operatingRoomNoName;
        }

        public void setOperatingRoomNoName(String operatingRoomNoName) {
            this.operatingRoomNoName = operatingRoomNoName;
        }

        public String getIdNo() {
            return idNo;
        }

        public void setIdNo(String idNo) {
            this.idNo = idNo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        public String getOperatingRoomNo() {
            return operatingRoomNo;
        }

        public void setOperatingRoomNo(String operatingRoomNo) {
            this.operatingRoomNo = operatingRoomNo;
        }

        public String getSurgeryTime() {
            return surgeryTime;
        }

        public void setSurgeryTime(String surgeryTime) {
            this.surgeryTime = surgeryTime;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }
}
