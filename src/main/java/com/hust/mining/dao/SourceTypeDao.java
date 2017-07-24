package com.hust.mining.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hust.mining.dao.mapper.SourceTypeMapper;
import com.hust.mining.model.SourceType;
import com.hust.mining.model.SourceTypeExample;
import com.hust.mining.model.SourceTypeExample.Criteria;
import com.hust.mining.model.params.SourceTypeQueryCondition;
@Repository
public class SourceTypeDao {
	@Autowired
	private SourceTypeMapper sourceTypeMapper;

	public List<SourceType> selectSourceType(int start, int limit) {
		SourceTypeExample example = new SourceTypeExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdIsNotNull();
		example.setStart(start);
		example.setLimit(limit);
		List<SourceType> sourceType = sourceTypeMapper.selectByExample(example);
		return sourceType;
	}
	
	public long selectSourceTypeCount(){
		SourceTypeExample example = new SourceTypeExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdIsNotNull();
		example.setStart(0);
		example.setLimit(0);
		return sourceTypeMapper.countByExample(example);
	}
	
	public long selectSourceTypeCountByName(SourceTypeQueryCondition sourceType){
		SourceTypeExample example = new SourceTypeExample();
		Criteria criteria = example.createCriteria();
		if (!StringUtils.isBlank(sourceType.getName())) {
			criteria.andNameLike("%" + sourceType.getName() + "%");
		}
		example.setStart(0);
		example.setLimit(0);		
		return sourceTypeMapper.countByExample(example);
	}

	public List<SourceType> selectSourceTypeByName(SourceTypeQueryCondition sourceType) {
		SourceTypeExample example = new SourceTypeExample();
		Criteria criteria = example.createCriteria();
		if (!StringUtils.isBlank(sourceType.getName())) {
			criteria.andNameLike("%" + sourceType.getName() + "%");
		}
		if (sourceType.getStart() != 0) {
			example.setStart(sourceType.getStart());
		}
		if (sourceType.getLimit() != 0) {
			example.setLimit(sourceType.getLimit());
		}
		List<SourceType> sourceTypes = sourceTypeMapper.selectByExample(example);
		return sourceTypes;
	}

	public int deleteSourceTypeById(int id) {
		return sourceTypeMapper.deleteByPrimaryKey(id);
	}

	public int insertSourceType(String name) {
		SourceType sourceType = new SourceType();
		sourceType.setName(name);
		return sourceTypeMapper.insert(sourceType);
	}

	public int updateSourceType(SourceType sourceType) {
		return sourceTypeMapper.updateByPrimaryKey(sourceType);
	}
}
