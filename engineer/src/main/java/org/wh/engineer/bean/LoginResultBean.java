package org.wh.engineer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @作者: 钱凯
 * @创建时间: 2018/8/2
 * @功能描述:登录返回结果
 */
public class LoginResultBean implements Serializable {

    /**
     * operateSuccess : true
     * id : 0
     * userFeatureInfo : {"userId":"10000000000000000000000000000001","type":"1"}
     * thingId : 40288297677830f50167783478140000
     * appAccountInfoVo : {"clientId":null,"accountName":"admin","tenantId":"10000000000000000000000000000001","userName":"系统管理员","roles":[{"roleCode":"admin","roleName":"系统管理员","funcs":[]}],"accountId":"10000000000000000000000000000001","password":null,"salt":null,"userId":"10000000000000000000000000000001","isFinger":0,"isWaidai":0,"isEmergency":0,"sex":null,"useState":"1","emergencyPwd":null}
     * systemType : 2
     * loginTime : 2018-12-05
     * accessToken : {"createTime":"2018-12-05","tokenId":"ed226da50fb7a1c92cec9e168ad854e0","username":"admin","clientId":"2","authenticationId":"3b48ccf82fbc9ad613f2481b4d8cf910","refreshToken":"e6a0ace2c49f7124e24c85f4bc837bf0","tokenType":"Bearer","tokenExpiredSeconds":540,"refreshTokenExpiredSeconds":720,"userDetail":null}
     */

    private boolean operateSuccess;
    private int                  id;
    private UserFeatureInfoBean  userFeatureInfo;
    private String               thingId;
    private String               msg;
    private AppAccountInfoVoBean appAccountInfoVo;
    private String               systemType;
    private String               loginTime;
    private String               patientType;//2,3手术，1是非手术
    private AccessTokenBean      accessToken;
    private List<HomeAuthorityMenuBean>     menuVos;

    public List<HomeAuthorityMenuBean> getMenuVos() {
        return menuVos;
    }

    public void setMenuVos(List<HomeAuthorityMenuBean> menuVos) {
        this.menuVos = menuVos;
    }

    public String getPatientType() {
        return patientType;
    }

