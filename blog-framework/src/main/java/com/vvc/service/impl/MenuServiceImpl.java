package com.vvc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vvc.constants.ResponseResult;
import com.vvc.constants.SystemConstants;
import com.vvc.domain.entity.Menu;
import com.vvc.domain.entity.VO.MenuVo;
import com.vvc.mapper.MenuMapper;
import com.vvc.service.MenuService;
import com.vvc.utils.BeanCopyUtils;
import com.vvc.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-02-27 14:16:25
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<String> getPermsByUserId(Long userId) {
        //如果是管理员返回所有权限
        if(userId.equals(1L)){
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType, SystemConstants.MENU,SystemConstants.BUTTON);
            queryWrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(queryWrapper);
            List<String> perms = menus.stream().map(menu -> menu.getPerms()).collect(Collectors.toList());
            return perms;
        }else return getBaseMapper().selectPermsByUserId(userId);
    }

    @Override
    public List<Menu> getRouters(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        if(SecurityUtils.isAdmin()){
            //查询所有的menus
            menus = menuMapper.selectAllRouterMenu();
        }
        else {
            menus = menuMapper.selectAllRouterMenuByUserId(userId);
        }
        //构建tree
        List<Menu> menuTree = builderMenuTree(menus,0L);
        return menuTree;
    }

    @Override
    public ResponseResult getMenuList(Menu menu) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(!Objects.isNull(menu.getStatus()),Menu::getStatus,menu.getStatus());
        queryWrapper.like(StringUtils.hasText(menu.getMenuName()),Menu::getMenuName,menu.getMenuName());
        queryWrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        List<Menu> menus = list(queryWrapper);
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }

    private List<Menu> builderMenuTree(List<Menu> menus, long parentId) {
        List<Menu> menuTree = menus.stream().filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menus,menu.getId())))
                .collect(Collectors.toList());
        return menuTree;
    }

    private List<Menu> getChildren(List<Menu> menus, Long parentId) {
        List<Menu> childrenList = menus.stream().filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menus, menu.getId())))
                .collect(Collectors.toList());
        return childrenList;
    }
}

