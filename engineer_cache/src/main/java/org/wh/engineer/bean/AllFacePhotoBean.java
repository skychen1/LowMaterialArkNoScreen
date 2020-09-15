package org.wh.engineer.bean;

import android.text.TextUtils;

import java.util.List;

/**
 * 项目名称：高值
 * 创建者：chenyanling
 * 创建时间：2019/6/10
 * 描述：人脸照
 */
public class AllFacePhotoBean {
    /**
     * operateSuccess : true
     * id : 0
     * opFlg : 200
     * pageNo : 1
     * pageSize : 20
     * systemType : HCT
     * userVos : [{"userId":"10000000000000000000000000000001","name":"系统管理员","sex":"男","updateTime":"2019-06-11 10:15:05","dbCenterUserId":"001","userState":"1","userType":1,"deptId":"","faceFileName":"http://192.168.10.25:8050/faceRecognition/.jpg","faceUpdateTime":"2019-06-06 17:19:32"}]
     */

    private boolean operateSuccess;
    private int id;
    private String opFlg;
    private String endDate;
    private int pageNo;
    private int pageSize;
    private String systemType;
    private List<UsersBean> userVos;

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isOperateSuccess() {
        return operateSuccess;
    }

    public void setOperateSuccess(boolean operateSuccess) {
        this.operateSuccess = operateSuccess;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpFlg() {
        return opFlg;
    }

    public void setOpFlg(String opFlg) {
        this.opFlg = opFlg;
    }

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

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public List<UsersBean> getUserVos() {
        return userVos;
    }

    public void setUserVos(List<UsersBean> userVos) {
        this.userVos = userVos;
    }

    public static class UsersBean {
        public boolean isUpdate() {
            return isUpdate;
        }

        public void setUpdate(boolean update) {
            isUpdate = update;
        }

        /**
         * userId : 10000000000000000000000000000001
         * name : 系统管理员
         * sex : 男
         * updateTime : 2019-06-11 10:15:05
         * dbCenterUserId : 001
         * userState : 1
         * userType : 1
         * deptId :
         * faceFileName : http://192.168.10.25:8050/faceRecognition/.jpg
         * faceUpdateTime : 2019-06-06 17:19:32
         */

        private boolean isUpdate;
        private String userId;
        private String faceId;
        private String name;
        private String sex;
        private String updateTime;
        private String dbCenterUserId;
        private String userState;
        private int userType;
        private String deptId;
        private String faceFileName;
        private String faceUpdateTime;

        public String getFaceId() {
            return faceId;
        }

        public void setFaceId(String faceId) {
            this.faceId = faceId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getUpdateTime() {
            return TextUtils.isEmpty(updateTime)?"":updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getDbCenterUserId() {
            return dbCenterUserId;
        }

        public void setDbCenterUserId(String dbCenterUserId) {
            this.dbCenterUserId = dbCenterUserId;
        }

        public String getUserState() {
            return userState;
        }

        public void setUserState(String userState) {
            this.userState = userState;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }

        public String getDeptId() {
            return deptId;
        }

        public void setDeptId(String deptId) {
            this.deptId = deptId;
        }

        public String getFaceFileName() {
            return faceFileName;
        }

        public void setFaceFileName(String faceFileName) {
            this.faceFileName = faceFileName;
        }

        public String getFaceUpdateTime() {
            return  TextUtils.isEmpty(faceUpdateTime)?"":faceUpdateTime;
        }

        public void setFaceUpdateTime(String faceUpdateTime) {
            this.faceUpdateTime = faceUpdateTime;
        }

        @Override
        public boolean equals(Object obj) {
            UsersBean photoBean = (UsersBean) obj;
            return faceId.equals(photoBean.getFaceId());
        }
    }
}
