package com.vvc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vvc.constants.ResponseResult;
import com.vvc.domain.entity.Article;
import com.vvc.domain.entity.ArticleTag;
import com.vvc.domain.entity.VO.ArticleDetailVo;
import com.vvc.domain.entity.VO.ArticleListVo;
import com.vvc.domain.entity.VO.HotArticleVo;
import com.vvc.domain.entity.VO.PageVo;
import com.vvc.mapper.ArticleMapper;
import com.vvc.service.ArticleService;
import com.vvc.utils.BeanCopyUtils;
import com.vvc.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.vvc.constants.SystemConstants.ARTICLE_STATUS_NORMAL;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Resource
    private RedisCache redisCache;
   @Autowired
   private CategoryServiceImpl categoryService;
    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //发布的文章
        queryWrapper.eq(Article::getStatus,ARTICLE_STATUS_NORMAL );
        //按照浏览量进行降序排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多只查询十条
        Page<Article> page = new Page<>(1,10);
        //调用的是service的page方法
        page(page,queryWrapper);
        List<Article> articles = page.getRecords();
        //bean拷贝
        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);

        return ResponseResult.okResult(hotArticleVos);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //如果有categoryId要求查询该类型文章 ，如果没有则不需要
        queryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,Article::getCategoryId,categoryId);
        //状态是发布的
        queryWrapper.eq(Article::getStatus, ARTICLE_STATUS_NORMAL);
        //对isTop进行排序（降序），实现置顶
        queryWrapper.orderByDesc(Article::getIsTop);
        //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page, queryWrapper);
        //连表查询分类名称
        List<Article> articles = page.getRecords();
        articles.stream()
                .map(article ->article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());
        //拷贝vo
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);
        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        Article article = getById(id);
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(Long.valueOf(viewCount));
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根據分類id查詢分類名稱
        Long categoryId = article.getCategoryId();
        String categoryName = categoryService.getById(categoryId).getName();
        if (categoryName!=null){
            articleDetailVo.setCategoryName(categoryName);
        }
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(String key, String hKey, int v) {
        redisCache.incrementCacheMapValue(key,hKey,v);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getArticleList(Integer pageNum, Integer pageSize, Article article) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(article.getTitle()),Article::getTitle,article.getTitle());
        queryWrapper.like(StringUtils.hasText(article.getSummary()),Article::getSummary,article.getSummary());
        Page<Article> articlePage = new Page<>(pageNum,pageSize);
        page(articlePage,queryWrapper);
        PageVo pageVo = new PageVo(articlePage.getRecords(), articlePage.getTotal());
        return ResponseResult.okResult(pageVo);
    }
    @Resource
    private ArticleTagServiceImpl articleTagService;
    @Override
    public ResponseResult getArticle(Long id) {
        Article article = getById(id);
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,id);
        List<ArticleTag> articleTags = articleTagService.list(queryWrapper);
        List<String> tags = articleTags.stream().map(articleTag -> articleTag.getTagId().toString()).collect(Collectors.toList());
        article.setTags(tags);
        return ResponseResult.okResult(article);
    }

    @Override
    public ResponseResult updateArticle(Article article) {
        updateById(article);
        updateTags(article);
        return ResponseResult.okResult();
    }

    private void updateTags(Article article) {
        List<String> tags = article.getTags();
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,article.getId());
        articleTagService.remove(queryWrapper);
        List<ArticleTag> articleTags =
                tags.stream().map(tag -> new ArticleTag(article.getId(), Long.valueOf(tag))).collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
    }
}
