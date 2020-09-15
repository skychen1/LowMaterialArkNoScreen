package org.wh.engineer.dto.vo;

import java.io.Serializable;

/**
* <p>标题: CstExpirationVo.java</p>
* <p>业务描述:耗材效期统计vo </p>
* <p>公司:北京瑞华康源科技有限公司</p>
* <p>版权:rivamed-2018</p>
* @author 魏小波
* @date 2018年7月19日
* @version V1.0 
*/

public class CstExpirationVo implements Serializable{

	//设备编码
	private String deviceCode;
	
	//设备名称
	private String deviceName;
	
	//过期耗材数量
	private Long expireCount;
	
	//近效期耗材数量
	private Long nearExpireCount;

	public CstExpirationVo(String deviceCode, String deviceName, Long expireCount, Long nearExpireCount) {
		super();
		this.deviceCode = deviceCode;
		this.deviceName = deviceName;
		this.expireCount = expireCount;
		this.nearExpireCount = nearExpireCount;
	}
	
	
}
