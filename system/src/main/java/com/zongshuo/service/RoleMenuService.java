package com.zongshuo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zongshuo.model.RoleMenuModel;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-6-16
 * @Time: 15:07
 * @Description:
 */
public interface RoleMenuService extends IService<RoleMenuModel> {
    /**
     * 先删除管理员拥有的已删除的权限点
     * 再添加管理员为拥有的权限点
     * @param roleId
     */
    public void updateAdminMenu(Integer roleId);

    /**
     * 给角色添加权限
     * 以merge方式添加多个权限
     * @param roleKey
     * @param menuId
     */
    public void addRoleMenus(String roleKey, Integer ... menuId);
}
