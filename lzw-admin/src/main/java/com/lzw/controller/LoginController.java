package com.lzw.controller;

import com.lzw.annotation.SystemLog;
import com.lzw.domain.ResponseResult;
import com.lzw.domain.entity.Menu;
import com.lzw.domain.entity.User;
import com.lzw.domain.vo.AdminUserInfoVo;
import com.lzw.domain.vo.RoutersVo;
import com.lzw.domain.vo.UserInfoVo;
import com.lzw.enums.AppHttpCodeEnum;
import com.lzw.exception.SystemException;
import com.lzw.service.BlogLoginService;
import com.lzw.service.LoginService;
import com.lzw.service.MenuService;
import com.lzw.service.RoleService;
import com.lzw.utils.BeanCopyUtils;
import com.lzw.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    @PostMapping("/user/login")
    // @SystemLog(businessName = "用户登录")
    public ResponseResult login(@RequestBody User user) {
        //判断参数是否合法
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
            //记得去处理异常
        }
        return loginService.login(user);
    }

    @GetMapping("getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo() {
        //获取当前登录用户
        User user = SecurityUtils.getLoginUser().getUser();
        //根据id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(user.getId());
        //根据id查角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(user.getId());
        //封装UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);

        //封装vo
        AdminUserInfoVo adminUserInfoVo=new AdminUserInfoVo(perms,roleKeyList,userInfoVo);

        return ResponseResult.okResult(adminUserInfoVo);
    }

    @GetMapping("getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        Long userId = SecurityUtils.getUserId();
        //查询menu 结果是tree的形式
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }

}
