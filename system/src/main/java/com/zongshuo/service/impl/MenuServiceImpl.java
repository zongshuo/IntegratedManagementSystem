package com.zongshuo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zongshuo.Contains;
import com.zongshuo.annotation.AuthDefinition;
import com.zongshuo.authorization.model.AccessType;
import com.zongshuo.mapper.MenuMapper;
import com.zongshuo.model.MenuModel;
import com.zongshuo.model.RoleMenuModel;
import com.zongshuo.model.RoleModel;
import com.zongshuo.service.MenuService;
import com.zongshuo.service.RoleMenuService;
import com.zongshuo.service.RoleService;
import com.zongshuo.util.MenuSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-20
 * @Time: 20:14
 * @Description: 系统菜单服务实现类
 */
@Slf4j
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuModel> implements MenuService {
    @Autowired(required = false)
    private MenuMapper menuMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMenuService roleMenuService;


    @Override
    @Transactional
    public void addMenu(MenuModel menu) throws IllegalAccessException {
        QueryWrapper<MenuModel> query = new QueryWrapper<>();

        if (AccessType.MENU.getType().equals(menu.getType()) && StringUtils.isNotBlank(menu.getTitle())) {
            query = new QueryWrapper<>();
            query.lambda()
                    .eq(MenuModel::getTitle, menu.getTitle())
                    .eq(MenuModel::getParent, menu.getParent());
            if (menuMapper.selectCount(query) > 0) {
                throw new IllegalAccessException("同级目录下菜单标题重复！");
            }
        }

        baseMapper.insert(menu);
        roleMenuService.addRoleMenus(Contains.SYS_ADMIN_NAME, menu.getId());
    }

    @Override
    public void addAccessPoint(MenuModel menu) {
        /**
         * 必须确保菜单权限标识分别唯一
         * 菜单权限标识用于授权，重复后权限校验不准确
         * 权限点收集时，以权限标识确认父权限
         */
        MenuModel tmpMenu = menuMapper.selectOne(
                new QueryWrapper<MenuModel>()
                        .lambda().eq(MenuModel::getAuthority, menu.getAuthority()));

        Date date = new Date();
        menu.setUpdateTime(date);
        menu.setSource(MenuSource.SYSTEM.getValue());
        if (tmpMenu == null) {
            menu.setCreateTime(date);
            menu.setHide(Contains.DEFAULT_NO);
            menuMapper.insert(menu);
            roleMenuService.addRoleMenus(Contains.SYS_ADMIN_NAME, menu.getId());
        }

        menuMapper.update(null,
                new UpdateWrapper<MenuModel>()
                        .lambda()
                        .set(MenuModel::getName, menu.getName())
                        .set(MenuModel::getParent, menu.getParent())
                        .set(MenuModel::getType, menu.getType())
                        .set(MenuModel::getUpdateTime, menu.getUpdateTime())
                        .eq(MenuModel::getId, tmpMenu.getId()));
    }

    @Override
    public void clearAccessPoint() {
        List<MenuModel> menus =
                menuMapper.selectList(
                        new QueryWrapper<MenuModel>()
                                .lambda()
                                .select(MenuModel::getId)
                                .eq(MenuModel::getSource, MenuSource.SYSTEM.getValue())
                                .isNull(MenuModel::getUpdateTime));
        if (menus == null) return;

        for (MenuModel menu : menus)
            removeMenu(menu.getId());
    }

    @Override
    public void updateMenu(MenuModel menu) throws IllegalAccessException {
        int count = menuMapper.selectCount(
                new QueryWrapper<MenuModel>()
                        .lambda()
                        .eq(MenuModel::getParent, menu.getParent())
                        .eq(MenuModel::getTitle, menu.getTitle())
                        .ne(MenuModel::getId, menu.getId()));
        if (count > 0) {
            throw new IllegalAccessException("同级目录菜单名已存在！");
        }

        count = menuMapper.update(null,
                new UpdateWrapper<MenuModel>()
                        .lambda()
                        .set(MenuModel::getParent, menu.getParent())
                        .set(MenuModel::getTitle, menu.getTitle())
                        .set(MenuModel::getSortNumber, menu.getSortNumber())
                        .set(MenuModel::getIcon, menu.getIcon())
                        .set(MenuModel::getType, menu.getType())
                        .set(MenuModel::getPath, menu.getPath())
                        .set(MenuModel::getHide, menu.getHide())
                        .set(MenuModel::getComponent, menu.getComponent())
                        .set(MenuModel::getUpdateTime, new Date())
                        .eq(MenuModel::getId, menu.getId()));
    }

    @Override
    public List<MenuModel> getMenuListByRole(RoleModel role) {
        return getMenuListByRole(Arrays.asList(role));
    }


    @Override
    public List<MenuModel> getMenuListByRole(List<RoleModel> roleModels) {
        if (roleModels == null || roleModels.isEmpty()) return new ArrayList<>(0);
        List<MenuModel> menuModels = menuMapper.getMenusByRoleIds(roleModels);
        if (menuModels == null) return new ArrayList<>(0);

        return menuModels;
    }

    @Override
    public List<MenuModel> getMenusByUserId(Integer userId) {
        List<MenuModel> menuModels = menuMapper.getMenusByUserId(userId);
        if (menuModels == null) {
            menuModels = new ArrayList<>(0);
        }
        return menuModels;
    }

    @Override
    public List<MenuModel> getAuthsByUserId(Integer userId) {
        List<MenuModel> menuModels = menuMapper.getAuthsByUserId(userId);
        if (menuModels == null) {
            menuModels = new ArrayList<>(0);
        }
        return menuModels;
    }

    @Override
    public List<MenuModel> getAllMenu() {
        List<MenuModel> menuModels =
                menuMapper.selectList(
                        new QueryWrapper<MenuModel>()
                                .lambda()
                                .orderByAsc(MenuModel::getParent, MenuModel::getSortNumber));
        if (menuModels == null) {
            menuModels = new ArrayList<>(0);
        }
        return menuModels;
    }


    @Override
    public List<MenuModel> searchMenu(String title, String path) {
        List<MenuModel> menuModels = menuMapper.selectList(
                new QueryWrapper<MenuModel>()
                        .lambda()
                        .like(MenuModel::getTitle, title)
                        .like(MenuModel::getPath, path));
        if (menuModels == null) menuModels = new ArrayList<>(0);
        return menuModels;
    }

    @Override
    public List<MenuModel> toMenuTree(List<MenuModel> menuModels, Integer parentId) {
        List<MenuModel> childes = new ArrayList<>();
        for (MenuModel menu : menuModels) {
            if (menu.getParent().equals(parentId)) {
                menu.setChildren(toMenuTree(menuModels, menu.getId()));
                childes.add(menu);
            }
        }
        return childes;
    }

    @Override
    @Transactional
    public void removeMenu(Integer menuId) {
        List<MenuModel> menuModels = getAllMenu();
        if (menuModels.isEmpty()) return;

        List<Integer> delIds = new ArrayList<>();
        delIds.add(menuId);
        menuModels = toMenuTree(menuModels, menuId);
        reTree(menuModels, delIds);

        //删除菜单角色映射
        roleMenuService.remove(new QueryWrapper<RoleMenuModel>().lambda().in(RoleMenuModel::getMenuId, delIds));
        //删除菜单
        menuMapper.deleteBatchIds(delIds);
    }

    private void reTree(List<MenuModel> menus, List<Integer> ids) {
        for (MenuModel menu : menus) {
            ids.add(menu.getId());
            if (!menu.getChildren().isEmpty()) {
                reTree(menu.getChildren(), ids);
            }
        }
    }
}
