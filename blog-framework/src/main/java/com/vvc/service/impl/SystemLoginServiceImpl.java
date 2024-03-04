package com.vvc.service.impl;

import com.vvc.constants.AppHttpCodeEnum;
import com.vvc.constants.ResponseResult;
import com.vvc.domain.entity.LoginUser;
import com.vvc.domain.entity.User;
import com.vvc.exception.SystemException;
import com.vvc.service.LoginService;
import com.vvc.utils.JwtUtil;
import com.vvc.utils.RedisCache;
import com.vvc.utils.SecurityUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Objects;
@Service
public class SystemLoginServiceImpl implements LoginService {
    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private RedisCache redisCache;
    @Override
    public ResponseResult login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new SystemException(AppHttpCodeEnum.LOGIN_ERROR);
        }
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String token = JwtUtil.createJWT(loginUser.getUser().getId().toString());
        redisCache.setCacheObject("login:"+loginUser.getUser().getId(),loginUser);
        HashMap<String, String> map = new HashMap<>();
        map.put("token",token);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        Long userId = SecurityUtils.getUserId();
        redisCache.deleteObject("login:"+userId);
        return ResponseResult.okResult();
    }
}
