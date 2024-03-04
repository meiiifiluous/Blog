package com.vvc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vvc.constants.ResponseResult;
import com.vvc.domain.entity.Dto.TagListDto;
import com.vvc.domain.entity.Tag;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2023-02-27 11:46:42
 */
public interface TagService extends IService<Tag> {


    ResponseResult pageTaglist(Integer pageNum, Integer pageSize, TagListDto tagListDto);
}
