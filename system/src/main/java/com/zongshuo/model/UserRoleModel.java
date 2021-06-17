package com.zongshuo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zongshuo.entity.Role;
import com.zongshuo.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-21
 * @Time: 23:06
 * @Description:
 */
@Data
@TableName("SYS_USER_ROLE")
@ApiModel(value = "UserRoleModel对象", description = "用户角色关联关系表")
public class UserRoleModel {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "主键")
    private Integer id ;

    @ApiModelProperty(name = "userId", value = "用户表主键")
    private Integer userId ;

    @ApiModelProperty(name = "roleId", value = "角色表主键")
    private Integer roleId;
}