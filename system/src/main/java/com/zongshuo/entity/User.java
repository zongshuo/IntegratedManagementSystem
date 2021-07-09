package com.zongshuo.entity;


import lombok.Data;

import javax.persistence.*;
import java.util.Date;


/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-21
 * @Time: 9:37
 * @Description:
 */
@Data
@Entity
@Table(name = "SYS_USER")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id ;

    @Column(name = "username", unique = true, nullable = false, length = 30)
    private String username ;

    @Column(name = "password", nullable = false, length = 200)
    private String password ;

    @Column(name = "nick_name", length = 30)
    private String nickName ;

    @Column(name = "gender")
    private Short gender;

    @Column(name = "email", nullable = false, length = 200)
    private String email ;

    @Column(name = "phone")
    private Integer phone;

    @Column(name = "is_account_non_expired")
    private boolean isAccountNonExpired ;

    @Column(name = "is_account_non_locked")
    private boolean isAccountNonLocked ;

    @Column(name = "is_credentials_non_expired")
    private boolean isCredentialsNonExpired;

    @Column(name = "is_enabled")
    private boolean isEnabled ;

    @Column(name = "create_time")
    private Date createTime;
}
