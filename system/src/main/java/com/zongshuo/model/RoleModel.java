package com.zongshuo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zongshuo.annotations.validators.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-21
 * @Time: 22:57
 * @Description:
 */
@Data
@TableName("SYS_ROLE")
@ApiModel(value = "RoleModel对象", description = "系统角色表")
public class RoleModel implements GrantedAuthority {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键", name = "id")
    @NotNull(groups = Update.class)
    private Integer id ;

    @ApiModelProperty(value = "角色标识", name = "roleKey")
    @NotEmpty(groups = Update.class)
    private String roleKey ;

    @ApiModelProperty(value = "角色名称", name = "roleName")
    @NotEmpty(groups = Update.class)
    private String roleName ;

    @ApiModelProperty(value = "角色说明", name = "descriptions")
    private String descriptions ;

    @Override
    public String getAuthority() {
        return this.roleKey;
    }
}
