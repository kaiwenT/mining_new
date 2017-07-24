package com.hust.mining.model.params;

public class StopwordQueryCondition {
	private String word;
	private int start;
	private int limit;
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
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
		// TODO Auto-generated method stub
		return "StopwordQueryCondition [word=" + word + ", start=" + start + ", limit=" + limit + "]";
	}
	
}
