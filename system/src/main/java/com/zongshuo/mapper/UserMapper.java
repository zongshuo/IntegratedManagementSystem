package com.zongshuo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zongshuo.model.UserModel;
import com.zongshuo.util.PageParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-22
 * @Time: 10:25
 * @Description:
 */
public interface UserMapper extends BaseMapper<UserModel> {
    /**
     * 分页查询用户列表
     * @param pageParam
     * @return
     */
    List<UserModel> selectPage(@Param("page")PageParam<UserModel> pageParam);
}
