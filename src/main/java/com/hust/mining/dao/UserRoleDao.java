package com.hust.mining.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hust.mining.dao.mapper.UserRoleMapper;
import com.hust.mining.model.User;
import com.hust.mining.model.UserExample;
import com.hust.mining.model.UserRole;
import com.hust.mining.model.UserRoleExample;
import com.hust.mining.model.UserRoleExample.Criteria;
import com.hust.mining.model.params.UserQueryCondition;
@Repository
public class UserRoleDao {

	@Autowired
	private UserRoleMapper userRoleMapper;

	/**
	 * 查询出用户角色表中的信息
	 * 
	 * @return
	 */
	public List<UserRole> selectAllUserRole() {
		UserRoleExample example = new UserRoleExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdIsNotNull();
		List<UserRole> userRoles = userRoleMapper.selectByExample(example);
		return userRoles;
	}

	public int deleteUserRoleByUserId(int id) {
		UserRoleExample example = new UserRoleExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(id);
		int statue = userRoleMapper.deleteByExample(example);
		return statue;
	}

	public List<UserRole> selectUserRoleByUserId(int userId) {
		UserRoleExample example = new UserRoleExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		List<UserRole> userRole = userRoleMapper.selectByExample(example);
		return userRole;
	}

	public List<UserRole> selectUserRoleByRoleId(int roleId) {
		UserRoleExample example = new UserRoleExample();
		Criteria criteria = example.createCriteria();
		criteria.andRoleIdEqualTo(roleId);
		List<UserRole> userRole = userRoleMapper.selectByExample(example);
		return userRole;
	}

	public int insertUserRole(UserRole userRole) {
		int status = userRoleMapper.insertSelective(userRole);
		return status;
	}

	public int deleteUserRoleByRoleId(int roleId) {
		UserRoleExample example = new UserRoleExample();
		Criteria criteria = example.createCriteria();
		criteria.andRoleIdEqualTo(roleId);
		int statue = userRoleMapper.deleteByExample(example);
		return statue;
	}
}
