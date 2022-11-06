package com.lzw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzw.domain.ResponseResult;
import com.lzw.domain.dto.TagListDto;
import com.lzw.domain.entity.Tag;
import com.lzw.domain.vo.PageVo;
import com.lzw.domain.vo.TagVo;
import com.lzw.mapper.TagMapper;
import com.lzw.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import com.lzw.service.TagService;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2022-10-26 14:51:50
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {
        //分页查询
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        queryWrapper.eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());

        Page<Tag> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);
        //封装数据返回
        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addTag(Tag tag) {
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult delTag(Integer id) {
        LambdaQueryWrapper<Tag> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Tag::getId,id);
        remove(queryWrapper);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getTag(Integer id) {
        Tag tag = getById(id);
        TagVo tagVo = BeanCopyUtils.copyBean(tag, TagVo.class);
        return ResponseResult.okResult(tagVo);
    }

    @Override
    public ResponseResult updateTag(Tag tag) {
        updateById(tag);
        return ResponseResult.okResult();
    }

    @Override
    public List<TagVo> listAllTag() {
        LambdaQueryWrapper<Tag> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId,Tag::getName);
        List<Tag> tagList = list(queryWrapper);
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(tagList, TagVo.class);
        return tagVos;
    }
}
