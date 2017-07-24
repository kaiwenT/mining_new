package com.hust.mining.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hust.mining.model.Power;
import com.hust.mining.model.PowerExample;

public interface PowerMapper {
	long countByExample(PowerExample example);

	int deleteByExample(PowerExample example);

	int deleteByPrimaryKey(Integer powerId);

	int insert(Power record);

	int insertSelective(Power record);

	List<Power> selectByExample(PowerExample example);

	Power selectByPrimaryKey(Integer powerId);

	int updateByExampleSelective(@Param("record") Power record, @Param("example") PowerExample example);

	int updateByExample(@Param("record") Power record, @Param("example") PowerExample example);

	int updateByPrimaryKeySelective(Power record);

	int updateByPrimaryKey(Power record);
}