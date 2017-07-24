package com.hust.mining.controller;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hust.mining.constant.Constant.KEY;
import com.hust.mining.model.User;
import com.hust.mining.service.PersonalService;
import com.hust.mining.service.RedisService;
import com.hust.mining.service.UserService;
import com.hust.mining.util.ResultUtil;
import com.hust.mining.util.TimeUtil;

/**
 * 用户个人信息管理控制器
 * 包括信息查看和修改、密码修改等
 * @author tankai
 *
 */
@Controller
@RequestMapping("/personal")
public class PersonalController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
	@Autowired
	private UserService userService;
	@Autowired
	private PersonalService personalService;
	@Autowired
    private RedisService redisService;
	
	/**
	 * 获得当前登录用户的个人信息
	 * @param request
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value = "/getPersonalInfo", method = RequestMethod.GET)
    public Object getCurrentUser(HttpServletRequest request) {
		//System.out.println("getCurrentUserInfo");
        String username = userService.getCurrentUser(request);
        List<User> users = userService.selectSingleUserInfo(username, request);
        if (null == users || users.size() == 0) {
            return ResultUtil.errorWithMsg("sorry! user is not exist");
        }
        //System.out.println(users.get(0).getTrueName());
        return ResultUtil.success(users.get(0));
    }
	
	/**
	 * 修改当前登录用户个人信息
	 * @param userId
	 * @param newuserName
	 * @param trueName
	 * @param telphone
	 * @param email
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updatePersonalInfo", method = RequestMethod.POST)
	public Object updatePersonalInfo(@RequestParam(value = "userId", required = true) int userId,
//			@RequestParam(value = "userName", required = true) String newuserName,
            @RequestParam(value = "trueName", required = true) String trueName,
            @RequestParam(value = "telphone", required = true) String telphone,
            @RequestParam(value = "email", required = true) String email,HttpServletRequest request){
		String username = userService.getCurrentUser(request);
//		if(!username.equals(newuserName)){
//			List<User> users = userService.selectSingleUserInfo(newuserName, request);
//	        if (users.size() > 0) {
//	            return ResultUtil.errorWithMsg("抱歉！用户名"+newuserName+"已存在，请重新输入！");
//	        }
//		}
		User user = new User();
		user.setUserId(userId);
//		user.setUserName(newuserName);
		user.setPassword(null);
		user.setTelphone(telphone);
		user.setEmail(email);
		try {
	        user.setCreateDate(TimeUtil.getSystemDate());
        } catch (ParseException e) {
            logger.info("get systemdate is error ");
            e.printStackTrace();
        }
		boolean statue = personalService.updateUserInfo(user);
        if (statue == false) {
            return ResultUtil.errorWithMsg("抱歉！修改个人信息失败");
        }
//        redisService.del(KEY.USER_NAME, request);
//        redisService.setString(KEY.USER_NAME, newuserName, request);
		return ResultUtil.success("修改个人信息成功");
	}
	
	@ResponseBody
	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	public Object updatePassword(@RequestParam(value = "userId", required = true) int userId,
			@RequestParam(value = "oldpass", required = true) String oldpass,
            @RequestParam(value = "newpass", required = true) String newpass,HttpServletRequest request){
		String username = userService.getCurrentUser(request);
		if (!userService.login(username, oldpass)) {
			return ResultUtil.errorWithMsg("密码输入错误");
        }
        		
		User user = new User();	
		user.setUserId(userId);
		user.setPassword(newpass);
		try {
	        user.setCreateDate(TimeUtil.getSystemDate());
        } catch (ParseException e) {
            logger.info("get systemdate is error ");
            e.printStackTrace();
        }
		boolean statue = personalService.updateUserInfo(user);
        if (statue == false) {
            return ResultUtil.errorWithMsg("抱歉！修改密码失败");
        }
		return ResultUtil.success("修改密码成功");
	}
}
