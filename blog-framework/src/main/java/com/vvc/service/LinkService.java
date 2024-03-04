package com.vvc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vvc.constants.ResponseResult;
import com.vvc.domain.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2023-02-17 23:59:06
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult getLinkPageList(Integer pageNum, Integer pageSize, Link link);
}
