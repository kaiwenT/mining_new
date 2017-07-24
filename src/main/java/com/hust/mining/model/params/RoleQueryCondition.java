package com.hust.mining.model.params;

public class RoleQueryCondition {
	private String roleName;
	private int start;
	private int limit;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
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
		return "RoleQueryCondition [roleName=" + roleName + ", start=" + start + ", limit=" + limit + "]";
	}

}
