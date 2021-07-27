package com.zongshuo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zongshuo.mapper.RoleMapper;
import com.zongshuo.model.*;
import com.zongshuo.service.RoleMenuService;
import com.zongshuo.service.RoleService;
import com.zongshuo.service.UserRoleService;
import com.zongshuo.annotation.util.PageParam;
import com.zongshuo.annotation.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-22
 * @Time: 11:23
 * @Description:
 */
@Service
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RoleModel> implements RoleService {
    @Autowired(required = false)
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private UserRoleService userRoleService;

    @Override
    public PageResult<RoleModel> getPage(PageParam<RoleModel> pageParam) {
        List<RoleModel> roleModels = roleMapper.selectPage(pageParam);
        return new PageResult<>(roleModels, pageParam.getTotal());
    }

    @Override
    public List<RoleModel> getUserRoles(UserModel userModel) {
        List<RoleModel> roles = roleMapper.selectUserRoles(userModel.getId());
        if (roles == null){
            roles = new ArrayList<>(0);
        }
        return roles;
    }

    @Override
    public List<MenuModel> getRoleMenus(Integer roleId) {
        List<MenuModel> menuModels = roleMapper.selectRoleMenus(roleId);
        if (menuModels == null){
            menuModels = new ArrayList<>(0);
        }
        return menuModels;
    }

    @Override
    @Transactional
    public void updateRoleMenus(Integer roleId, Integer[] menus) {
        roleMenuService.remove(
                new QueryWrapper<RoleMenuModel>()
                        .lambda()
                        .eq(RoleMenuModel::getRoleId, roleId));

        if (menus.length > 0){
            List<RoleMenuModel> roleMenus = new ArrayList<>(menus.length);
            for (Integer menuId : menus){
                RoleMenuModel roleMenu = new RoleMenuModel();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                roleMenus.add(roleMenu);
            }
            roleMenuService.saveBatch(roleMenus);
        }
    }

    @Override
    public void editRole(RoleModel role) throws IllegalStateException{
        int count = roleMapper.update(role, new UpdateWrapper<RoleModel>().lambda().eq(RoleModel::getId, role.getId()));
        if (count == 0) throw new IllegalStateException("更新失败！");
    }

    @Override
    @Transactional(rollbackFor = {IllegalAccessException.class, Exception.class})
    public void removeRole(Integer[] roleIds) throws IllegalAccessException{
        int count = roleMapper.selectCount(
                new QueryWrapper<RoleModel>()
                        .lambda().in(RoleModel::getId, roleIds));
        if (count != roleIds.length){
            throw new IllegalAccessException("数据异常，请刷新页面后重新选择！");
        }

        roleMenuService.remove(new UpdateWrapper<RoleMenuModel>()
                .lambda()
                .in(RoleMenuModel::getRoleId, roleIds));
        count = roleMenuService.count(new QueryWrapper<RoleMenuModel>().lambda().in(RoleMenuModel::getRoleId, roleIds));
        if (count > 0){
            throw new IllegalAccessException("删除角色菜单失败，请重试！");
        }

        userRoleService.remove(new UpdateWrapper<UserRoleModel>()
                .lambda()
                .in(UserRoleModel::getRoleId, roleIds));
        count = userRoleService.count(new QueryWrapper<UserRoleModel>().lambda().in(UserRoleModel::getRoleId, roleIds));
        if (count > 0){
            throw new IllegalAccessException("删除用户角色失败，请重试！");
        }

        count = roleMapper.deleteBatchIds(Arrays.asList(roleIds));
        if (count != roleIds.length){
            throw new IllegalAccessException("删除角色失败！");
        }
    }
}
