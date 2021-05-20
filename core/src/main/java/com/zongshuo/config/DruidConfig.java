package com.zongshuo.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-20
 * @Time: 17:48
 * @Description:
 * 数据库连接池druid监控平台的配置类
 * 访问路径 http://localhost:8080/druid/index.html
 */
@Configuration
public class DruidConfig {

    @Bean
    public ServletRegistrationBean druidServlet(){
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        //ip白名单
        servletRegistrationBean.addInitParameter("allow", "");
        //ip黑名单(共同存在时，deny优先于allow)
        servletRegistrationBean.addInitParameter("deny","");

        //控制台管理用户
        servletRegistrationBean.addInitParameter("loginUsername", "admin");
        servletRegistrationBean.addInitParameter("loginPassword", "admin");

        //是否能够重置数据，禁用页面上的Rest all功能
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }
}
