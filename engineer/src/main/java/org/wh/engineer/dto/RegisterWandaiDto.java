package org.wh.engineer.dto;

/**
 * @作者: 钱凯
 * @创建时间: 2018/8/9
 * @功能描述:绑定腕带dto
 */
public class RegisterWandaiDto {
    /**
     * userFeatureInfo : {"userId":"8a80cb8164d9b3940164da1bff760003","data":"CB2F7FA9","type":"2"}
     */

    private UserFeatureInfoBean userFeatureInfo;

    public UserFeatureInfoBean getUserFeatureInfo() {
        return userFeatureInfo;
    }

    public void setUserFeatureInfo(UserFeatureInfoBean userFeatureInfo) {
        this.userFeatureInfo = userFeatureInfo;
    }

    public static class UserFeatureInfoBean {
        /**
         * userId : 8a80cb8164d9b3940164da1bff760003
         * data : CB2F7FA9
         * type : 2
         */

        private String userId;
        private String data;
        private String type;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

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
