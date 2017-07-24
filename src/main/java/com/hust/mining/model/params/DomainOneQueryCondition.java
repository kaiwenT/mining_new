package com.hust.mining.model.params;

public class DomainOneQueryCondition {
	private String url;
	private String name;
	private String type;
	private String column;
	private Boolean isFather;
	private String incidence;
	private String rank;
	private Integer weight;
	private Integer start;
	private Integer limit;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public boolean isFather() {
		return isFather;
	}

	public void setFather(boolean isFather) {
		this.isFather = isFather;
	}

	public String getIncidence() {
		return incidence;
	}

	public void setIncidence(String incidence) {
		this.incidence = incidence;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public Boolean getIsFather() {
		return isFather;
	}

	public void setIsFather(Boolean isFather) {
		this.isFather = isFather;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

}
