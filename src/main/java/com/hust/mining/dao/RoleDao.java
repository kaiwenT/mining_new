package com.hust.mining.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hust.mining.dao.mapper.RoleMapper;
import com.hust.mining.model.Role;
import com.hust.mining.model.RoleExample;
import com.hust.mining.model.RoleExample.Criteria;
import com.hust.mining.model.params.RoleQueryCondition;

@Repository
public class RoleDao {

	@Autowired
	private RoleMapper roleMapper;

	public long countByExample(RoleExample example) {
		return roleMapper.countByExample(example);
	}

	public List<Role> selectRoles(int start, int limit) {
		RoleExample example = new RoleExample();
		Criteria criteria = example.createCriteria();
		criteria.andRoleIdIsNotNull();
		example.setStart(start);
		example.setLimit(limit);
		List<Role> roles = roleMapper.selectByExample(example);
		return roles;
	}
	
	public long selectRoleCount(RoleQueryCondition role){
		RoleExample example = new RoleExample();
		Criteria criteria = example.createCriteria();
		if (!StringUtils.isBlank(role.getRoleName())) {
			criteria.andRoleNameLike("%" + role.getRoleName() + "%");
		}
		return roleMapper.countByExample(example);
	}

	public List<Role> selectRole() {
		RoleExample example = new RoleExample();
		Criteria criteria = example.createCriteria();
		criteria.andRoleIdIsNotNull();
		List<Role> roles = roleMapper.selectByExample(example);
		return roles;
	}

	public List<Role> selectRoleById(int roleId) {
		RoleExample example = new RoleExample();
		Criteria criteria = example.createCriteria();
		criteria.andRoleIdEqualTo(roleId);
		List<Role> roles = roleMapper.selectByExample(example);
		return roles;
	}

	public List<Role> selectRoleByName(String roleName) {
		RoleExample example = new RoleExample();
		Criteria criteria = example.createCriteria();
		criteria.andRoleNameEqualTo(roleName);
		List<Role> roles = roleMapper.selectByExample(example);
		return roles;
	}

	public List<Role> selectByLikeRoleName(RoleQueryCondition role) {
		RoleExample example = new RoleExample();
		Criteria criteria = example.createCriteria();
		if (!StringUtils.isBlank(role.getRoleName())) {
			criteria.andRoleNameLike("%" + role.getRoleName() + "%");
		}
		if (role.getStart() != 0) {
			example.setStart(role.getStart());
		}
		if (role.getLimit() != 0) {
			example.setLimit(role.getLimit());
		}
		List<Role> roles = roleMapper.selectByExample(example);
		return roles;
	}

	public List<Role> selectByNotIncluedRoleName(String roleName) {
		RoleExample example = new RoleExample();
		Criteria criteria = example.createCriteria();
		criteria.andRoleNameNotEqualTo(roleName);
		List<Role> roles = roleMapper.selectByExample(example);
		return roles;
	}

	public int deleteByExample(RoleExample example) {
		return roleMapper.deleteByExample(example);
	}

	public int deleteByPrimaryKey(Integer roleId) {
		return roleMapper.deleteByPrimaryKey(roleId);
	}

	public int insert(Role record) {
		return roleMapper.insert(record);
	}

	public int insertRole(String roleName) {
		Role roles = new Role();
		roles.setRoleName(roleName);
		return roleMapper.insert(roles);
	}

	public int insertSelective(Role record) {
		return roleMapper.insertSelective(record);
	}

	public List<Role> selectByExample(RoleExample example) {
		return roleMapper.selectByExample(example);
	}

	public Role selectByPrimaryKey(Integer roleId) {
		return roleMapper.selectByPrimaryKey(roleId);
	}

	public int updateByExampleSelective(Role record, RoleExample example) {
		return roleMapper.updateByExampleSelective(record, example);
	}

	public int updateByExample(Role record, RoleExample example) {
		return roleMapper.updateByExample(record, example);
	}

	public int updateByPrimaryKeySelective(Role record) {
		return roleMapper.updateByPrimaryKeySelective(record);
	}

	public int updateByPrimaryKey(Role record) {
		return roleMapper.updateByPrimaryKey(record);
	}

}
