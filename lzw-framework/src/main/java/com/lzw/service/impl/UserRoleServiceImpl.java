package com.lzw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzw.domain.entity.UserRole;
import com.lzw.mapper.UserRoleMapper;
import com.lzw.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2022-10-30 19:59:46
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
