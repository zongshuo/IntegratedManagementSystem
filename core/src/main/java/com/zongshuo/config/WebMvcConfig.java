package com.zongshuo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-20
 * @Time: 13:44
 * @Description:
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 支持跨域
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路径
        registry.addMapping("/**")
                // 设置允许跨域请求的域名
                .allowedOrigins("*")
                // 设置允许使用的方法
                .allowedMethods("OPTIONS", "PUT", "DELETE", "GET", "POST", "HEAD")
                // 设置允许的header属性
                .allowedHeaders("*")
                // 是否允许证书，此处为true时，allowedOrigins不能为*
//                .allowCredentials(true)
                // 跨域允许的时间
                .maxAge(3600);

    }
}
