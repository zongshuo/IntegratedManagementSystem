package com.zongshuo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zongshuo.mapper.OrganizationPersonMapper;
import com.zongshuo.model.OrganizationPersonModel;
import com.zongshuo.model.UserModel;
import com.zongshuo.service.OrganizationPersonService;
import com.zongshuo.util.PageParam;
import com.zongshuo.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
}
