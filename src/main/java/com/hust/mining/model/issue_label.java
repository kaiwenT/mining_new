package com.hust.mining.model;

import java.io.Serializable;

public class issue_label implements Serializable{
	
    private String issueid;

    private Integer labelid;

    public String getIssueid() {
        return issueid;
    }

    public void setIssueid(String issueid) {
        this.issueid = issueid == null ? null : issueid.trim();
    }

    public Integer getLabelid() {
        return labelid;
    }

    public void setLabelid(Integer labelid) {
        this.labelid = labelid;
    }
}