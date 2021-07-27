package com.zongshuo.annotation.annotations;

import java.lang.annotation.*;

/**
 * ClassName: AuthDefMenu
 * date: 2021/7/20 9:52
 * @author zongshuo
 * version: 1.0
 * Description:
 */
@Target(value={ElementType.TYPE, ElementType.METHOD, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
//@PreAuthorize("isAuthenticated")
public @interface AuthDefinition {
    // 权限名称
    String name() ;

    // 权限标识
    String authority() ;

    AuthType authType() ;

    // 路由地址
    String path() default "" ;

    public static enum AuthType {
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

        AuthType(String name, Byte type){
            this.name = name;
            this.type = type;
        }
    }
}
