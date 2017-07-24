package com.hust.mining.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.mining.dao.SourceTypeDao;
import com.hust.mining.model.SourceType;
import com.hust.mining.model.params.SourceTypeQueryCondition;
import com.hust.mining.service.SourceTypeService;

@Service
public class SourceTypeServiceImple implements SourceTypeService {
	private static final Logger logger = LoggerFactory.getLogger(SourceTypeServiceImple.class);
	@Autowired
	private SourceTypeDao sourceTypeDao;

	@Override
	public List<SourceType> selectSourceType(int start, int limit) {
		List<SourceType> sourceType = sourceTypeDao.selectSourceType(start, limit);
		if (sourceType.isEmpty()) {
			logger.info("sourceTYpe is empty");
			return null;
		}
		return sourceType;
	}

	@Override
	public List<SourceType> selectSourceTypeByName(SourceTypeQueryCondition sourceType) {
		List<SourceType> sourceTypes = sourceTypeDao.selectSourceTypeByName(sourceType);
		if (sourceTypes.isEmpty()) {
			logger.info("The name is not exist");
		}
		return sourceTypes;
	}

	@Override
	public int deleteSourceTypeById(int id) {
		int status = sourceTypeDao.deleteSourceTypeById(id);
		if (status == 0) {
			logger.info("delete sourceType is error");
			return status;
		}
		return status;
	}

	@Override
	public int insertSourceType(String name) {
		int status = sourceTypeDao.insertSourceType(name);
		if (status == 0) {
			logger.info("insert sourceType is error");
			return status;
		}
		return status;
	}

	@Override
	public int updateSourceType(SourceType sourceType) {
		int status = sourceTypeDao.updateSourceType(sourceType);
		if (status == 0) {
			logger.info("update sourcetype is error");
			return status;
		}
		return status;
	}

	@Override
	public long selectSourceTypeCount() {
		return sourceTypeDao.selectSourceTypeCount();
	}

	@Override
	public long selectSourceTypeCountByName(String name) {
		// TODO Auto-generated method stub
		SourceTypeQueryCondition sourceType = new SourceTypeQueryCondition();
		sourceType.setName(name);
		return sourceTypeDao.selectSourceTypeCountByName(sourceType);
	}
}
