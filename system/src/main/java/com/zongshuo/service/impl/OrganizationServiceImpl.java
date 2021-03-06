package com.zongshuo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
        checkRepeat(org);

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

    @Override
    public void removeOrg(OrganizationModel org) throws IllegalAccessException {
        List<OrganizationModel> children = getOrgTree(org.getId());
        if ( ! children.isEmpty()){
            for (OrganizationModel model : children){
                this.removeOrg(model);
            }
        }

        orgMapper.delete(
                new QueryWrapper<OrganizationModel>()
                        .lambda()
                        .eq(OrganizationModel::getId, org.getId()));
    }

    @Override
    public void editOrg(OrganizationModel org) throws IllegalAccessException {
        checkRepeat(org);

        LambdaUpdateWrapper<OrganizationModel> updateWrapper =
                new UpdateWrapper<OrganizationModel>()
                        .lambda()
                        .set(OrganizationModel::getParentId, org.getParentId())
                        .set(OrganizationModel::getName, org.getName())
                        .set(OrganizationModel::getFullName, org.getFullName())
                        .set(OrganizationModel::getCode, org.getCode())
                        .set(OrganizationModel::getType, org.getType())
                        .set(OrganizationModel::getSort, org.getSort())
                        .set(OrganizationModel::getComments, org.getComments())
                        .set(OrganizationModel::getUpdateTime, new Date())
                        .eq(OrganizationModel::getId, org.getId());
        orgMapper.update(null, updateWrapper);
    }

    /**
     * ????????????????????????
     * ??????????????????????????????
     * ??????????????????????????????
     * ????????????????????????????????????
     * @param org
     * @throws IllegalAccessException
     */
    private void checkRepeat(OrganizationModel org) throws IllegalAccessException{
        int count = 0;
        if (0 != org.getParentId().intValue()){
            count = orgMapper.selectCount(
                    new QueryWrapper<OrganizationModel>()
                            .lambda()
                            .eq(OrganizationModel::getId, org.getParentId()));
            if (count < 1) throw new IllegalAccessException("????????????????????????");
        }

        // todo ????????????????????????????????????????????????
        if (StringUtils.isNotBlank(org.getCode())){
            count = orgMapper.selectCount(
                    new QueryWrapper<OrganizationModel>()
                            .lambda()
                            .eq(OrganizationModel::getCode, org.getCode())
                            .ne(org.getId() != null, OrganizationModel::getId, org.getId()));
            if (count > 0) throw new IllegalAccessException("????????????????????????");
        }

        count = orgMapper.selectCount(
                new QueryWrapper<OrganizationModel>()
                        .lambda()
                        .eq(OrganizationModel::getParentId, org.getParentId())
                        .eq(OrganizationModel::getName, org.getName())
                        .ne(org.getId() != null, OrganizationModel::getId, org.getId()));
        if (count > 0) throw new IllegalAccessException("????????????????????????????????????");
    }

    /**
     * ???????????????????????????
     * @param orgList
     * @param parentOrgId
     * @return
     */
    private List<OrganizationModel> getOrgTree(List<OrganizationModel> orgList, Integer parentOrgId){
        // ??????????????????
        List<OrganizationModel> childrenOrgies = new ArrayList<>();

        //  ????????????????????????????????????????????????????????????childrenOrgies???????????????????????????????????????????????????
        Iterator<OrganizationModel> iterator = orgList.iterator();
        while (iterator.hasNext()){
            OrganizationModel org  = iterator.next();
            if (parentOrgId.equals(org.getParentId())){
                childrenOrgies.add(org);
                iterator.remove();

                //  ???????????????????????????????????????
                org.setChildren(getOrgTree(orgList, org.getId()));
                iterator = orgList.iterator();
            }
        }
        return childrenOrgies;
    }
}
