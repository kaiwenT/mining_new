package com.hust.mining.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CoreResultExample {
	protected String orderByClause;

	protected boolean distinct;

	protected List<Criteria> oredCriteria;

	protected int start;

	protected int limit;

	public CoreResultExample() {
		oredCriteria = new ArrayList<Criteria>();
	}

	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	public String getOrderByClause() {
		return orderByClause;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public boolean isDistinct() {
		return distinct;
	}

	public List<Criteria> getOredCriteria() {
		return oredCriteria;
	}

	public void or(Criteria criteria) {
		oredCriteria.add(criteria);
	}

	public Criteria or() {
		Criteria criteria = createCriteriaInternal();
		oredCriteria.add(criteria);
		return criteria;
	}

	public Criteria createCriteria() {
		Criteria criteria = createCriteriaInternal();
		if (oredCriteria.size() == 0) {
			oredCriteria.add(criteria);
		}
		return criteria;
	}

	protected Criteria createCriteriaInternal() {
		Criteria criteria = new Criteria();
		return criteria;
	}

	public void clear() {
		oredCriteria.clear();
		orderByClause = null;
		distinct = false;
	}

	protected abstract static class GeneratedCriteria {
		protected List<Criterion> criteria;

		protected GeneratedCriteria() {
			super();
			criteria = new ArrayList<Criterion>();
		}

		public boolean isValid() {
			return criteria.size() > 0;
		}

		public List<Criterion> getAllCriteria() {
			return criteria;
		}

		public List<Criterion> getCriteria() {
			return criteria;
		}

		protected void addCriterion(String condition) {
			if (condition == null) {
				throw new RuntimeException("Value for condition cannot be null");
			}
			criteria.add(new Criterion(condition));
		}

		protected void addCriterion(String condition, Object value, String property) {
			if (value == null) {
				throw new RuntimeException("Value for " + property + " cannot be null");
			}
			criteria.add(new Criterion(condition, value));
		}

		protected void addCriterion(String condition, Object value1, Object value2, String property) {
			if (value1 == null || value2 == null) {
				throw new RuntimeException("Between values for " + property + " cannot be null");
			}
			criteria.add(new Criterion(condition, value1, value2));
		}

		public Criteria andCoreRidIsNull() {
			addCriterion("core_rid is null");
			return (Criteria) this;
		}

		public Criteria andCoreRidIsNotNull() {
			addCriterion("core_rid is not null");
			return (Criteria) this;
		}

		public Criteria andCoreRidEqualTo(String value) {
			addCriterion("core_rid =", value, "coreRid");
			return (Criteria) this;
		}

		public Criteria andCoreRidNotEqualTo(String value) {
			addCriterion("core_rid <>", value, "coreRid");
			return (Criteria) this;
		}

		public Criteria andCoreRidGreaterThan(String value) {
			addCriterion("core_rid >", value, "coreRid");
			return (Criteria) this;
		}

		public Criteria andCoreRidGreaterThanOrEqualTo(String value) {
			addCriterion("core_rid >=", value, "coreRid");
			return (Criteria) this;
		}

		public Criteria andCoreRidLessThan(String value) {
			addCriterion("core_rid <", value, "coreRid");
			return (Criteria) this;
		}

		public Criteria andCoreRidLessThanOrEqualTo(String value) {
			addCriterion("core_rid <=", value, "coreRid");
			return (Criteria) this;
		}

		public Criteria andCoreRidLike(String value) {
			addCriterion("core_rid like", value, "coreRid");
			return (Criteria) this;
		}

		public Criteria andCoreRidNotLike(String value) {
			addCriterion("core_rid not like", value, "coreRid");
			return (Criteria) this;
		}

		public Criteria andCoreRidIn(List<String> values) {
			addCriterion("core_rid in", values, "coreRid");
			return (Criteria) this;
		}

		public Criteria andCoreRidNotIn(List<String> values) {
			addCriterion("core_rid not in", values, "coreRid");
			return (Criteria) this;
		}

		public Criteria andCoreRidBetween(String value1, String value2) {
			addCriterion("core_rid between", value1, value2, "coreRid");
			return (Criteria) this;
		}

		public Criteria andCoreRidNotBetween(String value1, String value2) {
			addCriterion("core_rid not between", value1, value2, "coreRid");
			return (Criteria) this;
		}

		public Criteria andResNameIsNull() {
			addCriterion("res_name is null");
			return (Criteria) this;
		}

		public Criteria andResNameIsNotNull() {
			addCriterion("res_name is not null");
			return (Criteria) this;
		}

		public Criteria andResNameEqualTo(String value) {
			addCriterion("res_name =", value, "resName");
			return (Criteria) this;
		}

		public Criteria andResNameNotEqualTo(String value) {
			addCriterion("res_name <>", value, "resName");
			return (Criteria) this;
		}

		public Criteria andResNameGreaterThan(String value) {
			addCriterion("res_name >", value, "resName");
			return (Criteria) this;
		}

		public Criteria andResNameGreaterThanOrEqualTo(String value) {
			addCriterion("res_name >=", value, "resName");
			return (Criteria) this;
		}

		public Criteria andResNameLessThan(String value) {
			addCriterion("res_name <", value, "resName");
			return (Criteria) this;
		}

		public Criteria andResNameLessThanOrEqualTo(String value) {
			addCriterion("res_name <=", value, "resName");
			return (Criteria) this;
		}

		public Criteria andResNameLike(String value) {
			addCriterion("res_name like", value, "resName");
			return (Criteria) this;
		}

		public Criteria andResNameNotLike(String value) {
			addCriterion("res_name not like", value, "resName");
			return (Criteria) this;
		}

		public Criteria andResNameIn(List<String> values) {
			addCriterion("res_name in", values, "resName");
			return (Criteria) this;
		}

		public Criteria andResNameNotIn(List<String> values) {
			addCriterion("res_name not in", values, "resName");
			return (Criteria) this;
		}

		public Criteria andResNameBetween(String value1, String value2) {
			addCriterion("res_name between", value1, value2, "resName");
			return (Criteria) this;
		}

		public Criteria andResNameNotBetween(String value1, String value2) {
			addCriterion("res_name not between", value1, value2, "resName");
			return (Criteria) this;
		}

		public Criteria andIssueIdIsNull() {
			addCriterion("issue_id is null");
			return (Criteria) this;
		}

		public Criteria andIssueIdIsNotNull() {
			addCriterion("issue_id is not null");
			return (Criteria) this;
		}

		public Criteria andIssueIdEqualTo(String value) {
			addCriterion("issue_id =", value, "issueId");
			return (Criteria) this;
		}

		public Criteria andIssueIdNotEqualTo(String value) {
			addCriterion("issue_id <>", value, "issueId");
			return (Criteria) this;
		}

		public Criteria andIssueIdGreaterThan(String value) {
			addCriterion("issue_id >", value, "issueId");
			return (Criteria) this;
		}

		public Criteria andIssueIdGreaterThanOrEqualTo(String value) {
			addCriterion("issue_id >=", value, "issueId");
			return (Criteria) this;
		}

		public Criteria andIssueIdLessThan(String value) {
			addCriterion("issue_id <", value, "issueId");
			return (Criteria) this;
		}

		public Criteria andIssueIdLessThanOrEqualTo(String value) {
			addCriterion("issue_id <=", value, "issueId");
			return (Criteria) this;
		}

		public Criteria andIssueIdLike(String value) {
			addCriterion("issue_id like", value, "issueId");
			return (Criteria) this;
		}

		public Criteria andIssueIdNotLike(String value) {
			addCriterion("issue_id not like", value, "issueId");
			return (Criteria) this;
		}

		public Criteria andIssueIdIn(List<String> values) {
			addCriterion("issue_id in", values, "issueId");
			return (Criteria) this;
		}

		public Criteria andIssueIdNotIn(List<String> values) {
			addCriterion("issue_id not in", values, "issueId");
			return (Criteria) this;
		}

		public Criteria andIssueIdBetween(String value1, String value2) {
			addCriterion("issue_id between", value1, value2, "issueId");
			return (Criteria) this;
		}

		public Criteria andIssueIdNotBetween(String value1, String value2) {
			addCriterion("issue_id not between", value1, value2, "issueId");
			return (Criteria) this;
		}

		public Criteria andCreateTimeIsNull() {
			addCriterion("create_time is null");
			return (Criteria) this;
		}

		public Criteria andCreateTimeIsNotNull() {
			addCriterion("create_time is not null");
			return (Criteria) this;
		}

		public Criteria andCreateTimeEqualTo(Date value) {
			addCriterion("create_time =", value, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeNotEqualTo(Date value) {
			addCriterion("create_time <>", value, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeGreaterThan(Date value) {
			addCriterion("create_time >", value, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
			addCriterion("create_time >=", value, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeLessThan(Date value) {
			addCriterion("create_time <", value, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
			addCriterion("create_time <=", value, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeIn(List<Date> values) {
			addCriterion("create_time in", values, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeNotIn(List<Date> values) {
			addCriterion("create_time not in", values, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeBetween(Date value1, Date value2) {
			addCriterion("create_time between", value1, value2, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
			addCriterion("create_time not between", value1, value2, "createTime");
			return (Criteria) this;
		}

		public Criteria andCreatorIsNull() {
			addCriterion("creator is null");
			return (Criteria) this;
		}

		public Criteria andCreatorIsNotNull() {
			addCriterion("creator is not null");
			return (Criteria) this;
		}

		public Criteria andCreatorEqualTo(String value) {
			addCriterion("creator =", value, "creator");
			return (Criteria) this;
		}

		public Criteria andCreatorNotEqualTo(String value) {
			addCriterion("creator <>", value, "creator");
			return (Criteria) this;
		}

		public Criteria andCreatorGreaterThan(String value) {
			addCriterion("creator >", value, "creator");
			return (Criteria) this;
		}

		public Criteria andCreatorGreaterThanOrEqualTo(String value) {
			addCriterion("creator >=", value, "creator");
			return (Criteria) this;
		}

		public Criteria andCreatorLessThan(String value) {
			addCriterion("creator <", value, "creator");
			return (Criteria) this;
		}

		public Criteria andCreatorLessThanOrEqualTo(String value) {
			addCriterion("creator <=", value, "creator");
			return (Criteria) this;
		}

		public Criteria andCreatorLike(String value) {
			addCriterion("creator like", value, "creator");
			return (Criteria) this;
		}

		public Criteria andCreatorNotLike(String value) {
			addCriterion("creator not like", value, "creator");
			return (Criteria) this;
		}

		public Criteria andCreatorIn(List<String> values) {
			addCriterion("creator in", values, "creator");
			return (Criteria) this;
		}

		public Criteria andCreatorNotIn(List<String> values) {
			addCriterion("creator not in", values, "creator");
			return (Criteria) this;
		}

		public Criteria andCreatorBetween(String value1, String value2) {
			addCriterion("creator between", value1, value2, "creator");
			return (Criteria) this;
		}

		public Criteria andCreatorNotBetween(String value1, String value2) {
			addCriterion("creator not between", value1, value2, "creator");
			return (Criteria) this;
		}
	}

	public static class Criteria extends GeneratedCriteria {

		protected Criteria() {
			super();
		}
	}

	public static class Criterion {
		private String condition;

		private Object value;

		private Object secondValue;

		private boolean noValue;

		private boolean singleValue;

		private boolean betweenValue;

		private boolean listValue;

		private String typeHandler;

		public String getCondition() {
			return condition;
		}

		public Object getValue() {
			return value;
		}

		public Object getSecondValue() {
			return secondValue;
		}

		public boolean isNoValue() {
			return noValue;
		}

		public boolean isSingleValue() {
			return singleValue;
		}

		public boolean isBetweenValue() {
			return betweenValue;
		}

		public boolean isListValue() {
			return listValue;
		}

		public String getTypeHandler() {
			return typeHandler;
		}

		protected Criterion() {
			super();
			// TODO Auto-generated constructor stub
		}

		protected Criterion(String condition) {
			super();
			this.condition = condition;
			this.typeHandler = null;
			this.noValue = true;
		}

		protected Criterion(String condition, Object value, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.typeHandler = typeHandler;
			if (value instanceof List<?>) {
				this.listValue = true;
			} else {
				this.singleValue = true;
			}
		}

		protected Criterion(String condition, Object value) {
			this(condition, value, null);
		}

		protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.secondValue = secondValue;
			this.typeHandler = typeHandler;
			this.betweenValue = true;
		}

		protected Criterion(String condition, Object value, Object secondValue) {
			this(condition, value, secondValue, null);
		}
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
}