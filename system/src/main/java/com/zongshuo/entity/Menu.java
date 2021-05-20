package com.zongshuo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
@Entity
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
    @ApiModelProperty("菜单id")
    private Integer menuId;

    @Column(name = "parent_id")
    @ApiModelProperty("上级菜单id，0是顶级菜单")
    private Integer parentId;

    @Column(name = "title", length = 100)
    @ApiModelProperty("菜单名称")
    private String title ;

    @Column(name = "icon", length = 100)
    @ApiModelProperty("菜单图标")
    private String icon ;

    @Column(name = "path", length = 100)
    @ApiModelProperty("菜单路由")
    private String path ;

    @Column(name = "component", length = 100)
    @ApiModelProperty("菜单组件地址")
    private String component ;

    @Column(name = "menu_type")
    @ApiModelProperty("菜单类型：0-菜单、1-按钮")
    private Byte menuType ;

    @Column(name = "sort_number")
    @ApiModelProperty("排序号")
    private Integer sortNumber ;

    @Column(name = "authority", length = 100)
    @ApiModelProperty("权限标识")
    private String authority ;

    @Column(name = "target", length = 100)
    @ApiModelProperty("打开位置")
    private String target ;

    @Column(name = "color", length = 100)
    @ApiModelProperty("图标颜色")
    private String color ;

    @Column(name = "hide")
    @ApiModelProperty("是否隐藏：0-否、1-是")
    private Byte hide ;

    @Column(name = "left_uid", length = 200)
    @ApiModelProperty("嵌套路由左侧选中")
    private String leftUid;

    @Column(name = "deleted")
    @ApiModelProperty("是否删除：0-否、1-是")
    private Byte deleted ;

    @Column(name = "create_time")
    @ApiModelProperty("创建时间")
    private Date createTime ;

    @Column(name = "update_time")
    @ApiModelProperty("更新时间")
    private Date updateTime ;

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public Byte getMenuType() {
        return menuType;
    }

    public void setMenuType(Byte menuType) {
        this.menuType = menuType;
    }

    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Byte getHide() {
        return hide;
    }

    public void setHide(Byte hide) {
        this.hide = hide;
    }

    public String getLeftUid() {
        return leftUid;
    }

    public void setLeftUid(String leftUid) {
        this.leftUid = leftUid;
    }

    public Byte getDeleted() {
        return deleted;
    }

    public void setDeleted(Byte deleted) {
        this.deleted = deleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "menuId=" + menuId +
                ", parentId=" + parentId +
                ", title='" + title + '\'' +
                ", icon='" + icon + '\'' +
                ", path='" + path + '\'' +
                ", component='" + component + '\'' +
                ", menuType=" + menuType +
                ", sortNumber=" + sortNumber +
                ", authority='" + authority + '\'' +
                ", target='" + target + '\'' +
                ", color='" + color + '\'' +
                ", hide=" + hide +
                ", leftUid='" + leftUid + '\'' +
                ", deleted=" + deleted +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
