package com.vvc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vvc.constants.ResponseResult;
import com.vvc.domain.entity.Category;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2023-02-13 23:53:08
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();


    ResponseResult getCategoryPage(Integer pageNum, Integer pageSize, Category category);
}
