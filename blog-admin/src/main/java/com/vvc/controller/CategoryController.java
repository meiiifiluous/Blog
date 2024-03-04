package com.vvc.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.vvc.constants.AppHttpCodeEnum;
import com.vvc.constants.ResponseResult;
import com.vvc.domain.entity.Category;
import com.vvc.domain.entity.VO.CategoryInfoVo;
import com.vvc.domain.entity.VO.CategoryVo;
import com.vvc.domain.entity.VO.ExcelCategoryVo;
import com.vvc.service.CategoryService;
import com.vvc.utils.BeanCopyUtils;
import com.vvc.utils.WebUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        List<Category> categories = categoryService.list();
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }
    @GetMapping("/export")
    @PreAuthorize("@ps.hasPermission('content:category:export')")
    public void export(HttpServletResponse response){
        try {
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            List<Category> categories = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categories, ExcelCategoryVo.class);
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).sheet("分类导出").doWrite(excelCategoryVos);
        } catch (IOException e) {
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
    @GetMapping("/list")
    public ResponseResult getCategoryPage(Integer pageNum,Integer pageSize,Category category){
        return categoryService.getCategoryPage(pageNum,pageSize,category);
    }
    @PostMapping()
    public ResponseResult add(@RequestBody Category category){
        categoryService.save(category);
        return ResponseResult.okResult();
    }
    @GetMapping("/{id}")
    public ResponseResult getCategoryInfo(@PathVariable Long id){
        Category category = categoryService.getById(id);
        CategoryInfoVo categoryInfoVo = BeanCopyUtils.copyBean(category, CategoryInfoVo.class);
        return ResponseResult.okResult(categoryInfoVo);
    }
    @PutMapping
    public ResponseResult updateCategory(@RequestBody Category category){
        categoryService.updateById(category);
        return ResponseResult.okResult();
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteCategory(@PathVariable Long id){
        categoryService.removeById(id);
        return ResponseResult.okResult();
    }

}
