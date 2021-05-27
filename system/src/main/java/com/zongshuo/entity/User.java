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
@Data
@Entity
@Table(name = "SYS_USER")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id ;

    @Column(name = "user_name", unique = true, nullable = false, length = 30)
    private String username ;

    @Column(name = "password", nullable = false, length = 200)
    private String password ;

    @Column(name = "nick_name", nullable = false, length = 30)
    private String nickName ;

    @Column(name = "email", nullable = false, length = 200)
    private String email ;

    @Column(name = "is_account_non_expired")
    private boolean isAccountNonExpired ;

    @Column(name = "is_account_non_locked")
    private boolean isAccountNonLocked ;

    @Column(name = "is_credentials_non_expired")
    private boolean isCredentialsNonExpired;

    @Column(name = "is_enabled")
    private boolean isEnabled ;

    @ManyToMany
    @JoinTable(name = "SYS_USER_ROLE"
            , joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
            , inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private Set<Role> roles = new HashSet<>(0);
}
