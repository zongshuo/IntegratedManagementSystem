package com.zongshuo.annotation.handler;

import com.zongshuo.annotation.util.AnnotationUtil;
import lombok.extern.slf4j.Slf4j;

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
}
