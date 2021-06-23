package com.zongshuo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zongshuo.model.MenuModel;
import com.zongshuo.model.RoleModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-22
 * @Time: 10:22
 * @Description:
 */
public interface MenuMapper extends BaseMapper<MenuModel> {

    List<MenuModel> getMenusByRoleIds(@Param("roles") List<RoleModel> roles);

    List<MenuModel> getMenusByUserId(@Param("userId")Integer userId);
}
