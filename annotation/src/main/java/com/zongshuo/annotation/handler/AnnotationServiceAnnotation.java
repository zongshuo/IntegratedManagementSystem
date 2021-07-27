package com.zongshuo.annotation.handler;

import com.zongshuo.annotation.util.AnnotationUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * ClassName: AnnotationServiceImpl
 * date: 2021/7/26 14:23
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 */
@Slf4j
class AnnotationServiceAnnotation extends AnnotationServiceBase {
    public AnnotationServiceAnnotation(AnnotatedElement element) {
        super(element);
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
