package com.zongshuo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zongshuo.entity.User;
import com.zongshuo.mapper.UserMapper;
import com.zongshuo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-22
 * @Time: 10:24
 * @Description:
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired(required = false)
    private UserMapper userMapper;


    @Override
    public User getUserAndRoles(String username) {
        return null;
    }
}
