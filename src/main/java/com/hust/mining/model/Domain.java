package com.hust.mining.model;

import java.util.Map;

/**
 * 域名的基本信息，extraProperty属性为null代表没有扩展属性
 * @author Jack
 *
 */
public class Domain {
    private String url;

    private String name;

    private String column;

    private String type;

    private String rank;

    private String incidence;

    private Integer weight;
    
    private Map<String,String> extraProperty;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getIncidence() {
		return incidence;
	}

	public void setIncidence(String incidence) {
		this.incidence = incidence;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Map<String, String> getExtraProperty() {
		return extraProperty;
	}

	public void setExtraProperty(Map<String, String> extraProperty) {
		this.extraProperty = extraProperty;
	}
}
