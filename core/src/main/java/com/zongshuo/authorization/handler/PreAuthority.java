package com.zongshuo.authorization.handler;

import java.lang.annotation.*;

/**
 * ClassName: PreAuthority
 * date: 2021/7/29 14:27
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreAuthority {
    String value() ;
}
