package com.zongshuo.web;

import com.zongshuo.Contains;
import com.zongshuo.model.UserModel;
import com.zongshuo.service.UserService;
import com.zongshuo.util.PageParam;
import com.zongshuo.util.PageResult;
import com.zongshuo.util.ResponseJsonMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    private UserService userService;
    @ApiOperation("分页查询人员信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页记录数", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "username", value = "用户账号", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "nickName", value = "用户昵称", dataType = "String", paramType = "query")
    })
    @GetMapping("/page")
    public PageResult<UserModel> getPage(HttpServletRequest request){
        log.info("分页查询客户信息:{}", request);
        PageParam<UserModel> pageParam = new PageParam<>(request);
        return userService.getPage(pageParam);
    }

    @ApiOperation("查询用户信息")
    @GetMapping("/userInfo")
    public ResponseJsonMsg getUserInfo(){
        UserModel loginUser = getLoginUser();
        if (StringUtils.isEmpty(loginUser.getUsername())){
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_PARAM, "用户名不能为空！");
        }
        UserModel userModel;
        try {
            userModel = userService.getUserInfo(loginUser.getUsername());
        } catch (IllegalAccessException e) {
            log.error("查询用户信息异常：{}-{}", loginUser.getUsername(), e.getMessage());
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_DATA_NOT_FOUND, e.getMessage());
        }

        return ResponseJsonMsg.ok().setData(userModel);
    }

    @ApiOperation("新增用户")
    @PutMapping
    public ResponseJsonMsg addUser(@RequestBody UserModel user){
        log.info("新增用户:", user);
        try {
            userService.addUser(user);
        } catch (IllegalAccessException e) {
            log.error("添加用户异常:", e);
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_DATA_UPDATE, e.getMessage());
        }
        return ResponseJsonMsg.ok();
    }

}
