package com.zongshuo.security;

import com.zongshuo.model.UserModel;
import com.zongshuo.service.UserModelService;
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
    private UserModelService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userService.getUserAndRoles(username);
        if (user == null) throw new UsernameNotFoundException("用户名不存在");

        return user;
    }
}
