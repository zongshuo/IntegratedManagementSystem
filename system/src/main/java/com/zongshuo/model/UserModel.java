package com.zongshuo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zongshuo.annotations.ValidateAuchCode;
import com.zongshuo.annotations.ValidateEmail;
import com.zongshuo.annotations.ValidatePassword;
import com.zongshuo.annotations.ValidateUsername;
import com.zongshuo.annotations.validators.Insert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-21
 * @Time: 9:37
 * @Description:
 */
@Data
@TableName("SYS_USER")
@ApiModel(value = "UserModel对象", description = "系统用户模型")
public class UserModel implements UserDetails {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键", name = "id")
    private Integer id ;

    @ValidateUsername(groups = Insert.class)
    @ApiModelProperty(value = "用户名", name = "username")
    private String username ;

    // TODO 不向前端返回密码等敏感信息
    @ValidatePassword(groups = Insert.class)
    @ApiModelProperty(value = "密码", name = "password")
    private String password ;

    @ApiModelProperty(value = "昵称", name = "nickName")
    private String nickName ;

    @ApiModelProperty(value = "性别", name = "gender")
    private String gender ;

    @ValidateEmail(groups = Insert.class)
    @ApiModelProperty(value = "邮箱", name = "email")
    private String email ;

    @ApiModelProperty(value = "手机号", name = "phone")
    private Integer phone;

    @ApiModelProperty(value = "账号是否过期", name = "isAccountNonExpired")
    private boolean isAccountNonExpired ;

    @ApiModelProperty(value = "账户是否被冻结", name = "isAccountNonLocked")
    private boolean isAccountNonLocked ;

    @ApiModelProperty(value = "账户密码是否过期", name = "isCredentialsNonExpired")
    private boolean isCredentialsNonExpired;

    @ApiModelProperty(value = "账号是否可用", name = "isEnabled")
    private boolean isEnabled ;

    @ApiModelProperty(value = "创建时间")
    private Date createTime ;

    @TableField(exist = false)
    @ApiModelProperty(value = "用户角色列表", name = "roles")
    private List<RoleModel> roles = new ArrayList<>(0);

    @TableField(exist = false)
    @ApiModelProperty(value = "用户菜单列表", name = "menus")
    private List<MenuModel> menus = new ArrayList<>(0);

    @ValidateAuchCode(groups = Insert.class)
    @ApiModelProperty(value = "验证码", name = "authCode")
    @TableField(exist = false)
    private String authCode ;

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
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }


    public void setIsAccountNonExpired(boolean accountNonExpired) {
        isAccountNonExpired = accountNonExpired;
    }

    public void setIsAccountNonLocked(boolean accountNonLocked) {
        isAccountNonLocked = accountNonLocked;
    }

    public void setIsCredentialsNonExpired(boolean credentialsNonExpired) {
        isCredentialsNonExpired = credentialsNonExpired;
    }

    public void setIsEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
