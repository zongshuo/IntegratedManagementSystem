package com.zongshuo;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-22
 * @Time: 14:54
 * @Description:
 * 常量类
 */
public final class Contains {
    private Contains(){}

    /**
     * 接口响应报告状态码
     */
    public static final String RET_CODE_SUCCESS = "AAAAAAA"; //成功

    public static final String RET_CODE_FAILED_UNKNOWN = "E000001"; //未知错误
    public static final String RET_CODE_FAILED_SYS = "SYS_E001"; //系统错误
    public static final String RET_CODE_FAILED_AUTH_NON = "AUTH_E01"; //没有权限
    public static final String RET_CODE_FAILED_AUTH_LOGIN = "AUTH_E02"; //未登录
    public static final String RET_CODE_FAILED_AUTH_EXPIRE = "AUTH_E03"; //登录已过期



}
