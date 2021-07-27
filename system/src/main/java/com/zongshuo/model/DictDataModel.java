package com.zongshuo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zongshuo.annotation.annotations.validators.Delete;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-7-10
 * @Time: 11:12
 * @Description:
 */
@Data
@TableName("SYS_DICT_DATA")
@ApiModel(value = "DictDataModel对象", description = "字典项字典值对象")
public class DictDataModel implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键", name = "id")
    @NotNull(message = "主键不能为空！", groups = {Delete.class})
    private Integer id ;

    @ApiModelProperty(value = "字典项主键", name = "dictId")
    private Integer dictId ;

    @ApiModelProperty(value = "字典值", name = "code")
    private String code ;

    @ApiModelProperty(value = "字典值名称", name = "name")
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
