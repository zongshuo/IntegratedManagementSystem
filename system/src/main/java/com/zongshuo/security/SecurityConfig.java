package com.zongshuo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-21
 * @Time: 9:35
 * @Description: spring security 配置类
 * spring security是基于springAOP和servlet过滤器的可高度定制的安全框架
 * 可同时在web请求级和方法调用级处理身份认证和授权
 * security的web基础是Filters，即通过一层层的Filters来对web请求做处理
 *
 * security的重要概念
 *  SecurityContext：上下文对象，Authentication对象会放在里面
 *  SecurityContextHolder：用于拿到上下文对象的静态工具类
 *  Authentication：认证接口，定义了认证对象的数据形式
 *  AuthenticationManager：用于校验Authentication，返回一个认证完成后的Authentication对象
 *
 * EnableWebSecurity用于开启webSecurity功能
 * EnableGlobalMethodSecurity用于启用security的注解功能，默认是关闭的
 * 有三种注解类型可以选择：jsr-250、prePostEnabled、securedEnabled
 * 每种类型的注解提供了功能各异的注解供验证
 * TODO 使用MDC进行用户日志追踪
 */

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //开始http请求的验证
        //打开跨域资源共享，解决跨域问题。会使用OPTIONS类型的http请求
        // 关闭csrf攻击防护，前后端分离项目，已存在token验证，不再需要CSRF的token验证
        http.cors().and().csrf().disable()
                .authorizeRequests()
                //跨域预查接口全部允许
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                //新增swagger请求资源不认证处理
                .antMatchers("/swagger-resources/**", "/swagger-ui/*", "/v2/api-docs", "/webjars/**").permitAll()
                //新增无需认证的请求接口
                .antMatchers("/sys/login/*").permitAll()
                //此处后面配置的请求都需要经过认证
                .anyRequest().authenticated()
                //springSecurity永远不会创建HTTPSession，且不会使用HTTPSession获取SecurityContext
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().formLogin().loginPage("/sys/login").usernameParameter("username").passwordParameter("password");

        //设置认证或授权异常自定义处理类
        http.exceptionHandling().accessDeniedHandler(jwtExceptionHandler()).authenticationEntryPoint(jwtExceptionHandler());
        //默认登出路径“/logout”，默认重定向到登录页面
        http.logout().logoutSuccessHandler(jwtLogoutSuccessHandler());
        /**
         * spring security中对过滤器的配置使用addFilterBefore，该方法的作用是在指定的过滤器前加入自定义的过滤器
         * 其中第一个参数为自定义的过滤器，第二个参数为指定的过滤器
         * spring security基于Filter技术，通过一系列内置的或自定义的安全filter，实现接口的认证和授权。
         * 我们想要自定义基于JWT拦截的过滤器，可以通过以下方式实现
         */
        http.addFilterBefore(jwtLoginFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /**
         * AuthenticationManagerBuilder用来构建AuthenticationManager
         * 本方法用来配置父类中的authenticationManagerBuilder属性
         * 父类创建AuthenticationManager对象时需要用到该属性
         *
         * builder构建AuthenticationManager时通常有三种类型：
         * 1、InMemoryUserDetailsManagerConfigurer：基于内存存储用户账号详情的安全配置器，通常用于开发调试环境
         * 2、JdbcUserDetailsManagerConfigurer：基于关系型数据库存储用户账号详情的安全配置
         * 3、LdapAuthenticationProviderConfigurer：基于Ldap（轻量级目录访问协议）存储账号的安全配置器
         * 前两种类型最终生成一个DaoAuthenticationProvider，内含一个UserDetailsService
         * 因存储形式类似，统一抽象成UserDetailsManagerConfigurer
         */

        //根据自定义的实现类获取认证配置对象并设置密码的加密算法
        auth.userDetailsService(myUserDetailsService()).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        /**
         * AuthenticationManager(认证管理器)的作用是对用户的未授信凭据进行认证，认证通过返回授信状态的凭据，否则抛出异常
         * 该类中规范了springSecurity的过滤器要如何执行身份认证，并在身份认证成功后返回一个经过认证的Authentication对象
         * 通过configure（AuthenticationManager auth）方法配置该bean
         * 返回的类型为AuthenticationManagerDelegator
         */
        return super.authenticationManagerBean();
    }

    /**
     * BCrypt：一个跨平台的文件加密工具，由它加密的文件可以在所有操作系统和处理器上进行转移
     * 它的口令必须是8-56个字符
     * spring security认证时会使用我们指定的加密器，如果没有回抛出异常
     * @return
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MyUserDetailsService myUserDetailsService() {
        return new MyUserDetailsService();
    }

    @Bean
    public JwtExceptionHandler jwtExceptionHandler() {
        return new JwtExceptionHandler();
    }

    @Bean
    public JwtLogoutSuccessHandler jwtLogoutSuccessHandler() {
        return new JwtLogoutSuccessHandler();
    }

    @Bean
    public JwtLoginFilter jwtLoginFilter() throws Exception{
        return new JwtLoginFilter(authenticationManagerBean());
    }
}
