package com.lzw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzw.constants.SystemConstants;
import com.lzw.domain.ResponseResult;
import com.lzw.domain.entity.Comment;
import com.lzw.domain.vo.CommentVo;
import com.lzw.domain.vo.PageVo;
import com.lzw.enums.AppHttpCodeEnum;
import com.lzw.exception.SystemException;
import com.lzw.mapper.CommentMapper;
import com.lzw.service.CommentService;
import com.lzw.service.UserService;
import com.lzw.utils.BeanCopyUtils;
import com.lzw.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2022-10-19 17:25:06
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    UserService userService;

    @Override
    public ResponseResult commentList(String commentType, Long articleId, Integer pageNun, Integer pageSize) {
        //查对应文章的根评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //根据articleId查询对应文章的评论
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType), Comment::getArticleId, articleId)
                //查询根评论
                .eq(Comment::getRootId, -1)
                //评论类型
                .eq(Comment::getType,commentType);
        //分页查询
        Page<Comment> page = new Page<>(pageNun, pageSize);
        page(page, queryWrapper);
        //封装Vo
        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());

        //查询!所有!根评论对应的子评论集合，并且赋值给对应的属性
        for (CommentVo commentVo : commentVoList) {
            List<CommentVo> children = getChildren(commentVo.getId());
            commentVo.setChildren(children);
        }

        return ResponseResult.okResult(new PageVo(commentVoList, page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        //登录后才能评论
        Long userId = SecurityUtils.getUserId();
        if (Objects.isNull(userId)){
            throw new SystemException(AppHttpCodeEnum.NEED_LOGIN);
        }
        //评论内容不能为空
        if (!StringUtils.hasText(comment.getContent())) {
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }

    private List<CommentVo> getChildren(Long id) {
        //根据id查对应评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId, id)
                //根据时间升序
                .orderByAsc(Comment::getCreateBy);
        List<Comment> list = list(queryWrapper);
        //将查询结果封装到CommentVo
        List<CommentVo> childrenComments = toCommentVoList(list);
        return childrenComments;
    }

    private List<CommentVo> toCommentVoList(List<Comment> list) {
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //遍历commentVoList集合
        for (CommentVo commentVo : commentVos) {
            //通过creatyBy查询用户的昵称并赋值
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(nickName);
            //通过toCommentUserId查询用户的昵称并赋值
            //如果toCommentUserId不为-1才进行查询
            if (commentVo.getToCommentUserId() != -1) {
                String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(toCommentUserName);
            }
        }
        return commentVos;
    }
}
