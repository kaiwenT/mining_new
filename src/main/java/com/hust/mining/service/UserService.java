package com.hust.mining.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hust.mining.model.User;
import com.hust.mining.model.params.UserQueryCondition;

public interface UserService {

    List<User> selectAllUserInfo(int start, int limit, HttpServletRequest request);

    List<User> selectSingleUserInfo(String userName, HttpServletRequest request);

    boolean deleteUserInfoById(int userId, HttpServletRequest request);

    boolean updateUserInfo(User user, List<String> roleName);

    boolean insertUserInfo(User user, List<String> roleName);

    boolean login(String userName, String password);

    void logout(HttpServletRequest request);
    
    boolean updateUser(User user);

    List<String> selectUserPowerUrl(String userName);
    
    long selectUserCount(UserQueryCondition userQueryCondition);

    long countOfUser();

    List<User> selectUserByPageLimit(UserQueryCondition userQueryCondition);

    String getCurrentUser(HttpServletRequest request);
}
