package com.hust.mining.service;

import java.util.List;

import com.hust.mining.model.Power;
import com.hust.mining.model.params.PowerQueryCondition;

public interface PowerService {

	List<Power> selectAllPower(int start, int limit);

	List<Power> selectOnePowerInfo(PowerQueryCondition powerName);
	
	long selectPowerCount(String powerName);

	boolean insertPowerInfo(Power power);

	boolean deletePowerById(int powerId);

	boolean updatePowerInfo(Power power);
}
