package com.hust.mining.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StandardResultExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

	protected int start;

	protected int limit;
	
    public StandardResultExample() {
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

        public Criteria andStdRidIsNull() {
            addCriterion("std_rid is null");
            return (Criteria) this;
        }

        public Criteria andStdRidIsNotNull() {
            addCriterion("std_rid is not null");
            return (Criteria) this;
        }

        public Criteria andStdRidEqualTo(String value) {
            addCriterion("std_rid =", value, "stdRid");
            return (Criteria) this;
        }

        public Criteria andStdRidNotEqualTo(String value) {
            addCriterion("std_rid <>", value, "stdRid");
            return (Criteria) this;
        }

        public Criteria andStdRidGreaterThan(String value) {
            addCriterion("std_rid >", value, "stdRid");
            return (Criteria) this;
        }

        public Criteria andStdRidGreaterThanOrEqualTo(String value) {
            addCriterion("std_rid >=", value, "stdRid");
            return (Criteria) this;
        }

        public Criteria andStdRidLessThan(String value) {
            addCriterion("std_rid <", value, "stdRid");
            return (Criteria) this;
        }

        public Criteria andStdRidLessThanOrEqualTo(String value) {
            addCriterion("std_rid <=", value, "stdRid");
            return (Criteria) this;
        }

        public Criteria andStdRidLike(String value) {
            addCriterion("std_rid like", value, "stdRid");
            return (Criteria) this;
        }

        public Criteria andStdRidNotLike(String value) {
            addCriterion("std_rid not like", value, "stdRid");
            return (Criteria) this;
        }

        public Criteria andStdRidIn(List<String> values) {
            addCriterion("std_rid in", values, "stdRid");
            return (Criteria) this;
        }

        public Criteria andStdRidNotIn(List<String> values) {
            addCriterion("std_rid not in", values, "stdRid");
            return (Criteria) this;
        }

        public Criteria andStdRidBetween(String value1, String value2) {
            addCriterion("std_rid between", value1, value2, "stdRid");
            return (Criteria) this;
        }

        public Criteria andStdRidNotBetween(String value1, String value2) {
            addCriterion("std_rid not between", value1, value2, "stdRid");
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

        public Criteria andDateCountIsNull() {
            addCriterion("date_count is null");
            return (Criteria) this;
        }

        public Criteria andDateCountIsNotNull() {
            addCriterion("date_count is not null");
            return (Criteria) this;
        }

        public Criteria andDateCountEqualTo(String value) {
            addCriterion("date_count =", value, "dateCount");
            return (Criteria) this;
        }

        public Criteria andDateCountNotEqualTo(String value) {
            addCriterion("date_count <>", value, "dateCount");
            return (Criteria) this;
        }

        public Criteria andDateCountGreaterThan(String value) {
            addCriterion("date_count >", value, "dateCount");
            return (Criteria) this;
        }

        public Criteria andDateCountGreaterThanOrEqualTo(String value) {
            addCriterion("date_count >=", value, "dateCount");
            return (Criteria) this;
        }

        public Criteria andDateCountLessThan(String value) {
            addCriterion("date_count <", value, "dateCount");
            return (Criteria) this;
        }

        public Criteria andDateCountLessThanOrEqualTo(String value) {
            addCriterion("date_count <=", value, "dateCount");
            return (Criteria) this;
        }

        public Criteria andDateCountLike(String value) {
            addCriterion("date_count like", value, "dateCount");
            return (Criteria) this;
        }

        public Criteria andDateCountNotLike(String value) {
            addCriterion("date_count not like", value, "dateCount");
            return (Criteria) this;
        }

        public Criteria andDateCountIn(List<String> values) {
            addCriterion("date_count in", values, "dateCount");
            return (Criteria) this;
        }

        public Criteria andDateCountNotIn(List<String> values) {
            addCriterion("date_count not in", values, "dateCount");
            return (Criteria) this;
        }

        public Criteria andDateCountBetween(String value1, String value2) {
            addCriterion("date_count between", value1, value2, "dateCount");
            return (Criteria) this;
        }

        public Criteria andDateCountNotBetween(String value1, String value2) {
            addCriterion("date_count not between", value1, value2, "dateCount");
            return (Criteria) this;
        }

        public Criteria andSourceCountIsNull() {
            addCriterion("source_count is null");
            return (Criteria) this;
        }

        public Criteria andSourceCountIsNotNull() {
            addCriterion("source_count is not null");
            return (Criteria) this;
        }

        public Criteria andSourceCountEqualTo(String value) {
            addCriterion("source_count =", value, "sourceCount");
            return (Criteria) this;
        }

        public Criteria andSourceCountNotEqualTo(String value) {
            addCriterion("source_count <>", value, "sourceCount");
            return (Criteria) this;
        }

        public Criteria andSourceCountGreaterThan(String value) {
            addCriterion("source_count >", value, "sourceCount");
            return (Criteria) this;
        }

        public Criteria andSourceCountGreaterThanOrEqualTo(String value) {
            addCriterion("source_count >=", value, "sourceCount");
            return (Criteria) this;
        }

        public Criteria andSourceCountLessThan(String value) {
            addCriterion("source_count <", value, "sourceCount");
            return (Criteria) this;
        }

        public Criteria andSourceCountLessThanOrEqualTo(String value) {
            addCriterion("source_count <=", value, "sourceCount");
            return (Criteria) this;
        }

        public Criteria andSourceCountLike(String value) {
            addCriterion("source_count like", value, "sourceCount");
            return (Criteria) this;
        }

        public Criteria andSourceCountNotLike(String value) {
            addCriterion("source_count not like", value, "sourceCount");
            return (Criteria) this;
        }

        public Criteria andSourceCountIn(List<String> values) {
            addCriterion("source_count in", values, "sourceCount");
            return (Criteria) this;
        }

        public Criteria andSourceCountNotIn(List<String> values) {
            addCriterion("source_count not in", values, "sourceCount");
            return (Criteria) this;
        }

        public Criteria andSourceCountBetween(String value1, String value2) {
            addCriterion("source_count between", value1, value2, "sourceCount");
            return (Criteria) this;
        }

        public Criteria andSourceCountNotBetween(String value1, String value2) {
            addCriterion("source_count not between", value1, value2, "sourceCount");
            return (Criteria) this;
        }

        public Criteria andContentNameIsNull() {
            addCriterion("content_name is null");
            return (Criteria) this;
        }

        public Criteria andContentNameIsNotNull() {
            addCriterion("content_name is not null");
            return (Criteria) this;
        }

        public Criteria andContentNameEqualTo(String value) {
            addCriterion("content_name =", value, "contentName");
            return (Criteria) this;
        }

        public Criteria andContentNameNotEqualTo(String value) {
            addCriterion("content_name <>", value, "contentName");
            return (Criteria) this;
        }

        public Criteria andContentNameGreaterThan(String value) {
            addCriterion("content_name >", value, "contentName");
            return (Criteria) this;
        }

        public Criteria andContentNameGreaterThanOrEqualTo(String value) {
            addCriterion("content_name >=", value, "contentName");
            return (Criteria) this;
        }

        public Criteria andContentNameLessThan(String value) {
            addCriterion("content_name <", value, "contentName");
            return (Criteria) this;
        }

        public Criteria andContentNameLessThanOrEqualTo(String value) {
            addCriterion("content_name <=", value, "contentName");
            return (Criteria) this;
        }

        public Criteria andContentNameLike(String value) {
            addCriterion("content_name like", value, "contentName");
            return (Criteria) this;
        }

        public Criteria andContentNameNotLike(String value) {
            addCriterion("content_name not like", value, "contentName");
            return (Criteria) this;
        }

        public Criteria andContentNameIn(List<String> values) {
            addCriterion("content_name in", values, "contentName");
            return (Criteria) this;
        }

        public Criteria andContentNameNotIn(List<String> values) {
            addCriterion("content_name not in", values, "contentName");
            return (Criteria) this;
        }

        public Criteria andContentNameBetween(String value1, String value2) {
            addCriterion("content_name between", value1, value2, "contentName");
            return (Criteria) this;
        }

        public Criteria andContentNameNotBetween(String value1, String value2) {
            addCriterion("content_name not between", value1, value2, "contentName");
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