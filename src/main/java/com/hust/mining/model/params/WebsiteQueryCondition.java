package com.hust.mining.model.params;

public class WebsiteQueryCondition {

	private String url;
	private String name;
	private String level;
	private String type;
	private int start;
	private int limit;

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

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	@Override
	public String toString() {
		return "WebsiteQueryCondition [url=" + url + ", name=" + name + ", level=" + level + ", type=" + type
				+ ", start=" + start + ", limit=" + limit + "]";
	}

}
