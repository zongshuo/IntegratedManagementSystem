package com.zongshuo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zongshuo.mapper.UserRoleMapper;
import com.zongshuo.model.RoleModel;
import com.zongshuo.model.UserModel;
import com.zongshuo.model.UserRoleModel;
import com.zongshuo.service.RoleService;
import com.zongshuo.service.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-22
 * @Time: 11:26
 * @Description:
 */
@Service
@Slf4j
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleModel> implements UserRoleService {
    @Autowired
    private RoleService roleService;
    @Autowired(required = false)
    private UserRoleMapper userRoleMapper;

    @Override
    @Transactional
    public void saveUserRole(UserModel user) throws IllegalAccessException {
        if (user.getId() == null) return;

        userRoleMapper.delete(new QueryWrapper<UserRoleModel>().lambda().eq(UserRoleModel::getUserId, user.getId()));
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            return;
        }

        List<Integer> roleIds = new ArrayList<>(user.getRoles().size());
        user.getRoles().stream().forEach(role -> roleIds.add(role.getId()));
        LambdaQueryWrapper query = new QueryWrapper<RoleModel>()
                .lambda()
                .in(RoleModel::getId, roleIds);
        if (roleService.count(query) < user.getRoles().size()) {
            throw new IllegalAccessException("部分角色不存在！");
        }


        for (RoleModel role : user.getRoles()) {
            UserRoleModel userRole = new UserRoleModel();
            userRole.setRoleId(role.getId());
            userRole.setUserId(user.getId());
            userRoleMapper.insert(userRole);
        }
    }
}
