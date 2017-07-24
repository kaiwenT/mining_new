package com.hust.mining.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.mining.dao.PowerDao;
import com.hust.mining.dao.RoleDao;
import com.hust.mining.dao.RolePowerDao;
import com.hust.mining.dao.UserRoleDao;
import com.hust.mining.model.Power;
import com.hust.mining.model.Role;
import com.hust.mining.model.RolePower;
import com.hust.mining.model.UserRole;
import com.hust.mining.model.params.RoleQueryCondition;
import com.hust.mining.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private PowerDao powerDao;
	@Autowired
	private RolePowerDao rolePowerDao;
	@Autowired
	private UserRoleDao userRoleDao;

	@Override
	public List<Role> selectAllRole(int start, int limit) {
		List<Role> roles = roleDao.selectRoles(start, limit);
		if (roles.isEmpty()) {
			logger.info("role  is empty");
		}
		return roles;
	}

	@Override
	public List<Role> selectOneRoleInfo(RoleQueryCondition role) {
		// 角色名必须是表已经存在的角色名
		List<Role> roles = roleDao.selectByLikeRoleName(role);
		if (roles.isEmpty()) {
			logger.info("roleName is not exist");
		}
		return roles;
	}

	@Override
	public List<Role> selectRole() {
		List<Role> roles = roleDao.selectRole();
		if (roles.isEmpty()) {
			logger.info("role  is empty");
		}
		return roles;
	}

	@Override
	public boolean insertRoleInfo(String roleName) {
		// 添加新的角色信息，信息不能重复
		List<Role> roles = roleDao.selectRoleByName(roleName);
		if (!roles.isEmpty()) {
			logger.info("role table have been roelinfo");
			return false;
		}
		// 状态值
		int statue = roleDao.insertRole(roleName);
		if (statue == 0) {
			logger.info("insert role error");
			return false;
		}
		return true;
	}

	@Override
	public boolean deleteRoleInfoById(int roleId) {
		// 首先去查找一下是否有这个角色所属的信息，有的话那就删除没有的话就不需要删除
		List<UserRole> userDao = userRoleDao.selectUserRoleByRoleId(roleId);
		if (!userDao.isEmpty()) {
			int statue = userRoleDao.deleteUserRoleByRoleId(roleId);
			if (statue == 0) {
				logger.info("delete userRole is error");
				return false;
			}
		}
		List<RolePower> rolePowers = rolePowerDao.selectRolePowerByRoleId(roleId);
		if (!rolePowers.isEmpty()) {
			int rolePowerStatus = rolePowerDao.deleteRolePowerByRoleId(roleId);
			if (rolePowerStatus == 0) {
				logger.info("delete rolepower about roleidinfo is error");
				return false;
			}
		}
		List<Role> role = roleDao.selectRoleById(roleId);
		if (!role.isEmpty()) {
			int roleStatus = roleDao.deleteByPrimaryKey(roleId);
			if (roleStatus == 0) {
				logger.info("delete role is error");
				return false;
			}
		}
		return true;
	}

	/**
	 * 更新角色信息，包括角色名，以及为角色分配的权限
	 */
	@Override
	public boolean updateRoleInfo(Role role, List<String> powerName) {
		//更新角色名，如果修改的角色名已存在数据库则返回
		List<Role> oldRole = roleDao.selectRoleById(role.getRoleId());
		List<Role> otherRole = roleDao.selectByNotIncluedRoleName(oldRole.get(0).getRoleName());
		for (Role roleInfo : otherRole) {
			if (roleInfo.getRoleName().equals(role.getRoleName())) {
				logger.info("update roleName has exist");
				return false;
			}
		}
		int statue = roleDao.updateByPrimaryKeySelective(role);
		if (statue == 0) {
			return false;
		}
		//删除角色对应的所有权限
		rolePowerDao.deleteRolePowerByRoleId(role.getRoleId());
		List<Integer> newPowerIds = new ArrayList<Integer>();
		//查找权限名对应的权限ID集合
		if (!powerName.isEmpty()) {
			for (String powerNameInfo : powerName) {
				//System.out.println(powerNameInfo);
				List<Power> powers = powerDao.selectPowerByPowerName(powerNameInfo);
				if(powers.isEmpty()){
					continue;
				}
				newPowerIds.add(powers.get(0).getPowerId());
			}
		} else {
			logger.info("power update is empty");
		}
		RolePower rolep = new RolePower();
		for(Integer powerId : newPowerIds){			
			rolep.setRoleId(role.getRoleId());
			rolep.setPowerId(powerId);
			//插入角色权限
			statue = rolePowerDao.insertSelective(rolep);
			if (statue == 0) {
				return false;
			}
		}
//		List<RolePower> rolePowers = rolePowerDao.selectRolePowerByRoleId(role.getRoleId());
//		List<Integer> oldPowers = new ArrayList<Integer>();
//		for (RolePower powers : rolePowers) {
//			oldPowers.add(powers.getPowerId());
//		}
//		List<Integer> newPowerInfo = new ArrayList<Integer>();
//		if (!powerName.isEmpty()) {
//			for (String powerNameInfo : powerName) {
//				List<Power> powers = powerDao.selectPowerByPowerName(powerNameInfo);
//				newPowerInfo.add(powers.get(0).getPowerId());
//			}
//		} else {
//			logger.info("power update is empty");
//		}
//		List<Integer> includePowers = new ArrayList<Integer>();
//		List<Integer> notIncludePowers = new ArrayList<Integer>();
//		if (!newPowerInfo.isEmpty()) {
//			for (Integer powerInfo : newPowerInfo) {
//				if (oldPowers.contains(powerInfo)) {
//					includePowers.add(powerInfo);
//				} else {
//					notIncludePowers.add(powerInfo);
//				}
//			}
//		}
//		if (includePowers.size() < oldPowers.size()) {
//			for (int power : oldPowers) {
//				if (!includePowers.contains(power)) {
//					rolePowerDao.deleteRolePowerById(power, role.getRoleId());
//				}
//			}
//		}
//		if (!notIncludePowers.isEmpty()) {
//			for (int powers : notIncludePowers) {
//				RolePower rolePower = new RolePower();
//				rolePower.setPowerId(powers);
//				rolePower.setRoleId(role.getRoleId());
//				rolePowerDao.insertSelective(rolePower);
//			}
//		}
		
		return true;
	}

	/**
	 * 为角色添加权限 是从角色不具有的权限中添加
	 */
	@Override
	public boolean insertPowerOfRole(int roleId, List<String> powerName) {

		List<Integer> powerId = new ArrayList<>();
		for (String powerNameIn : powerName) {
			List<Power> power = powerDao.selectPowerByPowerName(powerNameIn);
			powerId.add(power.get(0).getPowerId());
		}
		for (int powerIdInfo : powerId) {
			RolePower rolePower = new RolePower();
			rolePower.setPowerId(powerIdInfo);
			rolePower.setRoleId(roleId);
			int statue = rolePowerDao.insert(rolePower);
			if (statue == 0) {
				logger.info("insert power error");
				return false;
			}
		}
		return true;
	}

	/**
	 * 从角色中删除某些权限， 是根据角色中包含的权限进行删除
	 */
	@Override
	public boolean deletePowerOfRole(int roleId, List<String> powerName) {
		List<Integer> powerIds = new ArrayList<>();
		for (String powerNameIn : powerName) {
			List<Power> power = powerDao.selectPowerByPowerName(powerNameIn);
			powerIds.add(power.get(0).getPowerId());
		}
		for (int powerId : powerIds) {
			int statue = rolePowerDao.deleteRolePowerById(powerId, roleId);
			if (statue == 0) {
				logger.info("delete power error");
				return false;
			}
		}
		return true;
	}

	/**
	 * 为用户添加角色时，要先查询用户不具有的角色新
	 */
	@Override
	public List<Role> selectNotHaveRole(int roleId) {
		List<Role> roles = roleDao.selectRole();
		List<Role> role = new ArrayList<>();
		for (Role roleInfo : roles) {
			if (roleInfo.getRoleId() != roleId) {
				role.add(roleInfo);
			}
		}
		return role;
	}

	/**
	 * 角色不包含的权限信息
	 */
	@Override
	public List<Power> notIncludePowers(int roleId) {
		// 在角色权限表中 把权限ID取出来 。然后再得到权限名字
		List<Integer> powerIds = new ArrayList<>();
		List<RolePower> rolePowers = rolePowerDao.selectRolePowerByRoleId(roleId);
		for (RolePower rolePowerInfo : rolePowers) {
			powerIds.add(rolePowerInfo.getPowerId());
		}
		List<Power> notIncludePower = new ArrayList<>();
		List<Power> powers = powerDao.selectAllPowers();
		for (Power powerInfo : powers) {
//			for (int powerId : powerIds) {
//				if (powerInfo.getPowerId() != powerId) {
//					notIncludePower.add(powerInfo);
//				}
//			}
			if(!powerIds.contains(powerInfo.getPowerId())) {
				notIncludePower.add(powerInfo);
			}
		}
		
		return notIncludePower;
	}

	/**
	 * 角色包含的权限
	 */
	@Override
	public List<Power> includePowers(int roleId) {
		List<Integer> powerIds = new ArrayList<>();
		List<RolePower> rolePowers = rolePowerDao.selectRolePowerByRoleId(roleId);
		for (RolePower rolePowerInfo : rolePowers) {
			powerIds.add(rolePowerInfo.getPowerId());
		}
		List<Power> includePower = new ArrayList<>();
		List<Power> powers = powerDao.selectAllPowers();
		for (Power powerInfo : powers) {
			for (int powerId : powerIds) {
				if (powerInfo.getPowerId() == powerId) {
					includePower.add(powerInfo);
				}
			}
		}
		return includePower;
	}

	@Override
	public long selectRoleCount(String roleName) {
		// TODO Auto-generated method stub
		RoleQueryCondition role = new RoleQueryCondition();
		role.setRoleName(roleName);
		return roleDao.selectRoleCount(role);
	}
}