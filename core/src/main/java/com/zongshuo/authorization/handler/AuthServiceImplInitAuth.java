package com.zongshuo.authorization.handler;

import com.zongshuo.authorization.model.AccessPoint;
import com.zongshuo.authorization.model.AccessType;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * ClassName: AuthServiceImplInitAuth
 * date: 2021/7/28 18:04
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 * 收集权限点
 * 同时给权限点增加鉴权注解
 * 注解的属性值从权限点注解的属性中获取
 * 通过MapToAuth注解映射权限点注解与鉴权注解之间关系，包括注解名称和属性名称
 */
public class AuthServiceImplInitAuth implements AuthService{
    @Override
    public Map<AccessType, List<AccessPoint>> collectAccessPoint(String... packageNames) throws IOException {
        return null;
    }
}
