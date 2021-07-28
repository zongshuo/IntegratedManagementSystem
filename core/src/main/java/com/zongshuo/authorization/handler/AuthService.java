package com.zongshuo.authorization.handler;

import com.zongshuo.authorization.model.AccessPoint;
import com.zongshuo.authorization.model.AccessType;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

/**
 * ClassName: AuthService
 * date: 2021/7/28 13:40
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 * 提供权限的收集、控制等服务
 */
public interface AuthService {
    /**
     * 收集权限点
     * 扫描指定包
     * @param packageNames
     * @return 分权限类型存储的权限点列表
     */
    Map<AccessType, List<AccessPoint>>  collectAccessPoint(String ... packageNames) throws IOException;

    static AuthService fromBase(Class<? extends Annotation> accessPoint){
        return new AuthServiceImplBase(accessPoint);
    }

}
