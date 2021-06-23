package com.zongshuo.web;

import com.zongshuo.model.UserModel;
import com.zongshuo.service.MenuService;
import com.zongshuo.util.ResponseJsonMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-19
 * @Time: 20:59
 * @Description: 用于操作系统菜单
 */
@Api(tags = "系统功能-菜单管理")
@RestController
@RequestMapping("/sys/menu")
@Slf4j
public class MenuController extends BaseController{

    @Autowired
    private MenuService menuService;

    @ApiOperation("分页查询系统菜单")
    @GetMapping("/page")
    public void getList(){
    }

    @ApiOperation("获取用户的树状菜单列表")
    @GetMapping("/menuTree")
    public ResponseJsonMsg getMenuTree(){
        UserModel user = getLoginUser();
        return ResponseJsonMsg.ok().setData(menuService.toMenuTree(menuService.getMenusByUserId(user.getId()), 0)) ;
    }

}
