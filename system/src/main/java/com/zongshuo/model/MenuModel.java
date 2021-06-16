package com.zongshuo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("SYS_MENU")
@ApiModel(value = "MenuModel对象", description = "系统菜单实体")
public class MenuModel implements Serializable {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "菜单id", name = "menuId")
    private Integer menuId;

    @ApiModelProperty(value = "上级菜单id，0是顶级菜单", name = "parentId")
    private Integer parentId;

    @ApiModelProperty(value = "菜单名称", name = "title")
    private String title ;

    @ApiModelProperty(value = "菜单图标", name = "icon")
    private String icon ;

    @ApiModelProperty(value = "菜单路由", name = "path")
    private String path ;

    @ApiModelProperty(value = "菜单组件地址", name = "component")
    private String component ;

    @ApiModelProperty(value = "菜单类型：0-菜单、1-按钮", name = "menuType")
    private Byte menuType ;

    @ApiModelProperty(value = "排序号", name = "sortNumber")
    private Integer sortNumber ;

    @ApiModelProperty(value = "权限标识", name = "authority")
    private String authority ;

    @ApiModelProperty(value = "打开位置", name = "target")
    private String target ;

    @ApiModelProperty(value = "图标颜色", name = "color")
    private String color ;

    @ApiModelProperty(value = "是否隐藏：0-否、1-是", name = "hide")
    private Byte hide ;

    @ApiModelProperty(value = "嵌套路由左侧选中", name = "leftUid")
    private String leftUid;

    @ApiModelProperty(value = "是否删除：0-否、1-是", name = "deleted")
    private Byte deleted ;

    @ApiModelProperty(value = "创建时间", name = "createTime")
    private Date createTime ;

    @ApiModelProperty(value = "更新时间", name = "updateTime")
    private Date updateTime ;
}