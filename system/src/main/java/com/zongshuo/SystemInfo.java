package com.zongshuo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zongshuo.annotations.AuthDefinition;
import com.zongshuo.model.MenuModel;
import com.zongshuo.service.MenuService;
import com.zongshuo.util.EnumAuthType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
@AuthDefinition(name = "系统管理", authority = "sys")
public class SystemInfo {
    private static final String CLASS_PATH_PRE = "classpath*:";

    @Autowired
    private MenuService menuService;

    @Transactional
    public void handleRootMenu(ClassLoader classLoader, ResourcePatternResolver resolver, MetadataReaderFactory reader, Integer roleId) throws IOException, ClassNotFoundException {
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
                menu.setParent(0);
                menu.setType(EnumAuthType.MENU.getType());
                menu.setCreateTime(new Date());
                try {
                    menuService.addMenu(menu);
                } catch (IllegalAccessException e) {
                    log.error("添加目录异常:{}-{}", menu, e.getMessage());
                }
            }
        }
    }

    @Transactional
    public void handleSubMenu(ClassLoader classLoader, ResourcePatternResolver resolver, MetadataReaderFactory reader, Integer roleId) throws IOException, ClassNotFoundException {
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
                if (!subMenu.containsKey(authDefinition.parentAuth())) {
                    // 根据父权限标识获取父权限主键
                    // 如果父权限不存在直接指定为0
                    MenuModel menu = menuService.getOne(
                            new QueryWrapper<MenuModel>()
                                    .lambda()
                                    .eq(MenuModel::getAuthority, authDefinition.parentAuth()));
                    subMenu.put(authDefinition.parentAuth(), 0);
                    if (menu != null) {
                        subMenu.put(authDefinition.parentAuth(), menu.getId());
                    }
                }

                MenuModel menu = new MenuModel();
                menu.setName(authDefinition.name());
                menu.setTitle(authDefinition.name());
                menu.setAuthority(authDefinition.authority());
                menu.setParent(subMenu.get(authDefinition.parentAuth()));
                menu.setType(EnumAuthType.MENU.getType());
                menu.setCreateTime(new Date());
                try {
                    menuService.addMenu(menu);
                } catch (IllegalAccessException e) {
                    log.error("菜单添加异常:{}-{}", menu, e.getMessage());
                }
            }
        }
    }

    private boolean authDefinitionIsEmpty(AuthDefinition authDefinition){
        return StringUtils.isBlank(authDefinition.name())
                || StringUtils.isBlank(authDefinition.authority());
    }
}
