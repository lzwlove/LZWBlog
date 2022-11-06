package com.lzw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzw.constants.SystemConstants;
import com.lzw.domain.ResponseResult;
import com.lzw.domain.dto.AddArticleDto;
import com.lzw.domain.entity.Article;
import com.lzw.domain.entity.ArticleTag;
import com.lzw.domain.entity.Category;
import com.lzw.domain.vo.ArticleDetailVo;
import com.lzw.domain.vo.ArticleListVo;
import com.lzw.domain.vo.HotArticleVo;
import com.lzw.domain.vo.PageVo;
import com.lzw.mapper.ArticleMapper;
import com.lzw.service.ArticleService;
import com.lzw.service.ArticleTagService;
import com.lzw.service.CategoryService;
import com.lzw.utils.BeanCopyUtils;
import com.lzw.utils.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleTagService articleTagService;

    @Override
    public ResponseResult getHotArticleList() {

        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        //必须是已发布的
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL)
                .orderByDesc(Article::getViewCount);
        //开启分页，显示浏览量最多的10篇文章
        Page<Article> page=new Page<>(1,10);
        List<Article> articles = page(page, queryWrapper).getRecords();

        // List<HotArticleVo> hotArticleVos=new ArrayList<>();
        // for (Article article : articles) {
        //     //Bean拷贝
        //     HotArticleVo hotArticleVo=new HotArticleVo();
        //     BeanUtils.copyProperties(article,hotArticleVo);
        //     hotArticleVos.add(hotArticleVo);
        // }

        List<HotArticleVo> vs = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
        //响应数据
        return ResponseResult.okResult(vs);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        LambdaQueryWrapper<Article> articleWrapper=new LambdaQueryWrapper<>();
        //判断categoryId是否为空
        articleWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,Article::getCategoryId,categoryId)
                //正式发布的文章
                .eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL)
                //置顶文章在最前
                .orderByDesc(Article::getIsTop);
        //开启分页，需要在配置类中开启此功能
        Page<Article> page=new Page<>(pageNum,pageSize);
        //分页查询，此时已经查到了符合条件的文章
        List<Article> articles = page(page, articleWrapper).getRecords();
        //获取categoryName
        /*for (Article article : articles) {
            Category category = categoryService.getById(article.getCategoryId());
            article.setCategoryName(category.getName());
        }*/
        //获取categoryName
        articles = articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());
        //封装VO
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);
        PageVo pageVo=new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        redisCache.incrementCacheMapValue("article:viewCount",id.toString(),1);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查文章
        Article article = articleService.getById(id);
        //从Redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
        //封装vo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //此时还没有categoryName，需要查询
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        articleDetailVo.setCategoryName(category.getName());
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    @Transactional //开启事务
    public ResponseResult add(AddArticleDto articleDto) {
        //添加博客
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);
        //save执行完后id会自动赋值
        //随后添加标签和博客的关联
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult adminArticleList(Integer pageNum, Integer pageSize, String title, String summary) {
        LambdaQueryWrapper<Article> articleWrapper=new LambdaQueryWrapper<>();
        articleWrapper
                //正式发布的文章
                .eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL)
                .like(StringUtils.hasLength(title),Article::getTitle,title)
                .like(StringUtils.hasLength(summary),Article::getSummary,summary);
        //开启分页，需要在配置类中开启此功能
        Page<Article> page=new Page<>(pageNum,pageSize);
        //分页查询，此时已经查到了符合条件的文章
        List<Article> articles = page(page, articleWrapper).getRecords();
        //封装VO
        List<ArticleDetailVo> articleDetailVos = BeanCopyUtils.copyBeanList(articles, ArticleDetailVo.class);
        PageVo pageVo=new PageVo(articleDetailVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult adminArticleDetail(Long articleId) {
        Article article = getById(articleId);
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //现在还没有文章标签，需要查询得到
        LambdaQueryWrapper<ArticleTag> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,articleId);
        List<ArticleTag> articleTags = articleTagService.list(queryWrapper);
        List<Long> list = articleTags.stream().map(articleTag -> articleTag.getTagId()).collect(Collectors.toList());
        articleDetailVo.setTags(list);
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    @Transactional
    public ResponseResult updateArticle(AddArticleDto articleDto) {
        //得到Article对象
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        //更新博客信息
        updateById(article);
        //删除原有的博客和标签关联
        LambdaQueryWrapper<ArticleTag> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ArticleTag::getArticleId,article.getId());
        articleTagService.remove(lambdaQueryWrapper);
        //添加新的博客和标签关联的信息
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    //逻辑删除文章
    @Override
    public ResponseResult deleteArticle(Long articleId) {
        articleService.removeById(articleId);
        return ResponseResult.okResult();
    }
}
