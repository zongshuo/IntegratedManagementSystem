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
@Table(name = "SYS_USER_ROLE")
public class UserRole{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false, referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", insertable = false, updatable = false, referencedColumnName = "id")
    private Role role;

}