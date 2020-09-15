package org.wh.engineer.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者: 钱凯
 * @创建时间: 2018/8/2
 * @功能描述:绑定指纹
 */
public class RegisterFingerDto {

    private List<UserFeatureInfosBean> userFeatureInfos = new ArrayList<>();
    private String  deviceType;
    public List<UserFeatureInfosBean> getUserFeatureInfos() {
        return userFeatureInfos;
    }

    public void setUserFeatureInfos(List<UserFeatureInfosBean> userFeatureInfos) {
        this.userFeatureInfos = userFeatureInfos;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public static class UserFeatureInfosBean {
        /**
         * userId : 8a80cb8164d9b3940164da1bff760003
         * data : 1111111111111111111111
         */

        private String featureName;
        private String userId;
        private String data;

        public String getFeatureName() {
            return featureName;
        }

        public void setFeatureName(String featureName) {
            this.featureName = featureName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

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
    }
}