    public void setPatientType(String patientType) {
        this.patientType = patientType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isOperateSuccess() { return operateSuccess;}

    public void setOperateSuccess(boolean operateSuccess) { this.operateSuccess = operateSuccess;}

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public UserFeatureInfoBean getUserFeatureInfo() { return userFeatureInfo;}

    public void setUserFeatureInfo(
          UserFeatureInfoBean userFeatureInfo) { this.userFeatureInfo = userFeatureInfo;}

    public String getThingId() { return thingId;}

    public void setThingId(String thingId) { this.thingId = thingId;}

    public AppAccountInfoVoBean getAppAccountInfoVo() { return appAccountInfoVo;}

    public void setAppAccountInfoVo(
          AppAccountInfoVoBean appAccountInfoVo) { this.appAccountInfoVo = appAccountInfoVo;}

    public String getSystemType() { return systemType;}

    public void setSystemType(String systemType) { this.systemType = systemType;}

    public String getLoginTime() { return loginTime;}

    public void setLoginTime(String loginTime) { this.loginTime = loginTime;}

    public AccessTokenBean getAccessToken() { return accessToken;}

    public void setAccessToken(AccessTokenBean accessToken) { this.accessToken = accessToken;}

    public static class UserFeatureInfoBean {

        /**
         * userId : 10000000000000000000000000000001
         * type : 1
         */

        private String userId;
        private String type;

        public String getUserId() { return userId;}

        public void setUserId(String userId) { this.userId = userId;}

        public String getType() { return type;}

        public void setType(String type) { this.type = type;}
    }

    public static class AppAccountInfoVoBean {

        /**
         * clientId : null
         * accountName : admin
         * tenantId : 10000000000000000000000000000001
         * userName : 系统管理员
         * roles : [{"roleCode":"admin","roleName":"系统管理员","funcs":[]}]
         * accountId : 10000000000000000000000000000001
         * password : null
         * salt : null
         * userId : 10000000000000000000000000000001
         * isFinger : 0
         * isWaidai : 0
         * isEmergency : 0
         * sex : null
         * useState : 1
         * emergencyPwd : null
         */

        private Object clientId;
        private String          accountName;
        private String          tenantId;
        private String          userName;
        private String          accountId;
        private Object          password;
        private Object          salt;
        private String          userId;
        private String          faceId;
        private int             isFinger;
        private int             isWaidai;
        private int             isFace;
        private int             isEmergency;
        private String          sex;
        private String          useState;
        private Object          emergencyPwd;
        private List<RolesBean> roles;
        private String          fingerNames;

        public String getFaceId() {
            return faceId;
        }

        public void setFaceId(String faceId) {
            this.faceId = faceId;
        }

        public int getIsFace() {
            return isFace;
        }

        public void setIsFace(int isFace) {
            this.isFace = isFace;
        }

        public String getFingerNames() {
            return fingerNames;
        }

        public void setFingerNames(String fingerNames) {
            this.fingerNames = fingerNames;
        }

        public Object getClientId() { return clientId;}

        public void setClientId(Object clientId) { this.clientId = clientId;}

        public String getAccountName() { return accountName;}

        public void setAccountName(String accountName) { this.accountName = accountName;}

        public String getTenantId() { return tenantId;}

        public void setTenantId(String tenantId) { this.tenantId = tenantId;}

        public String getUserName() { return userName;}

        public void setUserName(String userName) { this.userName = userName;}

        public String getAccountId() { return accountId;}

        public void setAccountId(String accountId) { this.accountId = accountId;}

        public Object getPassword() { return password;}

        public void setPassword(Object password) { this.password = password;}

        public Object getSalt() { return salt;}

        public void setSalt(Object salt) { this.salt = salt;}

        public String getUserId() { return userId;}

        public void setUserId(String userId) { this.userId = userId;}

        public int getIsFinger() { return isFinger;}

        public void setIsFinger(int isFinger) { this.isFinger = isFinger;}

        public int getIsWaidai() { return isWaidai;}

        public void setIsWaidai(int isWaidai) { this.isWaidai = isWaidai;}

        public int getIsEmergency() { return isEmergency;}

        public void setIsEmergency(int isEmergency) { this.isEmergency = isEmergency;}

        public String getSex() { return sex;}

        public void setSex(String sex) { this.sex = sex;}

        public String getUseState() { return useState;}

        public void setUseState(String useState) { this.useState = useState;}

        public Object getEmergencyPwd() { return emergencyPwd;}

        public void setEmergencyPwd(Object emergencyPwd) { this.emergencyPwd = emergencyPwd;}

        public List<RolesBean> getRoles() { return roles;}

        public void setRoles(List<RolesBean> roles) { this.roles = roles;}

        public static class RolesBean {

            /**
             * roleCode : admin
             * roleName : 系统管理员
             * funcs : []
             */

            private String roleCode;
            private String  roleName;
            private List<?> funcs;

            public String getRoleCode() { return roleCode;}

            public void setRoleCode(String roleCode) { this.roleCode = roleCode;}

            public String getRoleName() { return roleName;}

            public void setRoleName(String roleName) { this.roleName = roleName;}

            public List<?> getFuncs() { return funcs;}

            public void setFuncs(List<?> funcs) { this.funcs = funcs;}
        }
    }

    public static class AccessTokenBean {

        /**
         * createTime : 2018-12-05
         * tokenId : ed226da50fb7a1c92cec9e168ad854e0
         * username : admin
         * clientId : 2
         * authenticationId : 3b48ccf82fbc9ad613f2481b4d8cf910
         * refreshToken : e6a0ace2c49f7124e24c85f4bc837bf0
         * tokenType : Bearer
         * tokenExpiredSeconds : 540
         * refreshTokenExpiredSeconds : 720
         * userDetail : null
         */

        private String createTime;
        private String tokenId;
        private String username;
        private String clientId;
        private String authenticationId;
        private String refreshToken;
        private String tokenType;
        private int    tokenExpiredSeconds;
        private int    refreshTokenExpiredSeconds;
        private Object userDetail;

        public String getCreateTime() { return createTime;}

        public void setCreateTime(String createTime) { this.createTime = createTime;}

        public String getTokenId() { return tokenId;}

        public void setTokenId(String tokenId) { this.tokenId = tokenId;}

        public String getUsername() { return username;}

        public void setUsername(String username) { this.username = username;}

        public String getClientId() { return clientId;}

        public void setClientId(String clientId) { this.clientId = clientId;}

        public String getAuthenticationId() { return authenticationId;}

        public void setAuthenticationId(
              String authenticationId) { this.authenticationId = authenticationId;}

        public String getRefreshToken() { return refreshToken;}

        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken;}

        public String getTokenType() { return tokenType;}

        public void setTokenType(String tokenType) { this.tokenType = tokenType;}

        public int getTokenExpiredSeconds() { return tokenExpiredSeconds;}

        public void setTokenExpiredSeconds(
              int tokenExpiredSeconds) { this.tokenExpiredSeconds = tokenExpiredSeconds;}

        public int getRefreshTokenExpiredSeconds() { return refreshTokenExpiredSeconds;}

        public void setRefreshTokenExpiredSeconds(
              int refreshTokenExpiredSeconds) { this.refreshTokenExpiredSeconds = refreshTokenExpiredSeconds;}

        public Object getUserDetail() { return userDetail;}

        public void setUserDetail(Object userDetail) { this.userDetail = userDetail;}
    }
}
