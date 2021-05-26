package com.zongshuo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
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
@ToString
@Table(name = "SYS_MENU")
@ApiModel(value = "SYS_MENU", description = "系统菜单实体")
public class Menu implements Serializable {

    /**
     * TABLE：使用一个特定的数据库表格来保存主键。
     * SEQUENCE：根据底层数据库的序列来生成主键，条件是数据库支持序列。
     * IDENTITY：主键由数据库自动生成（主要是自动增长型）
     * AUTO：主键由程序控制。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id", unique = true, nullable = false)
    @ApiModelProperty(value = "菜单id", name = "menuId")
    private Integer menuId;

    @Column(name = "parent_id")
    @ApiModelProperty(value = "上级菜单id，0是顶级菜单", name = "parentId")
    private Integer parentId;

    @Column(name = "title", length = 100)
    @ApiModelProperty(value = "菜单名称", name = "title")
    private String title ;

    @Column(name = "icon", length = 100)
    @ApiModelProperty(value = "菜单图标", name = "icon")
    private String icon ;

    @Column(name = "path", length = 100)
    @ApiModelProperty(value = "菜单路由", name = "path")
    private String path ;

    @Column(name = "component", length = 100)
    @ApiModelProperty(value = "菜单组件地址", name = "component")
    private String component ;

    @Column(name = "menu_type")
    @ApiModelProperty(value = "菜单类型：0-菜单、1-按钮", name = "menuType")
    private Byte menuType ;

    @Column(name = "sort_number")
    @ApiModelProperty(value = "排序号", name = "sortNumber")
    private Integer sortNumber ;

    @Column(name = "authority", length = 100)
    @ApiModelProperty(value = "权限标识", name = "authority")
    private String authority ;

    @Column(name = "target", length = 100)
    @ApiModelProperty(value = "打开位置", name = "target")
    private String target ;

    @Column(name = "color", length = 100)
    @ApiModelProperty(value = "图标颜色", name = "color")
    private String color ;

    @Column(name = "hide")
    @ApiModelProperty(value = "是否隐藏：0-否、1-是", name = "hide")
    private Byte hide ;

    @Column(name = "left_uid", length = 200)
    @ApiModelProperty(value = "嵌套路由左侧选中", name = "leftUid")
    private String leftUid;

    @Column(name = "deleted")
    @ApiModelProperty(value = "是否删除：0-否、1-是", name = "deleted")
    private Byte deleted ;

    @Column(name = "create_time")
    @ApiModelProperty(value = "创建时间", name = "createTime")
    private Date createTime ;

    @Column(name = "update_time")
    @ApiModelProperty(value = "更新时间", name = "updateTime")
    private Date updateTime ;
}
