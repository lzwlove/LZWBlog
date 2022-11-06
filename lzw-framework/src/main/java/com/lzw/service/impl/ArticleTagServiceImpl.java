package com.lzw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzw.domain.entity.ArticleTag;
import com.lzw.mapper.ArticleTagMapper;
import com.lzw.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2022-10-29 10:38:25
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {

}
