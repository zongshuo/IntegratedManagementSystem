package com.zongshuo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zongshuo.annotation.validators.Delete;
import com.zongshuo.annotation.validators.Insert;
import com.zongshuo.annotation.validators.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-7-14
 * @Time: 10:28
 * @Description:
 */
@Data
@TableName("SYS_ORGANIZATION")
@ApiModel(value = "Organization对象", description = "组织机构实体对象")
public class OrganizationModel implements Serializable {
    @NotNull(message = "主键不能为空！", groups = {Update.class, Delete.class})
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "主键")
    private Integer id ;

    @NotNull(message = "上级组织编码不能为空！", groups = {Insert.class, Update.class})
    @ApiModelProperty(name = "parentId", value = "上级机构编码")
    private Integer parentId ;

    @ApiModelProperty(name = "code", value = "机构编码")
    private String code ;

    @NotEmpty(message = "名称不能为空！", groups = {Insert.class, Update.class})
    @ApiModelProperty(name = "name", value = "机构名称")
    private String name ;

    @ApiModelProperty(name = "fullName", value = "机构全称")
    private String fullName ;

    @NotNull(message = "类型不能为空！", groups = {Insert.class, Update.class})
    @ApiModelProperty(name = "type", value = "机构类型")
    private Byte type ;

    @ApiModelProperty(name = "sort", value = "机构排序号")
    private Short sort ;

    @ApiModelProperty(name = "comments", value = "备注")
    private String comments ;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime ;

    @ApiModelProperty(name = "updateTime", value = "更新时间")
    private Date updateTime ;

    @TableField(exist = false)
    @ApiModelProperty(name = "children", value = "下级机构")
    private List<OrganizationModel> children ;
}
