package com.vvc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vvc.constants.AppHttpCodeEnum;
import com.vvc.constants.ResponseResult;
import com.vvc.constants.SystemConstants;
import com.vvc.domain.entity.Comment;
import com.vvc.domain.entity.VO.CommentVo;
import com.vvc.domain.entity.VO.PageVo;
import com.vvc.exception.SystemException;
import com.vvc.mapper.CommentMapper;
import com.vvc.service.CommentService;
import com.vvc.service.UserService;
import com.vvc.utils.BeanCopyUtils;
import io.jsonwebtoken.lang.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-02-24 15:03:26
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Resource
    private UserService userService;
    @Override
    public ResponseResult commentList(String type, Long articleId, Integer pageNum, Integer pageSize) {
        //查询根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(articleId!=null,Comment::getArticleId,articleId);
        queryWrapper.eq(Comment::getRootId, SystemConstants.ROOT_ID);
        queryWrapper.eq(Comment::getType,type);
        queryWrapper.orderByDesc(Comment::getCreateTime);
        //分页查询
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page,queryWrapper);
        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());
        //查询所有根评论的子评论
        for (CommentVo commentVo:commentVoList) {
            List<CommentVo> children = getChildren(commentVo.getId());
            commentVo.setChildren(children);
        }
        return ResponseResult.okResult(new PageVo(commentVoList,page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        if(!Strings.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.COMMEN_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }

    private List<CommentVo> getChildren(Long rootId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,rootId);
        List<Comment> comments = list(queryWrapper);
        List<CommentVo> commentVos = toCommentVoList(comments);
        return commentVos;
    }

    private List<CommentVo> toCommentVoList(List<Comment> list) {
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        for (CommentVo commentVo:commentVos) {
            //得到user的nickname存入commentVo的username
            commentVo.setUsername(userService.getById(commentVo.getCreateBy()).getNickName());
            //查询to_comment_user_id
            if(commentVo.getToCommentUserId()!=-1){
                commentVo.setToCommentUserName(userService.getById(commentVo.getToCommentUserId())
                        .getNickName());
            }
        }
        return commentVos;
    }
}

