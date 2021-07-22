package com.zongshuo.security;

import com.zongshuo.AppInitRunner;
import com.zongshuo.annotations.AuthDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * ClassName: UserPermissionEvaluator
 * date: 2021/7/21 15:26
 *
 * @author zongshuo
 * version: 1.0
 * Description: 自定义授权控制，判断当前用户是否有资源访问权限
 */
@Slf4j
//@Component
public class UserPermissionEvaluator{
//    @Autowired
//    private ResourceLoader resourceLoader;

    public void run() throws Exception {
        ClassLoader classLoader = AppInitRunner.class.getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReader = new CachingMetadataReaderFactory(resolver);
        try {
            Resource[] resources = resolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "com/zongshuo/**/*.class");
            for (Resource resource : resources) {
                String className = metadataReader.getMetadataReader(resource).getClassMetadata().getClassName();
                Class clazz = classLoader.loadClass(className);
                if (clazz.isAnnotationPresent(AuthDefinition.class)) {
                    AuthDefinition authDefinition = (AuthDefinition) clazz.getDeclaredAnnotation(AuthDefinition.class);
                    PreAuthorize preAuthorize = authDefinition.annotationType().getDeclaredAnnotation(PreAuthorize.class);
                    InvocationHandler invocationHandler = Proxy.getInvocationHandler(preAuthorize);
                    Field field = invocationHandler.getClass().getDeclaredField("memberValues");
                    field.setAccessible(true);
                    Map<String, String> memberValues = (Map<String, String>) field.get(invocationHandler);
                    memberValues.put("value", "hasAuthority('" + authDefinition.authority() + "')");
                }
            }
        } catch (Exception e) {

        }
    }
}
