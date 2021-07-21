package com.zongshuo.web;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zongshuo.Contains;
import com.zongshuo.annotations.AuthDefinition;
import com.zongshuo.model.MenuModel;
import com.zongshuo.service.MenuService;
import com.zongshuo.util.ResponseJsonMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-19
 * @Time: 20:59
 * @Description: 用于操作系统菜单
 */
@Slf4j
@RestController
@RequestMapping("/sys/menu")
@Api(tags = "系统功能-菜单管理")
@AuthDefinition(name = "菜单管理", authority = "sys:menu", path = "/system/menu")
public class MenuController extends BaseController {
    @Autowired
    private MenuService menuService;

    @ApiOperation("获取用户的树状菜单列表")
    @ApiImplicitParam(name = "path", value = "菜单地址", dataType = "String", paramType = "query")
    @GetMapping("/menuTree")
    public ResponseJsonMsg getMenuTree(Integer userId) {
        log.info("查询树状菜单:userId-{}", userId);
        List<MenuModel> menuModelList;
        if (userId == null) {
            menuModelList = menuService.getAllMenu();
        } else {
            menuModelList = menuService.getMenusByUserId(userId);
        }
        return ResponseJsonMsg.ok().setData(menuService.toMenuTree(menuModelList, 0));
    }

    @ApiOperation("条件查询系统菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户编号", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "菜单名称", dataType = "String", paramType = "query")
    })
    @GetMapping("/searchMenuTree")
    public ResponseJsonMsg searchMenuTree(String title, String path){
        return ResponseJsonMsg.ok().setData(menuService.searchMenu(title, path));
    }

    @ApiOperation("获取角色菜单列表")
    @ApiImplicitParam(name = "roleId", value = "角色编号", dataType = "Integer", paramType = "query")
    @GetMapping("/role")
    public ResponseJsonMsg getMenus(Integer roleId) {
        log.info("查询菜单列表:{}", roleId);

        List<MenuModel> menuModels;
        if (roleId == null) {
            menuModels = menuService.getAllMenu();
        } else {
            menuModels = menuService.getMenusByUserId(roleId);
        }

        if (menuModels == null) {
            menuModels = new ArrayList<>(0);
        }
        return ResponseJsonMsg.ok().setData(menuModels);
    }

    @ApiOperation("新增菜单")
    @PutMapping
    public ResponseJsonMsg addMenu(@RequestBody MenuModel menu) {
        log.info("新增菜单:{}", menu);
        int count = menuService.count(new QueryWrapper<MenuModel>().lambda().eq(MenuModel::getTitle, menu.getTitle()));
        if (count > 0) {
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_DATA_STATE, "菜单已存在！");
        }

        if (menu.getParent() == null) {
            menu.setParent(0);
        }
        menu.setCreateTime(new Date());
        if (!menuService.save(menu)) {
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_SYS, "保存菜单失败！");
        }
        return ResponseJsonMsg.ok();
    }

    @ApiOperation("编辑菜单")
    @PutMapping("/edit")
    public ResponseJsonMsg editMenu(@RequestBody @Valid MenuModel menu) {
        log.info("编辑菜单：", menu);
        try {
            menuService.updateMenu(menu);
        } catch (IllegalAccessException e) {
            log.error("编辑菜单异常:", e);
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_DATA_UPDATE, e.getMessage());
        }
        return ResponseJsonMsg.ok();
    }

    @ApiOperation("删除菜单")
    @ApiImplicitParam(name = "menuId", value = "菜单id", dataType = "Integer", paramType = "query")
    @DeleteMapping
    public ResponseJsonMsg removeMenu(@RequestBody JSONObject request) {
        log.info("删除菜单:{}", request);
        Integer menuId = request.getInteger("menuId");
        if (menuId == null) {
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_PARAM, "菜单编号不能为空！");
        }

        menuService.removeMenu(menuId);
        return ResponseJsonMsg.ok();
    }
}
