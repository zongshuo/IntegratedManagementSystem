package com.zongshuo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zongshuo.mapper.OrganizationPersonMapper;
import com.zongshuo.model.OrganizationPersonModel;
import com.zongshuo.model.UserModel;
import com.zongshuo.service.OrganizationPersonService;
import com.zongshuo.annotation.util.PageParam;
import com.zongshuo.annotation.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class OrganizationPersonServiceImpl extends ServiceImpl<OrganizationPersonMapper, OrganizationPersonModel> implements OrganizationPersonService {
    @Autowired(required = false)
    private OrganizationPersonMapper personMapper ;


    @Override
    public PageResult<UserModel> getPage(PageParam<OrganizationPersonModel> pageParam) {
        List<UserModel> userModelList = personMapper.selectPage(pageParam);
        if (userModelList == null){
            userModelList = new ArrayList<>(0);
        }
        return new PageResult<>(userModelList, pageParam.getTotal());
    }

    @Override
    public void addOrgPerson(Integer orgId, Integer userId) {
        if (orgId == null){
            personMapper.delete(new QueryWrapper<OrganizationPersonModel>()
                    .lambda().eq(OrganizationPersonModel::getUserId, userId));
            return ;
        }

        OrganizationPersonModel personModel = new OrganizationPersonModel();
        personModel.setOrgId(orgId);
        personModel.setUserId(userId);
        personModel.setCreateTime(new Date());
        saveOrUpdate(personModel, new UpdateWrapper<OrganizationPersonModel>()
                .lambda().set(OrganizationPersonModel::getOrgId, personModel.getOrgId())
                .eq(OrganizationPersonModel::getUserId, personModel.getUserId()));
    }
}
