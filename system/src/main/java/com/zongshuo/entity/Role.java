package com.zongshuo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-21
 * @Time: 22:57
 * @Description:
 */
@Entity
@Data
@ToString
@Table(name = "SYS_ROLE")
@ApiModel(value = "SYS_ROLE", description = "系统角色表")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    @ApiModelProperty(value = "主键", name = "id")
    private Integer id ;

    @Column(name = "role_key", unique = true, nullable = false, length = 50)
    @ApiModelProperty(value = "角色标识", name = "roleKey")
    private String roleKey ;

    @Column(name = "role_name", unique = true, nullable = false, length = 50)
    @ApiModelProperty(value = "角色名称", name = "roleName")
    private String roleName ;

    @Column(name = "descriptions", length = 200)
    @ApiModelProperty(value = "角色说明", name = "descriptions")
    private String descriptions ;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getAuthority() {
        return null;
    }
}
