package com.zongshuo.authorization;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * ClassName: AuthUtil
 * date: 2021/7/28 16:24
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 */
public final class AuthUtil {
    private AuthUtil(){}

    /**
     * 将注解中的属性值映射到对象中，无法映射对象父类中的属性
     * @param annotation
     * @param obj
     */
    public static void mapAnnotation2Obj(Annotation annotation, Object obj){
        Method [] methods = annotation.annotationType().getDeclaredMethods();
        for (Method method : methods){
            method.setAccessible(true);
            try {
                Field field = obj.getClass().getDeclaredField(method.getName());
                field.setAccessible(true);
                field.set(obj, method.invoke(annotation));
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
