package org.wh.engineer.bean;

import java.io.Serializable;

/**
 * 项目名称:    Android_PV_2.6.1
 * 创建者:      DanMing
 * 创建时间:    2018/9/26 18:17
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class VersionBean implements Serializable{

   /**
    * id : 0
    * systemVersion : {"versionId":"4028728167a05dc60167a1287a410003","contextRoot":"1544597041031","description":"1","parentId":"1","systemName":"耗材柜","systemType":"2","useState":"1","version":"1","updateTime":"2018-12-14 10:40:14","createTime":"2018-12-12 14:44:04","apkFileName":"aa.png"}
    * systemType : 2
    */

   private int id;
   private SystemVersionBean systemVersion;
   private String            systemType;
   private String            apkFtpUrl;
   private boolean            operateSuccess;

   public String getApkFtpUrl() {
      return apkFtpUrl;
   }

   public void setApkFtpUrl(String apkFtpUrl) {
      this.apkFtpUrl = apkFtpUrl;
   }

   public boolean isOperateSuccess() {
      return operateSuccess;
   }

   public void setOperateSuccess(boolean operateSuccess) {
      this.operateSuccess = operateSuccess;
   }

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public SystemVersionBean getSystemVersion() { return systemVersion;}

   public void setSystemVersion(
         SystemVersionBean systemVersion) { this.systemVersion = systemVersion;}

   public String getSystemType() { return systemType;}

   public void setSystemType(String systemType) { this.systemType = systemType;}

   public static class SystemVersionBean {

      /**
       * versionId : 4028728167a05dc60167a1287a410003
       * contextRoot : 1544597041031
       * description : 1
       * parentId : 1
       * systemName : 耗材柜
       * systemType : 2
       * useState : 1
       * version : 1
       * updateTime : 2018-12-14 10:40:14
       * createTime : 2018-12-12 14:44:04
       * apkFileName : aa.png
       */

      private String versionId;
      private String contextRoot;
      private String description;
      private String parentId;
      private String systemName;
      private String systemType;
      private String useState;
      private String version;
      private String updateTime;
      private String createTime;
      private String apkFileName;

      public String getVersionId() { return versionId;}

      public void setVersionId(String versionId) { this.versionId = versionId;}

      public String getContextRoot() { return contextRoot;}

      public void setContextRoot(String contextRoot) { this.contextRoot = contextRoot;}

      public String getDescription() { return description;}

      public void setDescription(String description) { this.description = description;}

      public String getParentId() { return parentId;}

      public void setParentId(String parentId) { this.parentId = parentId;}

      public String getSystemName() { return systemName;}

      public void setSystemName(String systemName) { this.systemName = systemName;}

      public String getSystemType() { return systemType;}

      public void setSystemType(String systemType) { this.systemType = systemType;}

      public String getUseState() { return useState;}

      public void setUseState(String useState) { this.useState = useState;}

      public String getVersion() { return version;}

      public void setVersion(String version) { this.version = version;}

      public String getUpdateTime() { return updateTime;}

      public void setUpdateTime(String updateTime) { this.updateTime = updateTime;}

      public String getCreateTime() { return createTime;}

      public void setCreateTime(String createTime) { this.createTime = createTime;}

      public String getApkFileName() { return apkFileName;}

      public void setApkFileName(String apkFileName) { this.apkFileName = apkFileName;}
   }
}
