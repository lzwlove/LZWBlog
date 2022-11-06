package com.lzw.controller;

import com.lzw.domain.ResponseResult;
import com.lzw.domain.dto.TagListDto;
import com.lzw.domain.entity.Tag;
import com.lzw.domain.vo.PageVo;
import com.lzw.domain.vo.TagVo;
import com.lzw.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

    @PostMapping
    public ResponseResult addTag(@RequestBody Tag tag){
        return tagService.addTag(tag);
    }

    @DeleteMapping("/{id}")
    public ResponseResult dalTag(@PathVariable Integer id){
        return tagService.delTag(id);
    }

    @GetMapping("/{id}")
    public ResponseResult getTag(@PathVariable Integer id){
        return tagService.getTag(id);
    }

    @PutMapping()
    public ResponseResult updateTag(@RequestBody Tag tag){
        return tagService.updateTag(tag);
    }

    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<TagVo> list = tagService.listAllTag();
        return ResponseResult.okResult(list);
    }

}
