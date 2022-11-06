package com.lzw.controller;

import com.lzw.annotation.SystemLog;
import com.lzw.domain.ResponseResult;
import com.lzw.domain.entity.User;
import com.lzw.enums.AppHttpCodeEnum;
import com.lzw.exception.SystemException;
import com.lzw.service.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogLoginController {

    @Autowired
    private BlogLoginService blogLoginService;

    @PostMapping("/login")
    @SystemLog(businessName = "用户登录")
    public ResponseResult login(@RequestBody User user){
        //判断参数是否合法
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
            //记得去处理异常
        }
        return blogLoginService.login(user);
    }

    @PostMapping("/logout")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }

}
