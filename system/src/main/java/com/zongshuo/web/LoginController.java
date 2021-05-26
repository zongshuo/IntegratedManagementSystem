package com.zongshuo.web;

import com.alibaba.fastjson.JSONObject;
import com.zongshuo.util.ResponseJsonMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-23
 * @Time: 19:58
 * @Description:
 */
@Slf4j
@CrossOrigin
@Api(tags = "系统功能-登录及注册")
@RestController
@RequestMapping("/api/sys/login")
public class LoginController {

    @ApiOperation("注册用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "checkCode", value = "验证码", required = true, dataType = "String", paramType = "body")
    })
    @PostMapping("/register")
    public ResponseJsonMsg registerUser(@RequestBody JSONObject request){
        log.info("注册用户：{}",request);
        return ResponseJsonMsg.ok();
    }
}
