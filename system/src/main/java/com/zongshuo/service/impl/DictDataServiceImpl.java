package com.zongshuo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zongshuo.mapper.DictDataMapper;
import com.zongshuo.model.DictDataModel;
import com.zongshuo.service.DictDataService;
import com.zongshuo.util.PageParam;
import com.zongshuo.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-7-10
 * @Time: 11:32
 * @Description:
 */
@Service
public class DictDataServiceImpl extends ServiceImpl<DictDataMapper, DictDataModel> implements DictDataService {
    @Autowired(required = false)
    private DictDataMapper dictDataMapper;

    @Override
    public PageResult<DictDataModel> getDictDataPage(PageParam<DictDataModel> pageParam) {
        List<DictDataModel> dictDataModelList = dictDataMapper.selectPage(pageParam);
        if (dictDataModelList == null){
            dictDataModelList = new ArrayList<>(0);
        }

        return new PageResult<>(dictDataModelList, pageParam.getTotal());
    }
}
