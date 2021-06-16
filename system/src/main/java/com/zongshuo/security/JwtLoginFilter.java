package com.zongshuo.security;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zongshuo.Contains;
import com.zongshuo.model.UserModel;
import com.zongshuo.util.JwtUtil;
import com.zongshuo.util.ResponseJsonMsg;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-31
 * @Time: 18:21
 * @Description: 登录过滤器
 * 用于指定你登录操作请求的路径
 * 设置登录成功后的响应信息，包括token
 * 设置登录失败后的响应信息
 */
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    public JwtLoginFilter(AuthenticationManager manager) {
        this.setAuthenticationManager(manager);
        this.setFilterProcessesUrl("/sys/login");
    }

    /**
     * spring security不能解析json格式的请求参数
     * 需要通过此方法将参数取出后重新组装后UserDetailsService才能接受到用户名
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //此处需要前端请求是将content-type设置成：application/json
        if (MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType())) {
            ObjectMapper objectMapper = new ObjectMapper();
            UsernamePasswordAuthenticationToken token = null;
            try(InputStream inputStream = request.getInputStream()) {
                UserModel userModel = objectMapper.readValue(inputStream, UserModel.class);
                token = new UsernamePasswordAuthenticationToken(userModel.getUsername(), userModel.getPassword());
            } catch (IOException e) {
                token = new UsernamePasswordAuthenticationToken("", "");
            }finally {
                setDetails(request, token);
                return this.getAuthenticationManager().authenticate(token);
            }

        } else {
            return super.attemptAuthentication(request, response);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
            String userToken = JwtUtil.buildToken(userDetails.getUsername(), Contains.JWT_SLOT, Contains.EFFECTIVE_TIME_USER_TOKEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = response.getWriter();
        writer.write(JSONObject.toJSONString(
                ResponseJsonMsg.ok("登录成功！").put("access_token", userToken).put("token_type", JwtUtil.TOKEN_TYPE)));
        writer.flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        PrintWriter writer = response.getWriter();
        ResponseJsonMsg responseJsonMsg = null;
        //AbstractUserDetailsAuthenticationProvider类的hideUserNotFoundExceptions字段默认为true
        // 需要设置为false才会在捕获UsernameNotFoundException异常时直接抛出，否则只会抛出BadCredentialsException异常
        if (failed instanceof BadCredentialsException){
            responseJsonMsg = ResponseJsonMsg.error(Contains.RET_CODE_FAILED_AUTH_LOGIN, "账号或密码不正确！");
        } else {
            responseJsonMsg = ResponseJsonMsg.error(Contains.RET_CODE_FAILED_UNKNOWN, failed.getMessage());
        }

        writer.write(JSONObject.toJSONString(responseJsonMsg));
        writer.flush();
    }
}
