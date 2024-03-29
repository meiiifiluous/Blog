package com.vvc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vvc.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-27 14:16:12
 */
public interface RoleMapper extends BaseMapper<Role> {
    List<String> getRoleKeyByUserId(Long userId);
}
