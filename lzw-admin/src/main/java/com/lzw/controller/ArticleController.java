package com.lzw.controller;

import com.lzw.domain.ResponseResult;
import com.lzw.domain.dto.AddArticleDto;
import com.lzw.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto articleDto) {
        return articleService.add(articleDto);
    }

    @GetMapping("/list")
    public ResponseResult adminArticleList(Integer pageNum, Integer pageSize, String title, String summary) {
        return articleService.adminArticleList(pageNum,pageSize,title,summary);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticle(@PathVariable("id") Long articleId){
        return articleService.adminArticleDetail(articleId);
    }

    @PutMapping
    public ResponseResult updateArticle(@RequestBody AddArticleDto articleDto){
        return articleService.updateArticle(articleDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteArticle(@PathVariable("id") Long articleId){
        return articleService.deleteArticle(articleId);
    }

    // @PostMapping
}
