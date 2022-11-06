package com.lzw.service.impl;

import com.lzw.domain.ResponseResult;
import com.lzw.domain.entity.LoginUser;
import com.lzw.domain.entity.User;
import com.lzw.domain.vo.BlogUserLoginVo;
import com.lzw.domain.vo.UserInfoVo;
import com.lzw.service.BlogLoginService;
import com.lzw.service.LoginService;
import com.lzw.utils.BeanCopyUtils;
import com.lzw.utils.JwtUtil;
import com.lzw.utils.RedisCache;
import com.lzw.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class SystemLoginServiceImpl implements LoginService {

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
        redisCache.setCacheObject("login:"+userId,loginUser);

        //封装token返回
        Map<String,String> map=new HashMap<>();
        map.put("token",jwt);

        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //从redis中删除
        redisCache.deleteObject("login:"+userId);
        return ResponseResult.okResult();
    }

}
