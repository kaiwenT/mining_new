package com.hust.mining.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hust.mining.dao.mapper.WeightMapper;
import com.hust.mining.model.Weight;
import com.hust.mining.model.WeightExample;
import com.hust.mining.model.WeightExample.Criteria;
import com.hust.mining.model.params.WeightQueryCondition;

@Repository
public class WeightDao {

	@Autowired
	private WeightMapper weightMapper;

	public int queryWeightByName(String name) {
		WeightExample example = new WeightExample();
		example.createCriteria().andNameEqualTo(name);
		List<Weight> list = weightMapper.selectByExample(example);
		if (null == list || list.size() == 0) {
			return 0;
		}
		return list.get(0).getWeight();
	}

	public long selectCountOfWeight() {
		WeightExample example = new WeightExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdIsNotNull();
		return weightMapper.countByExample(example);
	}

	public long selectCountOfWeight(WeightQueryCondition weight) {
		WeightExample example = new WeightExample();
		Criteria criteria = example.createCriteria();
		if (!StringUtils.isBlank(weight.getName())) {
			criteria.andNameLike("%" + weight.getName() + "%");
		}
		if (weight.getWeight() != null) {
			criteria.andWeightEqualTo(weight.getWeight());
		}
		return weightMapper.countByExample(example);
	}

	public List<Weight> selectAllWeight(int start, int limit) {
		WeightExample example = new WeightExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdIsNotNull();
		example.setStart(start);
		example.setLimit(limit);
		List<Weight> weight = weightMapper.selectByExample(example);
		return weight;
	}

	public List<Weight> selectWeightById(int id) {
		WeightExample example = new WeightExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(id);
		List<Weight> weight = weightMapper.selectByExample(example);
		return weight;
	}

	public List<Weight> selectWeightByName(String name) {
		WeightExample example = new WeightExample();
		Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(name);
		List<Weight> weight = weightMapper.selectByExample(example);
		return weight;
	}

	public List<Weight> selectNotIncluedWeigth(String name) {
		WeightExample example = new WeightExample();
		Criteria criteria = example.createCriteria();
		criteria.andNameNotEqualTo(name);
		List<Weight> weight = weightMapper.selectByExample(example);
		return weight;

	}

	public List<Weight> selectByWeightName(String weightName) {
		WeightExample example = new WeightExample();
		Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(weightName);
		List<Weight> weight = weightMapper.selectByExample(example);
		return weight;
	}

	public int insertWeight(Weight weight) {
		return weightMapper.insert(weight);
	}

	public int deleteWeightById(int id) {
		WeightExample example = new WeightExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(id);
		int status = weightMapper.deleteByExample(example);
		return status;
	}

	public int updateWeight(Weight weight) {
		return weightMapper.updateByPrimaryKeySelective(weight);
	}

	public List<Weight> selectByCondition(WeightQueryCondition weight) {
		WeightExample example = new WeightExample();
		Criteria criteria = example.createCriteria();
		if (!StringUtils.isBlank(weight.getName())) {
			criteria.andNameLike("%" + weight.getName() + "%");
		}
		if (weight.getWeight() != null) {
			criteria.andWeightEqualTo(weight.getWeight());
		}
		if (weight.getStart() != 0) {
			example.setStart(weight.getStart());
		}
		if (weight.getLimit() != 0) {
			example.setLimit(weight.getLimit());
		}
		List<Weight> weights = weightMapper.selectByExample(example);
		return weights;
	}
}
