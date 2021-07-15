package com.zongshuo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zongshuo.model.OrganizationPersonModel;
import com.zongshuo.model.UserModel;
import com.zongshuo.util.PageParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrganizationPersonMapper extends BaseMapper<OrganizationPersonModel> {
    /**
     * 分页查询组织人员记录
     * @param pageParam
     * @return
     */
    List<UserModel> selectPage(@Param("page")PageParam<OrganizationPersonModel> pageParam);
}
