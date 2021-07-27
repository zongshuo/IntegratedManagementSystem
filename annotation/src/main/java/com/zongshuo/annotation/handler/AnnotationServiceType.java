package com.zongshuo.annotation.handler;

import lombok.extern.slf4j.Slf4j;

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
class AnnotationServiceType extends AnnotationServiceBase {
    public AnnotationServiceType(AnnotatedElement element) {
        super(element);
    }

}
