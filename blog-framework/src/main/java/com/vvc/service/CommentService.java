package com.vvc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vvc.constants.ResponseResult;
import com.vvc.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-02-24 15:03:26
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String type, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}
