package com.hust.mining.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hust.mining.dao.mapper.UserMapper;
import com.hust.mining.model.User;
import com.hust.mining.model.UserExample;
import com.hust.mining.model.UserExample.Criteria;
import com.hust.mining.model.params.UserQueryCondition;
@Repository
public class UserDao {

	@Autowired
	private UserMapper userMapper;

	public List<User> selectAllUserInfo() {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdIsNotNull();
		List<User> users = userMapper.selectByExample(example);
		return users;
	}

	public List<User> selectAllUser(int start, int limit) {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdIsNotNull();
		example.setRow(start);
		example.setPage(limit);
		List<User> users = userMapper.selectByExample(example);
		return users;
	}

	public long selectUserCount(UserQueryCondition userQueryCondition){
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		if (!StringUtils.isBlank(userQueryCondition.getUserName())) {
			criteria.andUserNameLike("%" +userQueryCondition.getUserName() +"%");
		}
		if (!StringUtils.isBlank(userQueryCondition.getEmail())) {
			criteria.andEmailLike("%" +userQueryCondition.getEmail() +"%");
		}
		if (!StringUtils.isBlank(userQueryCondition.getTelphone())) {
			criteria.andTelphoneLike("%" +userQueryCondition.getTelphone() +"%");
		}
		if (!StringUtils.isBlank(userQueryCondition.getTrueName())) {
			criteria.andTrueNameLike("%" +userQueryCondition.getTrueName() +"%");
		}
		if (userQueryCondition.getPage() != 0) {
			example.setPage(userQueryCondition.getPage());
		}
		if (userQueryCondition.getRow() != 0) {
			example.setRow(userQueryCondition.getRow());
		}
		return userMapper.countByExample(example);
	}
	
	public List<User> selectByUserName(String userName) {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserNameEqualTo(userName);
		List<User> users = userMapper.selectByExample(example);
		return users;
	}

	public List<User> selectBynotIncluedUserName(String userName) {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserNameNotEqualTo(userName);
		List<User> users = userMapper.selectByExample(example);
		return users;
	}

	public List<User> selectByLikeUserName(String userName) {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserNameLike("%" + userName + "%");
		List<User> users = userMapper.selectByExample(example);
		return users;
	}

	public List<User> checkLogin(String userName, String password) {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserNameEqualTo(userName);
		criteria.andPasswordEqualTo(password);
		List<User> users = userMapper.selectByExample(example);
		return users;
	}

	public long selectCountOfUser() {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdIsNotNull();
		long count = userMapper.countByExample(example);
		return count;
	}

	public int updateByPrimaryKeySelective(User record) {
		return userMapper.updateByPrimaryKeySelective(record);
	}

	public long countByExample(UserExample example) {
		return userMapper.countByExample(example);
	}

	public int deleteByExample(UserExample example) {
		return userMapper.deleteByExample(example);
	}

	public int deleteByPrimaryKey(Integer userId) {
		return userMapper.deleteByPrimaryKey(userId);
	}

	public int insert(User record) {
		return userMapper.insert(record);
	}

	public int insertSelective(User record) {
		return userMapper.insertSelective(record);
	}

	public List<User> selectByExample(UserQueryCondition userQueryCondition) {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		//模糊查询
		if (!StringUtils.isBlank(userQueryCondition.getUserName())) {
			criteria.andUserNameLike("%" +userQueryCondition.getUserName() +"%");
		}
		if (!StringUtils.isBlank(userQueryCondition.getEmail())) {
			criteria.andEmailLike("%" +userQueryCondition.getEmail() +"%");
		}
		if (!StringUtils.isBlank(userQueryCondition.getTelphone())) {
			criteria.andTelphoneLike("%" +userQueryCondition.getTelphone() +"%");
		}
		if (!StringUtils.isBlank(userQueryCondition.getTrueName())) {
			criteria.andTrueNameLike("%" +userQueryCondition.getTrueName() +"%");
		}
		if (userQueryCondition.getPage() != 0) {
			example.setPage(userQueryCondition.getPage());
		}
		if (userQueryCondition.getRow() != 0) {
			example.setRow(userQueryCondition.getRow());
		}
		List<User> users = userMapper.selectByExample(example);
		return users;
	}

	public User selectByPrimaryKey(Integer userId) {
		return userMapper.selectByPrimaryKey(userId);
	}

	public int updateByExampleSelective(User record, UserExample example) {
		return userMapper.updateByExample(record, example);
	}

	public int updateByExample(User record, UserExample example) {
		return userMapper.updateByExample(record, example);
	}

	public int updateByPrimaryKey(User record) {
		return userMapper.updateByPrimaryKey(record);
	}

}
