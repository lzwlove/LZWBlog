package com.lzw.runner;

import com.lzw.domain.entity.Article;
import com.lzw.mapper.ArticleMapper;
import com.lzw.service.ArticleService;
import com.lzw.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 在SpringBoot启动时将浏览量存入Redis
 */
@Component
public class ViewCountRunner implements CommandLineRunner {

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public void run(String... args) throws Exception {
        //查询博客信息（id和浏览量）   id    viewCount
        List<Article> articles = articleMapper.selectList(null);
        Map<String, Integer> viewCountMap = articles.stream().collect(
                Collectors.toMap(article -> article.getId().toString(),
                                 article -> article.getViewCount().intValue()));
        //存入redis
        redisCache.setCacheMap("article:viewCount",viewCountMap);
    }
}
