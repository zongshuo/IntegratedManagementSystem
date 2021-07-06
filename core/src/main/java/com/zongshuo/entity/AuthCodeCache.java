package com.zongshuo.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-26
 * @Time: 20:59
 * @Description:
 * 该对象用于jpa自动创建表结构的定义
 * 该表用于缓存验证码，需要存储验证码，过期时间，验证码使用渠道, 所属用户等
 */
@Data
@Entity
@ToString
@Table(name = "SYS_AUTH_CODE_CACHE")
public class AuthCodeCache{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "auth_code", nullable = false, length = 12)
    private String authCode ;

    @Column(name = "expire_time")
    private Long expireTime ;

    @Column(name = "channel_no", nullable = false, length = 2)
    private String channelNo ;

    @Column(name = "user_join", nullable = false, length = 300)
    private String userJoin ;
}
