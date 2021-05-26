package com.zongshuo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-26
 * @Time: 20:59
 * @Description:
 * 该表用于缓存验证码，需要存储验证码，过期时间，验证码使用渠道, 所属用户等
 */
@Data
@Entity
@ToString
@Table(name = "SYS_AUTH_CODE-CACHE")
@ApiModel(value = "SYS_AUTH_CODE-CACHE", description = "系统验证码缓存表")
public class AuthCodeCache implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, unique = true)
    @ApiModelProperty(value = "主键", name = "id")
    private Integer id;

    @Column(name = "auth_code", nullable = false, length = 12)
    @ApiModelProperty(value = "验证码", name = "authCode")
    private String authCode ;

    @Column(name = "expire_time")
    @ApiModelProperty(value = "过期时间-毫秒", name = "expireTime")
    private Long expireTime ;


}
