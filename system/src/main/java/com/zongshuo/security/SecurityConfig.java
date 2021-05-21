package com.zongshuo.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-21
 * @Time: 9:35
 * @Description:
 * spring security 配置类
 * spring security是基于springAOP和servlet过滤器的可高度定制的安全框架
 * 可同时在web请求级和方法调用级处理身份认证和授权
 *
 * EnableWebSecurity用于开启webSecurity功能
 * EnableGlobalMethodSecurity用于启用security的注解功能，默认是关闭的
 * 有三种注解类型可以选择：jsr-250、prePostEnabled、securedEnabled
 * 每种类型的注解提供了功能各异的注解供验证
 */

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //开始http请求的验证
        http.authorizeRequests()
                //新增swagger请求资源不认证处理
                .antMatchers("/swagger-resources/**", "/swagger-ui/*", "/v2/api-docs", "/webjars/**").permitAll()
                //新增无需认证的请求接口
                .antMatchers("/api/login").permitAll()
                //此处后面配置的请求都需要经过认证
                .anyRequest().authenticated()
                //打开跨域资源共享，解决跨域问题。会使用OPTIONS类型的http请求
                .and().cors();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
