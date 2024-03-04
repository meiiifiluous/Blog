package com.vvc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vvc.domain.entity.UserRole;
import com.vvc.mapper.UserRoleMapper;
import com.vvc.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2023-03-01 11:52:24
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}

