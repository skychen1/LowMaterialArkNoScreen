package org.wh.engineer.dto;

import org.wh.engineer.dto.entity.TCstInventoryJournal;
import org.wh.engineer.dto.vo.TCstInventoryJournalVo;

import java.util.List;

/**
 * 
 * 描述: TODO<br/>
 * 公司: 北京瑞华康源科技有限公司<br/>
 * 版权: rivamed2018<br/>
 *
 * @author 魏小波
 * @date 2018-07-12 12:14:19
 * @version V1.0
 */
public class TCstInventoryJournalDto extends BaseAbstractDto {

	private static final long serialVersionUID = 1L;

	private TCstInventoryJournal tCstInventoryJournal;
	private List<TCstInventoryJournal> tCstInventoryJournals;
	private List<TCstInventoryJournalVo> tCstInventoryJournalVos;
	private String                       startTime;
	private String                       endTime;
	private String                       term;
	private String                       deviceCode;
	private String                       thingCode;
	private String                       status;
	private int                          pageNo;
	private int                          pageSize;

	public TCstInventoryJournal getTCstInventoryJournal() {
		return tCstInventoryJournal;
	}

	public void setTCstInventoryJournal(TCstInventoryJournal tCstInventoryJournal) {
		this.tCstInventoryJournal = tCstInventoryJournal;
	}

	public List<TCstInventoryJournal> getTCstInventoryJournals() {
		return tCstInventoryJournals;
	}

	public void setTCstInventoryJournals(List<TCstInventoryJournal> tCstInventoryJournals) {
		this.tCstInventoryJournals = tCstInventoryJournals;
	}

	public List<TCstInventoryJournalVo> gettCstInventoryJournalVos() {
		return tCstInventoryJournalVos;
	}

	public void settCstInventoryJournalVos(List<TCstInventoryJournalVo> tCstInventoryJournalVos) {
		this.tCstInventoryJournalVos = tCstInventoryJournalVos;
	}

	
	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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

	public String getThingCode() {
		return thingCode;
	}

	public void setThingCode(String thingCode) {
		this.thingCode = thingCode;
	}
}
