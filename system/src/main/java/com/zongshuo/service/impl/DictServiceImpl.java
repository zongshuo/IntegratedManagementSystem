package com.zongshuo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zongshuo.mapper.DictMapper;
import com.zongshuo.model.DictModel;
import com.zongshuo.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, DictModel> implements DictService {
    @Autowired(required = false)
    private DictMapper dictMapper;

    @Override
    public List<DictModel> getAllDict() {
        List<DictModel> dictModels = dictMapper.selectList(
                new QueryWrapper<DictModel>()
                        .lambda().orderByAsc(DictModel::getId));
        if (dictModels == null){
            dictModels = new ArrayList<>(0);
        }
        return dictModels;
    }

    @Override
    public void saveDict(DictModel dict) throws IllegalAccessException{
        int count = dictMapper.selectCount(
                new QueryWrapper<DictModel>()
                        .lambda()
                        .eq(DictModel::getCode, dict.getCode())
                        .or()
                        .eq(DictModel::getName, dict.getName()));
        if (count > 0){
            throw new IllegalAccessException("字典名称或字典值已存在！");
        }

        dict.setCreateTime(new Date());
        dictMapper.insert(dict);
        if (dict.getId() == null){
            throw new IllegalAccessException("字典保存失败！");
        }
    }
}
