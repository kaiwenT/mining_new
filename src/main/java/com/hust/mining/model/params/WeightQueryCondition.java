package com.hust.mining.model.params;

public class WeightQueryCondition {
	private String name;
	private Integer weight;
	private int start;
	private int limit;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
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
		return "WeightQueryCondition [name=" + name + ", weight=" + weight + ", start=" + start + ", limit=" + limit
				+ "]";
	}

}
