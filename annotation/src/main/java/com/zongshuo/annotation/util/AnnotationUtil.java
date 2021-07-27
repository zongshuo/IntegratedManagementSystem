package com.zongshuo.annotation.util;

import com.zongshuo.annotation.handler.AnnotationData;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * ClassName: AnnotationUtil
 * date: 2021/7/23 8:54
 *
 * @author zongshuo
 * version: 1.0
 * Description: 处理注解的工具类
 */
public final class AnnotationUtil {
    private AnnotationUtil(){}

    /**
     * 获取注解的属性值
     * 以map格式返回：key-属性名、value-属性值
     * 通过操作返回的map可以变更注解的属性值
     * @param annotation
     * @return
     */
    public static Map<String, Object> getMemberValues(final Annotation annotation){
        InvocationHandler handler = Proxy.getInvocationHandler(annotation);
        try {
            Field field = handler.getClass().getDeclaredField("memberValues");
            field.setAccessible(true);
            return (Map<String, Object>) field.get(handler);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Get annotation member values failed") ;
        }
    }

    /**
     * 慎用
     * 获取类的注解存储对象(Class.AnnotationData)
     * 该对象为Class的内部私有静态类
     * 该对象内部包含当前类拥有的注解的所有实例
     * 通过操作该对象内注解的实例可以动态操作类的注解
     * 该对象(AnnotationData)有三个属性：
     *    Map<Class<? extends Annotation>, Annotation> annotations:当前类及其父类拥有的所有注解，包含declaredAnnotations
     *    Map<Class<? extends Annotation>, Annotation> declaredAnnotations:当前类拥有的注解
     *    int redefinedCount: 当调用Class.annotationData方法时会判断该属性与Class.classRedefinedCount是否相等，
     *    如果不相等会重新创建AnnotationData对象，如果相等就直接返回Class.annotationData属性
     * @param clazz
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Object getAnnotationData(final Class clazz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = Class.class.getDeclaredMethod("annotationData");
        method.setAccessible(true);
        return method.invoke(clazz);
    }

    /**
     * 判断当前值是否为属性默认值
     * @param method
     * @param value
     * @return
     */
    public static boolean isDefaultValue(final Method method, final Object value){
        final Object defaultValue = method.getDefaultValue();
        if (method.getReturnType().isArray()){
            return Arrays.equals((Object []) defaultValue, (Object []) value);
        }
        return defaultValue.equals(value);
    }
    public static boolean isDefaultValue(final Method method, final Map<String, Object> memberValues){
        return isDefaultValue(method, memberValues.get(method.getName()));
    }

    /**
     * 判断注解类型是否JDK注解
     * @param type
     * @return
     */
    public static boolean isJdkAnnotation(final Class<? extends Annotation> type){
        return type == Documented.class ||
                type == Target.class ||
                type == Retention.class ||
                type == Inherited.class ||
                type == Repeatable.class ||
                type == Native.class ;
    }

    /**
     * 生成新的动态代理
     * @param type
     * @param values
     * @param <A>
     * @return
     */
    public static <A extends Annotation> A newAnnotation(final Class<A> type, final Map<String, Object> values){
        return (A) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, new AnnotationInvocationHandler(type, values));
    }

    /**
     * 递归获取当前类的所有注解
     * 包括被合并的注解
     * @param element
     * @return
     */
    public static Map<Class<? extends Annotation>, List<Annotation>> getAnnotations(AnnotatedElement element){
        Map<Class<? extends Annotation>, List<Annotation>> result = new HashMap<>();
        recurrenceAnnotations(element.getAnnotations(), result);
        return  result;
    }
    private static void recurrenceAnnotations(Annotation [] annotations, Map<Class<? extends Annotation>, List<Annotation>> baseMap){
        if (baseMap == null) baseMap = new HashMap<>();
        if (annotations == null || annotations.length ==0) return ;

        for (Annotation annotation : annotations){
            if (isJdkAnnotation(annotation.annotationType())) continue;
            if (!baseMap.containsKey(annotation.annotationType())){
                baseMap.put(annotation.annotationType(), new ArrayList<>());
            }
            baseMap.get(annotation.annotationType()).add(annotation);

            recurrenceAnnotations(annotation.annotationType().getAnnotations(), baseMap);
        }
    }

    /**
     * 返回所有约定类型的注解
     * @param targetClass
     * @param targetType
     * @return
     */
    public static <A extends Annotation> List<A> getAnnotations(
            Class targetClass,
            Class<? extends Annotation> targetType){
        List<Annotation> annotations = new ArrayList<>();
        Annotation [] annotationArray = targetClass.getAnnotations();
        for (Annotation annotation : annotationArray){
            if (annotation.annotationType() == targetType) {
               Collections.addAll(annotations, targetClass.getAnnotationsByType(annotation.annotationType()));
            }
            annotations.addAll(getAnnotations(annotation.annotationType(), targetType));
        }

        return (List<A>) annotations;
    }

    /**
     * 从当前类开始向父级注解查找，直到找到需要替换的注解
     * 找不到返回false
     * @param targetClass
     * @param annotation
     * @return
     */
    public static boolean resetFirstAnnotation(Class targetClass, Annotation annotation){
        try {
            AnnotationData data = AnnotationData.getInstance(targetClass);
            for (Map.Entry<Class<? extends Annotation>, Annotation> entry : data.getAnnotations().entrySet()){
                if (isJdkAnnotation(entry.getKey())) continue;
                if (entry.getKey() == annotation.annotationType()){
                    entry.setValue(annotation);
                    return true;
                }

                if (resetFirstAnnotation(entry.getValue().annotationType(), annotation)){
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }
    public static boolean resetFirstAnnotation(AnnotatedElement element, Annotation annotation){
        return resetFirstAnnotation(element.getClass(), annotation);
    }
}


