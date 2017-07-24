package com.hust.mining.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hust.mining.dao.mapper.issue_labelMapper;
import com.hust.mining.model.issue_label;
import com.hust.mining.model.issue_labelExample;
import com.hust.mining.model.issue_labelExample.Criteria;

@Repository
public class issue_labelDao {
	
	@Autowired
	private issue_labelMapper issue_labelMapper;
	
	/**
	 * 插入语句
	 * @param issue_label
	 * @return
	 */
	public int insert(issue_label issue_label)
	{
		return issue_labelMapper.insert(issue_label);
	}
	
	/**
	 * 返回的是任务id=issueId的实体类
	 * @param issueId
	 * @return
	 */
	public List<issue_label> selectLabelsForStandResult(String issueId)
	{
		issue_labelExample example = new issue_labelExample();
		Criteria criteria = example.createCriteria();
		criteria.andIssueidEqualTo(issueId);
		List<issue_label> issue_labels = issue_labelMapper.selectByExample(example);
		return issue_labels;
	}
	
	/**
	 * 根据标签查找任务
	 * @param labelid
	 * @return
	 */
	public List<issue_label> selectIssuesBylabel(int labelid)
	{
		issue_labelExample example = new issue_labelExample();
		Criteria criteria = example.createCriteria();
		criteria.andLabelidEqualTo(labelid);
		List<issue_label> issue_labels = issue_labelMapper.selectByExample(example);
		return issue_labels;
	}
	
	/**
	 * 删除
	 * @param issueId
	 * @param labelid
	 * @return
	 */
	public int delete(String issueId, Integer labelid)
	{
		return issue_labelMapper.deleteByPrimaryKey(issueId, labelid);
	}

}
