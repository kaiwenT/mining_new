package com.hust.mining.model;

import java.io.Serializable;

public class StandardResult_label implements Serializable{
    private String stdRid;

    private Integer labelid;

    public String getStdRid() {
        return stdRid;
    }

    public void setStdRid(String stdRid) {
        this.stdRid = stdRid == null ? null : stdRid.trim();
    }

    public Integer getLabelid() {
        return labelid;
    }

    public void setLabelid(Integer labelid) {
        this.labelid = labelid;
    }
}