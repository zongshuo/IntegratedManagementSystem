package com.zongshuo.security;

import com.alibaba.fastjson.JSON;
import com.zongshuo.Contains;
import com.zongshuo.util.ResponseJsonMsg;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
 * 特别适用分布式站点的单点登录场景
 * JWT的声明一般被用来在身份提供者和服务提供者间传递被认证的用户身份信息，以便于从服务器获取资源。也可以增加额外的业务所必须的声明信息。
 * 基于token的鉴权机制类似于http协议，也是无状态的，它不需要再服务端去保留用户的认证信息或者会话信息。
 * 这意味着基于token认证机制的应用不需要去考虑用户在那一台服务器登录。
 * token必须在每次请求时传递给服务端，它应该保存到请求头中，还需要支持CORS策略。一般我们在服务端增加“Access-Control-Allow-Origin: *”就可以。
 * JWT由三段信息组成，它们之间使用“.”连接。第一部分称为头部（header），第二部分称为载荷（payload），第三部分是签证（signature）。
 * 头部承载两部分信息，声明类型和加密算法：{'type':'JWT', 'alg':'HS256'}。将以上信息进行base64加密，就构成了第一部分。
 * 载荷就是存放有效信息的地方，包含三部分：标准中注册的声明，公共的声明，私有的声明
 *  标准中注册的声明：
 *      iss: jwt签发者
 *      sub: jwt所面向的用户
 *      aud: 接收jwt的一方
 *      exp: jwt的过期时间，这个过期时间必须要大于签发时间
 *      nbf: 定义在什么时间之前，该jwt都是不可用的.
 *      iat: jwt的签发时间
 *      jti: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击。
 *  公共的声明：公共的声明可以添加任何信息，一般添加用户信息或者业务需要的必要信息，但不建议添加敏感信息，因为该部分在客户端不加密
 *  私有的声明：是提供者和消费者所共同定义的声明，一般不建议存放敏感信息，因为base64是对称解密的，意味着该类信息可以归类为明文信息。
 * 签证包含三个部分：header（base64后），payload（base64后），secret。这个部分需要使用header和payload经过base64后使用“.”连接的字符串。
 * 然后按照header中的加密方式通过加盐加密后就组成了签证部分。
 * 需要注意的是：盐是保存在服务端的，jwt的签发也是发生在服务端的。盐就是用来进行签发和验证的，是服务器的私钥。不能泄露，否则客户端就可以自行签发jwt token了。
 *
 * 应用方式一般是在请求头中加上Authentication属性，一般在token前加上Bearer。headers:{'Authentication': 'Bearer' + token}
 *
 * AccessDeniedHandler：处理授权异常
 * AuthenticationEntryPoint：处理认证异常
 *
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
        }else if (e instanceof UsernameNotFoundException){
            responseJsonMsg = ResponseJsonMsg.error(Contains.RET_CODE_FAILED_PARAM, "没有用户");
        }else{
            responseJsonMsg = ResponseJsonMsg.error(Contains.RET_CODE_FAILED_UNKNOWN, "未登录或登录已过期");
        }

        writer.write(JSON.toJSONString(responseJsonMsg));
        writer.flush();
    }
}
