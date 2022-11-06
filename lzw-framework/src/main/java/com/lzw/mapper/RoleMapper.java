package com.lzw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzw.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-28 10:01:32
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long userId);

    List<Long> selectRoleIdByUserId(Long userId);
}

