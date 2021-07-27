package com.zongshuo.annotation.util.annotation;

import com.zongshuo.annotation.util.Convert;
import com.zongshuo.annotation.util.ReflectUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * ClassName: AnnotationService
 * date: 2021/7/26 14:18
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 */
public interface AnnotationService {
    String VALUE = "value";

    /**
     * 获取所有注解
     * 包括父类的注解
     * 包括被组合的注解
     * @return 注解 Map
     */
    Map<Class<? extends Annotation>, List<Annotation>> annotations();

    /**
     * 获取注解
     *
     * @param annotationType 注解类型
     * @param <A>            注解类型
     * @return 注解
     */
    default <A extends Annotation> A getAnnotation(
            final Class<A> annotationType
    ) {
        return this.getAnnotation(annotationType, 0);
    }

    /**
     * 获取注解
     *
     * @param annotationType 注解类型
     * @param index          索引
     * @param <A>            注解类型
     * @return 注解
     */
    @SuppressWarnings("unchecked")
    default <A extends Annotation> A getAnnotation(
            final Class<A> annotationType,
            final int index
    ) {
        final List<Annotation> annotations =
                this.annotations().get(annotationType);
        if (
                annotations == null ||
                        annotations.isEmpty() ||
                        index < 0 ||
                        index >= annotations.size()
        ) {
            return null;
        }
        return (A) annotations.get(index);
    }

    /**
     * 获取指定类型的注解列表
     *
     * @param annotationType 注解类型
     * @param <A>            注解类型
     * @return 注解列表
     */
    @SuppressWarnings("unchecked")
    default <A extends Annotation> List<A> getAnnotations(
            Class<A> annotationType
    ) {
        return (List<A>) this.annotations()
                .getOrDefault(annotationType, Collections.emptyList());
    }

    /**
     * 是否存在注解
     *
     * @param annotationType 注解类型
     * @return 是否存在
     */
    default boolean hasAnnotation(
            final Class<? extends Annotation> annotationType
    ) {
        return this.annotations().containsKey(annotationType);
    }

    /**
     * 是否不存在注解
     *
     * @param annotationType 注解类型
     * @return 是否不存在
     */
    default boolean notAnnotation(Class<? extends Annotation> annotationType) {
        return !this.hasAnnotation(annotationType);
    }

    /**
     * 是否包含多个相同类型的注解
     *
     * @param annotationType 注解类型
     * @return 是否包含
     */
    default boolean hasMultiAnnotation(
            final Class<? extends Annotation> annotationType
    ) {
        return (
                this.annotations().containsKey(annotationType) &&
                        this.annotations().get(annotationType).size() != 1
        );
    }

    /**
     * 添加注解
     * 以merge方式操作，不存在就添加，存在就替换
     *
     * @param annotation 注解
     */
    default <A extends Annotation> void addAnnotation(A annotation) {
        throw new UnsupportedOperationException(
                "Unsupported remove annotation to merge annotation"
        );
    }

    /**
     * 删除注解
     *
     * @param annotationType 注解类型
     */
    default void removeAnnotation(Class<? extends Annotation> annotationType) {
        throw new UnsupportedOperationException(
                "Unsupported remove annotation to merge annotation"
        );
    }

    /**
     * 删除注解
     *
     * @param annotationType 注解类型
     * @param index          索引
     */
    default void removeAnnotation(
            Class<? extends Annotation> annotationType,
            int index
    ) {
        throw new UnsupportedOperationException(
                "Unsupported add annotation to merge annotation"
        );
    }

    /**
     * 获取注解值
     *
     * @param annotationType 注解类型
     * @return 注解值
     */
    default Object get(Class<? extends Annotation> annotationType) {
        return this.get(annotationType, VALUE);
    }

    /**
     * 获取注解值
     *
     * @param annotationType 注解类型
     * @param name           方法名称
     * @return 注解值
     */
    default Object get(
            Class<? extends Annotation> annotationType,
            String name
    ) {
        return this.get(annotationType, name, Object.class);
    }

    /**
     * 获取注解值
     *
     * @param annotationType 注解类型
     * @return 注解值
     */
    default <T> T get(
            Class<? extends Annotation> annotationType,
            Class<T> returnType
    ) {
        return this.get(annotationType, VALUE, returnType);
    }

    /**
     * 获取注解值
     *
     * @param annotationType 注解类型
     * @param name           方法名称
     * @param returnType     返回类型
     * @param <T>            注解值类型
     * @return 注解值
     */
    default <T> T get(
            Class<? extends Annotation> annotationType,
            String name,
            Class<T> returnType
    ) {
        return this.get(annotationType, name, returnType, 0);
    }

    /**
     * 获取注解值
     *
     * @param annotationType 注解类型
     * @param name           方法名称
     * @param returnType     返回类型
     * @param index          索引
     * @param <T>            注解值类型
     * @return 注解值
     */
    default <T> T get(
            Class<? extends Annotation> annotationType,
            String name,
            Class<T> returnType,
            int index
    ) {
        final Annotation annotation = this.getAnnotation(annotationType, index);
        final Method method = ReflectUtil.getMethodByName(annotationType, name);
        if (annotation == null || method == null) {
            return null;
        }
        return Convert.convert(
                returnType,
                ReflectUtil.invoke(annotation, method)
        );
    }

    static AnnotationService from(Class<? extends AnnotatedElement> element) {
        return new AnnotationServiceType(element);
    }
    static AnnotationService from(Method method){
        return new AnnotationServiceMethod(method);
    }

    static boolean has(
            Class<? extends AnnotatedElement> element,
            Class<? extends Annotation> annotationType
    ) {
        return from(element).getAnnotation(annotationType) != null;
    }
}
