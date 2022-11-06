package com.lzw.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzw.constants.SystemConstants;
import com.lzw.domain.ResponseResult;
import com.lzw.domain.entity.Link;
import com.lzw.domain.vo.LinkVo;
import com.lzw.service.LinkService;
import com.lzw.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/link")
public class LinkController {

    @Autowired
    LinkService linkService;

    @GetMapping("/getAllLink")
    public ResponseResult getAllLink() {
        return linkService.getAllLink();
    }

}
