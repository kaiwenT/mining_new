package com.hust.mining.model.params;

import java.util.Date;

public class IssueQueryCondition {

	private String issueId;
	private String issueName;
	private String issueType;
	private String issueHold;
	private String issueBelongTo;
	private Date createStartTime;
	private Date createEndTime;
	private String user;
	private Date lastUpdateStartTime;
	private Date lastUpdateEndTime;
	private int pageNo;
	private int pageSize;

	public String getIssueName() {
		return issueName;
	}

	public void setIssueName(String issueName) {
		this.issueName = issueName;
	}

	public Date getCreateStartTime() {
		return createStartTime;
	}

	public void setCreateStartTime(Date createStartTime) {
		this.createStartTime = createStartTime;
	}

	public Date getCreateEndTime() {
		return createEndTime;
	}

	public void setCreateEndTime(Date createEndTime) {
		this.createEndTime = createEndTime;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
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

	public Date getLastUpdateStartTime() {
		return lastUpdateStartTime;
	}

	public void setLastUpdateStartTime(Date lastUpdateStartTime) {
		this.lastUpdateStartTime = lastUpdateStartTime;
	}

	public Date getLastUpdateEndTime() {
		return lastUpdateEndTime;
	}

	public void setLastUpdateEndTime(Date lastUpdateEndTime) {
		this.lastUpdateEndTime = lastUpdateEndTime;
	}

	public String getIssueId() {
		return issueId;
	}

	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}

	public String getIssueType() {
		return issueType;
	}

	public void setIssueType(String issueType) {
		this.issueType = issueType;
	}

	public String getIssueHold() {
		return issueHold;
	}

	public void setIssueHold(String issueHold) {
		this.issueHold = issueHold;
	}

	public String getIssueBelongTo() {
		return issueBelongTo;
	}

	public void setIssueBelongTo(String issueBelongTo) {
		this.issueBelongTo = issueBelongTo;
	}
}
