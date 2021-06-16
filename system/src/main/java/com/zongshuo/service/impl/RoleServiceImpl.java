package com.zongshuo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zongshuo.entity.Role;
import com.zongshuo.mapper.RoleMapper;
import com.zongshuo.model.RoleModel;
import com.zongshuo.model.UserModel;
import com.zongshuo.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-22
 * @Time: 11:23
 * @Description:
 */
@Service
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired(required = false)
    private RoleMapper roleMapper;

    @Override
    public List<RoleModel> getUserRoles(UserModel userModel) {
        List<RoleModel> roles = roleMapper.selectUserRoles(userModel.getId());
        if (roles == null){
            roles = new ArrayList<>(0);
        }
        return roles;
    }
}
