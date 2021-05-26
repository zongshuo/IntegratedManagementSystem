package com.zongshuo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-21
 * @Time: 9:37
 * @Description:
 */
@Entity
@Data
@ToString
@Table(name = "SYS_USER")
@TableName("SYS_USER")
@ApiModel(value = "SYS_USER", description = "系统用户表")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @ApiModelProperty(value = "主键", name = "id")
    @TableId(type = IdType.AUTO)
    private Integer id ;

    @Column(name = "user_name", unique = true, nullable = false, length = 30)
    @ApiModelProperty(value = "用户名", name = "username")
    private String username ;

    @Column(name = "password", nullable = false, length = 200)
    @ApiModelProperty(value = "密码", name = "password")
    private String password ;

    @Column(name = "nick_name", nullable = false, length = 30)
    @ApiModelProperty(value = "昵称", name = "nickName")
    private String nickName ;

    @Column(name = "email", nullable = false, length = 200)
    @ApiModelProperty(value = "邮箱", name = "email")
    private String email ;

    @Column(name = "is_account_non_expired")
    @ApiModelProperty(value = "账号是否过期", name = "isAccountNonExpired")
    private boolean isAccountNonExpired ;

    @Column(name = "is_account_non_locked")
    @ApiModelProperty(value = "账户是否被冻结", name = "isAccountNonLocked")
    private boolean isAccountNonLocked ;

    @Column(name = "is_credentials_non_expired")
    @ApiModelProperty(value = "账户密码是否过期", name = "isCredentialsNonExpired")
    private boolean isCredentialsNonExpired;

    @Column(name = "is_enabled")
    @ApiModelProperty(value = "账号是否可用", name = "isEnabled")
    private boolean isEnabled ;

    @ManyToMany
    @JoinTable(name = "SYS_USER_ROLE"
            , joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
            , inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Set<Role> roles = new HashSet<>(0);

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
