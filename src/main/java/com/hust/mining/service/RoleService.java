package com.hust.mining.service;

import java.util.List;

import com.hust.mining.model.Power;
import com.hust.mining.model.Role;
import com.hust.mining.model.params.RoleQueryCondition;

public interface RoleService {
	List<Role> selectAllRole(int start, int limit);

	List<Role> selectRole();

	List<Role> selectOneRoleInfo(RoleQueryCondition role);

	boolean insertRoleInfo(String roleName);

	boolean deleteRoleInfoById(int roleId);

	boolean updateRoleInfo(Role role, List<String> powerName);

	boolean insertPowerOfRole(int roleId, List<String> powerName);

	boolean deletePowerOfRole(int roleId, List<String> powerName);

	List<Role> selectNotHaveRole(int roleId);
	
	long selectRoleCount(String roleName);

	List<Power> notIncludePowers(int roleId);

	List<Power> includePowers(int roleId);

}
