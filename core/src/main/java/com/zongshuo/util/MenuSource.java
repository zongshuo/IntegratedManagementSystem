package com.zongshuo.util;

/**
 * ClassName: MenuSource
 * date: 2021/7/30 15:13
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 * 描述彩菜单来源，系统收集、手工添加等
 */
public enum MenuSource {
    SYSTEM("系统收集", Byte.parseByte("0")),
    MODE("表单建模", Byte.parseByte("1")),
    AGENT("前端添加", Byte.parseByte("2"));

    private String name ;
    private Byte value ;
    MenuSource(String name, Byte value){
        this.name = name;
        this.value = value;
    }
    public String getName() {
        return name;
    }

    public Byte getValue() {
        return value;
    }
}
