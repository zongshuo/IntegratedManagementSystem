package com.zongshuo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zongshuo.entity.Menu;
import com.zongshuo.entity.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.persistence.*;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-6-15
 * @Time: 15:56
 * @Description:
 */
@Data
@TableName("SYS_ROLE_MENU")
@ApiModel(value = "RoleMenuModel对象", description = "角色与菜单关联关系")
public class RoleMenuModel {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "主键")
    private Integer id;

    @ApiModelProperty(name = "roleId", value = "角色表主键")
    private Integer roleId;

    @ApiModelProperty(name = "menuId", value = "菜单表主键")
    private Integer menuId ;
 }
