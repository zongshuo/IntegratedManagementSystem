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
     * 判断组织是否存在
     * 判断上级组织是否存在
     * 判断组织编码是否重复
     * 判断同级是否存在同名组织
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
            if (count < 1) throw new IllegalAccessException("上级组织不存在！");
        }

        // todo 增加判断上级机构非本机构的子机构
        if (StringUtils.isNotBlank(org.getCode())){
            count = orgMapper.selectCount(
                    new QueryWrapper<OrganizationModel>()
                            .lambda()
                            .eq(OrganizationModel::getCode, org.getCode())
                            .ne(org.getId() != null, OrganizationModel::getId, org.getId()));
            if (count > 0) throw new IllegalAccessException("机构编号已存在！");
        }

        count = orgMapper.selectCount(
                new QueryWrapper<OrganizationModel>()
                        .lambda()
                        .eq(OrganizationModel::getParentId, org.getParentId())
                        .eq(OrganizationModel::getName, org.getName())
                        .ne(org.getId() != null, OrganizationModel::getId, org.getId()));
        if (count > 0) throw new IllegalAccessException("同级别中机构名称已存在！");
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
