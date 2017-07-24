package com.hust.mining.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hust.mining.model.Role;
import com.hust.mining.model.User;
import com.hust.mining.model.UserRole;
import com.hust.mining.model.params.UserQueryCondition;
import com.hust.mining.service.RoleService;
import com.hust.mining.service.UserRoleService;
import com.hust.mining.service.UserService;
import com.hust.mining.util.ResultUtil;
import com.hust.mining.util.TimeUtil;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;

    /**
     * 获取所有用户的信息 显示信息为：用户编号、用户名、角色、邮箱、电话、真实姓名
     * 
     * @author zhang
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectAllUser", method = RequestMethod.GET)
    public Object selectAllUserInfo(@RequestParam(value = "start", required = true) int start,
            @RequestParam(value = "limit", required = true) int limit, HttpServletRequest request) {
        List<User> users = userService.selectAllUserInfo(start, limit, request);
        if (null == users || users.size() == 0) {
            return ResultUtil.errorWithMsg("select all user empty");
        }
        List<Role> roles = roleService.selectRole();
        if (null == roles || roles.size() == 0) {
            return ResultUtil.errorWithMsg("select all role empty");
        }
        List<UserRole> userRole = userRoleService.selectUserRole();
        if (userRole == null || userRole.size() == 0) {
            return ResultUtil.errorWithMsg("select userRole empty");
        }
        Map<Object, Object> map = new HashMap<>();
        map.put("user", users);
        map.put("role", roles);
        map.put("userRole", userRole);
        return ResultUtil.success(map);
    }

    /**
     * 查询用户：普通用户只能查询自己的个人信息、 管理员和超级管理员能查询所有人员的信息
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/selectOneUserInfo")
    public Object selectOneUserInfo(@RequestParam(value = "userName", required = true) String userName,
            HttpServletRequest request) {
        List<User> elements = userService.selectSingleUserInfo(userName, request);
        if (null == elements || elements.size() == 0) {
            return ResultUtil.errorWithMsg("user is  not exist");
        }
        return ResultUtil.success(elements);
    }

    /**
     * 删除用户：通过用户ID删除用户;前台传递到后台需要使用用户名
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping("/deleteUserInfoById")
    public Object deleteUserInfoById(@RequestParam(value = "userId", required = true) int userId,
            HttpServletRequest request) {
        boolean statue = userService.deleteUserInfoById(userId, request);
        if (statue == false) {
            return ResultUtil.errorWithMsg("delete users error");
        }
        return ResultUtil.success("delete user success");
    }
    
    /**
     * 设置算法和粒度选择
     * @param algorithm
     * @param granularity
     * @param request
     * @return
     */
    
    @ResponseBody
    @RequestMapping("/setAlgorithmAndGranularity")
    public Object setAlgorithmAndgranularity(@RequestParam(value = "algorithm", required = true) int algorithm,
    		@RequestParam(value = "granularity", required = true) int granularity,HttpServletRequest request) {
       
    	String username = userService.getCurrentUser(request);
    	List<User> users =userService.selectSingleUserInfo(username, request);
    	User user = users.get(0);
    	user.setAlgorithm(algorithm);
    	user.setGranularity(granularity);
    	boolean statue = userService.updateUser(user);
    	if (statue == false) {
    		return ResultUtil.errorWithMsg("update user error");
		}
    	return ResultUtil.success("update user success");
    }
    
    /**
     * 获取当前用户的算法选择和粒度选择，显示到页面
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/getAlgorithmAndGranularity")
    public Object getAlgorithmAndgranularity(HttpServletRequest request) {
       
    	String username = userService.getCurrentUser(request);
    	List<User> users =userService.selectSingleUserInfo(username, request);
    	User user = users.get(0);
    	return ResultUtil.success(user);
    }


    /**
     * 
     * @param user
     * @param roleName
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateUserInfo")
    public Object updateUseInfo(@RequestParam(value = "userId", required = true) int userId,
            @RequestParam(value = "userName", required = true) String userName,
            @RequestParam(value = "trueName", required = true) String trueName,
            @RequestParam(value = "password", required = true) String password,
            @RequestParam(value = "telphone", required = true) String telphone,
            @RequestParam(value = "email", required = true) String email,
            @RequestParam(value = "roleName", required = true) List<String> roleName, HttpServletRequest request) {
        User user = new User();
        user.setUserId(userId);
        user.setUserName(userName);
        user.setTrueName(trueName);
        user.setPassword(password);
        user.setTelphone(telphone);
        user.setEmail(email);
        try {
            user.setCreateDate(TimeUtil.getSystemDate());
        } catch (ParseException e) {
            logger.info("get systemdate is error ");
            e.printStackTrace();
        }
        boolean statue = userService.updateUserInfo(user, roleName);
        if (statue == false) {
            return ResultUtil.errorWithMsg("update user error ");
        }
        return ResultUtil.success("update user success");
    }

    /**
     * 添加用户信息：只能添加用户信息。不需要为用户设置角色
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/insertUserInfo")
    public Object insertUserInfo(@RequestParam(value = "userName", required = true) String userName,
            @RequestParam(value = "trueName", required = true) String trueName,
            @RequestParam(value = "password", required = true) String password,
            @RequestParam(value = "telphone", required = true) String telphone,
            @RequestParam(value = "email", required = true) String email,
            @RequestParam(value = "roleName", required = true) List<String> roleName, HttpServletRequest request) {
        User user = new User();
        user.setUserName(userName);
        user.setEmail(email);
        user.setTrueName(trueName);
        user.setPassword(password);
        user.setTelphone(telphone);
        try {
            user.setCreateDate(TimeUtil.getSystemDate());
        } catch (ParseException e) {
            logger.info("get systemdate is error ");
            e.printStackTrace();
        }
        boolean statue = userService.insertUserInfo(user, roleName);
        if (statue == false) {
            return ResultUtil.errorWithMsg("insert userinfo error ");
        }
        return ResultUtil.success("insert user success");
    }

    @ResponseBody
    @RequestMapping("/count")
    public Object count() {
        long numbers = userService.countOfUser();
        return numbers;
    }

    @ResponseBody
    @RequestMapping(value = "/getUserInfoByPageLimit", method = RequestMethod.POST)
    public Object getUserInfoByPageLimit(@RequestParam(value = "userName", required = true) String userName,
            @RequestParam(value = "email", required = true) String email,
            @RequestParam(value = "telphone", required = true) String telphone,
            @RequestParam(value = "trueName", required = true) String trueName,
            @RequestParam(value = "roleName", required = true) String roleName,
            @RequestParam(value = "page", required = true) int page,
            @RequestParam(value = "row", required = true) int row, HttpServletRequest request) {
        UserQueryCondition userQueryCondition = new UserQueryCondition();
        userQueryCondition.setUserName(userName);
        userQueryCondition.setEmail(email);
        userQueryCondition.setTelphone(telphone);
        userQueryCondition.setTrueName(trueName);
        userQueryCondition.setRoleName(roleName);
        userQueryCondition.setPage(page);
        userQueryCondition.setRow(row);
        List<User> userInfo = userService.selectUserByPageLimit(userQueryCondition);
        List<Role> roles = roleService.selectRole();
        if (null == roles || roles.size() == 0) {
            return ResultUtil.errorWithMsg("select all role empty");
        }
        List<UserRole> userRole = userRoleService.selectUserRole();
        if (userRole.isEmpty() || userRole.size() == 0) {
            return ResultUtil.errorWithMsg("select userRole empty");
        }
        if (null == userInfo || userInfo.size() == 0) {
            return ResultUtil.errorWithMsg("select userInfo empty");
        }
        Map<Object, Object> map = new HashMap<>();
        map.put("user", userInfo);
        map.put("role", roles);
        map.put("userRole", userRole);
        return ResultUtil.success(map);
    }
    
    @ResponseBody
    @RequestMapping(value = "/selectUserInfoCount", method = RequestMethod.POST)
    public Object selectUserInfoCount(@RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "telphone", required = false) String telphone,
            @RequestParam(value = "trueName", required = false) String trueName,
            @RequestParam(value = "roleName", required = false) String roleName,
            HttpServletRequest request) {
    /*	if(null == userName && null == email && null == telphone && null == trueName && null == roleName){
    		
    	}else{*/
        UserQueryCondition userQueryCondition = new UserQueryCondition();
        userQueryCondition.setUserName(userName);
        userQueryCondition.setEmail(email);
        userQueryCondition.setTelphone(telphone);
        userQueryCondition.setTrueName(trueName);
        userQueryCondition.setRoleName(roleName);
        long count = 0;
        if(null == roleName){
        	count = userService.selectUserByPageLimit(userQueryCondition).size();
        }else{
        	count = userService.selectUserCount(userQueryCondition);
        }
        if(count<=0){
        	return ResultUtil.errorWithMsg("无用户信息！");
        }
        return ResultUtil.success(count);
    }
    
}

