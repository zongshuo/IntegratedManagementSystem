package com.zongshuo;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-20
 * @Time: 10:42
 * @Description:
 */
@Slf4j
@MapperScan("com.zongshuo.mapper")
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
//        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        SpringApplication.run(Application.class, args);
    }
}
