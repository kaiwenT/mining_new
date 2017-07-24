package com.hust.mining.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.mining.dao.UserRoleDao;
import com.hust.mining.model.UserRole;
import com.hust.mining.service.UserRoleService;

@Service
public class UserRoleServiceI implements UserRoleService {
	private static final Logger logger = LoggerFactory.getLogger(UserRoleServiceI.class);

	@Autowired
	private UserRoleDao userRoleDao;

	@Override
	public List<UserRole> selectUserRole() {
		List<UserRole> userRole = userRoleDao.selectAllUserRole();
		if (userRole.isEmpty()) {
			logger.info("userRole is empty");
			return userRole;
		}
		return userRole;
	}

}
