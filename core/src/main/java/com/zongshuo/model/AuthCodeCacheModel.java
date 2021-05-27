package com.zongshuo.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-26
 * @Time: 20:59
 * @Description:
 * 系统验证码缓存表模型，用于前端参数接收，数据库操作等
 */
@Data
@ToString
@TableName("SYS_AUTH_CODE_CACHE")
@ApiModel(value = "AuthCodeCacheModel", description = "系统验证码缓存表模型")
public class AuthCodeCacheModel implements Serializable {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "主键", name = "id")
    private Integer id;

    @ApiModelProperty(value = "验证码", name = "authCode")
    private String authCode ;

    @ApiModelProperty(value = "过期时间-毫秒", name = "expireTime")
    private Long expireTime ;

    @ApiModelProperty(value = "使用渠道", name = "channelNo")
    private String channelNo ;

    @ApiModelProperty(value = "关联用户", name = "userJoin")
    private String userJoin ;
}
