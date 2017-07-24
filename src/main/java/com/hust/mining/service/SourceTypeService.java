package com.hust.mining.service;

import java.util.List;

import com.hust.mining.model.SourceType;
import com.hust.mining.model.params.SourceTypeQueryCondition;

public interface SourceTypeService {

	List<SourceType> selectSourceType(int start, int limit);

	List<SourceType> selectSourceTypeByName(SourceTypeQueryCondition sourceType);
	
	long selectSourceTypeCount();
	
	long selectSourceTypeCountByName(String name);

	int deleteSourceTypeById(int id);

	int insertSourceType(String name);

	int updateSourceType(SourceType sourceType);

}
