package com.zongshuo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zongshuo.entity.User;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-22
 * @Time: 10:05
 * @Description:
 */
public interface UserService extends IService<User> {

    /**
     *
     * @param username
     * @return
     */
    User getUserAndRoles(String username);
}
