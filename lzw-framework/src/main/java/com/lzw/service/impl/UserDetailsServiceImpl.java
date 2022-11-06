package com.lzw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzw.constants.SystemConstants;
import com.lzw.domain.entity.LoginUser;
import com.lzw.domain.entity.User;
import com.lzw.mapper.MenuMapper;
import com.lzw.mapper.UserMapper;
import com.lzw.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> userWrapper=new LambdaQueryWrapper<>();
        userWrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(userWrapper);
        //判断是否查到  如果没有则抛出异常
        if (Objects.isNull(user)){
            throw new RuntimeException("该用户不存在");
        }
        //注意！我们使用了BCryptPasswordEncoder进行密码的加密
        //由于返回值是UserDetails类型，所以我们写一个类去实现UserDetails

        //查询权限信息并封装
        if (user.getType().equals(SystemConstants.ADMIN)){
            List<String> perms = menuMapper.selectPermsByUserId(user.getId());
            return new LoginUser(user,perms);
        }
        return new LoginUser(user,null);
    }
}
