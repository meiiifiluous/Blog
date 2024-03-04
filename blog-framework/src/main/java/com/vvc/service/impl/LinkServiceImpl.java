package com.vvc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vvc.constants.ResponseResult;
import com.vvc.constants.SystemConstants;
import com.vvc.domain.entity.Link;
import com.vvc.domain.entity.VO.LinkVo;
import com.vvc.domain.entity.VO.PageVo;
import com.vvc.mapper.LinkMapper;
import com.vvc.service.LinkService;
import com.vvc.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2023-02-17 23:59:07
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {
    @Autowired
    private LinkService linkService;

    @Override
    public ResponseResult getAllLink() {
        //得到审核状态通过的link
        LambdaQueryWrapper<Link>  queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> list = list(queryWrapper);
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(list,LinkVo.class);
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult getLinkPageList(Integer pageNum, Integer pageSize, Link link) {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!Objects.isNull(link.getName()),Link::getName,link.getName());
        queryWrapper.eq(!Objects.isNull(link.getStatus()),Link::getStatus,link.getStatus());
        Page page = new Page(pageNum, pageSize);
        page(page,queryWrapper);
        PageVo pageVo = new PageVo(page.getRecords(), page.getTotal());
        return ResponseResult.okResult(pageVo);
    }
}

