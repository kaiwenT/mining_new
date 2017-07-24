package com.hust.mining.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class IssueExample {
	protected String orderByClause;

	protected boolean distinct;

	protected List<Criteria> oredCriteria;

	protected int start;

	protected int limit;

	public IssueExample() {
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

		public Criteria andIssueNameIsNull() {
			addCriterion("issue_name is null");
			return (Criteria) this;
		}

		public Criteria andIssueNameIsNotNull() {
			addCriterion("issue_name is not null");
			return (Criteria) this;
		}

		public Criteria andIssueNameEqualTo(String value) {
			addCriterion("issue_name =", value, "issueName");
			return (Criteria) this;
		}

		public Criteria andIssueNameNotEqualTo(String value) {
			addCriterion("issue_name <>", value, "issueName");
			return (Criteria) this;
		}

		public Criteria andIssueNameGreaterThan(String value) {
			addCriterion("issue_name >", value, "issueName");
			return (Criteria) this;
		}

		public Criteria andIssueNameGreaterThanOrEqualTo(String value) {
			addCriterion("issue_name >=", value, "issueName");
			return (Criteria) this;
		}

		public Criteria andIssueNameLessThan(String value) {
			addCriterion("issue_name <", value, "issueName");
			return (Criteria) this;
		}

		public Criteria andIssueNameLessThanOrEqualTo(String value) {
			addCriterion("issue_name <=", value, "issueName");
			return (Criteria) this;
		}

		public Criteria andIssueNameLike(String value) {
			addCriterion("issue_name like", value, "issueName");
			return (Criteria) this;
		}

		public Criteria andIssueNameNotLike(String value) {
			addCriterion("issue_name not like", value, "issueName");
			return (Criteria) this;
		}

		public Criteria andIssueNameIn(List<String> values) {
			addCriterion("issue_name in", values, "issueName");
			return (Criteria) this;
		}

		public Criteria andIssueNameNotIn(List<String> values) {
			addCriterion("issue_name not in", values, "issueName");
			return (Criteria) this;
		}

		public Criteria andIssueNameBetween(String value1, String value2) {
			addCriterion("issue_name between", value1, value2, "issueName");
			return (Criteria) this;
		}

		public Criteria andIssueNameNotBetween(String value1, String value2) {
			addCriterion("issue_name not between", value1, value2, "issueName");
			return (Criteria) this;
		}

		public Criteria andIssueHoldIsNull() {
			addCriterion("issue_hold is null");
			return (Criteria) this;
		}

		public Criteria andIssueHoldIsNotNull() {
			addCriterion("issue_hold is not null");
			return (Criteria) this;
		}

		public Criteria andIssueHoldEqualTo(String value) {
			addCriterion("issue_hold =", value, "issueHold");
			return (Criteria) this;
		}

		public Criteria andIssueHoldNotEqualTo(String value) {
			addCriterion("issue_hold <>", value, "issueHold");
			return (Criteria) this;
		}

		public Criteria andIssueHoldGreaterThan(String value) {
			addCriterion("issue_hold >", value, "issueHold");
			return (Criteria) this;
		}

		public Criteria andIssueHoldGreaterThanOrEqualTo(String value) {
			addCriterion("issue_hold >=", value, "issueHold");
			return (Criteria) this;
		}

		public Criteria andIssueHoldLessThan(String value) {
			addCriterion("issue_hold <", value, "issueHold");
			return (Criteria) this;
		}

		public Criteria andIssueHoldLessThanOrEqualTo(String value) {
			addCriterion("issue_hold <=", value, "issueHold");
			return (Criteria) this;
		}

		public Criteria andIssueHoldLike(String value) {
			addCriterion("issue_hold like", value, "issueHold");
			return (Criteria) this;
		}

		public Criteria andIssueHoldNotLike(String value) {
			addCriterion("issue_hold not like", value, "issueHold");
			return (Criteria) this;
		}

		public Criteria andIssueHoldIn(List<String> values) {
			addCriterion("issue_hold in", values, "issueHold");
			return (Criteria) this;
		}

		public Criteria andIssueHoldNotIn(List<String> values) {
			addCriterion("issue_hold not in", values, "issueHold");
			return (Criteria) this;
		}

		public Criteria andIssueHoldBetween(String value1, String value2) {
			addCriterion("issue_hold between", value1, value2, "issueHold");
			return (Criteria) this;
		}

		public Criteria andIssueHoldNotBetween(String value1, String value2) {
			addCriterion("issue_hold not between", value1, value2, "issueHold");
			return (Criteria) this;
		}

		public Criteria andIssueBelongToIsNull() {
			addCriterion("issue_belong_to is null");
			return (Criteria) this;
		}

		public Criteria andIssueBelongToIsNotNull() {
			addCriterion("issue_belong_to is not null");
			return (Criteria) this;
		}

		public Criteria andIssueBelongToEqualTo(String value) {
			addCriterion("issue_belong_to =", value, "issueBelongTo");
			return (Criteria) this;
		}

		public Criteria andIssueBelongToNotEqualTo(String value) {
			addCriterion("issue_belong_to <>", value, "issueBelongTo");
			return (Criteria) this;
		}

		public Criteria andIssueBelongToGreaterThan(String value) {
			addCriterion("issue_belong_to >", value, "issueBelongTo");
			return (Criteria) this;
		}

		public Criteria andIssueBelongToGreaterThanOrEqualTo(String value) {
			addCriterion("issue_belong_to >=", value, "issueBelongTo");
			return (Criteria) this;
		}

		public Criteria andIssueBelongToLessThan(String value) {
			addCriterion("issue_belong_to <", value, "issueBelongTo");
			return (Criteria) this;
		}

		public Criteria andIssueBelongToLessThanOrEqualTo(String value) {
			addCriterion("issue_belong_to <=", value, "issueBelongTo");
			return (Criteria) this;
		}

		public Criteria andIssueBelongToLike(String value) {
			addCriterion("issue_belong_to like", value, "issueBelongTo");
			return (Criteria) this;
		}

		public Criteria andIssueBelongToNotLike(String value) {
			addCriterion("issue_belong_to not like", value, "issueBelongTo");
			return (Criteria) this;
		}

		public Criteria andIssueBelongToIn(List<String> values) {
			addCriterion("issue_belong_to in", values, "issueBelongTo");
			return (Criteria) this;
		}

		public Criteria andIssueBelongToNotIn(List<String> values) {
			addCriterion("issue_belong_to not in", values, "issueBelongTo");
			return (Criteria) this;
		}

		public Criteria andIssueBelongToBetween(String value1, String value2) {
			addCriterion("issue_belong_to between", value1, value2, "issueBelongTo");
			return (Criteria) this;
		}

		public Criteria andIssueBelongToNotBetween(String value1, String value2) {
			addCriterion("issue_belong_to not between", value1, value2, "issueBelongTo");
			return (Criteria) this;
		}

		public Criteria andIssueTypeIsNull() {
			addCriterion("issue_type is null");
			return (Criteria) this;
		}

		public Criteria andIssueTypeIsNotNull() {
			addCriterion("issue_type is not null");
			return (Criteria) this;
		}

		public Criteria andIssueTypeEqualTo(String value) {
			addCriterion("issue_type =", value, "issueType");
			return (Criteria) this;
		}

		public Criteria andIssueTypeNotEqualTo(String value) {
			addCriterion("issue_type <>", value, "issueType");
			return (Criteria) this;
		}

		public Criteria andIssueTypeGreaterThan(String value) {
			addCriterion("issue_type >", value, "issueType");
			return (Criteria) this;
		}

		public Criteria andIssueTypeGreaterThanOrEqualTo(String value) {
			addCriterion("issue_type >=", value, "issueType");
			return (Criteria) this;
		}

		public Criteria andIssueTypeLessThan(String value) {
			addCriterion("issue_type <", value, "issueType");
			return (Criteria) this;
		}

		public Criteria andIssueTypeLessThanOrEqualTo(String value) {
			addCriterion("issue_type <=", value, "issueType");
			return (Criteria) this;
		}

		public Criteria andIssueTypeLike(String value) {
			addCriterion("issue_type like", value, "issueType");
			return (Criteria) this;
		}

		public Criteria andIssueTypeNotLike(String value) {
			addCriterion("issue_type not like", value, "issueType");
			return (Criteria) this;
		}

		public Criteria andIssueTypeIn(List<String> values) {
			addCriterion("issue_type in", values, "issueType");
			return (Criteria) this;
		}

		public Criteria andIssueTypeNotIn(List<String> values) {
			addCriterion("issue_type not in", values, "issueType");
			return (Criteria) this;
		}

		public Criteria andIssueTypeBetween(String value1, String value2) {
			addCriterion("issue_type between", value1, value2, "issueType");
			return (Criteria) this;
		}

		public Criteria andIssueTypeNotBetween(String value1, String value2) {
			addCriterion("issue_type not between", value1, value2, "issueType");
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

		public Criteria andLastOperatorIsNull() {
			addCriterion("last_operator is null");
			return (Criteria) this;
		}

		public Criteria andLastOperatorIsNotNull() {
			addCriterion("last_operator is not null");
			return (Criteria) this;
		}

		public Criteria andLastOperatorEqualTo(String value) {
			addCriterion("last_operator =", value, "lastOperator");
			return (Criteria) this;
		}

		public Criteria andLastOperatorNotEqualTo(String value) {
			addCriterion("last_operator <>", value, "lastOperator");
			return (Criteria) this;
		}

		public Criteria andLastOperatorGreaterThan(String value) {
			addCriterion("last_operator >", value, "lastOperator");
			return (Criteria) this;
		}

		public Criteria andLastOperatorGreaterThanOrEqualTo(String value) {
			addCriterion("last_operator >=", value, "lastOperator");
			return (Criteria) this;
		}

		public Criteria andLastOperatorLessThan(String value) {
			addCriterion("last_operator <", value, "lastOperator");
			return (Criteria) this;
		}

		public Criteria andLastOperatorLessThanOrEqualTo(String value) {
			addCriterion("last_operator <=", value, "lastOperator");
			return (Criteria) this;
		}

		public Criteria andLastOperatorLike(String value) {
			addCriterion("last_operator like", value, "lastOperator");
			return (Criteria) this;
		}

		public Criteria andLastOperatorNotLike(String value) {
			addCriterion("last_operator not like", value, "lastOperator");
			return (Criteria) this;
		}

		public Criteria andLastOperatorIn(List<String> values) {
			addCriterion("last_operator in", values, "lastOperator");
			return (Criteria) this;
		}

		public Criteria andLastOperatorNotIn(List<String> values) {
			addCriterion("last_operator not in", values, "lastOperator");
			return (Criteria) this;
		}

		public Criteria andLastOperatorBetween(String value1, String value2) {
			addCriterion("last_operator between", value1, value2, "lastOperator");
			return (Criteria) this;
		}

		public Criteria andLastOperatorNotBetween(String value1, String value2) {
			addCriterion("last_operator not between", value1, value2, "lastOperator");
			return (Criteria) this;
		}

		public Criteria andLastUpdateTimeIsNull() {
			addCriterion("last_update_time is null");
			return (Criteria) this;
		}

		public Criteria andLastUpdateTimeIsNotNull() {
			addCriterion("last_update_time is not null");
			return (Criteria) this;
		}

		public Criteria andLastUpdateTimeEqualTo(Date value) {
			addCriterion("last_update_time =", value, "lastUpdateTime");
			return (Criteria) this;
		}

		public Criteria andLastUpdateTimeNotEqualTo(Date value) {
			addCriterion("last_update_time <>", value, "lastUpdateTime");
			return (Criteria) this;
		}

		public Criteria andLastUpdateTimeGreaterThan(Date value) {
			addCriterion("last_update_time >", value, "lastUpdateTime");
			return (Criteria) this;
		}

		public Criteria andLastUpdateTimeGreaterThanOrEqualTo(Date value) {
			addCriterion("last_update_time >=", value, "lastUpdateTime");
			return (Criteria) this;
		}

		public Criteria andLastUpdateTimeLessThan(Date value) {
			addCriterion("last_update_time <", value, "lastUpdateTime");
			return (Criteria) this;
		}

		public Criteria andLastUpdateTimeLessThanOrEqualTo(Date value) {
			addCriterion("last_update_time <=", value, "lastUpdateTime");
			return (Criteria) this;
		}

		public Criteria andLastUpdateTimeIn(List<Date> values) {
			addCriterion("last_update_time in", values, "lastUpdateTime");
			return (Criteria) this;
		}

		public Criteria andLastUpdateTimeNotIn(List<Date> values) {
			addCriterion("last_update_time not in", values, "lastUpdateTime");
			return (Criteria) this;
		}

		public Criteria andLastUpdateTimeBetween(Date value1, Date value2) {
			addCriterion("last_update_time between", value1, value2, "lastUpdateTime");
			return (Criteria) this;
		}

		public Criteria andLastUpdateTimeNotBetween(Date value1, Date value2) {
			addCriterion("last_update_time not between", value1, value2, "lastUpdateTime");
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