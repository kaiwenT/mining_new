package com.hust.mining.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.mining.dao.UserDao;
import com.hust.mining.model.User;
import com.hust.mining.service.PersonalService;
/**
 * 个人信息修改Service
 * @author tankai
 *
 */
@Service
public class PersonalServiceImpl implements PersonalService {
	private static final Logger logger = LoggerFactory.getLogger(PersonalServiceImpl.class);

    @Autowired
    private UserDao userDao;
    
   
    @Override
	public boolean updateUserInfo(User user){
    	int statue = userDao.updateByPrimaryKeySelective(user);
    	if (statue == 0) {
            logger.info("update UserInfo is error");
            return false;
        }
    	return true;
    }
}
