package com.zongshuo.web;

import com.zongshuo.Contains;
import com.zongshuo.model.UserModel;
import com.zongshuo.service.UserModelService;
import com.zongshuo.util.ResponseJsonMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-6-15
 * @Time: 15:00
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/sys/user")
@Api(tags = "系统功能-用户操作")
public class UserController extends BaseController{
    @Autowired
    private UserModelService userModelService;

    @ApiOperation("查询用户信息")
    @GetMapping("/userInfo")
    public ResponseJsonMsg getUserInfo(){
        UserModel loginUser = getLoginUser();
        if (StringUtils.isEmpty(loginUser.getUsername())){
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_PARAM, "用户名不能为空！");
        }
        UserModel userModel;
        try {
            userModel = userModelService.getUserInfo(loginUser.getUsername());
        } catch (IllegalAccessException e) {
            log.error("查询用户信息异常：{}-{}", loginUser.getUsername(), e.getMessage());
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_DATA_NOT_FOUND, e.getMessage());
        }

        return ResponseJsonMsg.ok().setData(userModel);
    }
}
