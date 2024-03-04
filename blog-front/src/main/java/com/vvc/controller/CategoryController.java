package com.vvc.controller;

import com.vvc.constants.ResponseResult;
import com.vvc.service.impl.CategoryServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
@Api(tags = "文章分类",consumes = "文章相关接口")
public class CategoryController {
    @Autowired
    private CategoryServiceImpl categoryService;
    @GetMapping("/getCategoryList")
    public ResponseResult getCategoryList(){
        return categoryService.getCategoryList();
    }
}
