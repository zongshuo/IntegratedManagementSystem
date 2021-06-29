package com.zongshuo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zongshuo.model.*;
import com.zongshuo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-6-12
 * @Time: 16:54
 * @Description:
 * 用于系统启动后初始化系统参数
 */
@Component
public class AppInitRunner implements ApplicationRunner {
    @Autowired
    private UserService userModelService;
    @Autowired
    private MenuService menuModelService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private UserRoleService userRoleService;

    private List<Integer> menuIds = new ArrayList<>();
    private Integer roleId ;
    private Integer userId;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try{
            // todo 此处转成投产sql脚本
            initUser();
            initMenu();
            initRole();
            intiRoleMenu();
            initUserRole();
        }catch (Exception e){
            //TODO 处理异常
        }
    }

    private boolean initUserRole() {
        if (userId == null || roleId == null) return true;
        UserRoleModel userRoleModel = new UserRoleModel();
        userRoleModel.setRoleId(roleId);
        userRoleModel.setUserId(userId);
        userRoleService.saveOrUpdate(userRoleModel);
        return false;
    }

    private boolean intiRoleMenu() {
        if (roleId == null) return true;
        List<RoleMenuModel> roleMenuModels = new ArrayList<>();
        for (Integer id : menuIds){
            RoleMenuModel roleMenuModel = new RoleMenuModel();
            roleMenuModel.setMenuId(id);
            roleMenuModel.setRoleId(roleId);
            roleMenuModels.add(roleMenuModel);
        }
        roleMenuService.saveOrUpdateBatch(roleMenuModels);
        return false;
    }

    private void initRole() {
        //新增角色
        RoleModel role = new RoleModel();
        role.setRoleKey(Contains.SYS_ADMIN_NAME);
        role.setRoleName(Contains.SYS_ADMIN_NAME);
        role.setDescriptions("系统超级管理员，拥有系统所有权限");
        roleService.save(role);
        roleId = role.getId();
    }

    private void initMenu() throws IllegalAccessException {
        //新增系统管理菜单
        MenuModel menuModel = new MenuModel();
        menuModel.setParentId(0);
        menuModel.setTitle(Contains.MENU_SYS_ADMIN_NAME);
        menuModel.setPath("/system");
        menuModel.setSortNumber(1);
        menuModelService.addMenu(menuModel);
        menuIds.add(menuModel.getMenuId());

        menuModel.setTitle("菜单管理");
        menuModel.setSortNumber(1);
        menuModel.setPath("/system/menu");
        menuModel.setComponent("/system/menu");
        menuModel.setParentId(menuIds.get(0));
        menuModel.setMenuId(null);
        menuModelService.addMenu(menuModel);
        menuIds.add(menuModel.getMenuId());

        menuModel.setTitle("角色管理");
        menuModel.setSortNumber(2);
        menuModel.setPath("/system/role");
        menuModel.setComponent("/system/role");
        menuModel.setParentId(menuIds.get(0));
        menuModel.setMenuId(null);
        menuModelService.addMenu(menuModel);
        menuIds.add(menuModel.getMenuId());
    }

    private void initUser() {
        //新增超级管理员
        UserModel adminUser = userModelService.getOne(
                new QueryWrapper<UserModel>().lambda().eq(UserModel::getUsername, Contains.SYS_ADMIN_NAME));
        if (adminUser == null) {
            adminUser = new UserModel();
            adminUser.setUsername(Contains.SYS_ADMIN_NAME);
            adminUser.setPassword(new BCryptPasswordEncoder().encode(Contains.SYS_ADMIN_PASSWORD));
            adminUser.setNickName("系统管理员");
            adminUser.setEmail("1737290510@qq.com");
            adminUser.setCreateTime(new Date());
            userModelService.save(adminUser);
            userId = adminUser.getId();
        }
    }
}
