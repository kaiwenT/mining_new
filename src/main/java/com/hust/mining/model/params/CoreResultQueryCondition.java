package com.hust.mining.model.params;

import java.util.Date;
import java.util.List;

public class CoreResultQueryCondition {
	private String issueId;
	private String coreResName;
	private String stdResId;
	private Date start;
	private Date end;
	private String coreResId;
	private List<String> coreResIds;

	public String getIssueId() {
		return issueId;
	}

	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}

	public String getCoreResName() {
		return coreResName;
	}

	public void setCoreResName(String coreResName) {
		this.coreResName = coreResName;
	}

	public String getStdResId() {
		return stdResId;
	}

	public void setStdResId(String stdResId) {
		this.stdResId = stdResId;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public String getCoreResId() {
		return coreResId;
	}

	public void setCoreResId(String coreResId) {
		this.coreResId = coreResId;
	}

	public List<String> getCoreResIds() {
		return coreResIds;
	}

	public void setCoreResIds(List<String> coreResIds) {
		this.coreResIds = coreResIds;
	}

}
