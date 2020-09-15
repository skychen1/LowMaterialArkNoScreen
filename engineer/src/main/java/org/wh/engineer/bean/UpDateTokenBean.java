package org.wh.engineer.bean;

import java.io.Serializable;

/**
 * 项目名称:    Android_PV_2.6.5New
 * 创建者:      DanMing
 * 创建时间:    2019/1/24 10:23
 * 描述:        更换Token的bean
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class UpDateTokenBean implements Serializable{

   /**
    * operateSuccess : true
    * id : 0
    * opFlg : 200
    * pageNo : 1
    * pageSize : 20
    * systemType : HCT
    * accessToken : {"createTime":"2019-01-24","tokenId":"4c598192223384e632afac788df69267","username":"admin","clientId":"HCT","authenticationId":"f849e79cfcb1343ea3be01abadc3221c","refreshToken":"bc93a338642c1d19d9af0d3903d014f5","tokenType":"Bearer","tokenExpiredSeconds":54000,"refreshTokenExpiredSeconds":72000,"userDto":{"grantType":"PASSWORD","validateType":null,"clientId":"HCT","supportRefreshToken":0,"refreshToken":null,"userName":null,"accountName":"admin","password":null,"tokenId":null,"userInfoByte":"rO0ABXNyAChjbi5yaXZhbWVkLnVtLmVudGl0eS51c2VyTWFuYWdlLlVzZXJJbmZvx+F2mysJxf8CAAFMAAJ2b3QAMExjbi9yaXZhbWVkL3VtL2VudGl0eS92by9iYXNpYy9BcHBBY2NvdW50SW5mb1ZvO3hwc3IALmNuLnJpdmFtZWQudW0uZW50aXR5LnZvLmJhc2ljLkFwcEFjY291bnRJbmZvVm8AAAAAAAAAAQIADkwACWFjY291bnRJZHQAEkxqYXZhL2xhbmcvU3RyaW5nO0wAC2FjY291bnROYW1lcQB+AARMAAxlbWVyZ2VuY3lQd2RxAH4ABEwAC2lzRW1lcmdlbmN5dAATTGphdmEvbGFuZy9JbnRlZ2VyO0wACGlzRmluZ2VycQB+AAVMAAhpc1dhaWRhaXEAfgAFTAAIcGFzc3dvcmRxAH4ABEwABXJvbGVzdAAQTGphdmEvdXRpbC9MaXN0O0wABHNhbHRxAH4ABEwAA3NleHEAfgAETAAIdGVuYW50SWRxAH4ABEwACHVzZVN0YXRlcQB+AARMAAZ1c2VySWRxAH4ABEwACHVzZXJOYW1lcQB+AAR4cgAcY24ucml2YW1lZC5lbnRpdHkuVXNlckRldGFpbHGMY1W99OPKAgACTAAIY2xpZW50SWRxAH4ABFsACHVzZXJJbmZvdAACW0J4cHBwdAAgMTAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDF0AAVhZG1pbnBzcgARamF2YS5sYW5nLkludGVnZXIS4qCk94GHOAIAAUkABXZhbHVleHIAEGphdmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhwAAAAAHNxAH4ADAAAAAFxAH4AD3BzcgATamF2YS51dGlsLkFycmF5TGlzdHiB0h2Zx2GdAwABSQAEc2l6ZXhwAAAABXcEAAAABXNyACRjbi5yaXZhbWVkLnVtLmVudGl0eS51c2VyTWFuYWdlLlJvbGUAAAAAAAAAAQIAC0wACGFjY291bnRzdAAPTGphdmEvdXRpbC9TZXQ7TAAKY3JlYXRlVGltZXQAEExqYXZhL3V0aWwvRGF0ZTtMAAtkZXNjcmlwdGlvbnEAfgAETAAFZnVuY3NxAH4AE0wACXJhbmdlSW5mb3EAfgAETAAIcm9sZUNvZGVxAH4ABEwABnJvbGVJZHEAfgAETAAIcm9sZU5hbWVxAH4ABEwACnN5c3RlbVR5cGVxAH4ABEwACnVwZGF0ZVRpbWVxAH4AFEwACHVzZVN0YXRlcQB+AAR4cHBwcHNyABFqYXZhLnV0aWwuSGFzaFNldLpEhZWWuLc0AwAAeHB3DAAAABA/QAAAAAAAAHhwdAAFYWRtaW5wdAAP57O757uf566h55CG5ZGYcHBwc3EAfgAScHBwc3EAfgAWdwwAAAAQP0AAAAAAAAB4cHQAATBwdAAO566h55CG5ZGYKHBhZClwcHBzcQB+ABJwcHBzcQB+ABZ3DAAAABA/QAAAAAAAAHhwdAABMHB0ABvnrqHnkIblkZjvvIjor4rpl7TorqHotLnvvIlwcHBzcQB+ABJwcHBzcQB+ABZ3DAAAABA/QAAAAAAAAHhwdAABMHB0ABvnrqHnkIblkZjvvIjmma7pgJrlupPmiL/vvIlwcHBzcQB+ABJwcHBzcQB+ABZ3DAAAABA/QAAAAAAAAHhwdAABMHB0AB7nrqHnkIblkZjvvIjkuIDnuqflupPnm5jngrnvvIlwcHB4cHQAA+eUt3QAIDEwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAxdAABMXQAIDEwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAxdAAP57O757uf566h55CG5ZGY","accessTokenValidity":null,"refreshTokenValidity":null}}
    */

   private boolean operateSuccess;
   private int             id;
   private String          opFlg;
   private int             pageNo;
   private int             pageSize;
   private String          systemType;
   private AccessTokenBean accessToken;

   public boolean isOperateSuccess() { return operateSuccess;}

   public void setOperateSuccess(boolean operateSuccess) { this.operateSuccess = operateSuccess;}

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public String getOpFlg() { return opFlg;}

   public void setOpFlg(String opFlg) { this.opFlg = opFlg;}

   public int getPageNo() { return pageNo;}

   public void setPageNo(int pageNo) { this.pageNo = pageNo;}

   public int getPageSize() { return pageSize;}

   public void setPageSize(int pageSize) { this.pageSize = pageSize;}

   public String getSystemType() { return systemType;}

   public void setSystemType(String systemType) { this.systemType = systemType;}

   public AccessTokenBean getAccessToken() { return accessToken;}

   public void setAccessToken(AccessTokenBean accessToken) { this.accessToken = accessToken;}

   public static class AccessTokenBean {

	/**
	 * createTime : 2019-01-24
	 * tokenId : 4c598192223384e632afac788df69267
	 * username : admin
	 * clientId : HCT
	 * authenticationId : f849e79cfcb1343ea3be01abadc3221c
	 * refreshToken : bc93a338642c1d19d9af0d3903d014f5
	 * tokenType : Bearer
	 * tokenExpiredSeconds : 54000
	 * refreshTokenExpiredSeconds : 72000
	 * userDto : {"grantType":"PASSWORD","validateType":null,"clientId":"HCT","supportRefreshToken":0,"refreshToken":null,"userName":null,"accountName":"admin","password":null,"tokenId":null,"userInfoByte":"rO0ABXNyAChjbi5yaXZhbWVkLnVtLmVudGl0eS51c2VyTWFuYWdlLlVzZXJJbmZvx+F2mysJxf8CAAFMAAJ2b3QAMExjbi9yaXZhbWVkL3VtL2VudGl0eS92by9iYXNpYy9BcHBBY2NvdW50SW5mb1ZvO3hwc3IALmNuLnJpdmFtZWQudW0uZW50aXR5LnZvLmJhc2ljLkFwcEFjY291bnRJbmZvVm8AAAAAAAAAAQIADkwACWFjY291bnRJZHQAEkxqYXZhL2xhbmcvU3RyaW5nO0wAC2FjY291bnROYW1lcQB+AARMAAxlbWVyZ2VuY3lQd2RxAH4ABEwAC2lzRW1lcmdlbmN5dAATTGphdmEvbGFuZy9JbnRlZ2VyO0wACGlzRmluZ2VycQB+AAVMAAhpc1dhaWRhaXEAfgAFTAAIcGFzc3dvcmRxAH4ABEwABXJvbGVzdAAQTGphdmEvdXRpbC9MaXN0O0wABHNhbHRxAH4ABEwAA3NleHEAfgAETAAIdGVuYW50SWRxAH4ABEwACHVzZVN0YXRlcQB+AARMAAZ1c2VySWRxAH4ABEwACHVzZXJOYW1lcQB+AAR4cgAcY24ucml2YW1lZC5lbnRpdHkuVXNlckRldGFpbHGMY1W99OPKAgACTAAIY2xpZW50SWRxAH4ABFsACHVzZXJJbmZvdAACW0J4cHBwdAAgMTAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDF0AAVhZG1pbnBzcgARamF2YS5sYW5nLkludGVnZXIS4qCk94GHOAIAAUkABXZhbHVleHIAEGphdmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhwAAAAAHNxAH4ADAAAAAFxAH4AD3BzcgATamF2YS51dGlsLkFycmF5TGlzdHiB0h2Zx2GdAwABSQAEc2l6ZXhwAAAABXcEAAAABXNyACRjbi5yaXZhbWVkLnVtLmVudGl0eS51c2VyTWFuYWdlLlJvbGUAAAAAAAAAAQIAC0wACGFjY291bnRzdAAPTGphdmEvdXRpbC9TZXQ7TAAKY3JlYXRlVGltZXQAEExqYXZhL3V0aWwvRGF0ZTtMAAtkZXNjcmlwdGlvbnEAfgAETAAFZnVuY3NxAH4AE0wACXJhbmdlSW5mb3EAfgAETAAIcm9sZUNvZGVxAH4ABEwABnJvbGVJZHEAfgAETAAIcm9sZU5hbWVxAH4ABEwACnN5c3RlbVR5cGVxAH4ABEwACnVwZGF0ZVRpbWVxAH4AFEwACHVzZVN0YXRlcQB+AAR4cHBwcHNyABFqYXZhLnV0aWwuSGFzaFNldLpEhZWWuLc0AwAAeHB3DAAAABA/QAAAAAAAAHhwdAAFYWRtaW5wdAAP57O757uf566h55CG5ZGYcHBwc3EAfgAScHBwc3EAfgAWdwwAAAAQP0AAAAAAAAB4cHQAATBwdAAO566h55CG5ZGYKHBhZClwcHBzcQB+ABJwcHBzcQB+ABZ3DAAAABA/QAAAAAAAAHhwdAABMHB0ABvnrqHnkIblkZjvvIjor4rpl7TorqHotLnvvIlwcHBzcQB+ABJwcHBzcQB+ABZ3DAAAABA/QAAAAAAAAHhwdAABMHB0ABvnrqHnkIblkZjvvIjmma7pgJrlupPmiL/vvIlwcHBzcQB+ABJwcHBzcQB+ABZ3DAAAABA/QAAAAAAAAHhwdAABMHB0AB7nrqHnkIblkZjvvIjkuIDnuqflupPnm5jngrnvvIlwcHB4cHQAA+eUt3QAIDEwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAxdAABMXQAIDEwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAxdAAP57O757uf566h55CG5ZGY","accessTokenValidity":null,"refreshTokenValidity":null}
	 */

	private String createTime;
	private String      tokenId;
	private String      username;
	private String      clientId;
	private String      authenticationId;
	private String      refreshToken;
	private String      tokenType;
	private int         tokenExpiredSeconds;
	private int         refreshTokenExpiredSeconds;
	private UserDtoBean userDto;

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

	public UserDtoBean getUserDto() { return userDto;}

	public void setUserDto(UserDtoBean userDto) { this.userDto = userDto;}

	public static class UserDtoBean {

	   /**
	    * grantType : PASSWORD
	    * validateType : null
	    * clientId : HCT
	    * supportRefreshToken : 0
	    * refreshToken : null
	    * userName : null
	    * accountName : admin
	    * password : null
	    * tokenId : null
	    * userInfoByte : rO0ABXNyAChjbi5yaXZhbWVkLnVtLmVudGl0eS51c2VyTWFuYWdlLlVzZXJJbmZvx+F2mysJxf8CAAFMAAJ2b3QAMExjbi9yaXZhbWVkL3VtL2VudGl0eS92by9iYXNpYy9BcHBBY2NvdW50SW5mb1ZvO3hwc3IALmNuLnJpdmFtZWQudW0uZW50aXR5LnZvLmJhc2ljLkFwcEFjY291bnRJbmZvVm8AAAAAAAAAAQIADkwACWFjY291bnRJZHQAEkxqYXZhL2xhbmcvU3RyaW5nO0wAC2FjY291bnROYW1lcQB+AARMAAxlbWVyZ2VuY3lQd2RxAH4ABEwAC2lzRW1lcmdlbmN5dAATTGphdmEvbGFuZy9JbnRlZ2VyO0wACGlzRmluZ2VycQB+AAVMAAhpc1dhaWRhaXEAfgAFTAAIcGFzc3dvcmRxAH4ABEwABXJvbGVzdAAQTGphdmEvdXRpbC9MaXN0O0wABHNhbHRxAH4ABEwAA3NleHEAfgAETAAIdGVuYW50SWRxAH4ABEwACHVzZVN0YXRlcQB+AARMAAZ1c2VySWRxAH4ABEwACHVzZXJOYW1lcQB+AAR4cgAcY24ucml2YW1lZC5lbnRpdHkuVXNlckRldGFpbHGMY1W99OPKAgACTAAIY2xpZW50SWRxAH4ABFsACHVzZXJJbmZvdAACW0J4cHBwdAAgMTAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDF0AAVhZG1pbnBzcgARamF2YS5sYW5nLkludGVnZXIS4qCk94GHOAIAAUkABXZhbHVleHIAEGphdmEubGFuZy5OdW1iZXKGrJUdC5TgiwIAAHhwAAAAAHNxAH4ADAAAAAFxAH4AD3BzcgATamF2YS51dGlsLkFycmF5TGlzdHiB0h2Zx2GdAwABSQAEc2l6ZXhwAAAABXcEAAAABXNyACRjbi5yaXZhbWVkLnVtLmVudGl0eS51c2VyTWFuYWdlLlJvbGUAAAAAAAAAAQIAC0wACGFjY291bnRzdAAPTGphdmEvdXRpbC9TZXQ7TAAKY3JlYXRlVGltZXQAEExqYXZhL3V0aWwvRGF0ZTtMAAtkZXNjcmlwdGlvbnEAfgAETAAFZnVuY3NxAH4AE0wACXJhbmdlSW5mb3EAfgAETAAIcm9sZUNvZGVxAH4ABEwABnJvbGVJZHEAfgAETAAIcm9sZU5hbWVxAH4ABEwACnN5c3RlbVR5cGVxAH4ABEwACnVwZGF0ZVRpbWVxAH4AFEwACHVzZVN0YXRlcQB+AAR4cHBwcHNyABFqYXZhLnV0aWwuSGFzaFNldLpEhZWWuLc0AwAAeHB3DAAAABA/QAAAAAAAAHhwdAAFYWRtaW5wdAAP57O757uf566h55CG5ZGYcHBwc3EAfgAScHBwc3EAfgAWdwwAAAAQP0AAAAAAAAB4cHQAATBwdAAO566h55CG5ZGYKHBhZClwcHBzcQB+ABJwcHBzcQB+ABZ3DAAAABA/QAAAAAAAAHhwdAABMHB0ABvnrqHnkIblkZjvvIjor4rpl7TorqHotLnvvIlwcHBzcQB+ABJwcHBzcQB+ABZ3DAAAABA/QAAAAAAAAHhwdAABMHB0ABvnrqHnkIblkZjvvIjmma7pgJrlupPmiL/vvIlwcHBzcQB+ABJwcHBzcQB+ABZ3DAAAABA/QAAAAAAAAHhwdAABMHB0AB7nrqHnkIblkZjvvIjkuIDnuqflupPnm5jngrnvvIlwcHB4cHQAA+eUt3QAIDEwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAxdAABMXQAIDEwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAxdAAP57O757uf566h55CG5ZGY
	    * accessTokenValidity : null
	    * refreshTokenValidity : null
	    */

	   private String grantType;
	   private Object validateType;
	   private String clientId;
	   private int    supportRefreshToken;
	   private Object refreshToken;
	   private Object userName;
	   private String accountName;
	   private Object password;
	   private Object tokenId;
	   private String userInfoByte;
	   private Object accessTokenValidity;
	   private Object refreshTokenValidity;

	   public String getGrantType() { return grantType;}

	   public void setGrantType(String grantType) { this.grantType = grantType;}

	   public Object getValidateType() { return validateType;}

	   public void setValidateType(Object validateType) { this.validateType = validateType;}

	   public String getClientId() { return clientId;}

	   public void setClientId(String clientId) { this.clientId = clientId;}

	   public int getSupportRefreshToken() { return supportRefreshToken;}

	   public void setSupportRefreshToken(
		   int supportRefreshToken) { this.supportRefreshToken = supportRefreshToken;}

	   public Object getRefreshToken() { return refreshToken;}

	   public void setRefreshToken(Object refreshToken) { this.refreshToken = refreshToken;}

	   public Object getUserName() { return userName;}

	   public void setUserName(Object userName) { this.userName = userName;}

	   public String getAccountName() { return accountName;}

	   public void setAccountName(String accountName) { this.accountName = accountName;}

	   public Object getPassword() { return password;}

	   public void setPassword(Object password) { this.password = password;}

	   public Object getTokenId() { return tokenId;}

	   public void setTokenId(Object tokenId) { this.tokenId = tokenId;}

	   public String getUserInfoByte() { return userInfoByte;}

	   public void setUserInfoByte(String userInfoByte) { this.userInfoByte = userInfoByte;}

	   public Object getAccessTokenValidity() { return accessTokenValidity;}

	   public void setAccessTokenValidity(
		   Object accessTokenValidity) { this.accessTokenValidity = accessTokenValidity;}

	   public Object getRefreshTokenValidity() { return refreshTokenValidity;}

	   public void setRefreshTokenValidity(
		   Object refreshTokenValidity) { this.refreshTokenValidity = refreshTokenValidity;}
	}
   }
}
