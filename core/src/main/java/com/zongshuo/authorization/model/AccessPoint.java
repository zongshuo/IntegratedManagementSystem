package com.zongshuo.authorization.model;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: AuthPoint
 * date: 2021/7/28 11:29
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 * 存储系统权限点
 */
public class AccessPoint {
    private String name ; //权限点名称
    private String authority ; //权限标识
    private String path ; //权限路径
    private AccessType type ; //权限类型
    private List<AccessPoint> children = new ArrayList<>(0);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public AccessType getType() {
        return type;
    }

    public void setType(AccessType type) {
        this.type = type;
    }

    public List<AccessPoint> getChildren() {
        return children;
    }

    public void setChildren(List<AccessPoint> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "AuthPoint{" +
                "name='" + name + '\'' +
                ", authority='" + authority + '\'' +
                ", path='" + path + '\'' +
                ", type=" + type +
                '}';
    }
}
