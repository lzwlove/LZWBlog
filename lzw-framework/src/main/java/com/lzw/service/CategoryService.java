package com.lzw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzw.domain.ResponseResult;
import com.lzw.domain.entity.Category;
import com.lzw.domain.vo.CategoryVo;
import com.lzw.domain.vo.PageVo;

import java.util.List;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2022-10-15 09:44:08
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();

    List<CategoryVo> listAllCategory();

    PageVo selectCategoryPage(Category category, Integer pageNum, Integer pageSize);
}
