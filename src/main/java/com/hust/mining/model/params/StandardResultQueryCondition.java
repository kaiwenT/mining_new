package com.hust.mining.model.params;

import java.util.Date;
import java.util.List;

public class StandardResultQueryCondition {
	private String issueId;
	private String stdResName;
	private String resId;
	private Date start;
	private Date end;
	private String stdResId;
	private List<String> stdResIds;

	public String getIssueId() {
		return issueId;
	}

	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}

	public String getStdResName() {
		return stdResName;
	}

	public void setStdResName(String resName) {
		this.stdResName = resName;
	}

	public String getResId() {
		return resId;
	}

	public void setResId(String resId) {
		this.resId = resId;
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

	public String getStdResId() {
		return stdResId;
	}

	public void setStdResId(String stdResId) {
		this.stdResId = stdResId;
	}

	public List<String> getStdResIds() {
		return stdResIds;
	}

	public void setStdResIds(List<String> stdResIds) {
		this.stdResIds = stdResIds;
	}

}
