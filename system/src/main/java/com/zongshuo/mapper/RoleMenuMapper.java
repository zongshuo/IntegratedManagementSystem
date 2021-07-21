package com.zongshuo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zongshuo.model.RoleMenuModel;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-6-16
 * @Time: 15:06
 * @Description:
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenuModel> {

    /**
     * 添加管理员不具有的权限点
     * @param roleId
     */
    public void insertAdminMenu(@Param("roleId") Integer roleId);

    /**
     * 给角色添加指定权限
     * @param roleId
     * @param menus
     */
    public void addMenus(@Param("roleId")Integer roleId, @Param("menus") Integer [] menus);

    /**
     * 删除管理员拥有的已经删除的权限点
     * @param roleId
     */
    public void deleteAdminMenu(@Param("roleId") Integer roleId);
}
