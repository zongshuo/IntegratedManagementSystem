package com.zongshuo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zongshuo.model.UserModel;
import com.zongshuo.model.UserRoleModel;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-22
 * @Time: 11:25
 * @Description:
 */
public interface UserRoleService extends IService<UserRoleModel> {
    /**
     * 保存用户角色列表
     * 先判断角色是否都存在
     * 删除用户所有角色
     * 添加用户所有角色
     */
    void saveUserRole(UserModel user) throws IllegalAccessException;
}
