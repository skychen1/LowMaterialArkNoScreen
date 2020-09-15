package org.wh.engineer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      DanMing
 * 创建时间:    2018/7/27 11:08
 * 描述:        预注册返回的
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class ThingDto implements Serializable{

    /**
     * id : 0
     * thing : {"thingId":"40288297673554f20167355be8720000","sthId":"40288b5f670febb90167100456270000","thingName":"2.6.3柜子","thingModel":"1","macAddress":"","status":"1","sn":"191919","remark":"","activation":"1","deptId":"40287281674eaca301674ebd649d0003","deptName":"产科","createTime":"2018-12-03 07:24:30","sthName":"中央一库房"}
     * thingSnVo : {"thingId":"40288297673554f20167355be8720000","deptId":"40287281674eaca301674ebd649d0003","deptName":"产科","macAddress":"","roomNo":null,"roomName":null,"remark":"","sn":"191919","status":"1","sthId":"40288b5f670febb90167100456270000","sthName":"中央一库房","thingName":"2.6.3柜子","thingModel":"1","createTime":"2018-12-03","activation":"1","branchId":"111"}
     * deviceVos : [{"deviceName":"柜1","deviceId":"40288297673554f20167355be8a30001","deviceType":"10","parent":"-1","devices":[{"deviceId":"40288297673554f20167355be8ac0002","deviceName":"网络多功能集控设备","deviceType":"1","identification":"0","ip":"192.168.100.134","parent":"40288297673554f20167355be8a30001","remark":"","status":1,"thingId":"40288297673554f20167355be8720000","operationTime":"2018-12-03","dictId":"ff80818166c7b5d00166c8f7f8260010"},{"deviceId":"40288297673554f20167355be8af0003","deviceName":"罗丹贝尔-reader","deviceType":"2","identification":"0","ip":"192.168.100.192","parent":"40288297673554f20167355be8a30001","remark":"","status":1,"thingId":"40288297673554f20167355be8720000","operationTime":"2018-12-03","dictId":"ff80818166c7b5d00166c8fbcbf50014"}]},{"deviceName":"柜2","deviceId":"40288297673554f20167355be8af0004","deviceType":"10","parent":"-1","devices":[{"deviceId":"40288297673554f20167355be8b20005","deviceName":"网络多功能集控设备","deviceType":"1","identification":"0","ip":"192.168.100.122","parent":"40288297673554f20167355be8af0004","remark":"","status":1,"thingId":"40288297673554f20167355be8720000","operationTime":"2018-12-03","dictId":"ff80818166c7b5d00166c8f7f8260010"},{"deviceId":"40288297673554f20167355be8b50006","deviceName":"reader","deviceType":"2","identification":"0","ip":"192.168.100.13","parent":"40288297673554f20167355be8af0004","remark":"","status":1,"thingId":"40288297673554f20167355be8720000","operationTime":"2018-12-03","dictId":"ff80818166c7b5d00166c8fb579d0013"}]}]
     * sn : 191919
     */
    private String                   msg;
    private boolean operateSuccess;
    private int id;
    private ThingBean           thing;
    private ThingSnVoBean       thingSnVo;
    private String              sn;
    private List<DeviceVosBean> deviceVos;
    private HospitalInfoVo hospitalInfoVo;
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

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public ThingBean getThing() { return thing;}

    public void setThing(ThingBean thing) { this.thing = thing;}

    public ThingSnVoBean getThingSnVo() { return thingSnVo;}

    public void setThingSnVo(ThingSnVoBean thingSnVo) { this.thingSnVo = thingSnVo;}

    public String getSn() { return sn;}

    public void setSn(String sn) { this.sn = sn;}

    public List<DeviceVosBean> getDeviceVos() { return deviceVos;}

    public void setDeviceVos(List<DeviceVosBean> deviceVos) { this.deviceVos = deviceVos;}

    public static class ThingBean {

        /**
         * thingId : 40288297673554f20167355be8720000
         * sthId : 40288b5f670febb90167100456270000
         * thingName : 2.6.3柜子
         * thingModel : 1
         * macAddress :
         * status : 1
         * sn : 191919
         * remark :
         * activation : 1
         * deptId : 40287281674eaca301674ebd649d0003
         * deptName : 产科
         * createTime : 2018-12-03 07:24:30
         * sthName : 中央一库房
         */

        private String thingId;
        private String sthId;
        private String thingName;
        private String thingModel;
        private String macAddress;
        private String status;
        private String sn;
        private String remark;
        private String activation;
        private String deptId;
        private String deptName;
        private String createTime;
        private String sthName;
        private String thingType;
        private String localIp;
        private String                   systemType;

        public String getSystemType() {
            return systemType;
        }

        public void setSystemType(String systemType) {
            this.systemType = systemType;
        }

        public String getLocalIp() {
            return localIp;
        }

        public void setLocalIp(String localIp) {
            this.localIp = localIp;
        }

        public String getThingType() {
            return thingType;
        }

        public void setThingType(String thingType) {
            this.thingType = thingType;
        }

        public String getThingId() { return thingId;}

        public void setThingId(String thingId) { this.thingId = thingId;}

        public String getSthId() { return sthId;}

        public void setSthId(String sthId) { this.sthId = sthId;}

        public String getThingName() { return thingName;}

        public void setThingName(String thingName) { this.thingName = thingName;}

        public String getThingModel() { return thingModel;}

        public void setThingModel(String thingModel) { this.thingModel = thingModel;}

        public String getMacAddress() { return macAddress;}

        public void setMacAddress(String macAddress) { this.macAddress = macAddress;}

        public String getStatus() { return status;}

        public void setStatus(String status) { this.status = status;}

        public String getSn() { return sn;}

        public void setSn(String sn) { this.sn = sn;}

        public String getRemark() { return remark;}

        public void setRemark(String remark) { this.remark = remark;}

        public String getActivation() { return activation;}

        public void setActivation(String activation) { this.activation = activation;}

        public String getDeptId() { return deptId;}

        public void setDeptId(String deptId) { this.deptId = deptId;}

        public String getDeptName() { return deptName;}

        public void setDeptName(String deptName) { this.deptName = deptName;}

        public String getCreateTime() { return createTime;}

        public void setCreateTime(String createTime) { this.createTime = createTime;}

        public String getSthName() { return sthName;}

        public void setSthName(String sthName) { this.sthName = sthName;}
    }

    public static class ThingSnVoBean {

        /**
         * thingId : 40288297673554f20167355be8720000
         * deptId : 40287281674eaca301674ebd649d0003
         * deptName : 产科
         * macAddress :
         * roomNo : null
         * roomName : null
         * remark :
         * sn : 191919
         * status : 1
         * sthId : 40288b5f670febb90167100456270000
         * sthName : 中央一库房
         * thingName : 2.6.3柜子
         * thingModel : 1
         * createTime : 2018-12-03
         * activation : 1
         * branchId : 111
         */

        private String thingId;
        private String deptId;
        private String deptName;
        private String macAddress;
        private String roomNo;
        private String roomName;
        private String remark;
        private String sn;
        private String status;
        private String sthId;
        private String sthName;
        private String thingName;
        private String thingModel;
        private String createTime;
        private String activation;
        private String branchId;

        public String getThingId() { return thingId;}

        public void setThingId(String thingId) { this.thingId = thingId;}

        public String getDeptId() { return deptId;}

        public void setDeptId(String deptId) { this.deptId = deptId;}

        public String getDeptName() { return deptName;}

        public void setDeptName(String deptName) { this.deptName = deptName;}

        public String getMacAddress() { return macAddress;}

        public void setMacAddress(String macAddress) { this.macAddress = macAddress;}

        public String getRoomNo() { return roomNo;}

        public void setRoomNo(String roomNo) { this.roomNo = roomNo;}

        public String getRoomName() { return roomName;}

        public void setRoomName(String roomName) { this.roomName = roomName;}

        public String getRemark() { return remark;}

        public void setRemark(String remark) { this.remark = remark;}

        public String getSn() { return sn;}

        public void setSn(String sn) { this.sn = sn;}

        public String getStatus() { return status;}

        public void setStatus(String status) { this.status = status;}

        public String getSthId() { return sthId;}

        public void setSthId(String sthId) { this.sthId = sthId;}

        public String getSthName() { return sthName;}

        public void setSthName(String sthName) { this.sthName = sthName;}

        public String getThingName() { return thingName;}

        public void setThingName(String thingName) { this.thingName = thingName;}

        public String getThingModel() { return thingModel;}

        public void setThingModel(String thingModel) { this.thingModel = thingModel;}

        public String getCreateTime() { return createTime;}

        public void setCreateTime(String createTime) { this.createTime = createTime;}

        public String getActivation() { return activation;}

        public void setActivation(String activation) { this.activation = activation;}

        public String getBranchId() { return branchId;}

        public void setBranchId(String branchId) { this.branchId = branchId;}
    }

    public static class DeviceVosBean {

        /**
         * deviceName : 柜1
         * deviceId : 40288297673554f20167355be8a30001
         * deviceType : 10
         * parent : -1
         * devices : [{"deviceId":"40288297673554f20167355be8ac0002","deviceName":"网络多功能集控设备","deviceType":"1","identification":"0","ip":"192.168.100.134","parent":"40288297673554f20167355be8a30001","remark":"","status":1,"thingId":"40288297673554f20167355be8720000","operationTime":"2018-12-03","dictId":"ff80818166c7b5d00166c8f7f8260010"},{"deviceId":"40288297673554f20167355be8af0003","deviceName":"罗丹贝尔-reader","deviceType":"2","identification":"0","ip":"192.168.100.192","parent":"40288297673554f20167355be8a30001","remark":"","status":1,"thingId":"40288297673554f20167355be8720000","operationTime":"2018-12-03","dictId":"ff80818166c7b5d00166c8fbcbf50014"}]
         */

        private String deviceName;
        private String            deviceId;
        private String            deviceType;
        private String            parent;
        private String            cabinetNum;//柜子编号：同个编号表示同一个柜子
        private String            cabinetType;//柜子类型：1：上柜 -1：下柜 0：单柜
        private List<DevicesBean> devices;

        public String getCabinetNum() {
            return cabinetNum;
        }

        public void setCabinetNum(String cabinetNum) {
            this.cabinetNum = cabinetNum;
        }

        public String getCabinetType() {
            return cabinetType;
        }

        public void setCabinetType(String cabinetType) {
            this.cabinetType = cabinetType;
        }

        public String getDeviceName() { return deviceName;}

        public void setDeviceName(String deviceName) { this.deviceName = deviceName;}

        public String getDeviceId() { return deviceId;}

        public void setDeviceId(String deviceId) { this.deviceId = deviceId;}

        public String getDeviceType() { return deviceType;}

        public void setDeviceType(String deviceType) { this.deviceType = deviceType;}

        public String getParent() { return parent;}

        public void setParent(String parent) { this.parent = parent;}

        public List<DevicesBean> getDevices() { return devices;}

        public void setDevices(List<DevicesBean> devices) { this.devices = devices;}

        public static class DevicesBean {

            /**
             * deviceId : 40288297673554f20167355be8ac0002
             * deviceName : 网络多功能集控设备
             * deviceType : 1
             * identification : 0
             * ip : 192.168.100.134
             * parent : 40288297673554f20167355be8a30001
             * remark :
             * status : 1
             * thingId : 40288297673554f20167355be8720000
             * operationTime : 2018-12-03
             * dictId : ff80818166c7b5d00166c8f7f8260010
             */

            private String deviceId;
            private String deviceName;
            private String deviceType;
            private String identification;
            private String ip;
            private String parent;
            private String remark;
            private int    status;
            private String thingId;
            private String operationTime;
            private String dictId;

            public String getDeviceId() { return deviceId;}

            public void setDeviceId(String deviceId) { this.deviceId = deviceId;}

            public String getDeviceName() { return deviceName;}

            public void setDeviceName(String deviceName) { this.deviceName = deviceName;}

            public String getDeviceType() { return deviceType;}

            public void setDeviceType(String deviceType) { this.deviceType = deviceType;}

            public String getIdentification() { return identification;}

            public void setIdentification(
                  String identification) { this.identification = identification;}

            public String getIp() { return ip;}

            public void setIp(String ip) { this.ip = ip;}

            public String getParent() { return parent;}

            public void setParent(String parent) { this.parent = parent;}

            public String getRemark() { return remark;}

            public void setRemark(String remark) { this.remark = remark;}

            public int getStatus() { return status;}

            public void setStatus(int status) { this.status = status;}

            public String getThingId() { return thingId;}

            public void setThingId(String thingId) { this.thingId = thingId;}

            public String getOperationTime() { return operationTime;}

            public void setOperationTime(
                  String operationTime) { this.operationTime = operationTime;}

            public String getDictId() { return dictId;}

            public void setDictId(String dictId) { this.dictId = dictId;}
        }
    }
    public HospitalInfoVo getHospitalInfoVo() {
        return hospitalInfoVo;
    }

    public void setHospitalInfoVo(
          HospitalInfoVo hospitalInfoVo) {
        this.hospitalInfoVo = hospitalInfoVo;
    }

    public static class HospitalInfoVo {//激活的时候医院信息
        private String sthId;
        private String deptId;
        private String optRoomId;
        private String branchId;
        private String deptName;

        public String getSthId() {
            return sthId;
        }

        public void setSthId(String sthId) {
            this.sthId = sthId;
        }

        public String getDeptId() {
            return deptId;
        }

        public void setDeptId(String deptId) {
            this.deptId = deptId;
        }

        public String getOptRoomId() {
            return optRoomId;
        }

        public void setOptRoomId(String optRoomId) {
            this.optRoomId = optRoomId;
        }

        public String getBranchId() {
            return branchId;
        }

        public void setBranchId(String branchId) {
            this.branchId = branchId;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }
    }
}
