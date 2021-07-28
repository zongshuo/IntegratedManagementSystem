package com.zongshuo.authorization.model;

/**
 * ClassName: AutyType
 * date: 2021/7/28 11:34
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 */
public enum AccessType {
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

    AccessType(String name, Byte type){
        this.name = name;
        this.type = type;
    }
}
