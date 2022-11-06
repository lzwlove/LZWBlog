package com.lzw.service;

import com.lzw.domain.ResponseResult;
import com.lzw.domain.entity.User;

public interface LoginService {

    ResponseResult login(User user);

    ResponseResult logout();

}
