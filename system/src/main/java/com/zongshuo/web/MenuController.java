package com.zongshuo.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zongshuo.Contains;
import com.zongshuo.model.MenuModel;
import com.zongshuo.model.UserModel;
import com.zongshuo.service.MenuService;
import com.zongshuo.util.ResponseJsonMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

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
    @ApiImplicitParam(name = "userId", value = "用户编号", dataType = "Integer", paramType ="query")
    @GetMapping("/menuTree")
    public ResponseJsonMsg getMenuTree(Integer userId){
        log.info("查询菜单:{}", userId);
        List<MenuModel> menuModelList ;
        if (userId == null){
            menuModelList = menuService.getAllMenu();
        }else {
            menuModelList = menuService.getMenusByUserId(userId);
        }
        return ResponseJsonMsg.ok().setData(menuService.toMenuTree(menuModelList, 0)) ;
    }

    @ApiOperation("新增菜单")
    @PutMapping("/add")
    public ResponseJsonMsg addMenu(@RequestBody MenuModel menu){
        log.info("新增菜单:{}", menu);
        int count = menuService.count(new QueryWrapper<MenuModel>().lambda().eq(MenuModel::getTitle, menu.getTitle()));
        if (count > 0){
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_DATA_STATE, "菜单已存在！");
        }

        if (menu.getParentId() == null){
            menu.setParentId(0);
        }
        menu.setCreateTime(new Date());
        if (! menuService.save(menu)){
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_SYS, "保存菜单失败！");
        }
        return ResponseJsonMsg.ok();
    }
}
