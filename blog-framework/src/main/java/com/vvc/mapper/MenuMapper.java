package com.vvc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vvc.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-27 14:16:25
 */
public interface MenuMapper extends BaseMapper<Menu> {
    List<String> selectPermsByUserId(Long userId);

    List<Menu> selectAllRouterMenu();

    List<Menu> selectAllRouterMenuByUserId(Long userId);
}
