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

    @Column(name = "parent_code", nullable = false, length = 100)
    private String parentCode ;

    @Column(name = "org_code", nullable = false, length = 100)
    private String orgCode ;

    @Column(name = "org_name", nullable = false, length = 200)
    private String orgName ;

    @Column(name = "org_full_name", nullable = false, length = 200)
    private String orgFullName ;

    @Column(name = "org_type", nullable = false)
    private Byte orgType ;

    @Column(name = "sort")
    private Short sort ;

    @Column(name = "comments", length = 500)
    private String comments ;

    @Column(name = "create_time", nullable = false)
    private Date createTime ;

    @Column(name = "update_time")
    private Date updateTime ;
}
