package com.zongshuo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zongshuo.Contains;
import com.zongshuo.mapper.UserMapper;
import com.zongshuo.model.MenuModel;
import com.zongshuo.model.RoleModel;
import com.zongshuo.model.UserModel;
import com.zongshuo.model.UserRoleModel;
import com.zongshuo.service.MenuService;
import com.zongshuo.service.RoleService;
import com.zongshuo.service.UserRoleService;
import com.zongshuo.service.UserService;
import com.zongshuo.util.PageParam;
import com.zongshuo.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    @Autowired
    private UserRoleService userRoleService;


    @Override
    public PageResult<UserModel> getPage(PageParam<UserModel> pageParam) {
        List<UserModel> userModelList = userMapper.selectPage(pageParam);
        if (userModelList == null) {
            userModelList = new ArrayList<>(0);
        }

        List<RoleModel> roleModels ;
        for (UserModel user : userModelList) {
            //查询用户角色
            roleModels = roleService.getUserRoles(user);
            user.setRoles(roleModels);
        }

        return new PageResult<>(userModelList, pageParam.getTotal());
    }

    @Override
    public UserModel getUserInfo(String username) throws IllegalAccessException {
        UserModel userModel = userMapper.selectOne(new QueryWrapper<UserModel>().lambda().eq(UserModel::getUsername, username));
        if (userModel == null) throw new IllegalAccessException("用户不存在！");

        List<RoleModel> roleModelList = roleService.getUserRoles(userModel);
        userModel.setRoles(roleModelList);

        return userModel;
    }

    @Override
    public UserModel getUserAndRoles(String username) {
        UserModel userModel = getOne(new QueryWrapper<UserModel>().eq("username", username));
        if (userModel == null)
            userModel = new UserModel();
        return userModel;
    }

    @Override
    public List<MenuModel> getUserMenuTree(UserModel user) {
        List<MenuModel> menuModels = menuService.getMenusByUserId(user.getId());
        menuModels = menuService.toMenuTree(menuModels, 0);
        return menuModels;
    }

    @Override
    @Transactional
    public void removeUser(Integer[] userIds) {
        userRoleService.remove(new QueryWrapper<UserRoleModel>().lambda().in(UserRoleModel::getUserId, userIds));

        userMapper.deleteBatchIds(Arrays.asList(userIds));
    }

    @Override
    public void toggleEnable(UserModel user) throws IllegalAccessException {
        if (!userExisted(user)){
            throw new IllegalAccessException("用户不存在！");
        }

        int count = userMapper.selectCount(
                new QueryWrapper<UserModel>()
                        .eq("id", user.getId())
                        .eq("is_enabled", user.isEnabled()));
        if (count > 0){
            throw new IllegalAccessException("用户状态不需要变更！");
        }

        userMapper.update(null,
                new UpdateWrapper<UserModel>()
                        .set("is_enabled", user.isEnabled())
                        .eq("id", user.getId()));
    }

    @Override
    @Transactional
    public void editUser(UserModel user) throws IllegalAccessException {
        if (!userExisted(user)){
            throw new IllegalAccessException("用户不存在！");
        }

        user.setUpdateTime(new Date());
        LambdaUpdateWrapper updateWrapper =
                new UpdateWrapper<UserModel>()
                        .lambda()
                        .set(UserModel::getUsername, user.getUsername())
                        .set(UserModel::getNickName, user.getNickName())
                        .set(UserModel::getGender, user.getGender())
                        .set(UserModel::getEmail, user.getEmail())
                        .set(UserModel::getPhone, user.getPhone())
                        .eq(UserModel::getId, user.getId());
        userMapper.update(null, updateWrapper);

        userRoleService.saveUserRole(user);
    }

    @Override
    @Transactional(rollbackFor = {IllegalAccessException.class, Exception.class})
    public void addUser(UserModel user) throws IllegalAccessException{
        if (this.userExisted(user)){
            throw new IllegalAccessException("用户已存在！");
        }

        user.setCreateTime(new Date());
        user.setPassword(Contains.DEFAULT_PASSWD);
        user.setIsEnabled(true);
        user.setIsCredentialsNonExpired(true);
        user.setIsAccountNonExpired(true);
        user.setIsAccountNonLocked(true);
        userMapper.insert(user);

        if (user.getId() == null) {
            throw new IllegalAccessException("添加用户失败！");
        }

        if (user.getRoles() == null || user.getRoles().isEmpty()){
            return ;
        }

        userRoleService.saveUserRole(user);
    }

    @Override
    public boolean userExisted(UserModel user) {
        LambdaQueryWrapper<UserModel> query =
                new QueryWrapper<UserModel>()
                        .lambda()
                        .eq(UserModel::getId, user.getId())
                        .or().eq(UserModel::getUsername, user.getUsername())
                        .or().eq(UserModel::getNickName, user.getNickName());
        return userMapper.selectCount(query) > 0;
    }
}
