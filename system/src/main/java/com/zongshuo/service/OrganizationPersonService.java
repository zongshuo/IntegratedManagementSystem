package com.zongshuo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zongshuo.model.OrganizationPersonModel;
import com.zongshuo.model.UserModel;
import com.zongshuo.annotation.util.PageParam;
import com.zongshuo.annotation.util.PageResult;

public interface OrganizationPersonService extends IService<OrganizationPersonModel> {

    /**
     * 分页获取组织人员列表
     * @param pageParam
     * @return
     */
    PageResult<UserModel> getPage(PageParam<OrganizationPersonModel> pageParam);

    /**
     * 新增组织人员关系
     * @param orgId
     * @param userId
     */
    void addOrgPerson(Integer orgId, Integer userId);

}
