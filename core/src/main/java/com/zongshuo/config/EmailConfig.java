package com.zongshuo.config;

import com.zongshuo.util.email.SendMail;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.security.GeneralSecurityException;
import java.util.Map;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-26
 * @Time: 14:56
 * @Description:
 * 配置邮件模块
 */
@Slf4j
@Data
@Component
@PropertySource(value = {"IMS.properties"},encoding="UTF-8")
@ConfigurationProperties(prefix = "ims")
public class EmailConfig {
    private Map<String, Map<String, String>> mails;

    @Bean
    public SendMail sendMail(){
        SendMail sendMail = null;
        try {
            for (String key : mails.keySet()){
                sendMail = SendMail.getInstance(key
                        , mails.get(key).get("host")
                        , mails.get(key).get("username")
                        , mails.get(key).get("password")
                        , Boolean.valueOf(mails.get(key).get("SLLEnable")));
            }
        } catch (GeneralSecurityException e) {
            log.error("邮件模块初始化异常：", e);
        }
        return sendMail;
    }
}
