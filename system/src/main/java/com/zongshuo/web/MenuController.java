package com.zongshuo.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-19
 * @Time: 20:59
 * @Description: 用于操作系统菜单
 */
@Api(tags = "系统功能-菜单管理")
@RestController
@RequestMapping("/api/sys/menu")
@Slf4j
public class MenuController {

    @ApiOperation("分页查询系统菜单")
    @GetMapping("/page")
    public void getList(){
    }

}
