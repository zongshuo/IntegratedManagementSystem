package com.zongshuo.security;

import com.zongshuo.model.UserModel;
import com.zongshuo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-21
 * @Time: 22:34
 * @Description:
 * 用户登录认证service
 * security获取用户信息的服务，需要实现UserDetailsService接口
 */
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userService.getUserAndAuths(username);
        if (user.isEmpty()) throw new UsernameNotFoundException("用户不存在");

        return user;
    }
}
