package com.vvc.runner;

import com.vvc.domain.entity.Article;
import com.vvc.mapper.ArticleMapper;
import com.vvc.utils.RedisCache;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Component
public class ViewCountRunner  implements CommandLineRunner {
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private RedisCache redisCache;
    @Override
    public void run(String... args) throws Exception {
        List<Article> articles = articleMapper.selectList(null);
        Map<String,Integer> ViewCountMap = articles.stream()
                .collect(Collectors.toMap(article -> article.getId().toString(),article -> article.getViewCount().intValue()));
        redisCache.setCacheMap("article:viewCount",ViewCountMap);
    }
}
