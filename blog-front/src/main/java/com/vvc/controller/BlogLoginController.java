package com.vvc.controller;

import com.vvc.constants.AppHttpCodeEnum;
import com.vvc.constants.ResponseResult;
import com.vvc.domain.entity.User;
import com.vvc.exception.SystemException;
import com.vvc.service.BlogLoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.Objects;

@RestController
@Api(tags = "博客登录",consumes = "博客登录相关接口")
public class BlogLoginController {
    @Resource
    private BlogLoginService blogLoginService;

    @PostMapping("/login")
    @ApiOperation(value = "前端登录接口")
    @ApiImplicitParam(name = "user" , value ="用户账号和密码" )
    public ResponseResult login(@RequestBody User user){
        if(Objects.isNull(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
       return blogLoginService.login(user);
    }
    @PostMapping("/logout")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }
}
