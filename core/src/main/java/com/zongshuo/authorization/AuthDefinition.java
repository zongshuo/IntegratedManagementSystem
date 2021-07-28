package com.zongshuo.authorization;

import com.zongshuo.authorization.model.AccessType;

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
//@PreAuthorize("isAuthenticated")
public @interface AuthDefinition {
    // 权限名称
    String name() ;

    // 权限标识
    String authority() ;

    AccessType type() ;

    // 路由地址
    String path() default "" ;
}
