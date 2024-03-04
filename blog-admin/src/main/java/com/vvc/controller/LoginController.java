package com.vvc.controller;

import com.vvc.constants.AppHttpCodeEnum;
import com.vvc.constants.ResponseResult;
import com.vvc.domain.entity.Menu;
import com.vvc.domain.entity.User;
import com.vvc.domain.entity.VO.AdminUserInfoVo;
import com.vvc.domain.entity.VO.RoutersVo;
import com.vvc.domain.entity.VO.UserInfoVo;
import com.vvc.exception.SystemException;
import com.vvc.service.LoginService;
import com.vvc.service.MenuService;
import com.vvc.service.RoleService;
import com.vvc.utils.BeanCopyUtils;
import com.vvc.utils.SecurityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
@RestController
@RequestMapping
public class LoginController {
    @Resource
    private MenuService menuService;
    @Resource
    private RoleService roleService;
    @Resource
    private LoginService loginService;
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            //提示 必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    @GetMapping("/getInfo")
    public ResponseResult getInfo(){
        //获取当前id
        Long userId = SecurityUtils.getUserId();
        //根据id去查询menu表中的perms
        List<String> permissions = menuService.getPermsByUserId(userId);
        //根据id去查询role表中的角色信息
        List<String> roles = roleService.getRoleKeyByUserId(userId);
        User loginUser = SecurityUtils.getLoginUser().getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser, UserInfoVo.class);
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(permissions,roles,userInfoVo);
        return ResponseResult.okResult(adminUserInfoVo);
    }

    @GetMapping("/getRouters")
    public ResponseResult getRouters(){
        Long userId = SecurityUtils.getUserId();
        List<Menu> menus =  menuService.getRouters(userId);
        return ResponseResult.okResult(new RoutersVo(menus));
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
}
