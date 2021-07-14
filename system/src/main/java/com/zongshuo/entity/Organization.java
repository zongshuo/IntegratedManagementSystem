package com.zongshuo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-6-29
 * @Time: 10:28
 * @Description:
 */
@Entity
@Data
@Table(name = "SYS_ORGANIZATION")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id ;

    @Column(name = "parent_id", nullable = false)
    private Integer parentId ;

    @Column(name = "code", length = 100)
    private String code ;

    @Column(name = "name", nullable = false, length = 200)
    private String name ;

    @Column(name = "full_name", length = 200)
    private String fullName ;

    @Column(name = "type", nullable = false)
    private Byte type ;

    @Column(name = "sort")
    private Short sort ;

    @Column(name = "comments", length = 500)
    private String comments ;

    @Column(name = "create_time", nullable = false)
    private Date createTime ;

    @Column(name = "update_time")
    private Date updateTime ;
}
