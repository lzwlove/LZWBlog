package com.lzw.job;

import com.lzw.domain.entity.Article;
import com.lzw.service.ArticleService;
import com.lzw.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleService articleService;

    //每10分钟执行一次
    @Scheduled(cron = "0 0/10 * * * ? ")
    //每5秒执行一次
    // @Scheduled(cron = "0/5 * * * * ?")
    public void updateViewCount() {
        //从redis中获取浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap("article:viewCount");
        //更新到数据库
        List<Article> articles = viewCountMap.entrySet()//双列集合不能直接转成流对象，使用entrySet拿到单列集合
                .stream()
                .map(entry -> new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue()))
                .collect(Collectors.toList());
        articleService.updateBatchById(articles);
    }
}
