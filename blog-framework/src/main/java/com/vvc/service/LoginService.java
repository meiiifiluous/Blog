package com.vvc.service;

import com.vvc.constants.ResponseResult;
import com.vvc.domain.entity.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
