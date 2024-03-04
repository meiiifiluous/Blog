package com.vvc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vvc.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-02-27 14:16:12
 */
public interface RoleService extends IService<Role> {

    List<String> getRoleKeyByUserId(Long userId);
}
