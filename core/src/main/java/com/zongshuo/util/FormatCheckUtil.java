package com.zongshuo.util;

import com.zongshuo.Contains;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-26
 * @Time: 9:30
 * @Description:
 * 格式检查工具类
 */
public final class FormatCheckUtil {
    private FormatCheckUtil(){}

    /**
     * 判断是否为用户名格式
     * @param username
     * @return
     */
    public static boolean isUsername(String username){
        return test(username, "^[a-zA-Z][a-zA-Z0-9]{7,29}$");
    }
    public static boolean isNotUsername(String username){
        return ! isUsername(username);
    }

    /**
     * 判断是否为邮箱格式
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        return test(email, "^[A-Za-z0-9_-\\u4e00-\\u9fa5]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
    }
    public static boolean isNotEmail(String email){
        return ! isEmail(email);
    }

    /**
     * 校验是否为验证码
     * @param authCode
     * @return
     */
    public static boolean isAuthCode(String authCode){
        return test(authCode, "^[a-zA-Z0-9]{" + Contains.CHECK_CODE_LENGTH + "}$");
    }
    public static boolean isNotAuthCode(String authCode){
        return !isAuthCode(authCode);
    }

    /**
     * 校验是否为验证码
     * @param password
     * @return
     */
    public static boolean isPassword(String password){
        return test(password, "^[\\S]{6,12}$");
    }
    public static boolean isNotPassword(String password){
        return !isPassword(password);
    }

    /**
     * 校验字符串是否匹配正则表达式
     * @param str 字符串
     * @param reg 正则表达式
     * @return
     */
    public static boolean test(String str, String reg){
        return str != null && str.matches(reg);
    }
}
