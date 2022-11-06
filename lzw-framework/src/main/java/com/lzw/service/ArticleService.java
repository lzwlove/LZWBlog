package com.lzw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzw.domain.ResponseResult;
import com.lzw.domain.dto.AddArticleDto;
import com.lzw.domain.entity.Article;

public interface ArticleService extends IService<Article> {
    ResponseResult getHotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult updateViewCount(Long id);

    ResponseResult getArticleDetail(Long id);

    ResponseResult add(AddArticleDto articleDto);

    ResponseResult adminArticleList(Integer pageNum, Integer pageSize, String title, String summary);

    ResponseResult adminArticleDetail(Long articleId);

    ResponseResult updateArticle(AddArticleDto articleDto);

    ResponseResult deleteArticle(Long articleId);
}
