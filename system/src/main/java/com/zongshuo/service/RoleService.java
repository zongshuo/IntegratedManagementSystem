package com.zongshuo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zongshuo.entity.Role;
import com.zongshuo.model.RoleModel;
import com.zongshuo.model.UserModel;

import java.util.List;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-22
 * @Time: 11:23
 * @Description:
 * 系统角色服务类
 */
public interface RoleService extends IService<Role> {
    /**
     * 根据用户获取用户角色列表
     * @param userModel
     * @return
     */
    List<RoleModel> getUserRoles(UserModel userModel);
}
