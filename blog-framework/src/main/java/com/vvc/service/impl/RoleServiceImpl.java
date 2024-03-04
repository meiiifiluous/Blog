package com.vvc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vvc.domain.entity.Role;
import com.vvc.mapper.RoleMapper;
import com.vvc.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-02-27 14:16:13
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public List<String> getRoleKeyByUserId(Long userId) {
        if(userId.equals(1L)){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        return getBaseMapper().getRoleKeyByUserId(userId);
    }
}

