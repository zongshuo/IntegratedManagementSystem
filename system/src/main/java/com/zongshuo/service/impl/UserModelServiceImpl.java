package com.zongshuo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zongshuo.mapper.UserModelMapper;
import com.zongshuo.model.UserModel;
import com.zongshuo.service.UserModelService;
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
public class UserModelServiceImpl extends ServiceImpl<UserModelMapper, UserModel> implements UserModelService {
    @Autowired(required = false)
    private UserModelMapper userMapper;


    @Override
    public UserModel getUserAndRoles(String username) {
        UserModel userModel = getOne(new QueryWrapper<UserModel>().eq("username", username));
        return userModel;
    }
}
