package com.zongshuo.util;

import com.zongshuo.Contains;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-22
 * @Time: 14:44
 * @Description:
 * 接口响应报文json格式对象
 * 该类为工具类不可被继承
 */
public final class ResponseJsonMsg extends HashMap<String, Object> implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String CODE_NAME = "retCode"; //响应码字段名称
    private static final String MSG_NAME = "msg"; //响应码说明字段名称
    private static final String DATA_NAME = "data"; //响应数据字段名称
    private static final String DEFAULT_SUCCESS_CODE = Contains.RET_CODE_SUCCESS; //默认成功码
    private static final String DEFAULT_FAILED_CODE = Contains.RET_CODE_FAILED_SYS; //默认失败码
    private static final String DEFAULT_SUCCESS_MSG = "成功"; //默认成功说明
    private static final String DEFAULT_FAILED_MSG = "失败"; //默认失败说明

    private ResponseJsonMsg(){}

    /**
     * 响应成功
     * @return
     */
    public static ResponseJsonMsg ok(){
        return ok(DEFAULT_SUCCESS_MSG);
    }
    public static ResponseJsonMsg ok(String msg){
        return ok(DEFAULT_SUCCESS_CODE, msg);
    }
    public static ResponseJsonMsg ok(String code, String msg){
        ResponseJsonMsg responseJsonMsg = new ResponseJsonMsg();
        responseJsonMsg.put(CODE_NAME, code);
        responseJsonMsg.put(MSG_NAME, msg);
        return responseJsonMsg;
    }

    /**
     * 响应失败
     * @return
     */
    public static ResponseJsonMsg error(){
        return error(DEFAULT_FAILED_MSG);
    }
    public static ResponseJsonMsg error(String msg){
        return error(DEFAULT_FAILED_CODE, msg);
    }
    public static ResponseJsonMsg error(String code, String msg){
        ResponseJsonMsg responseJsonMsg = new ResponseJsonMsg();
        responseJsonMsg.put(CODE_NAME, code);
        responseJsonMsg.put(MSG_NAME, msg);
        return responseJsonMsg;
    }

    public ResponseJsonMsg setData(Object data){
        return put(DATA_NAME, data);
    }

    public ResponseJsonMsg put(String key, Object value){
        if (key != null && value != null){
            super.put(key, value);
        }
        return this;
    }
}
