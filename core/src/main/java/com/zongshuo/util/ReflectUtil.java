package com.zongshuo.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * ClassName: ReflectUtil
 * date: 2021/7/26 14:27
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 */
public final class ReflectUtil {
    private ReflectUtil(){}

    public static Method getMethodByName(Class clazz, String name){
        Method method ;
        try {
            method = clazz.getDeclaredMethod(name);
            method.setAccessible(true);
        } catch (NoSuchMethodException e) {
            return null;
        }

        return method;
    }

    public static Object invoke(Object baseObj, Method method){
        try {
             return method.invoke(baseObj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return e;
        }
    }
}
