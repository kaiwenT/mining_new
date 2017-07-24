package com.hust.mining.model;

import java.io.Serializable;
import java.util.Date;

public class CoreResult implements Serializable {
	private static final long serialVersionUID = 1L;

	private String coreRid;

	private String resName;

	private String issueId;

	private Date createTime;

	private String creator;

	public String getCoreRid() {
		return coreRid;
	}

	public void setCoreRid(String coreRid) {
		this.coreRid = coreRid == null ? null : coreRid.trim();
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName == null ? null : resName.trim();
	}

	public String getIssueId() {
		return issueId;
	}

	public void setIssueId(String issueId) {
		this.issueId = issueId == null ? null : issueId.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator == null ? null : creator.trim();
	}
}