package org.wh.engineer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 项目名称:    Android_PV_2.6
 * 创建者:      LiangDanMing
 * 创建时间:    2018/8/8 17:25
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.bean
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class ConfigBean implements Serializable{

   /**
    * id : 0
    * thingId : 40288297677830f50167783478140000
    * configVos : [{"code":"009","configId":null,"name":"是否先开柜门后绑定患者","thingId":"40288297677830f50167783478140000","value":"1"},{"code":"012","configId":null,"name":"是否启用绑定临时患者领用耗材","thingId":"40288297677830f50167783478140000","value":"2"},{"code":"016","configId":null,"name":"是否开启功能开柜","thingId":"40288297677830f50167783478140000","value":"3"}]
    */

   private int                 id;
   private String              thingId;
   private List<ConfigVosBean> configVos;

   public int getId() { return id;}

   public void setId(int id) { this.id = id;}

   public String getThingId() { return thingId;}

   public void setThingId(String thingId) { this.thingId = thingId;}

   public List<ConfigVosBean> getConfigVos() { return configVos;}

   public void setConfigVos(
	   List<ConfigVosBean> configVos) { this.configVos = configVos;}

   public static class ConfigVosBean {

	/**
	 * code : 009
	 * configId : null
	 * name : 是否先开柜门后绑定患者
	 * thingId : 40288297677830f50167783478140000
	 * value : 1
	 */

	private String code;
	private Object configId;
	private String name;
	private String thingId;
	private String value;

	public String getCode() { return code;}

	public void setCode(String code) { this.code = code;}

	public Object getConfigId() { return configId;}

	public void setConfigId(Object configId) { this.configId = configId;}

	public String getName() { return name;}

	public void setName(String name) { this.name = name;}

	public String getThingId() { return thingId;}

	public void setThingId(String thingId) { this.thingId = thingId;}

	public String getValue() { return value;}

	public void setValue(String value) { this.value = value;}
   }
}
