package com.lzw.service;

import com.lzw.domain.entity.LoginUser;
import com.lzw.domain.entity.User;
import com.lzw.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PermissionService {

    /**
     * 判断当前用户是否具有该操作权限
     * @param permission 要判断的权限
     * @return
     */
    public boolean hasPermission(String permission){
        //如果是超级管理员，直接返回true
        if (SecurityUtils.isAdmin()){
            return true;
        }
        //否则查询该用户是否具有对应权限
        List<String> permissions = SecurityUtils.getLoginUser().getPermissions();
        return permissions.contains(permission);
    }

}
