package org.wh.engineer.dto;

/**
 * @作者: 钱凯
 * @创建时间: 2018/8/2
 * @功能描述:
 */
public class FingerLoginDto {

    /**
     * userFeatureInfo : {"data":"1111111111111111111111"}
     * thingCode : 402882a064e4f6e20164e52508ff0024
     */

    private UserFeatureInfoBean userFeatureInfo;
    private String thingId;
    private String deptId;
    private String  systemType;
    private String  deviceType;
    private String loginType;

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public UserFeatureInfoBean getUserFeatureInfo() {
        return userFeatureInfo;
    }

    public void setUserFeatureInfo(UserFeatureInfoBean userFeatureInfo) {
        this.userFeatureInfo = userFeatureInfo;
    }

    public String getThingId() {
        return thingId;
    }

    public void setThingId(String thingId) {
        this.thingId = thingId;
    }

    public static class UserFeatureInfoBean {
        /**
         * data : 1111111111111111111111
         */

        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
}
