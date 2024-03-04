package com.vvc.controller;

import com.vvc.annotation.SystemLog;
import com.vvc.constants.ResponseResult;
import com.vvc.domain.entity.User;
import com.vvc.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
@Api("用户")
public class UserController {
    @Resource
    private UserService userService;
    @GetMapping("/userInfo")
    public ResponseResult getUserInfo(){
        return userService.getUserInfo();
    }
    @SystemLog(businessName = "跟新用户信息")
    @PutMapping("/userInfo")
    public ResponseResult updateUserInfo(@RequestBody User user){
        return userService.updateUserInfo(user);
    }
    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
        return userService.register(user);
    }
}
