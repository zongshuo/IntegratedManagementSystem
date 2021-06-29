package com.zongshuo.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-6-25
 * @Time: 17:47
 * @Description:
 * MyBatisPlus配置类
 */
@Configuration
@EnableTransactionManagement
@ConditionalOnClass(value = {PaginationInterceptor.class})
public class MyBatisPlusConfig {

    /**
     * 分页拦截器
     * @return
     */
    @Bean
    public PaginationInterceptor PaginationInterceptor(){
        return new PaginationInterceptor();
    }
}
