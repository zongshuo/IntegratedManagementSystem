package com.zongshuo.util.annotation;

import sun.reflect.annotation.ExceptionProxy;

import java.lang.annotation.AnnotationTypeMismatchException;
import java.lang.reflect.Method;

/**
 * ClassName: AnnotationTypeMismatchExceptionProxy
 * date: 2021/7/26 10:01
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 * 注解反序列化时可能会抛出此异常
 */
class AnnotationTypeMismatchExceptionProxy extends ExceptionProxy {
    private static final long serialVersionUID = 7844069490309503934L;

    private Method member;

    private String foundType;

    AnnotationTypeMismatchExceptionProxy(String paramString) {
        this.foundType = paramString;
    }

    AnnotationTypeMismatchExceptionProxy setMember(Method paramMethod) {
        this.member = paramMethod;
        return this;
    }

    protected RuntimeException generateException() {
        return new AnnotationTypeMismatchException(this.member, this.foundType);
    }
}
