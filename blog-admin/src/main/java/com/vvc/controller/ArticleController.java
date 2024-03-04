package com.vvc.controller;

import com.vvc.constants.ResponseResult;
import com.vvc.domain.entity.Article;
import com.vvc.service.ArticleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Resource
    private ArticleService articleService;

    @GetMapping("/list")
    public ResponseResult getArticleList(Integer pageNum, Integer pageSize,Article article){
        return articleService.getArticleList(pageNum,pageSize,article);
    }
    @GetMapping("/{id}")
    public ResponseResult getArticle(@PathVariable Long id){
        return articleService.getArticle(id);
    }
    @PutMapping
    public ResponseResult updateArticle(@RequestBody Article article){
        return articleService.updateArticle(article);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable Long id){
        articleService.removeById(id);
        return ResponseResult.okResult();
    }
}
