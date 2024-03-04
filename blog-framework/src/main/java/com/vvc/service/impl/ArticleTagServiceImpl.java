package com.vvc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vvc.domain.entity.ArticleTag;
import com.vvc.mapper.ArticleTagMapper;
import com.vvc.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2023-02-28 19:56:23
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}

