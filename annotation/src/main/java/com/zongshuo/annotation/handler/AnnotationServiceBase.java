package com.zongshuo.annotation.handler;

import com.zongshuo.annotation.util.AnnotationUtil;
import lombok.extern.slf4j.Slf4j;

import javax.management.modelmbean.InvalidTargetObjectTypeException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.Map;

/**
 * ClassName: AnnotationServiceImpl
 * date: 2021/7/26 14:23
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 */
@Slf4j
abstract class AnnotationServiceBase implements AnnotationService {
    protected final AnnotatedElement element ;
    public AnnotationServiceBase(AnnotatedElement element) {
        this.element = element;
    }

    @Override
    public Map<Class<? extends Annotation>, List<Annotation>> annotations() {
        return AnnotationUtil.getAnnotations(element);
    }

    @Override
    public <A extends Annotation> void addAnnotation(A annotation) throws InvalidTargetObjectTypeException {
        addAnnotationConfirm(annotation);

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

    }

    @Override
    public void removeAnnotation(Class<? extends Annotation> annotationType, int index) {

    }


    /**
     * 校验注解是否可以添加到element对象上
     * @param annotation
     * @throws InvalidTargetObjectTypeException
     */
    protected void addAnnotationConfirm(Annotation annotation) throws InvalidTargetObjectTypeException {
        // jdk元注解只能添加到注解上
        if (AnnotationUtil.isJdkAnnotation(annotation) && ! ((Class)element).isAnnotation()){
            throw new InvalidTargetObjectTypeException("jdk meta annotation only used on annotation");
        }

        // 注解只能添加到target元注解指定的类型上
        if (! AnnotationUtil.isTargetType(element, annotation)){
            throw new InvalidTargetObjectTypeException("annotation target not include:" + element);
        }

        // TODO 判断是否允许重复添加
        // TODO 判断无默认值字段是否提供了值
    }

    // TODO 最终实现注解的增删改查
    // TODO 针对ElementType系统性测试
}
