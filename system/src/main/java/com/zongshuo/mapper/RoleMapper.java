package com.zongshuo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zongshuo.entity.Role;
import com.zongshuo.model.RoleModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-22
 * @Time: 11:24
 * @Description:
 */
public interface RoleMapper extends BaseMapper<RoleModel> {
    /**
     * 根据用户id关联用户角色表获取系统角色信息
     * @param id
     * @return
     */
    List<RoleModel> selectUserRoles(@Param("userId") Integer id);
}
