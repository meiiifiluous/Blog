package com.vvc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.vvc.constants.ResponseResult;
import com.vvc.constants.SystemConstants;
import com.vvc.domain.entity.Role;
import com.vvc.service.RoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Resource
    private RoleService roleService;
    @GetMapping("/listAllRole")
    public ResponseResult getlistAllRole(){
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getStatus, SystemConstants.STATUS_NORMAL);
        List<Role> roles = roleService.list(queryWrapper);
        return ResponseResult.okResult(roles);
    }
}
