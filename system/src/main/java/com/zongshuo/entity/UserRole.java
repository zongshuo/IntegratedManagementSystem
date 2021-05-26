package com.zongshuo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-21
 * @Time: 23:06
 * @Description:
 */
@Entity
@Data
@ToString
@Table(name = "SYS_USER_ROLE")
@TableName("SYS_USER_ROLE")
@ApiModel(value = "SYS_USER_ROLE", description = "系统人员角色表")
public class UserRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @ApiModelProperty(value = "主键", name = "id")
    @TableId(type = IdType.AUTO)
    private Integer id ;

    @Column(name = "user_id", nullable = false)
    @ApiModelProperty(value = "用户主键", name = "userId")
    private Integer userId ;

    @Column(name = "role_id", nullable = false)
    @ApiModelProperty(value = "角色主键", name = "roleId")
    private Integer roleId ;
}
