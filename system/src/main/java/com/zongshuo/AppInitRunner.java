package com.zongshuo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zongshuo.annotations.AuthDefinition;
import com.zongshuo.entity.RoleMenu;
import com.zongshuo.model.*;
import com.zongshuo.service.*;
import com.zongshuo.util.EnumAuthType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;


/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-6-12
 * @Time: 16:54
 * @Description: 用于系统启动后初始化系统参数
 */
@Slf4j
@Component
public class AppInitRunner implements ApplicationRunner, ResourceLoaderAware {
    private ResourceLoader resourceLoader;


    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Autowired
    private UserService userModelService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private SystemInfo systemInfo;

    private List<Integer> menuIds = new ArrayList<>();
    private Integer roleId;
    private Integer userId;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ClassLoader classLoader = AppInitRunner.class.getClassLoader();
        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        MetadataReaderFactory metadataReader = new CachingMetadataReaderFactory(resourceLoader);
        try {
            // 首先初始化管理员角色，添加菜单时默认给管理员添加
            initRole();
            systemInfo.handleRootMenu(classLoader, resolver, metadataReader, this.roleId);
            systemInfo.handleSubMenu(classLoader, resolver, metadataReader, this.roleId);
            // todo 此处转成投产sql脚本
//            initUser();
//            initMenu();
//            intiRoleMenu();
//            initUserRole();
        } catch (Exception e) {
            log.error("系统初始化异常:", e);
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
        for (Integer id : menuIds) {
            RoleMenuModel roleMenuModel = new RoleMenuModel();
            roleMenuModel.setMenuId(id);
            roleMenuModel.setRoleId(roleId);
            roleMenuModels.add(roleMenuModel);
        }
        roleMenuService.saveOrUpdateBatch(roleMenuModels);
        return false;
    }

    private void initRole() {
        QueryWrapper<RoleModel> query = new QueryWrapper();
        query.lambda()
                .select(RoleModel::getId)
                .eq(RoleModel::getRoleKey, Contains.SYS_ADMIN_NAME);
        RoleModel role = roleService.getOne(query);
        if (role != null){
            this.roleId = role.getId();
            return;
        }
        //新增角色
        role = new RoleModel();
        role.setRoleKey(Contains.SYS_ADMIN_NAME);
        role.setRoleName(Contains.SYS_ADMIN_NAME);
        role.setDescriptions("系统超级管理员，拥有系统所有权限");
        roleService.save(role);
    }

    private void initUser() {
        //新增超级管理员
        UserModel adminUser = userModelService.getOne(
                new QueryWrapper<UserModel>().lambda().eq(UserModel::getUsername, Contains.SYS_ADMIN_NAME));
        if (adminUser == null) {
            adminUser = new UserModel();
            adminUser.setUsername(Contains.SYS_ADMIN_NAME);
            adminUser.setPassword(Contains.SYS_ADMIN_PASSWD);
            adminUser.setNickName("系统管理员");
            adminUser.setEmail("1737290510@qq.com");
            adminUser.setCreateTime(new Date());
            userModelService.save(adminUser);
            userId = adminUser.getId();
        }
    }
}
