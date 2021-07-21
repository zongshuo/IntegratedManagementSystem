package com.zongshuo.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * ClassName: UserPermissionEvaluator
 * date: 2021/7/21 15:26
 *
 * @author zongshuo
 * version: 1.0
 * Description: 自定义授权控制，判断当前用户是否有资源访问权限
 */
@Slf4j
public class UserPermissionEvaluator implements PermissionEvaluator {
    private UserDetailsService userService ;
    public UserPermissionEvaluator(UserDetailsService user){
        this.userService = user;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object o, Object o1) {
        log.info("first:{}-{}-{}", authentication, o, o1);
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        log.info("second:{}-{}-{}-{}", authentication, serializable, s, o);
        return false;
    }
}
