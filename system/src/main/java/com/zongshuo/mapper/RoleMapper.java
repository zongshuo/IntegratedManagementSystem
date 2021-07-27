package com.zongshuo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zongshuo.model.MenuModel;
import com.zongshuo.model.RoleModel;
import com.zongshuo.util.PageParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-22
 * @Time: 11:24
 * @Description:
 */
public interface RoleMapper extends BaseMapper<RoleModel> {
    /**
     * 分页查询角色
     * @param pageParam
     * @return
     */
    List<RoleModel> selectPage(@Param("page")PageParam<RoleModel> pageParam);

    /**
     * 根据用户id关联用户角色表获取系统角色信息
     * @param id
     * @return
     */
    List<RoleModel> selectUserRoles(@Param("userId") Integer id);

    /**
     * 根据角色id搜索菜单
     * @param roleId
     * @return
     */
    List<MenuModel> selectRoleMenus(@Param("id") Integer roleId);
}
