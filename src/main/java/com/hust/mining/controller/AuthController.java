package com.hust.mining.controller;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hust.mining.constant.Constant.KEY;
import com.hust.mining.model.User;
import com.hust.mining.service.RedisService;
import com.hust.mining.service.UserService;
import com.hust.mining.util.ResultUtil;

@Controller
@RequestMapping("/")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object login(@RequestParam(value = "username", required = true) String userName,
            @RequestParam(value = "password", required = true) String passwd, HttpServletResponse response,
            HttpServletRequest request) {
        // request.getSession().setAttribute(KEY.USER_NAME, userName);
        // return "redirect:page/infoManager.html";
        if (userService.login(userName, passwd)) {
            redisService.setString(KEY.USER_NAME, userName, request);
            return ResultUtil.successWithoutMsg();
        }
        return ResultUtil.errorWithMsg("用户名密码错误");
    }

    @ResponseBody
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Object logout(HttpServletRequest request) {
        redisService.del(KEY.USER_NAME, request);
        return ResultUtil.successWithoutMsg();
    }

    @ResponseBody
    @RequestMapping(value = "/getCurrentUser", method = RequestMethod.POST)
    public Object getCurrentUser(HttpServletRequest request) {
        String username = userService.getCurrentUser(request);
        List<User> user = userService.selectSingleUserInfo(username, request);
        return ResultUtil.success(user.get(0).getTrueName());
    }
    
    /**
     * 请求得到当前用户的ID
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getCurrentUserId", method = RequestMethod.POST)
    public Object getCurrentUserId(HttpServletRequest request) {
        String username = userService.getCurrentUser(request);
        List<User> user = userService.selectSingleUserInfo(username, request);
        return ResultUtil.success(user.get(0).getUserId());
    }
}
