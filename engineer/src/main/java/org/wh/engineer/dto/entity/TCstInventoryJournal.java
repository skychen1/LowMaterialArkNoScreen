package org.wh.engineer.dto.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>标题: TCstInventoryJournal.java</p>
 * <p>业务描述:耗材流水表 </p>
 * <p>公司:北京瑞华康源科技有限公司</p>
 * <p>版权:rivamed-2018</p>
 *
 * @author 魏小波
 * @version V1.0
 * @date 2018年7月17日
 */

public class TCstInventoryJournal implements Serializable {

   private static final long serialVersionUID = 1L;

   private String id;

   private Date creatDate;

   private String cstId;

   private String cstName;

   private String deviceCode;

   private String epc;

   private BigDecimal price;

   private String remark;

   private String status;

   private String storehouseCode;

   //移出目标库房
   private String storehouseRemark;

   //HRP同步状态：0未上传；1已上传
   private int syncStatus;

   private String accountId;

   //供应商名称
   private String vendorName;

   public TCstInventoryJournal() {
   }

}