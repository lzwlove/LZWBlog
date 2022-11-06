package com.lzw.domain.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDetailVo {
    private Long id;

    //标题
    private String title;
    //文章摘要
    private String summary;
    //所属分类id
    private Long categoryId;
    //所属分类名
    private String categoryName;
    //缩略图
    private String thumbnail;
    //文章内容
    private String content;
    //访问量
    private Long viewCount;
    //创建时间
    private Date createTime;
    //=================================后面自己添加的
    //是否允许评论 1是，0否
    private String isComment;
    //是否置顶（0否，1是）
    private String isTop;
    //状态（0已发布，1草稿）
    private String status;
    //文章标签
    private List<Long> tags;
    //创建人
    private Long createBy;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;
    //更新时间
    private Date updateTime;

    //更新人
    private Long updateBy;
}
