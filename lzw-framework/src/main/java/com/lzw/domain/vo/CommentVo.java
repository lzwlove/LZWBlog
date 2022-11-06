package com.lzw.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentVo {
    private Long id;
    //文章id
    private Long articleId;
    //根评论id
    private Long rootId;
    //评论内容
    private String content;
    //所回复的目标评论的userid
    private Long toCommentUserId;
    //所回复的目标评论的昵称
    private String toCommentUserName;
    //回复目标评论id
    private Long toCommentId;
    //创建人
    private Long createBy;
    //创建时间
    private Date createTime;
    //评论文章的人的昵称
    private String username;
    //子评论
    private List<CommentVo> children;
}