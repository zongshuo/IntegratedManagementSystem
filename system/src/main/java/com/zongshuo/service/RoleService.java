package com.zongshuo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zongshuo.model.MenuModel;
import com.zongshuo.model.RoleModel;
import com.zongshuo.model.UserModel;
import com.zongshuo.util.PageParam;
import com.zongshuo.util.PageResult;

import java.util.List;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-22
 * @Time: 11:23
 * @Description:
 * 系统角色服务类
 */
public interface RoleService extends IService<RoleModel> {

    /**
     * 分页查询角色
     * @param pageParam
     * @return
     */
    PageResult<RoleModel> getPage(PageParam<RoleModel> pageParam);

    /**
     * 根据用户获取用户角色列表
     * @param userModel
     * @return
     */
    List<RoleModel> getUserRoles(UserModel userModel);

    /**
     * 根据角色id获取角色的菜单
     * @param roleId
     * @return
     */
    List<MenuModel> getRoleMenus(Integer roleId);

    /**
     * 更新角色菜单
     * @param roleId
     * @param menus
     */
    void updateRoleMenus(Integer roleId, Integer [] menus);

    /**
     * 编辑角色
     * 不做字段校验
     * 只关注业务操作
     * @param role
     */
    void editRole(RoleModel role) throws IllegalStateException;

}
