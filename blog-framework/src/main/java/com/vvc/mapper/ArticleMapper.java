package com.vvc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vvc.domain.entity.Article;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
