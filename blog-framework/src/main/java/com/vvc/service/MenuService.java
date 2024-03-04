package com.vvc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vvc.constants.ResponseResult;
import com.vvc.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-02-27 14:16:25
 */
public interface MenuService extends IService<Menu> {
    List<String> getPermsByUserId(Long userId);

    List<Menu> getRouters(Long userId);

    ResponseResult getMenuList(Menu menu);
}
