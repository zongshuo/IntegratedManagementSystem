package com.zongshuo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-6-29
 * @Time: 10:49
 * @Description:
 */
@Data
@Entity
@Table(name = "SYS_DICT_DATA")
public class DictData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id ;

    @Column(name = "dict_id", nullable = false)
    private Integer dictId ;

    @Column(name = "code", nullable = false, length = 100)
    private String code ;

    @Column(name = "name", nullable = false, length = 200)
    private String name ;

    @Column(name = "sort")
    private Short sort ;

    @Column(name = "comments", length = 400)
    private String comments ;

    @Column(name = "create_time")
    private Date createTime ;

    @Column(name = "update_time")
    private Date updateTime ;
}
