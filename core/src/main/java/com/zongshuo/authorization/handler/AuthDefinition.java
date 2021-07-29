package com.zongshuo.authorization.handler;

import com.zongshuo.authorization.model.AccessType;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.*;

/**
 * ClassName: AuthDefMenu
 * date: 2021/7/20 9:52
 * @author zongshuo
 * version: 1.0
 * Description:
 */
@Target(value={ElementType.TYPE, ElementType.METHOD, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthDefinition {
    // 权限名称
    String name() ;

    // 权限标识
    @MapToAuth(authAnnotation = PreAuthorize.class, name = "value")
    @MapToAuth(authAnnotation = PreAuthorize.class, name = "value")
    String authority() ;

    // 权限类型
    AccessType type() ;

    // 路由地址
    String path() default "" ;
}
