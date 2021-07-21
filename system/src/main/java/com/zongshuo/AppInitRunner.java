package com.zongshuo;

import com.zongshuo.service.RoleMenuService;
import com.zongshuo.service.RoleService;
import com.zongshuo.service.UserRoleService;
import com.zongshuo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;


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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ClassLoader classLoader = AppInitRunner.class.getClassLoader();
        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        MetadataReaderFactory metadataReader = new CachingMetadataReaderFactory(resourceLoader);
        try {
            // 首先初始化管理员角色，添加菜单时默认给管理员添加
            Integer roleId = systemInfo.initRole();
            systemInfo.handleRootMenu(classLoader, resolver, metadataReader);
            systemInfo.handleSubMenu(classLoader, resolver, metadataReader);
            systemInfo.handleMenuApi(classLoader, resolver, metadataReader);
            // 同时增加用户角色
            systemInfo.initUser(roleId);
        } catch (Exception e) {
            log.error("系统初始化异常:", e);
        }
    }
}
