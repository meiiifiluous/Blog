package com.vvc.controller;

import com.vvc.constants.ResponseResult;
import com.vvc.domain.entity.Dto.TagListDto;
import com.vvc.domain.entity.Tag;
import com.vvc.domain.entity.VO.TagVo;
import com.vvc.domain.entity.VO.listAllTagVo;
import com.vvc.service.TagService;
import com.vvc.utils.BeanCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Resource
    private TagService tagService;
    @GetMapping("/list")
    public ResponseResult list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTaglist(pageNum,pageSize,tagListDto);
    }
    @PostMapping
    public ResponseResult add(@RequestBody Tag tag){
        tagService.save(tag);
        return ResponseResult.okResult();
    }
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id){
        tagService.removeById(id);
        return ResponseResult.okResult();
    }
    @GetMapping("/{id}")
    public ResponseResult getTag(@PathVariable Long id){
        Tag tag = tagService.getById(id);
        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);
        return ResponseResult.okResult(tagVo);
    }
    @PutMapping
    public ResponseResult update(@RequestBody Tag tag){
        tagService.updateById(tag);
        return ResponseResult.okResult();
    }
    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<Tag> tags = tagService.list();
        List<listAllTagVo> listAllTagVos = BeanCopyUtils.copyBeanList(tags, listAllTagVo.class);
        return ResponseResult.okResult(listAllTagVos);
    }
}
