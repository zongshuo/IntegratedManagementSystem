package com.zongshuo.authorization.handler;

import com.zongshuo.authorization.AuthUtil;
import com.zongshuo.authorization.model.AccessPoint;
import com.zongshuo.authorization.model.AccessType;
import lombok.extern.slf4j.Slf4j;
import priv.zongshuo.annotation.handler.AnnotationService;
import priv.zongshuo.annotation.util.AnnotationHandlerException;

import javax.management.modelmbean.InvalidTargetObjectTypeException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * ClassName: AuthServiceImplInitAuth
 * date: 2021/7/28 18:04
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 * 收集权限点
 * 同时给权限点增加鉴权注解
 * 注解的属性值从权限点注解的属性中获取
 * 通过MapToAuth注解映射权限点注解与鉴权注解之间关系，包括注解名称和属性名称
 */
@Slf4j
class AuthServiceImplInitAuth extends AuthServiceImplBase {
    public AuthServiceImplInitAuth(Class<? extends Annotation> accessAnnotation) {
        super(accessAnnotation);
    }

    protected void setAccessPoint(Map<AccessType, List<AccessPoint>> accessPointMap, AnnotatedElement element) {
        if (element.isAnnotationPresent(accessDefinitionAnnotation)) {
            Object annotation = element.getAnnotation(accessDefinitionAnnotation);
            AccessPoint accessPoint = new AccessPoint();
            AuthUtil.mapAnnotation2Obj(accessDefinitionAnnotation.cast(annotation), accessPoint);
            if (!accessPointMap.containsKey(accessPoint.getType())) {
                accessPointMap.put(accessPoint.getType(), new ArrayList<>());
            }
            accessPointMap.get(accessPoint.getType()).add(accessPoint);

            Map<Class<? extends Annotation>, LinkedHashMap<String, Object>> buildAnnotationMap = new HashMap<>();
            setMapAnnotation(Annotation.class.cast(annotation), buildAnnotationMap);
            buildAnnotation(buildAnnotationMap, element);
        }
    }

    private void setMapAnnotation(Annotation annotation, Map<Class<? extends Annotation>, LinkedHashMap<String, Object>> buildAnnotationMap) {
        if (buildAnnotationMap == null) buildAnnotationMap = new HashMap<>();
        if (annotation == null) return;

        Method[] methods = annotation.annotationType().getDeclaredMethods();
        for (Method method : methods) {
            try {
                MapToAuth[] mapToAuths = method.getAnnotationsByType(MapToAuth.class);
                for (MapToAuth mapToAuth : mapToAuths) {
                    if (!buildAnnotationMap.containsKey(mapToAuth.authAnnotation())) {
                        buildAnnotationMap.put(mapToAuth.authAnnotation(), new LinkedHashMap<>());
                    }
                    FieldFormat format = mapToAuth.format().newInstance();
                    buildAnnotationMap.get(mapToAuth.authAnnotation()).put(
                            "".equals(mapToAuth.name()) ? method.getName() : mapToAuth.name(),
                            format.format(method.invoke(annotation)));
                }
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
                log.error("收集需要新增鉴权注解异常:", e);
            }
        }
    }

    private void buildAnnotation(Map<Class<? extends Annotation>, LinkedHashMap<String, Object>> buildAnnotationMap, AnnotatedElement element) {
        AnnotationService service = AnnotationService.from(element);
        for (Map.Entry<Class<? extends Annotation>, LinkedHashMap<String, Object>> entry : buildAnnotationMap.entrySet()) {
            try {
                service.addAnnotation(entry.getKey(), entry.getValue());
            } catch (AnnotationHandlerException e) {
                log.error("权限收集添加鉴权注解异常:", e);
            }
        }
    }
}
