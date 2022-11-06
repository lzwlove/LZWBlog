package com.lzw.controller;

import com.lzw.domain.ResponseResult;
import com.lzw.domain.entity.Link;
import com.lzw.domain.vo.PageVo;
import com.lzw.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    /**
     * 获取友链列表
     */
    @GetMapping("/list")
    public ResponseResult list(Link link, Integer pageNum, Integer pageSize)
    {
        PageVo pageVo = linkService.selectLinkPage(link,pageNum,pageSize);
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 新增友链
     */
    @PostMapping
    public ResponseResult add(@RequestBody Link link){
        linkService.save(link);
        return ResponseResult.okResult();
    }

    /**
     * 修改友链--回显
     */
    @GetMapping(value = "/{id}")
    public ResponseResult getInfo(@PathVariable(value = "id")Long id){
        Link link = linkService.getById(id);
        return ResponseResult.okResult(link);
    }

    /**
     * 修改友链
     */
    @PutMapping
    public ResponseResult edit(@RequestBody Link link){
        linkService.updateById(link);
        return ResponseResult.okResult();
    }

    /**
     * 删除友链
     */
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id){
        linkService.removeById(id);
        return ResponseResult.okResult();
    }

}
