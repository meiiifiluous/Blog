package com.vvc.controller;

import com.vvc.constants.ResponseResult;
import com.vvc.domain.entity.Link;
import com.vvc.service.LinkService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Resource
    private LinkService linkService;
    @GetMapping("/list")
    public ResponseResult getLinkPageList(Integer pageNum, Integer pageSize, Link link){
        return linkService.getLinkPageList(pageNum,pageSize,link);
    }
}
