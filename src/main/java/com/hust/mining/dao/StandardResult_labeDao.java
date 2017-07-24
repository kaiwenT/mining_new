package com.hust.mining.dao;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.hust.mining.dao.mapper.StandardResult_labelMapper;
import com.hust.mining.model.StandardResult_label;
import com.hust.mining.model.StandardResult_labelExample;
import com.hust.mining.model.StandardResult_labelExample.Criteria;

@Repository
public class StandardResult_labeDao {
	
	@Autowired
	private StandardResult_labelMapper standardResult_labelMapper;
	
	/**
	 * 插入语句
	 * @param standardResult_label
	 * @return
	 */
	public int insert(StandardResult_label standardResult_label)
	{
		return standardResult_labelMapper.insert(standardResult_label);
	}
	
	/**
	 * 返回的是任务id=stdResId的实体类
	 * @param stdResId 任务ID
	 * @return
	 */
	public List<StandardResult_label> selectLabelsForStandResult(String stdResId){
		
		StandardResult_labelExample example = new StandardResult_labelExample();
		Criteria criteria = example.createCriteria();
		criteria.andStdRidEqualTo(stdResId);
		List<StandardResult_label> standardResult_labels = standardResult_labelMapper.selectByExample(example);
		return standardResult_labels;
	}
	
	/**
	 * 根据标签查找任务
	 * @param labelid
	 * @return
	 */
	public List<StandardResult_label> selectStandResultsBylabel(int labelid) {
		StandardResult_labelExample example = new StandardResult_labelExample();
		Criteria criteria = example.createCriteria();
		criteria.andLabelidEqualTo(labelid);
		List<StandardResult_label> standardResult_labels = standardResult_labelMapper.selectByExample(example);
		return standardResult_labels;
	}
	/**
	 * 删除
	 * @param stdRid
	 * @param labelid
	 * @return
	 */
	public int delete(String stdRid,Integer labelid)
	{
		return standardResult_labelMapper.deleteByPrimaryKey(stdRid, labelid);
	}

}
