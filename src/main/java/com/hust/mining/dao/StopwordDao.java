package com.hust.mining.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hust.mining.dao.mapper.StopwordMapper;
import com.hust.mining.model.Stopword;
import com.hust.mining.model.StopwordExample;
import com.hust.mining.model.StopwordExample.Criteria;
import com.hust.mining.model.params.StopwordQueryCondition;
@Repository
public class StopwordDao {
	@Autowired
	private StopwordMapper stopwordMapper;
	
	public List<String> selectAllStopword(){
		StopwordExample example = new StopwordExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdIsNotNull();
		example.setOrderByClause("create_time desc");
		List<Stopword> list = stopwordMapper.selectByExample(example);
		List<String> stopwords = new ArrayList<>();
		for (Stopword stopword : list) {
			stopwords.add(stopword.getWord());
		}
		return stopwords;
	}
	
	public List<Stopword> selectAllStopword(int start, int limit){
		List<Stopword> list = new ArrayList<Stopword>();
		StopwordExample example = new StopwordExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdIsNotNull();
		example.setStart(start);
		example.setLimit(limit);
		example.setOrderByClause("create_time desc");
		list = stopwordMapper.selectByExample(example);
		return list;
	}
	
	public List<Stopword> selectByExample(StopwordQueryCondition condition){
		List<Stopword> list = new ArrayList<Stopword>();
		StopwordExample example = new StopwordExample();
		Criteria criteria = example.createCriteria();
		if(!StringUtils.isBlank(condition.getWord())){
			criteria.andWordLike("%"+condition.getWord()+"%");
		}
		example.setStart(condition.getStart());
		example.setLimit(condition.getLimit());
		example.setOrderByClause("create_time desc");
		list = stopwordMapper.selectByExample(example);
		return list;
	}
	
	public long selectCountOfStopword(){
		StopwordExample example = new StopwordExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdIsNotNull();
		long count = stopwordMapper.countByExample(example);
		return count;
	}
	
	public long selectCountOfStopword(StopwordQueryCondition condition){
		StopwordExample example = new StopwordExample();
		Criteria criteria = example.createCriteria();
		if(!StringUtils.isBlank(condition.getWord())){
			criteria.andWordLike("%"+condition.getWord()+"%");
		}
		long count = stopwordMapper.countByExample(example);
		return count;
		
	}
	
	public int deleteByExample(StopwordExample example){
		return stopwordMapper.deleteByExample(example);
	}
	
	public int deleteById(Integer id){
		return stopwordMapper.deleteByPrimaryKey(id);
	}
	
	public int insert(Stopword stopword){
		return stopwordMapper.insert(stopword);
	}
	
	public int insertBatch(List<Stopword> list){
		return stopwordMapper.insertBatch(list);
	}
}
