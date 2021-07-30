package com.zongshuo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zongshuo.annotation.AuthDefinition;
import com.zongshuo.model.MenuModel;
import com.zongshuo.model.RoleModel;
import com.zongshuo.model.UserModel;
import com.zongshuo.model.UserRoleModel;
import com.zongshuo.service.MenuService;
import com.zongshuo.service.RoleService;
import com.zongshuo.service.UserRoleService;
import com.zongshuo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: System_info
 * date: 2021/7/20 11:08
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 */
@Slf4j
@Component
@AuthDefinition(name = "系统管理", authority = "sys", type = AuthDefinition.AuthType.MENU, path = "/system")
public class SystemInfo {
    private static final String CLASS_PATH_PRE = "classpath*:";

    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserService userService;

    @Transactional
    public void handleRootMenu(ClassLoader classLoader, ResourcePatternResolver resolver, MetadataReaderFactory reader) throws IOException, ClassNotFoundException {
        Resource[] resources = resolver.getResources(CLASS_PATH_PRE + "com/zongshuo/*.class");
        for (Resource resource : resources) {
            String className = reader.getMetadataReader(resource).getClassMetadata().getClassName();
            Class clazz = classLoader.loadClass(className);
            if (clazz.isAnnotationPresent(AuthDefinition.class)) {
                AuthDefinition authDefinition = (AuthDefinition) clazz.getDeclaredAnnotation(AuthDefinition.class);
                if (authDefinitionIsEmpty(authDefinition)) {
                    continue;
                }

                MenuModel menu = new MenuModel();
                menu.setName(authDefinition.name());
                menu.setTitle(authDefinition.name());
                menu.setAuthority(authDefinition.authority());
                menu.setPath(authDefinition.path());
                menu.setComponent(authDefinition.path());
                menu.setParent(0);
                menu.setType(AuthDefinition.AuthType.MENU.getType());
                menu.setHide(Contains.DEFAULT_NO);
                menu.setCreateTime(new Date());
                try {
                    menuService.addMenu(menu);
                    log.info("菜单已创建:{}", menu);
                } catch (IllegalAccessException e) {
                    log.error("添加目录异常:{}-{}", menu, e.getMessage());
                }
            }
        }
    }

    @Transactional
    public void handleSubMenu(ClassLoader classLoader, ResourcePatternResolver resolver, MetadataReaderFactory reader) throws IOException, ClassNotFoundException {
        // TODO 此处更新需要使用更高效的方式
        Resource[] resources = resolver.getResources(CLASS_PATH_PRE + "com/zongshuo/web/*.class");
        Map<String, Integer> subMenu = new HashMap<>();
        for (Resource resource : resources) {
            String className = reader.getMetadataReader(resource).getClassMetadata().getClassName();
            Class clazz = classLoader.loadClass(className);
            if (clazz.isAnnotationPresent(AuthDefinition.class)) {
                AuthDefinition authDefinition = (AuthDefinition) clazz.getDeclaredAnnotation(AuthDefinition.class);
                if (authDefinitionIsEmpty(authDefinition)) {
                    continue;
                }
                String parentAuth = getParentAuth(authDefinition.authority());
                setParentMap(subMenu, parentAuth);

                MenuModel menu = new MenuModel();
                menu.setName(authDefinition.name());
                menu.setTitle(authDefinition.name());
                menu.setAuthority(authDefinition.authority());
                menu.setParent(subMenu.get(parentAuth));
                menu.setPath(authDefinition.path());
                menu.setComponent(authDefinition.path());
                menu.setType(AuthDefinition.AuthType.MENU.getType());
                menu.setHide(Contains.DEFAULT_NO);
                menu.setCreateTime(new Date());
                try {
                    menuService.addMenu(menu);
                    log.info("菜单已创建:{}", menu);
                } catch (IllegalAccessException e) {
                    log.error("菜单添加异常:{}-{}", menu, e.getMessage());
                }
            }
        }
    }

