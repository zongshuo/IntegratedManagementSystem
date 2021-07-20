package com.zongshuo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-20
 * @Time: 19:35
 * @Description: 系统菜单实体类
 */
@Data
@Entity
@Table(name = "SYS_MENU")
public class Menu{

    /**
     * TABLE：使用一个特定的数据库表格来保存主键。
     * SEQUENCE：根据底层数据库的序列来生成主键，条件是数据库支持序列。
     * IDENTITY：主键由数据库自动生成（主要是自动增长型）
     * AUTO：主键由程序控制。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "parent", nullable = false)
    private Integer parent;

    @Column(name = "name", length = 50, nullable = false)
    private String name ;

    @Column(name = "authority", length = 100, nullable = false)
    private String authority ;

    @Column(name = "title", length = 100)
    private String title ;

    @Column(name = "icon", length = 100)
    private String icon ;

    @Column(name = "path", length = 100)
    private String path ;

    @Column(name = "component", length = 100)
    private String component ;

    @Column(name = "type")
    private Byte type ;

    @Column(name = "sort_number")
    private Integer sortNumber ;


    @Column(name = "target", length = 100)
    private String target ;

    @Column(name = "color", length = 100)
    private String color ;

    @Column(name = "hide")
    private Byte hide ;

    @Column(name = "left_uid", length = 200)
    private String leftUid;

    @Column(name = "deleted")
    private Byte deleted ;

    @Column(name = "create_time", nullable = false)
    private Date createTime ;

    @Column(name = "update_time")
    private Date updateTime ;
}
