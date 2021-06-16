package com.zongshuo.web;

import com.zongshuo.model.UserModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-6-15
 * @Time: 15:01
 * @Description:
 */
public class BaseController {

    public UserModel getLoginUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return new UserModel();
        Object obj = authentication.getPrincipal();
        if (obj == null) return new UserModel();
        return (UserModel) obj;
    }
}
