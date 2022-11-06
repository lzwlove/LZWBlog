package com.lzw.service.impl;

import com.lzw.domain.ResponseResult;
import com.lzw.domain.entity.LoginUser;
import com.lzw.domain.entity.User;
import com.lzw.domain.vo.BlogUserLoginVo;
import com.lzw.domain.vo.UserInfoVo;
import com.lzw.enums.AppHttpCodeEnum;
import com.lzw.service.BlogLoginService;
import com.lzw.utils.BeanCopyUtils;
import com.lzw.utils.JwtUtil;
import com.lzw.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BlogLoginServiceImpl implements BlogLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {

        //这里的意思是自定义登录接口
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        //由于authenticate方法会调用UserDetailsService类中的loadUserByUsername方法
        //loadUserByUsername它默认从内存中查用户名密码，所以我们要重写该方法让它从数据库中查
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if (Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //获取userId生成Token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();

        String jwt = JwtUtil.createJWT(userId);
        //将用户信息存入redis
        redisCache.setCacheObject("bloglogin:"+userId,loginUser);
        //封装VO
        //把User转换成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo vo=new BlogUserLoginVo(jwt,userInfoVo);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult logout() {
        //获取token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userId = loginUser.getUser().getId();
        //从Redis中删除
        redisCache.deleteObject("bloglogin:"+userId);
        return ResponseResult.okResult();
    }
}
