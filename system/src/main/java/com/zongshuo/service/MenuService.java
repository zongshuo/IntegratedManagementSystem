package com.zongshuo.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zongshuo.model.MenuModel;
import com.zongshuo.model.RoleModel;

import java.util.List;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-20
 * @Time: 20:11
 * @Description: 系统菜单操作服务类
 */
public interface MenuService extends IService<MenuModel> {

    /**
     * 新增系统菜单
     * 会根据父菜单编号和菜单名称防重复提交
     * @param menuModel
     */
    void addMenu(MenuModel menuModel) throws IllegalAccessException;

    /**
     * 编辑菜单
     * 验证菜单是否存在
     * 更新除menuId外的所有字段
     * @param menu
     * @throws IllegalAccessException
     */
    void updateMenu(MenuModel menu) throws IllegalAccessException;

    /**
     * 根据角色获取角色拥有的菜单
     * @param role
     * @return
     */
    List<MenuModel> getMenuListByRole(RoleModel role);

    /**
     * 根据角色id列表获取角色拥有的菜单
     * @param roles
     * @return
     */
    List<MenuModel> getMenuListByRole(List<RoleModel> roles);

    /**
     * 根据用户id获取用户菜单列表
     * @param userId
     * @return
     */
    List<MenuModel> getMenusByUserId(Integer userId);

    /**
     * 获取用户所有权限
     * @param userId
     * @return
     */
    List<MenuModel> getAuthsByUserId(Integer userId);

    /**
     * 查询系统所有菜单
     * 所有条件为空，返回所有菜单
     * @return
     */
    List<MenuModel> getAllMenu();

    /**
     * 根据条件查询系统菜单
     * 根据菜单名称模糊查询
     * 根据菜单路径模糊查询
     * @param title
     * @param path
     * @return
     */
    List<MenuModel> searchMenu(String title, String path);

    /**
     * 将菜单列表生成树状结构
     * 可以根据选择的父类作为根目录
     * @param menuModels
     * @return
     */
    List<MenuModel> toMenuTree(List<MenuModel> menuModels, Integer parentId);

    /**
     * 删除菜单及其子菜单
     * 删除被删除菜单关联的角色映射
     * @param menuId
     */
    void removeMenu(Integer menuId);
}
