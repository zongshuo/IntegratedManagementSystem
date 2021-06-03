package com.zongshuo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zongshuo.model.UserModel;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
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
        super.successfulAuthentication(request, response, chain, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
