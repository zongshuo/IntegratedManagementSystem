package com.zongshuo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-6-29
 * @Time: 10:42
 * @Description:
 */
@Entity
@Data
@Table(name = "SYS_DICT")
public class Dict {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id ;

    @Column(name = "code", nullable = false, unique = true, length = 200)
    private String code ;

    @Column(name = "name", nullable = false, length = 255)
    private String name ;

    @Column(name = "sort")
    private Short sort ;

    @Column(name = "comments", length = 500)
    private String comments ;

    @Column(name = "create_time", nullable = false)
    private Date createTime ;

    @Column(name = "update_time")
    private Date updateTime ;
}
