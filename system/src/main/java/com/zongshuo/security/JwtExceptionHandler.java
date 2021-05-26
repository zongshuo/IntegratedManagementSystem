package com.zongshuo.security;

import com.alibaba.fastjson.JSON;
import com.zongshuo.Contains;
import com.zongshuo.util.ResponseJsonMsg;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-22
 * @Time: 14:34
 * @Description:
 * 认证及授权异常处理类
 * 处理有关登录及授权失败的异常情况
 * JWT(JSON WEB TOKEN)是一种可安全传输的JSON对象，由于使用了数字签名，所以是安全和可信任的。
 * AccessDeniedHandler：处理授权异常
 * AuthenticationEntryPoint：处理认证异常
 */
public class JwtExceptionHandler implements AccessDeniedHandler, AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        doHandler(request, response, authException);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        doHandler(request, response, accessDeniedException);
    }

    private void doHandler(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException {
        //设置响应报文的媒体类型及编码格式
        response.setContentType("application/json;charset=UTF-8");
        //获取响应对象的输出流
        PrintWriter writer = response.getWriter();

        ResponseJsonMsg responseJsonMsg;
        //根据不同的异常响应对应的报文
        if (e instanceof AccessDeniedException){
            responseJsonMsg = ResponseJsonMsg.error(Contains.RET_CODE_FAILED_AUTH_NON, "没有访问权限");
        }else if (e instanceof InsufficientAuthenticationException){
            responseJsonMsg = ResponseJsonMsg.error(Contains.RET_CODE_FAILED_AUTH_LOGIN, "未登录");
        }else if (e instanceof AccountExpiredException){
            responseJsonMsg = ResponseJsonMsg.error(Contains.RET_CODE_FAILED_AUTH_EXPIRE, "登录已过期");
        }else{
            responseJsonMsg = ResponseJsonMsg.error(Contains.RET_CODE_FAILED_UNKNOWN, "未登录或登录已过期");
        }

        writer.write(JSON.toJSONString(responseJsonMsg));
        writer.flush();
    }
}
