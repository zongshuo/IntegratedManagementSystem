package com.zongshuo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zongshuo.mapper.RoleMapper;
import com.zongshuo.model.MenuModel;
import com.zongshuo.model.RoleMenuModel;
import com.zongshuo.model.RoleModel;
import com.zongshuo.model.UserModel;
import com.zongshuo.service.RoleMenuService;
import com.zongshuo.service.RoleService;
import com.zongshuo.util.PageParam;
import com.zongshuo.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
}
