package com.zongshuo;

import com.zongshuo.annotations.AuthDefinition;
import com.zongshuo.service.RoleMenuService;
import com.zongshuo.service.RoleService;
import com.zongshuo.service.UserRoleService;
import com.zongshuo.service.UserService;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
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
public class AppInitRunner implements ApplicationRunner, ResourceLoaderAware {
    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Autowired
    private SystemInfo systemInfo;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ClassLoader classLoader = AppInitRunner.class.getClassLoader();
        ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        MetadataReaderFactory metadataReader = new CachingMetadataReaderFactory(resourceLoader);
        try {
//            Resource[] resources = resolver.getResources("classpath*:com/zongshuo/**/*.class");
//            for (Resource resource : resources){
//                String className = metadataReader.getMetadataReader(resource).getClassMetadata().getClassName();
//                Class clazz = classLoader.loadClass(className);
//                if (clazz.isAnnotationPresent(AuthDefinition.class)) {
//                    AuthDefinition authDefinition = (AuthDefinition) clazz.getDeclaredAnnotation(AuthDefinition.class);
//                    PreAuthorize preAuthorize = authDefinition.annotationType().getDeclaredAnnotation(PreAuthorize.class);
//                    InvocationHandler invocationHandler = Proxy.getInvocationHandler(preAuthorize);
//                    Field field = invocationHandler.getClass().getDeclaredField("memberValues");
//                    field.setAccessible(true);
//                    Map<String, String> memberValues = (Map<String, String>) field.get(invocationHandler);
//                    memberValues.put("value", "hasAuthority('"+authDefinition.authority()+"')");
//                }
//            }
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
