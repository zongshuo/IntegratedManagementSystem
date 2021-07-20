package com.zongshuo.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zongshuo.mapper.RoleMenuMapper;
import com.zongshuo.model.RoleMenuModel;
import com.zongshuo.service.RoleMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-6-16
 * @Time: 15:07
 * @Description:
 */
@Service
@Slf4j
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenuModel> implements RoleMenuService {
    @Autowired(required = false)
    private RoleMenuMapper roleMenuMapper;

    @Override
    public void updateAdminMenu(Integer roleId) {
        roleMenuMapper.deleteAdminMenu(roleId);
        roleMenuMapper.insertAdminMenu(roleId);
    }
}
