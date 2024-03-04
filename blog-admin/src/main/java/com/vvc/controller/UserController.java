package com.vvc.controller;

import com.vvc.constants.ResponseResult;
import com.vvc.domain.entity.User;
import com.vvc.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Resource
    private UserService userService;
    @GetMapping("/list")
    public ResponseResult getUserList(Integer pageNum, Integer pageSize, User user){
        return userService.getUserList(pageNum,pageSize,user);
    }
    @PostMapping
    public ResponseResult add(@RequestBody User user){
        return userService.add(user);
    }
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id){
        userService.removeById(id);
        return ResponseResult.okResult();
    }
    @GetMapping("/{id}")
    public ResponseResult getUserDetailWithRoles(@PathVariable Long id){
        return userService.getUserDetailWithRoles(id);
    }
    @PutMapping()
    public ResponseResult updateUser(@RequestBody User user){
        return userService.updateUserWithRoles(user);
    }
    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody User user){
        userService.updateById(user);
        return ResponseResult.okResult();
    }
}
