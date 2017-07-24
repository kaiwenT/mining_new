package com.hust.mining.model.params;

public class PowerQueryCondition {

	private String name;
	private int start;
	private int limit;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		return "PowerQueryCondition [name=" + name + ", start=" + start + ", limit=" + limit + "]";
	}

}
