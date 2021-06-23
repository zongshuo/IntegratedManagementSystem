package com.zongshuo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zongshuo.entity.User;
import com.zongshuo.model.MenuModel;
import com.zongshuo.model.UserModel;

import java.util.List;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-22
 * @Time: 10:05
 * @Description:
 */
public interface UserService extends IService<UserModel> {
    /**
     * 根据用户名查询用户信息
     * 包括用户基本信息、用户角色信息、用户菜单信息
     * @param username
     * @return
     */
    UserModel getUserInfo(String username) throws IllegalAccessException;

    /**
     *
     * @param username
     * @return
     */
    UserModel getUserAndRoles(String username);

    /**
     * 获取用户的树状菜单列表
     * @param user
     * @return
     */
    List<MenuModel> getUserMenuTree(UserModel user);
}
