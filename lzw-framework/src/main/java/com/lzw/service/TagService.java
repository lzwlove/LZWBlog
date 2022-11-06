package com.lzw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzw.domain.ResponseResult;
import com.lzw.domain.dto.TagListDto;
import com.lzw.domain.entity.Tag;
import com.lzw.domain.vo.PageVo;
import com.lzw.domain.vo.TagVo;

import java.util.List;


/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2022-10-26 14:51:49
 */
public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(Tag tag);

    ResponseResult delTag(Integer id);

    ResponseResult getTag(Integer id);

    ResponseResult updateTag(Tag tag);

    List<TagVo> listAllTag();

}
