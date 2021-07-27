package com.zongshuo.annotation.util.annotation;

import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.List;

/**
 * ClassName: AnnotationServiceImpl
 * date: 2021/7/26 14:23
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 */
@Slf4j
class AnnotationServiceMethod extends AnnotationServiceBase{
    public AnnotationServiceMethod(AnnotatedElement element) {
        super(element);
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
        return null;
    }

    @Override
    public <A extends Annotation> A getAnnotation(Class<A> annotationType, int index) {
        return null;
    }

    @Override
    public <A extends Annotation> List<A> getAnnotations(Class<A> annotationType) {
        return AnnotationUtil.getAnnotations(element.getClass(), annotationType);
    }

    @Override
    public boolean notAnnotation(Class<? extends Annotation> annotationType) {
        return false;
    }

    @Override
    public boolean hasMultiAnnotation(Class<? extends Annotation> annotationType) {
        return false;
    }

    @Override
    public <A extends Annotation> void addAnnotation(A annotation) {
        if (hasAnnotation(annotation.annotationType())){
            AnnotationUtil.resetFirstAnnotation(element, annotation);
            return;
        }

        try {
            AnnotationData annotationData = AnnotationData.getInstance(element);
            annotationData.getDeclaredAnnotations().put(annotation.annotationType(), annotation);
            annotationData.getAnnotations().put(annotation.annotationType(), annotation);
        } catch (Exception e) {
            log.error("添加注解失败:", e);
        }
    }

    @Override
    public void removeAnnotation(Class<? extends Annotation> annotationType) {
        /**稍后实现*/
    }

    @Override
    public void removeAnnotation(Class<? extends Annotation> annotationType, int index) {
        /**稍后实现*/
    }

    @Override
    public Object get(Class<? extends Annotation> annotationType) {
        return null;
    }

    @Override
    public Object get(Class<? extends Annotation> annotationType, String name) {
        return null;
    }

    @Override
    public <T> T get(Class<? extends Annotation> annotationType, Class<T> returnType) {
        return null;
    }

    @Override
    public <T> T get(Class<? extends Annotation> annotationType, String name, Class<T> returnType) {
        return null;
    }

    @Override
    public <T> T get(Class<? extends Annotation> annotationType, String name, Class<T> returnType, int index) {
        return null;
    }
}
