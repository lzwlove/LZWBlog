package com.lzw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzw.domain.ResponseResult;
import com.lzw.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2022-10-19 17:25:06
 */
public interface CommentService extends IService<Comment> {

    ResponseResult commentList(String commentType, Long articleId, Integer pageNun, Integer pageSize);

    ResponseResult addComment(Comment comment);
}

