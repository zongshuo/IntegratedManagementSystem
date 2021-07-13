package com.zongshuo.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zongshuo.Contains;
import com.zongshuo.annotations.validators.Update;
import com.zongshuo.model.MenuModel;
import com.zongshuo.model.RoleModel;
import com.zongshuo.service.MenuService;
import com.zongshuo.service.RoleService;
import com.zongshuo.util.PageParam;
import com.zongshuo.util.PageResult;
import com.zongshuo.util.ResponseJsonMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-19
 * @Time: 20:59
 * @Description: 用于操作系统角色
 */
@Api(tags = "系统功能-角色管理")
@RestController
@RequestMapping("/sys/role")
@Slf4j
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;

    @ApiOperation("分页查询系统菜单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "limit", value = "每页条数", dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "roleName", value = "角色名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "roleKey", value = "角色标识", dataType = "String", paramType = "query")
    })
    @GetMapping("/page")
    public PageResult<RoleModel> getPage(HttpServletRequest request) {
        log.info("分页查询角色:{}", request);
        PageParam<RoleModel> pageParam = new PageParam<>(request);
        return roleService.getPage(pageParam);
    }

    @ApiOperation("查询所有角色的列表")
    @GetMapping("/list")
    public ResponseJsonMsg getList(){
        log.info("查询角色列表");
        List<RoleModel> roles = roleService.list(
                new QueryWrapper<RoleModel>()
                        .lambda()
                        .orderByAsc(RoleModel::getRoleName)
                        .orderByAsc(RoleModel::getId));
        if (roles == null){
            roles = new ArrayList<>(0);
        }

        return ResponseJsonMsg.ok().setData(roles);
    }

    @ApiOperation("获取角色的树状菜单列表")
    @ApiImplicitParam(name = "roleId", value = "角色编号", dataType = "Integer", paramType = "query")
    @GetMapping("/menuTree")
    public ResponseJsonMsg getMenuTree(Integer roleId) {
        log.info("查询角色树状菜单:{}", roleId);
        List<MenuModel> menuModelList = roleService.getRoleMenus(roleId);
        menuModelList = menuService.toMenuTree(menuModelList, 0);
        return ResponseJsonMsg.ok().setData(menuModelList);
    }

    @ApiOperation("获取角色的菜单列表")
    @ApiImplicitParam(name = "roleId", value = "角色编号", dataType = "Integer", paramType = "query")
    @GetMapping("/menus")
    public ResponseJsonMsg getMenus(Integer roleId){
        log.info("查询角色菜单列表：{}", roleId);
        return ResponseJsonMsg.ok().setData(roleService.getRoleMenus(roleId));
    }

    @ApiOperation("新增角色")
    @PutMapping("/save")
    public ResponseJsonMsg addMenu(@RequestBody RoleModel role) {
        log.info("新增角色:{}", role);
        Integer count = roleService.count(
                new QueryWrapper<RoleModel>()
                        .lambda()
                        .eq(RoleModel::getRoleName, role.getRoleName())
                        .or()
                        .eq(RoleModel::getRoleKey, role.getRoleKey()));
        if (count > 0) {
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_DATA_STATE, "角色名称或角色标识已存在！");
        }

        if (roleService.saveOrUpdate(role)) {
            return ResponseJsonMsg.ok();
        }

        return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_SYS, "保存角色失败！");
    }

    @ApiOperation("编辑角色")
    @PutMapping("/edit")
    public ResponseJsonMsg editMenu(@RequestBody @Validated(Update.class) RoleModel role){
        log.info("编辑角色:{}", role);
        try {
            roleService.editRole(role);
        }catch (IllegalStateException e){
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_DATA_UPDATE, e.getMessage());
        }
        return ResponseJsonMsg.ok();
    }

    @ApiOperation("删除角色")
    @ApiImplicitParam(name = "roleIds", value = "角色逐渐数组", dataType = "Array", paramType = "query")
    @DeleteMapping
    public ResponseJsonMsg removeRole(@RequestBody Integer [] roleIds){
        log.info("删除角色:{}", roleIds);
        if (roleIds == null || roleIds.length == 0){
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_PARAM, "请选择要删除的角色！");
        }
        try {
            roleService.removeRole(roleIds);
        } catch (IllegalAccessException e){
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_DATA_UPDATE, e.getMessage());
        }
        return ResponseJsonMsg.ok();
    }

    @ApiOperation("新增角色权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色编号", dataType = "Integer", paramType = "body"),
            @ApiImplicitParam(name = "menus", value = "菜单编号", dataType = "Array", paramType = "body")
    })
    @PutMapping("/menus")
    public ResponseJsonMsg saveMenus(@RequestBody JSONObject request){
        Integer roleId = request.getInteger("roleId");
        if (roleId == null) {
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_PARAM, "角色编号不能为空！");
        }
        JSONArray menus = request.getJSONArray("menus");
        if (menus == null){
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_PARAM, "菜单编号必须提供！");
        }

        roleService.updateRoleMenus(roleId, menus.toArray(new Integer[menus.size()]));

        return ResponseJsonMsg.ok();
    }

}
