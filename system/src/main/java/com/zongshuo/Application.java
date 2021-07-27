package com.zongshuo;

import com.zongshuo.annotation.annotations.AuthDefinition;
import com.zongshuo.util.annotation.AnnotationService;
import com.zongshuo.util.annotation.AnnotationUtil;
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

import java.lang.reflect.Method;
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
                    Map<String, Object> memberValues = new LinkedHashMap<>();
                    memberValues.put("value", "hasAuthority('"+authDefinition.authority()+"')");
                    PreAuthorize proxyPreAuthorize = AnnotationUtil.newAnnotation(PreAuthorize.class, memberValues);
                    AnnotationService service = AnnotationService.from(clazz);
                    service.addAnnotation(proxyPreAuthorize);

                    Method [] methods = clazz.getDeclaredMethods();
                    for (Method method : methods){
                        if (method.isAnnotationPresent(AuthDefinition.class)){
                            authDefinition = method.getAnnotation(AuthDefinition.class);
                            memberValues.put("value", "hasAuthority('"+authDefinition.authority()+"')");
                            proxyPreAuthorize = AnnotationUtil.newAnnotation(PreAuthorize.class, memberValues);
                            service = AnnotationService.from(method);
                            service.addAnnotation(proxyPreAuthorize);
                        }
                    }

                }
            }
        } catch (Exception e) {
            log.error("异常", e);
        }
        SpringApplication.run(Application.class, args);
    }
}
