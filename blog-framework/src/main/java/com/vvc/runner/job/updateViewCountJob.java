package com.vvc.runner.job;

import com.vvc.domain.entity.Article;
import com.vvc.service.ArticleService;
import com.vvc.utils.RedisCache;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class updateViewCountJob {
    @Resource
    private ArticleService articleService;
    @Resource
    private RedisCache redisCache;
    @Scheduled(cron = "0/5 * * * * ?")
    public void updateViewCount(){
        Map<String, Object> viewCountMap = redisCache.getCacheMap("article:viewCount");
        List<Article> articles = viewCountMap.entrySet().stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), Long.valueOf(entry.getValue().toString())))
                .collect(Collectors.toList());
        articleService.updateBatchById(articles);
    }
}
