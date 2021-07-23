package com.zongshuo;

import com.zongshuo.annotations.AuthDefinition;
import com.zongshuo.util.AnnotationUtil;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-20
 * @Time: 10:42
 * @Description:
 */
@Slf4j
@MapperScan("com.zongshuo.mapper")
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
//        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        ClassLoader classLoader = Application.class.getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory metadataReader = new CachingMetadataReaderFactory(resolver);
        try {
            Resource[] resources = resolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "com/zongshuo/**/*.class");
            for (Resource resource : resources) {
                String className = metadataReader.getMetadataReader(resource).getClassMetadata().getClassName();
                Class clazz = classLoader.loadClass(className);
                if (clazz.isAnnotationPresent(AuthDefinition.class)) {
                    AuthDefinition authDefinition = (AuthDefinition) clazz.getAnnotation(AuthDefinition.class);
                    PreAuthorize preAuthorize = authDefinition.annotationType().getAnnotation(PreAuthorize.class);
                    Map<String, Object> memberValues = new LinkedHashMap<>();
                    memberValues.put("value", "hasAuthority('"+authDefinition.authority()+"')");
                    PreAuthorize proxyPreAuthorize = AnnotationUtil.newAnnotation(PreAuthorize.class, memberValues);
                    InvocationHandler invocationHandler = Proxy.getInvocationHandler(preAuthorize);
                    Field field = invocationHandler.getClass().getDeclaredField("memberValues");
//                    field.setAccessible(true);
//                    Map<String, String> memberValues = (Map<String, String>) field.get(invocationHandler);

                }
            }
        } catch (Exception e) {
            log.error("异常", e);
        }
        SpringApplication.run(Application.class, args);
    }
}
