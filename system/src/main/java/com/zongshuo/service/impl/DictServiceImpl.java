package com.zongshuo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zongshuo.mapper.DictMapper;
import com.zongshuo.model.DictModel;
import com.zongshuo.service.DictDataService;
import com.zongshuo.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, DictModel> implements DictService {
    @Autowired(required = false)
    private DictMapper dictMapper;
    @Autowired
    @Lazy
    private DictDataService dictDataService;

    @Override
    public List<DictModel> getAllDict() {
        List<DictModel> dictModels = dictMapper.selectList(
                new QueryWrapper<DictModel>()
                        .lambda()
                        .orderByAsc(DictModel::getSort)
                        .orderByAsc(DictModel::getId));
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

    @Override
    public void editDict(DictModel dict) throws IllegalAccessException {
        dict.setUpdateTime(new Date());
        int count = dictMapper.update(null,
                new UpdateWrapper<DictModel>()
                        .lambda()
                        .set(DictModel::getName, dict.getName())
                        .set(DictModel::getCode, dict.getCode())
                        .set(DictModel::getComments, dict.getComments())
                        .set(DictModel::getSort, dict.getSort())
                        .set(DictModel::getUpdateTime, dict.getUpdateTime())
                        .eq(DictModel::getId, dict.getId()));
        if (count < 1){
            throw new IllegalAccessException("更新字典失败！");
        }
    }

    @Override
    public void removeDict(DictModel dict) throws IllegalAccessException {
        int count = dictMapper.delete(new QueryWrapper<DictModel>().lambda().eq(DictModel::getId, dict.getId()));
        if (count < 1) {
            throw new IllegalAccessException("该字典不存在！");
        }

        dictDataService.removeDictData(dict);
    }

    @Override
    public boolean dictExisted(DictModel dict) {
        LambdaQueryWrapper<DictModel> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictModel::getId, dict.getId());
        queryWrapper.or();
        queryWrapper.eq(DictModel::getCode, dict.getCode());
        queryWrapper.or();
        queryWrapper.eq(DictModel::getName, dict.getName());
        return dictMapper.selectCount(queryWrapper) > 0;
    }
}