    @Transactional
    public void handleMenuApi(ClassLoader classLoader, ResourcePatternResolver resolver, MetadataReaderFactory reader) throws IOException, ClassNotFoundException {
        // TODO 此处更新需要使用更高效的方式
        Resource[] resources = resolver.getResources(CLASS_PATH_PRE + "com/zongshuo/web/*.class");
        Map<String, Integer> parentMap = new HashMap<>();
        for (Resource resource : resources) {
            String className = reader.getMetadataReader(resource).getClassMetadata().getClassName();
            Class clazz = classLoader.loadClass(className);
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(AuthDefinition.class)) {
                    AuthDefinition authDefinition = method.getDeclaredAnnotation(AuthDefinition.class);
                    if (authDefinitionIsEmpty(authDefinition)) continue;

                    String parentAuth = getParentAuth(authDefinition.authority());
                    setParentMap(parentMap, parentAuth);

                    MenuModel menu = new MenuModel();
                    menu.setName(authDefinition.name());
                    menu.setAuthority(authDefinition.authority());
                    menu.setParent(parentMap.get(parentAuth));
                    menu.setType(AuthDefinition.AuthType.API.getType());
                    menu.setHide(Contains.DEFAULT_YES);
                    menu.setCreateTime(new Date());
                    try {
                        menuService.addMenu(menu);
                        log.info("API已创建:{}", menu);
                    } catch (IllegalAccessException e) {
                        log.error("API添加异常:{}-{}", menu, e.getMessage());
                    }
                }
            }
        }
    }

    private void setParentMap(Map<String, Integer> parent, String parentAuth) {
        if (!parent.containsKey(parentAuth)) {
            // 根据父权限标识获取父权限主键
            // 如果父权限不存在，就添加
            if (StringUtils.isEmpty(parentAuth)) {
                parent.put(parentAuth, 0);
            } else {
                MenuModel menu = menuService.getOne(
                        new QueryWrapper<MenuModel>()
                                .lambda().select(MenuModel::getId)
                                .eq(MenuModel::getAuthority, parentAuth));
                if (menu != null) {
                    parent.put(parentAuth, menu.getId());
                }

            }
        }
    }

    public Integer initRole() {
        QueryWrapper<RoleModel> query = new QueryWrapper();
        query.lambda()
                .select(RoleModel::getId)
                .eq(RoleModel::getRoleKey, Contains.SYS_ADMIN_NAME);
        RoleModel role = roleService.getOne(query);
        if (role == null) {
            //新增角色
            role = new RoleModel();
            role.setRoleKey(Contains.SYS_ADMIN_NAME);
            role.setRoleName(Contains.SYS_ADMIN_NAME);
            role.setDescriptions("系统超级管理员，拥有系统所有权限");
            roleService.save(role);
        }
        log.info("角色已创建:{}", role);
        return role.getId();
    }

    public void initUser(Integer roleId) {
        //新增超级管理员用户
        UserModel adminUser = userService.getOne(
                new QueryWrapper<UserModel>().lambda().eq(UserModel::getUsername, Contains.SYS_ADMIN_NAME));
        if (adminUser == null) {
            adminUser = new UserModel();
            adminUser.setUsername(Contains.SYS_ADMIN_NAME);
            adminUser.setPassword(Contains.SYS_ADMIN_PASSWD);
            adminUser.setNickName("系统管理员");
            adminUser.setEmail("1737290510@qq.com");
            adminUser.setCreateTime(new Date());
            userService.save(adminUser);
        }
        QueryWrapper<UserRoleModel> query = new QueryWrapper<>();
        query.lambda().eq(UserRoleModel::getUserId, adminUser.getId()).eq(UserRoleModel::getRoleId, roleId);
        if (userRoleService.count(query) > 0) return;

        UserRoleModel userRole = new UserRoleModel();
        userRole.setRoleId(roleId);
        userRole.setUserId(adminUser.getId());
        userRoleService.save(userRole);
    }

    private boolean authDefinitionIsEmpty(AuthDefinition authDefinition) {
        return StringUtils.isBlank(authDefinition.name())
                || StringUtils.isBlank(authDefinition.authority());
    }

    private String getParentAuth(String auth) {
        if (StringUtils.isEmpty(auth)) return "";
        int lastIndex = auth.lastIndexOf(':');
        if (lastIndex < 1) return "";

        return auth.substring(0, lastIndex);
    }
}
