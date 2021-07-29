package com.zongshuo.authorization.handler;

import java.lang.annotation.*;

/**
 * ClassName: MapToAuths
 * date: 2021/7/29 14:36
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@interface MapToAuths {
    MapToAuth[] value() ;
}
