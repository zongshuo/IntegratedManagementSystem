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
     * JWT token slot
     */
    public static final String JWT_SLOT = "MFpXPyZQbVpf5b2p6Imy5pi+56S65ZmoLXNlY3JldF9zbG90";//0ZW?&PmZ_彩色显示器-secret_slot
    /**
     * JWT token expire time
     * 单位毫秒
     */
    public static final long TOKEN_EFFECTIVE_TIME = 1000*60*60*8;
    /**
     * token提前刷新时间
     * 单位分钟
     */
    public static final int TOKEN_WILL_EXPIRE = 30;

    /**
     * 验证码长度
     */
    public static final int CHECK_CODE_LENGTH = 6;
    /**
     * 注册验证码失效时间
     * 单位毫秒
     */
    public static final long AUTH_CODE_REGISTER_OUT_TIME = 600000;
    /**
     * 验证码来源-用户注册
     */
    public static final String AUTH_CODE_CHANNEL_REGISTER = "01";

    /**
     * 接口响应报告状态码
     */
    public static final String RET_CODE_SUCCESS = "AAAAAAA"; //成功

    public static final String RET_CODE_FAILED_UNKNOWN = "E000001"; //未知错误
    public static final String RET_CODE_FAILED_SYS = "SYS_E001"; //系统错误
    public static final String RET_CODE_FAILED_AUTH_NON = "AUTH_E01"; //没有权限
    public static final String RET_CODE_FAILED_AUTH_LOGIN = "AUTH_E02"; //未登录
    public static final String RET_CODE_FAILED_AUTH_EXPIRE = "AUTH_E03"; //登录已过期
    public static final String RET_CODE_FAILED_PARAM = "PARM_E01"; //参数错误
    public static final String RET_CODE_FAILED_DATA_STATE = "DATA_E01"; //数据状态错误



}
