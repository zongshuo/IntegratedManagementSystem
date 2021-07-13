package com.zongshuo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zongshuo.entity.User;
import com.zongshuo.model.MenuModel;
import com.zongshuo.model.UserModel;
import com.zongshuo.util.PageParam;
import com.zongshuo.util.PageResult;

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
     * 分页查询用户列表
     * 按条件查询用户列表
     * @param pageParam
     * @return
     */
    PageResult<UserModel> getPage(PageParam<UserModel> pageParam);

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

    /**
     * 添加用户
     * 保存用户权限
     * 设置用户默认密码
     * 设置创建时间
     * 设置用户状态
     * @param user
     */
    void addUser(UserModel user) throws IllegalAccessException;

    /**
     * 判断用户是否存在
     * 根据用户主键
     * 根据用户账号
     * 根据用户名称
     * 三者任意字段存在即表示用户存在
     * @param user
     * @return
     */
    boolean userExisted(UserModel user);
}
