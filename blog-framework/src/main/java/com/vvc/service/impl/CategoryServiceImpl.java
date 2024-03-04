package com.vvc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vvc.constants.ResponseResult;
import com.vvc.constants.SystemConstants;
import com.vvc.domain.entity.Article;
import com.vvc.domain.entity.Category;
import com.vvc.domain.entity.VO.CategoryVo;
import com.vvc.domain.entity.VO.PageVo;
import com.vvc.mapper.CategoryMapper;
import com.vvc.service.CategoryService;
import com.vvc.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2023-02-13 23:53:09
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private ArticleServiceImpl articleService;
    @Override
    public ResponseResult getCategoryList() {
        //查询文章表状态为已发布
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(queryWrapper);
        //获取文章的分类id，并且去重
        Set<Long> categoryIds = articleList.stream().
                map(article -> article.getCategoryId()).collect(Collectors.toSet());
        //查询分类表 分类状态时正常的
        List<Category> categories = listByIds(categoryIds);
        categories.stream()
                        .filter(category -> category.getStatus().equals(SystemConstants.STATUS_NORMAL))
                                .collect(Collectors.toList());
        //封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);

    }

    @Override
    public ResponseResult getCategoryPage(Integer pageNum,Integer pageSize,Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!Objects.isNull(category.getName()),Category::getName,category.getName());
        queryWrapper.like(!Objects.isNull(category.getStatus()),Category::getStatus,category.getStatus());
        Page<Category> categoryPage = new Page<>(pageNum, pageSize);
        page(categoryPage,queryWrapper);
        PageVo pageVo = new PageVo(categoryPage.getRecords(), categoryPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }
}

