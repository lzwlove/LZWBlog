package com.lzw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzw.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-28 09:51:58
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    List<Menu> selectAllRouterMenu();

    List<Long> selectMenuListByRoleId(Long roleId);
}

