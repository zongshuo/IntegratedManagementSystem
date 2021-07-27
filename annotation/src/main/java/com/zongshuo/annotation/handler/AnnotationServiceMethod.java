package com.zongshuo.annotation.handler;

import lombok.extern.slf4j.Slf4j;

import javax.management.modelmbean.InvalidTargetObjectTypeException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

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
    public <A extends Annotation> void addAnnotation(A annotation) throws InvalidTargetObjectTypeException {
        addAnnotationConfirm(annotation);

        try {
            AnnotationData annotationData = AnnotationData.getInstance((Method) element);
            annotationData.getAnnotations().put(annotation.annotationType(), annotation);
        } catch (Exception e) {
            log.error("添加注解失败:", e);
        }
    }

}
