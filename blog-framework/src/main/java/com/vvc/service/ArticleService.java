package com.vvc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vvc.constants.ResponseResult;
import com.vvc.domain.entity.Article;

public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);
    ResponseResult updateViewCount(String key,String hKey,int v);

    ResponseResult getArticleList(Integer pageNum, Integer pageSize, Article article);

    ResponseResult getArticle(Long id);

    ResponseResult updateArticle(Article article);
}
