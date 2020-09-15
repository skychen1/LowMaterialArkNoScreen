 package org.wh.engineer.dto;
 

 import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


 public abstract class BaseAbstractDto
   implements Serializable
 {
   private static final long serialVersionUID = 2291962030426081727L;
   protected String dealMethod;
   private List<String> eventList = null;
   private Map<String, Map<String, Class>> evenParaMap;
   private Boolean operateSuccess;

   private String forward;

   private String reqSource;
   protected HashMap map;
   private PageModel pageModel;
   private int id;
   private String msg;
   private String opFlg;
   private String tradeAccount;
   private String filterStr;
   
   public BaseAbstractDto() {}
   

   public void setAttr(String valueKey, Object o)
   {
     if (this.map == null) {
       this.map = new HashMap(1);
     }
     this.map.put(valueKey, o);
   }
   
   public Object getAttr(String valueKey)
   {
     if (this.map == null) {
       return null;
     }
     return this.map.get(valueKey);
   }
   
   public void removeAttr(String key)
   {
     if (this.map == null) {
       return;
     }
     this.map.remove(key);
     if (this.map.isEmpty()) {
       this.map = null;
     }
   }
   
   public void clearDataContainer()
   {
     this.map = null;
   }
   
   public PageModel getPageModel()
   {
     return this.pageModel;
   }
   
   public void setPageModel(PageModel pageModel)
   {
     this.pageModel = pageModel;
   }
   
   public int getId()
   {
     return this.id;
   }
   
   public void setId(int id)
   {
     this.id = id;
   }
   
   public String getMsg()
   {
     return this.msg;
   }
   
   public void setMsg(String msg)
   {
     this.msg = msg;
   }
   
   public String getOpFlg()
   {
     return this.opFlg;
   }
   
   public void setOpFlg(String opFlg)
   {
     this.opFlg = opFlg;
   }
   
   public String getTradeAccount()
   {
     return this.tradeAccount;
   }
   
   public void setTradeAccount(String tradeAccount)
   {
     this.tradeAccount = tradeAccount;
   }
   
   public String getFilterStr()
   {
     return this.filterStr;
   }
   
   public void setFilterStr(String filterStr)
   {
     this.filterStr = filterStr;
   }
   
   public String getDealMethod()
   {
     return this.dealMethod;
   }
   
   public void setDealMethod(String dealMethod)
   {
     this.dealMethod = dealMethod;
   }
   
   public String getForward()
   {
     return this.forward;
   }
   
   public void setForward(String forward)
   {
     this.forward = forward;
   }
   
   public void beforeToJson(String BLHMI) {}
   
   public void clean()
   {
     this.map = null;
     this.pageModel = null;
   }
   
   public List<String> getEventList()
   {
     return this.eventList;
   }
   
   public void setEventList(List<String> eventList)
   {
     this.eventList = eventList;
   }
   
   public void addEvent(String eventCode)
   {
     if (this.eventList == null) {}
   }
   
   public Map<String, Map<String, Class>> getEvenParaMap()
   {
     return this.evenParaMap;
   }
   
   public void setEvenParaMap(Map<String, Map<String, Class>> evenParaMap)
   {
     this.evenParaMap = evenParaMap;
   }
   
   public Boolean isOperateSuccess()
   {
     return this.operateSuccess;
   }
   
   public void setOperateSuccess(Boolean operateSuccess)
   {
     this.operateSuccess = operateSuccess;
   }
   
   public String getReqSource()
   {
     return this.reqSource;
   }
   
   public void setReqSource(String reqSource)
   {
     this.reqSource = reqSource;
   }
   
   public void setPage(int page)
   {
     if (getPageModel() == null) {
       setPageModel(new PageModel());
     }
     getPageModel().setPageNo(page);
   }
   
   public void setRows(int rows)
   {
     if (getPageModel() == null) {
       setPageModel(new PageModel());
     }
     getPageModel().setPageSize(rows);
   }
   
   public void setLimit(int limit)
   {
     if (getPageModel() == null) {
       setPageModel(new PageModel());
     }
     getPageModel().setPageSize(limit);
     getPageModel().setPageNo(1 + getPageModel().getOffset() / getPageModel().getPageSize());
   }
   
   public void setOffset(int offset)
   {
     if (getPageModel() == null) {
       setPageModel(new PageModel());
     }
     getPageModel().setOffset(offset);
     getPageModel().setPageNo(1 + getPageModel().getOffset() / getPageModel().getPageSize());
   }
   
   public void setSort(String sort)
   {
     if (getPageModel() == null) {
       setPageModel(new PageModel());
     }
     getPageModel().setSort(sort);
   }
   
   public void setOrder(String order)
   {
     if (getPageModel() == null) {
       setPageModel(new PageModel());
     }
     getPageModel().setOrder(order);
   }
 }



/* Location:           C:\Users\Lenovo\Documents\Tencent Files\373748543\FileRecv\scms\scms\target\scms-3.5.0\WEB-INF\lib\rivamed-core-3.5.0\

 * Qualified Name:     cn.rivamed.framework.transmission.dto.BaseAbstractDto

 * JD-Core Version:    0.7.0.1

 */