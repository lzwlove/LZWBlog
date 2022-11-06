package com.lzw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzw.domain.ResponseResult;
import com.lzw.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2022-10-28 10:01:34
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult selectRolePage(Role role, Integer pageNum, Integer pageSize);

    void insertRole(Role role);

    void updateRole(Role role);

    List<Role> selectRoleAll();

    List<Long> selectRoleIdByUserId(Long userId);
}

