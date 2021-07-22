package com.zongshuo.service;

import com.baomidou.mybatisplus.extension.service.IService;
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
     * 根据用户账号查询用户拥有的角色
     * @param username
     * @return
     */
    UserModel getUserAndRoles(String username);

    /**
     * 根据用户账号查询用户拥有的权限
     * @param username
     * @return
     */
    UserModel getUserWithAuthsAndRoles(String username);

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
     * 编辑用户
     * 判断用户是否存在
     * 更新人员表字段
     * 更新用户角色
     * 设置更新时间
     * @param user
     * @throws IllegalAccessException
     */
    void editUser(UserModel user) throws IllegalAccessException;

    /**
     * 变更用户状态
     * 判断用户是否存在
     * 判断状态是否一致，如果一致抛出异常
     * @param user
     * @throws IllegalAccessException
     */
    void toggleEnable(UserModel user) throws IllegalAccessException;

    /**
     * 批量删除用户
     * 根据用户主键进行删除
     * 同时删除用户角色
     * @param userIds
     */
    void removeUser(Integer [] userIds);

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
