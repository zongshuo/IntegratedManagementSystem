package com.zongshuo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zongshuo.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-22
 * @Time: 10:25
 * @Description:
 */
public interface UserMapper extends BaseMapper<User> {
    User selectUserAndRoles(@Param("username") String username);
}