package org.wh.engineer.dto;

/**
 * @作者: 钱凯
 * @创建时间: 2018/8/8
 * @功能描述:
 */
public class IdCardLoginDto {

    /**
     * userFeatureInfo : {"data":"BA215FED","type":"2"}
     */

    private UserFeatureInfoBean userFeatureInfo;
    private String thingId;
    private String               systemType;
    private String deptId;
    private String loginType;

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getThingId() {
        return thingId;
    }

    public void setThingId(String thingId) {
        this.thingId = thingId;
    }

    public UserFeatureInfoBean getUserFeatureInfo() {
        return userFeatureInfo;
    }

    public void setUserFeatureInfo(UserFeatureInfoBean userFeatureInfo) {
        this.userFeatureInfo = userFeatureInfo;
    }

    public static class UserFeatureInfoBean {
        /**
         * data : BA215FED
         * type : 2
         */

        private String data;
        private String type;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
