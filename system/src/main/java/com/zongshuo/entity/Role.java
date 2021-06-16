package com.zongshuo.entity;

import lombok.Data;

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
@Table(name = "SYS_ROLE")
public class Role{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id ;

    @Column(name = "role_key", unique = true, nullable = false, length = 50)
    private String roleKey ;

    @Column(name = "role_name", unique = true, nullable = false, length = 50)
    private String roleName ;

    @Column(name = "descriptions", length = 200)
    private String descriptions ;
}
