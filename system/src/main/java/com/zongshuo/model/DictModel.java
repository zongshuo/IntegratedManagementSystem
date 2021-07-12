package com.zongshuo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zongshuo.annotations.validators.Delete;
import com.zongshuo.annotations.validators.Insert;
import com.zongshuo.annotations.validators.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-7-9
 * @Time: 18:04
 * @Description:
 */
@Data
@TableName("SYS_DICT")
@ApiModel(value = "dict对象", discriminator = "系统字典项实体类")
public class DictModel implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "字典项ID", name = "id")
    @NotNull(message = "主键不能为空！", groups = {Update.class, Delete.class})
    private Integer id ;

    @NotEmpty(message = "字典值不能为空！", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "字典值", name = "code")
    private String code ;

    @NotEmpty(message = "字典名称不能为空！", groups = {Insert.class, Update.class})
    @ApiModelProperty(value = "字典名称", name = "name")
    private String name ;

    @ApiModelProperty(value = "排序号", name = "sort")
    private Short sort ;

    @ApiModelProperty(value = "备注", name = "comments")
    private String comments ;

    @ApiModelProperty(value = "创建时间", name = "createTime")
    private Date createTime ;

    @ApiModelProperty(value = "更新时间", name = "updateTime")
    private Date updateTime ;
}
