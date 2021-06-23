package com.zongshuo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zongshuo.mapper.UserModelMapper;
import com.zongshuo.model.MenuModel;
import com.zongshuo.model.RoleModel;
import com.zongshuo.model.UserModel;
import com.zongshuo.service.MenuModelService;
import com.zongshuo.service.RoleService;
import com.zongshuo.service.UserModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuModelService menuModelService;


    @Override
    public UserModel getUserInfo(String username) throws IllegalAccessException{
        UserModel userModel = userMapper.selectOne(new QueryWrapper<UserModel>().lambda().eq(UserModel::getUsername, username));
        if (userModel == null) throw new IllegalAccessException("用户不存在！");

        List<RoleModel> roleModelList = roleService.getUserRoles(userModel);
        userModel.setRoles(roleModelList);

        List<MenuModel> menuModelList = menuModelService.getMenuListByRole(roleModelList);
        userModel.setMenus(menuModelService.toMenuTree(menuModelList, 0));

        return userModel;
    }

    @Override
    public UserModel getUserAndRoles(String username) {
        UserModel userModel = getOne(new QueryWrapper<UserModel>().eq("username", username));
        return userModel;
    }
}
