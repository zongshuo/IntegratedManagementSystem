package com.zongshuo.authorization.handler;

import com.zongshuo.authorization.AuthUtil;
import com.zongshuo.authorization.model.AccessPoint;
import com.zongshuo.authorization.model.AccessType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: AuthServiceBase
 * date: 2021/7/28 14:53
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 * 权限服务基础实现，只完成权限点收集工作
 */
@Slf4j
class AuthServiceImplBase implements AuthService{
    protected Class<? extends Annotation> accessDefinitionAnnotation ;
    public AuthServiceImplBase(Class<? extends Annotation> accessAnnotation){
        this.accessDefinitionAnnotation = accessAnnotation;
    }

    @Override
    public Map<AccessType, List<AccessPoint>> collectAccessPoint(String... packageNames) throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resolver);
        ClassLoader classLoader = this.getClass().getClassLoader();
        if (packageNames.length ==0){
            packageNames = new String[1];
            packageNames[0] = resolver.CLASSPATH_ALL_URL_PREFIX + "/**/";
        }

        Map<AccessType, List<AccessPoint>> accessPointMap = new HashMap<>();
        Resource [] resources ;
        Method [] methods ;
        String className ;
        Class clazz ;
        for (String pkg : packageNames){
            if (! pkg.endsWith("/")){
                pkg += "/";
            }
             resources = resolver.getResources(pkg + "*.class");
            for (Resource resource : resources){
                className = readerFactory.getMetadataReader(resource).getClassMetadata().getClassName();
                try {
                    clazz = classLoader.loadClass(className);
                    setAccessPoint(accessPointMap, clazz);
                    methods = clazz.getDeclaredMethods();
                    for (Method method : methods){
                        setAccessPoint(accessPointMap, method);
                    }
                } catch (Exception e) {
                    log.error("access point collection exception:", e);
                }
            }
        }
        return accessPointMap;
    }

    protected void setAccessPoint(Map<AccessType, List<AccessPoint>> accessPointMap, AnnotatedElement element) {
        if (element.isAnnotationPresent(accessDefinitionAnnotation)){
            Object annotation = element.getAnnotation(accessDefinitionAnnotation);
            AccessPoint accessPoint = new AccessPoint();
            AuthUtil.mapAnnotation2Obj(accessDefinitionAnnotation.cast(annotation), accessPoint);
            if (!accessPointMap.containsKey(accessPoint.getType())){
                accessPointMap.put(accessPoint.getType(), new ArrayList<>());
            }
            accessPointMap.get(accessPoint.getType()).add(accessPoint);
        }
    }


}
