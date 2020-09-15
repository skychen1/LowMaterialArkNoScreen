package org.wh.engineer.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public class PageModel
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private int pageNo = 1;
  private int pageSize = 10;
  private List<?> pageData;
  private int totals;
  private transient String queryHql;
  private transient String pageDataJson;
  private transient String countProName;
  private transient Map<String, Object> hqlParamMap;
  private int offset = 0;
  private String sort;
  private String order = "desc";
  
  public PageModel() {}
  
  public PageModel(int pageNo, int pageSize)
  {
    this.pageNo = pageNo;
    this.pageSize = pageSize;
  }
  
  public int getTotals()
  {
    return this.totals;
  }
  
  public void setTotals(int totals)
  {
    this.totals = totals;
  }
  
  public int getPageNo()
  {
    return this.pageNo;
  }
  
  public void setPageNo(int pageNo)
  {
    this.pageNo = pageNo;
  }
  
  public int getPageSize()
  {
    return this.pageSize;
  }
  
  public void setPageSize(int pageSize)
  {
    this.pageSize = pageSize;
  }
  
  public List<?> getPageData()
  {
    return this.pageData;
  }
  
  public void setPageData(List<?> pageData)
  {
    this.pageData = pageData;
  }
  
  public String getQueryHql()
  {
    return this.queryHql;
  }
  
  public void setQueryHql(String queryHql)
  {
    this.queryHql = queryHql;
  }
  
  public Map<String, Object> getHqlParamMap()
  {
    return this.hqlParamMap;
  }
  
  public void setHqlParamMap(Map<String, Object> hqlParamMap)
  {
    this.hqlParamMap = hqlParamMap;
  }
  
  public String getPageDataJson()
  {
    return this.pageDataJson;
  }
  
  public void setPageDataJson(String pageDataJson)
  {
    this.pageDataJson = pageDataJson;
  }
  
  public String getCountProName()
  {
    return this.countProName;
  }
  
  public void setCountProName(String countProName)
  {
    this.countProName = countProName;
  }
  
  public void clean()
  {
    this.pageData = null;
    this.pageDataJson = null;
    this.queryHql = null;
    this.hqlParamMap = null;
    this.countProName = null;
  }
  

  public int getOffset()
  {
    return this.offset;
  }
  
  public void setOffset(int offset)
  {
    this.offset = offset;
  }
  
  public String getSort()
  {
    return this.sort;
  }
  
  public void setSort(String sort)
  {
    this.sort = sort;
  }
  
  public String getOrder()
  {
    return this.order;
  }
  
  public void setOrder(String order)
  {
    this.order = order;
  }
}
