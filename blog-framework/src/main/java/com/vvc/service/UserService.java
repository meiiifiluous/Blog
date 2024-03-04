package com.vvc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vvc.constants.ResponseResult;
import com.vvc.domain.entity.User;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-02-19 13:32:57
 */
public interface UserService extends IService<User> {

    ResponseResult getUserInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult getUserList(Integer pageNum, Integer pageSize, User user);

    ResponseResult add(User user);

    ResponseResult getUserDetailWithRoles(Long id);

    ResponseResult updateUserWithRoles(User user);
}
