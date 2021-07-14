package com.zongshuo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zongshuo.model.OrganizationModel;

import java.util.List;

public interface OrganizationService extends IService<OrganizationModel> {
    /**
     * 查询组织机构树
     * 通过递归生成树状结构
     * 通过上级机构主键获取下级机构树
     * 如果上级机构主键为0，获取所有机构
     * @param orgId
     * @return
     */
    List<OrganizationModel> getOrgTree(Integer orgId);

    /**
     * 新增组织
     * 判断组织是否存在
     * 根据相同上级组织下的组织名称判断
     * 根据组织编号判断
     * 设置创建时间
     * 保存
     * @param org
     */
    void addOrg(OrganizationModel org) throws IllegalAccessException;
}
