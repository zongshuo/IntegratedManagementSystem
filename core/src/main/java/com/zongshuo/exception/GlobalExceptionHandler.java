package com.zongshuo.exception;

import com.zongshuo.Contains;
import com.zongshuo.util.ResponseJsonMsg;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-27
 * @Time: 17:36
 * @Description:
 *
 * @ResponseBody的作用其实是将java对象转为json格式的数据
 * 全局异常处理类
 * @ControllerAdvice注解是一个增强的controller，作用：
 *  1、全局异常处理
 *  2、全局数据绑定
 *  3、全局数据预处理
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * spring boot validation 属性校验失败异常处理
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseJsonMsg validateExceptionHandler(MethodArgumentNotValidException e){
        return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_PARAM, e.getBindingResult().getFieldError().getDefaultMessage());
    }
}