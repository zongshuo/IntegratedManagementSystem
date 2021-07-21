package com.zongshuo.util;

/**
 * ClassName: EnumAuthType
 * date: 2021/7/20 15:41
 *
 * @author zongshuo
 * version: 1.0
 * Description:声明系统权限类型：菜单、按钮等
 */
public enum EnumAuthType {
    MENU("菜单", Byte.parseByte("0")),
    BUTTON("按钮", Byte.parseByte("1")),
    API("接口", Byte.parseByte("2"));


    private String name ;
    private Byte type ;

    public String getName() {
        return name;
    }
    public Byte getType(){
        return type;
    }

    EnumAuthType(String name, Byte type){
        this.name = name;
        this.type = type;
    }
}
