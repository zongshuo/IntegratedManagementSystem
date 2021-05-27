package com.zongshuo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zongshuo.entity.User;
import com.zongshuo.model.UserModel;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-22
 * @Time: 10:05
 * @Description:
 */
public interface UserModelService extends IService<UserModel> {

    /**
     *
     * @param username
     * @return
     */
    UserModel getUserAndRoles(String username);
}
