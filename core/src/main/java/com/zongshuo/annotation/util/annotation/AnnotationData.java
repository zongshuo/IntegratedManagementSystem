package com.zongshuo.annotation.util.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * ClassName: AnnotationData
 * date: 2021/7/26 9:26
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 * 本类是Class.AnnotationData类的代理类
 * 我们设置本代理类，类内含有annotationDataFromClass属性是Class.AnnotationData对象的引用
 * 通过操作以下三个属性可以实现对Class.AnnotationData对象的操作。
 * Class.AnnotationData是Class的私有类，无法直接访问。
 * annotations包含当前类及其父类拥有的注解
 * declaredAnnotations包含当前类直接拥有的注解
 * redefinedCountField当调用Class.annotationData方法时会判断该属性与Class.classRedefinedCount是否相等，
 *     如果不相等会重新创建AnnotationData对象，如果相等就直接返回Class.annotationData属性
 */
public class AnnotationData {
    private final Map<Class<? extends Annotation>, Annotation> annotations;
    private final Map<Class<? extends Annotation>, Annotation> declaredAnnotations;
    private final Field redefinedCountField;
    private final Object annotationDataFromClass ;

    private AnnotationData(Object annotationDataFromClass) throws NoSuchFieldException, IllegalAccessException {
        // Class.AnnotationData对象包含三个属性：Annotations、DeclaredAnnotations、redefinedCountField
        // 分别获取三个属性，并将获取的属性赋值给当前类属性，形成代理模式
        Field annotationsField = annotationDataFromClass.getClass().getDeclaredField("annotations");
        Field declaredAnnotationsField = annotationDataFromClass.getClass().getDeclaredField("declaredAnnotations");
        this.redefinedCountField = annotationDataFromClass.getClass().getDeclaredField("redefinedCount");
        this.redefinedCountField.setAccessible(true);
        this.annotationDataFromClass = annotationDataFromClass;

        annotationsField.setAccessible(true);
        this.annotations = (Map<Class<? extends Annotation>, Annotation>) annotationsField.get(annotationDataFromClass);

        declaredAnnotationsField.setAccessible(true);
        this.declaredAnnotations = (Map<Class<? extends Annotation>, Annotation>) declaredAnnotationsField.get(annotationDataFromClass);
    }
    public static AnnotationData getInstance(final AnnotatedElement element) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        // annotationData是Class的，只能通过Class反射获取该方法
        // 调用该方法后可以获取对象的annotationData对象
        Method method = Class.class.getDeclaredMethod("annotationData");
        method.setAccessible(true);
        return new AnnotationData(method.invoke(element));
    }
    private static Method getMethod(Method method) throws NoSuchMethodException {
        return method.getClass().getDeclaredMethod("annotationData");
    }
    private static Method getMethod(Class clzz) throws NoSuchMethodException {
        return Class.class.getDeclaredMethod("annotationData");
    }
    public Map<Class<? extends Annotation>, Annotation> getAnnotations() {
        return annotations;
    }

    public Map<Class<? extends Annotation>, Annotation> getDeclaredAnnotations() {
        return declaredAnnotations;
    }

    public int getRedefinedCount() throws IllegalAccessException {
        return (int) this.redefinedCountField.get(annotationDataFromClass);
    }

    public void setRedefinedCount(int value) throws IllegalAccessException {
        this.redefinedCountField.set(annotationDataFromClass, value);
    }
}
