package com.hust.mining.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hust.mining.dao.mapper.RolePowerMapper;
import com.hust.mining.model.RolePower;
import com.hust.mining.model.RolePowerExample;
import com.hust.mining.model.RolePowerExample.Criteria;
@Repository
public class RolePowerDao {

	@Autowired
	private RolePowerMapper rolePowerMapper;

	public List<RolePower> selectRolePowerByRoleId(int roleId) {
		RolePowerExample example = new RolePowerExample();
		Criteria criteria = example.createCriteria();
		criteria.andRoleIdEqualTo(roleId);
		List<RolePower> rolePowers = rolePowerMapper.selectByExample(example);
		return rolePowers;
	}

	public int deleteRolePowerByRoleId(int roleId) {
		RolePowerExample example = new RolePowerExample();
		Criteria criteria = example.createCriteria();
		criteria.andRoleIdEqualTo(roleId);
		int statue = rolePowerMapper.deleteByExample(example);
		return statue;
	}

	public int deleteRolePowerById(int powerId, int roleId) {
		RolePowerExample example = new RolePowerExample();
		Criteria criteria = example.createCriteria();
		criteria.andPowerIdEqualTo(powerId);
		criteria.andRoleIdEqualTo(roleId);
		int statue = rolePowerMapper.deleteByExample(example);
		return statue;
	}

	public int deleteRolePowerByPowerId(int powerId) {
		RolePowerExample example = new RolePowerExample();
		Criteria criteria = example.createCriteria();
		criteria.andPowerIdEqualTo(powerId);
		int statue = rolePowerMapper.deleteByExample(example);
		return statue;
	}

	public long countByExample(RolePowerExample example) {
		return rolePowerMapper.countByExample(example);
	}

	public int deleteByExample(RolePowerExample example) {
		return rolePowerMapper.deleteByExample(example);
	}

	public int deleteByPrimaryKey(Integer id) {
		return rolePowerMapper.deleteByPrimaryKey(id);
	}

	public int insert(RolePower record) {
		return rolePowerMapper.insert(record);
	}

	public int insertSelective(RolePower record) {
		return rolePowerMapper.insertSelective(record);
	}

	public List<RolePower> selectByExample(RolePowerExample example) {
		return rolePowerMapper.selectByExample(example);
	}

	public RolePower selectByPrimaryKey(Integer id) {
		return rolePowerMapper.selectByPrimaryKey(id);
	}

	public int updateByExampleSelective(RolePower record, RolePowerExample example) {
		return rolePowerMapper.updateByExampleSelective(record, example);
	}

	public int updateByExample(RolePower record, RolePowerExample example) {
		return rolePowerMapper.updateByExample(record, example);
	}

	public int updateByPrimaryKeySelective(RolePower record) {
		return rolePowerMapper.updateByPrimaryKeySelective(record);
	}

	public int updateByPrimaryKey(RolePower record) {
		return rolePowerMapper.updateByPrimaryKey(record);
	}

}
