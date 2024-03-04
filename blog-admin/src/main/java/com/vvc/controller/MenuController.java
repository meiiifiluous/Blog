package com.vvc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.vvc.constants.AppHttpCodeEnum;
import com.vvc.constants.ResponseResult;
import com.vvc.domain.entity.Menu;
import com.vvc.domain.entity.VO.getMenuVo;
import com.vvc.exception.SystemException;
import com.vvc.service.MenuService;
import com.vvc.utils.BeanCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Resource
    private MenuService menuService;
    @GetMapping("/list")
    public ResponseResult getMenuList(Menu menu){
       return menuService.getMenuList(menu);
    }
    @PostMapping
    public ResponseResult add(@RequestBody Menu menu){
        if(Objects.isNull(menu)){
           throw new SystemException(AppHttpCodeEnum.COMMEN_NOT_NULL);
        }
        menuService.save(menu);
        return ResponseResult.okResult();
    }
    @GetMapping("/{id}")
    public ResponseResult getMenu(@PathVariable Long id){
        Menu menu = menuService.getById(id);
        getMenuVo menuVo = BeanCopyUtils.copyBean(menu,getMenuVo.class);
        return ResponseResult.okResult(menuVo);
    }
    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu){
        if(menu.getParentId().equals(menu.getId())){
            return ResponseResult.errorResult(500,"修改菜单'写博文'失败，上级菜单不能选择自己");
        }
        menuService.updateById(menu);
        return ResponseResult.okResult();
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteMenu(@PathVariable Long id){
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.eq(Menu::getParentId,id);
        if(menuService.count(menuLambdaQueryWrapper)>0){
            return ResponseResult.errorResult(500,"存在子菜单不允许删除");
        }
        menuService.removeById(id);
        return ResponseResult.okResult();
    }
}
