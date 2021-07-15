package com.zongshuo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zongshuo.entity.Organization;
import com.zongshuo.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("SYS_ORG_PERSON")
@ApiModel(value = "OrganizationPersonModel对象", description = "组织人员关联关系")
public class OrganizationPersonModel implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "主键")
    private Integer id ;

    @ApiModelProperty(name = "orgId", value = "组织主键")
    private Integer orgId ;

    @ApiModelProperty(name = "userId", value = "人员主键")
    private Integer userId ;

    @ApiModelProperty(name = "createTime", value = "创建时间")
    private Date createTime ;
}
