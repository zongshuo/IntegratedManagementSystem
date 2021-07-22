package com.zongshuo;

import com.zongshuo.annotations.AuthDefinition;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-20
 * @Time: 10:42
 * @Description:
 */
@MapperScan("com.zongshuo.mapper")
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ClassLoader classLoader = Application.class.getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReader = new CachingMetadataReaderFactory(resolver);
        try {
            Resource[] resources = resolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "com/zongshuo/**/*.class");
            for (Resource resource : resources) {
                String className = metadataReader.getMetadataReader(resource).getClassMetadata().getClassName();
                Class clazz = classLoader.loadClass(className);
                if (clazz.isAnnotationPresent(AuthDefinition.class)) {
                    AuthDefinition authDefinition = (AuthDefinition) clazz.getDeclaredAnnotation(AuthDefinition.class);
//                    InvocationHandler authHandler = Proxy.getInvocationHandler(authDefinition);
//                    PreAuthorize tempPre = authHandler.getClass().getDeclaredAnnotation(PreAuthorize.class);
                    PreAuthorize preAuthorize = authDefinition.annotationType().getDeclaredAnnotation(PreAuthorize.class);
                    Method method = preAuthorize.getClass().getMethod("value");
                    InvocationHandler invocationHandler = Proxy.getInvocationHandler(preAuthorize);
                    method.invoke(invocationHandler, "hasAuthority('" + authDefinition.authority() + "')");
//                    Field field = invocationHandler.getClass().getDeclaredField("memberValues");
//                    field.setAccessible(true);
//                    Map<String, String> memberValues = (Map<String, String>) field.get(invocationHandler);
//                    memberValues.put("value", "hasAuthority('" + authDefinition.authority() + "')");
                }
            }
        } catch (Exception e) {

        }
        SpringApplication.run(Application.class, args);
    }
}
