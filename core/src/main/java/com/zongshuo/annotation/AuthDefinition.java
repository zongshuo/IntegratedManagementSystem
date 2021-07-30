package com.zongshuo.annotation;

import com.zongshuo.authorization.handler.MapToAuth;
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
    @MapToAuth(authAnnotation = PreAuthorize.class, name = "value", format = FormatPreAuthorizeValue.class)
    String authority() ;

    AccessType type() ;

    // 路由地址
    String path() default "" ;
}
