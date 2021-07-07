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
    public static final long TOKEN_EFFECTIVE_TIME = 1000*60*60*8L;
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
     * 注册验证码有效时间
     * 单位毫秒
     */
    public static final long EFFECTIVE_TIME_AUTH_CODE_REGISTER = 1000*60*60L;
    public static final long EFFECTIVE_TIME_AUTH_CODE_RESET_PASSWORD = 1000*60*10L;
    public static final long EFFECTIVE_TIME_USER_TOKEN = 1000*60*60*8L;
    /**
     * 渠道编码
     * REGISTER：用户注册
     * RESET_PASSWORD：重置密码
     */
    public static final String CHANNEL_AUTH_CODE_REGISTER = "01";
    public static final String CHANNEL_AUTH_CODE_RESET_PASSWD = "02";


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
    public static final String RET_CODE_FAILED_DATA_NOT_FOUND = "DATA_E02";  //数据未发现缺失
    public static final String RET_CODE_FAILED_DATA_UPDATE = "DATA_E03"; //数据更新


    /**
     * 系统管理员名称
     */
    public static final String SYS_ADMIN_NAME = "admin";
    public static final String SYS_ADMIN_PASSWD = "P@ssw0rd";
    /**
     * 菜单-系统管理
     */
    public static final String MENU_SYS_ADMIN_NAME = "系统管理";

}
