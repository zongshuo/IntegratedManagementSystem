package com.zongshuo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zongshuo.authorization.model.AccessPoint;
import com.zongshuo.authorization.model.AccessType;
import com.zongshuo.model.MenuModel;
import com.zongshuo.model.RoleModel;
import com.zongshuo.model.UserModel;
import com.zongshuo.model.UserRoleModel;
import com.zongshuo.service.MenuService;
import com.zongshuo.service.RoleService;
import com.zongshuo.service.UserRoleService;
import com.zongshuo.service.UserService;
import com.zongshuo.util.MenuSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-6-12
 * @Time: 16:54
 * @Description: 用于系统启动后初始化系统参数
 */
@Slf4j
@Component
public class AppInitRunner implements ApplicationRunner{
    public static Map<AccessType, List<AccessPoint>> accessPointMap;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            // 初始化管理员角色，添加菜单时默认给管理员添加
            RoleModel role = initRole();
            // 同时增加用户角色
            initUser(role.getId());
            // 收集系统权限并创建鉴权注解
            initAccess();
        } catch (Exception e) {
            log.error("系统初始化异常:", e);
        }
    }

    private void initAccess(){
        if (accessPointMap.isEmpty()) return;
        /**
         * 首先将数据库中菜单来源为系统收集记录的updateTime置为空
         * 更新权限点,先insert，再update。两者都更新updateTime
         * 最后删除菜单来源为系统收集且updateTime为空的记录
         */
        menuService.update(
                new UpdateWrapper<MenuModel>()
                        .lambda()
                        .set(MenuModel::getUpdateTime, null)
                        .eq(MenuModel::getSource, MenuSource.SYSTEM.getValue()));

        saveAccess(accessPointMap.get(AccessType.MODULE));
        saveAccess(accessPointMap.get(AccessType.MENU));
        saveAccess(accessPointMap.get(AccessType.API));
        saveAccess(accessPointMap.get(AccessType.BUTTON));

        menuService.clearAccessPoint();
    }

    private void saveAccess(List<AccessPoint> points) {
        if (points == null || points.isEmpty()) return;

        Map<String, Integer> parentAuthMap = new HashMap<>();
        String parentAuth ;
        for (AccessPoint point : points) {
            parentAuth = getParentAuth(point.getAuthority());
            setParentMap(parentAuthMap, parentAuth);
            MenuModel menu = new MenuModel();
            menu.setName(point.getName());
            menu.setAuthority(point.getAuthority());
            menu.setParent(parentAuthMap.get(parentAuth));
            menu.setType(point.getType().getType());
            try {
                menuService.addAccessPoint(menu);
                log.info("添加权限点:{}", menu.getName());
            } catch (Exception e) {
                log.error("添加权限点异常:{}", menu, e.getMessage());
            }
        }
    }
    private String getParentAuth(String auth) {
        if (StringUtils.isEmpty(auth)) return "";
        int lastIndex = auth.lastIndexOf(':');
        if (lastIndex < 1) return "";

        return auth.substring(0, lastIndex);
    }
    private void setParentMap(Map<String, Integer> parent, String parentAuth) {
        // 如果父权限不存在，就添加
        if (!parent.containsKey(parentAuth)) {
            // 如果父权限为空，设置父权限主键为0
            if (StringUtils.isEmpty(parentAuth)) {
                parent.put(parentAuth, 0);
                return;
            }

            // 根据父权限标识获取父权限主键
            MenuModel menu = menuService.getOne(
                    new QueryWrapper<MenuModel>()
                            .lambda().select(MenuModel::getId)
                            .eq(MenuModel::getAuthority, parentAuth));
            if (menu != null) {
                parent.put(parentAuth, menu.getId());
            }
        }
    }

    private RoleModel initRole() {
        RoleModel role =
                roleService.getOne(
                        new QueryWrapper<RoleModel>()
                                .lambda()
                                .select(RoleModel::getId)
                                .eq(RoleModel::getRoleKey, Contains.SYS_ADMIN_NAME));
        if (role == null) {
            //新增角色
            role = new RoleModel();
            role.setRoleKey(Contains.SYS_ADMIN_NAME);
            role.setRoleName(Contains.SYS_ADMIN_NAME);
            role.setDescriptions("系统超级管理员，拥有系统所有权限");
            roleService.save(role);
        }
        log.info("角色已创建:{}", role);
        return role;
    }
    private void initUser(Integer roleId) {
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
}
