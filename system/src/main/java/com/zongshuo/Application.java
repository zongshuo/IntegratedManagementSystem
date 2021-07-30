package com.zongshuo;

import com.zongshuo.annotation.AuthDefinition;
import com.zongshuo.authorization.handler.AuthService;
import com.zongshuo.authorization.model.AccessPoint;
import com.zongshuo.authorization.model.AccessType;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
        try {
            AuthService service = AuthService.fromInitAuth(AuthDefinition.class);
            service.collectAccessPoint("com/zongshuo/**/");
        } catch (IOException e) {
            log.error("创建鉴权异常:", e);
        }
        SpringApplication.run(Application.class, args);
    }
}
