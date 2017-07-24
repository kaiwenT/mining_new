package com.hust.mining.model;

import java.io.Serializable;
import java.util.Date;

public class Issue implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String issueId;

    private String issueName;

    private String issueHold;

    private String issueBelongTo;

    private String issueType;

    private Date createTime;

    private String creator;

    private String lastOperator;

    private Date lastUpdateTime;

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId == null ? null : issueId.trim();
    }

    public String getIssueName() {
        return issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName == null ? null : issueName.trim();
    }

    public String getIssueHold() {
        return issueHold;
    }

    public void setIssueHold(String issueHold) {
        this.issueHold = issueHold == null ? null : issueHold.trim();
    }

    public String getIssueBelongTo() {
        return issueBelongTo;
    }

    public void setIssueBelongTo(String issueBelongTo) {
        this.issueBelongTo = issueBelongTo == null ? null : issueBelongTo.trim();
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType == null ? null : issueType.trim();
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

    public String getLastOperator() {
        return lastOperator;
    }

    public void setLastOperator(String lastOperator) {
        this.lastOperator = lastOperator == null ? null : lastOperator.trim();
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}