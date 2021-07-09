package com.zongshuo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zongshuo.Contains;
import com.zongshuo.mapper.UserMapper;
import com.zongshuo.model.MenuModel;
import com.zongshuo.model.RoleModel;
import com.zongshuo.model.UserModel;
import com.zongshuo.service.MenuService;
import com.zongshuo.service.RoleService;
import com.zongshuo.service.UserService;
import com.zongshuo.util.PageParam;
import com.zongshuo.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, UserModel> implements UserService {
    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;


    @Override
    public PageResult<UserModel> getPage(PageParam<UserModel> pageParam) {
        List<UserModel> userModelList = userMapper.selectPage(pageParam);
        if (userModelList == null){
            userModelList = new ArrayList<>(0);
        }
        return new PageResult<>(userModelList, pageParam.getTotal());
    }

    @Override
    public UserModel getUserInfo(String username) throws IllegalAccessException{
        UserModel userModel = userMapper.selectOne(new QueryWrapper<UserModel>().lambda().eq(UserModel::getUsername, username));
        if (userModel == null) throw new IllegalAccessException("用户不存在！");

        List<RoleModel> roleModelList = roleService.getUserRoles(userModel);
        userModel.setRoles(roleModelList);

        return userModel;
    }

    @Override
    public UserModel getUserAndRoles(String username) {
        UserModel userModel = getOne(new QueryWrapper<UserModel>().eq("username", username));
        return userModel;
    }

    @Override
    public List<MenuModel> getUserMenuTree(UserModel user) {
        List<MenuModel> menuModels = menuService.getMenusByUserId(user.getId());
        menuModels = menuService.toMenuTree(menuModels, 0);
        return menuModels;
    }
}
