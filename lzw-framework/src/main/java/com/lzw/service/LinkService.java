package com.lzw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzw.domain.ResponseResult;
import com.lzw.domain.entity.Link;
import com.lzw.domain.vo.PageVo;


/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2022-10-17 14:03:34
 */
public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    PageVo selectLinkPage(Link link, Integer pageNum, Integer pageSize);
}

