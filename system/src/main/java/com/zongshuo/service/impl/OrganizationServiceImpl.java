package com.zongshuo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zongshuo.mapper.OrganizationMapper;
import com.zongshuo.model.OrganizationModel;
import com.zongshuo.service.OrganizationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, OrganizationModel> implements OrganizationService {
    @Autowired(required = false)
    private OrganizationMapper orgMapper;

    @Override
    public void addOrg(OrganizationModel org) throws IllegalAccessException{
        int count = 0;
        if (StringUtils.isNotBlank(org.getCode())){
            count = orgMapper.selectCount(
                    new QueryWrapper<OrganizationModel>()
                            .lambda()
                            .eq(OrganizationModel::getCode, org.getCode()));
            if (count > 0) throw new IllegalAccessException("机构编号已存在！");
        }

        count = orgMapper.selectCount(
                new QueryWrapper<OrganizationModel>()
                        .lambda()
                        .eq(OrganizationModel::getParentId, org.getParentId())
                        .eq(OrganizationModel::getName, org.getName()));
        if (count > 0) throw new IllegalAccessException("同级别中机构名称已存在！");

       org.setCreateTime(new Date());
       orgMapper.insert(org);
    }

    @Override
    public List<OrganizationModel> getOrgTree(Integer orgId) {
        List<OrganizationModel> orgList = orgMapper.selectList(new QueryWrapper<>());
        if (orgList == null || orgList.isEmpty()){
            return new ArrayList<>(0);
        }

        orgList = getOrgTree(orgList, orgId);
        return orgList;
    }

    /**
     * 递归方式生成机构树
     * @param orgList
     * @param parentOrgId
     * @return
     */
    private List<OrganizationModel> getOrgTree(List<OrganizationModel> orgList, Integer parentOrgId){
        // 保存下级机构
        List<OrganizationModel> childrenOrgies = new ArrayList<>();

        //  迭代器遍历机构列表，发现下级机构就添加到childrenOrgies中，并将下级机构移除，减少遍历次数
        Iterator<OrganizationModel> iterator = orgList.iterator();
        while (iterator.hasNext()){
            OrganizationModel org  = iterator.next();
            if (parentOrgId.equals(org.getParentId())){
                childrenOrgies.add(org);
                iterator.remove();

                //  递归获取下级机构的下级机构
                org.setChildren(getOrgTree(orgList, org.getId()));
                iterator = orgList.iterator();
            }
        }
        return childrenOrgies;
    }
}
