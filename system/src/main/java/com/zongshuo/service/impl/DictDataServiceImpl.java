package com.zongshuo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zongshuo.mapper.DictDataMapper;
import com.zongshuo.model.DictDataModel;
import com.zongshuo.model.DictModel;
import com.zongshuo.service.DictDataService;
import com.zongshuo.service.DictService;
import com.zongshuo.util.PageParam;
import com.zongshuo.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    private DictService dictService;

    @Override
    public PageResult<DictDataModel> getDictDataPage(PageParam<DictDataModel> pageParam) {
        List<DictDataModel> dictDataModelList = dictDataMapper.selectPage(pageParam);
        if (dictDataModelList == null){
            dictDataModelList = new ArrayList<>(0);
        }

        return new PageResult<>(dictDataModelList, pageParam.getTotal());
    }

    @Override
    public List<DictDataModel> getDictDataList(String dictCode){
        DictModel dict = dictService.getOne(new QueryWrapper<DictModel>()
                .lambda()
                .eq(DictModel::getCode, dictCode));

        if (dict == null){
            return new ArrayList<DictDataModel>(0) ;
        }

        List<DictDataModel> dictDataModelList = dictDataMapper.selectList(
                new QueryWrapper<DictDataModel>().lambda().eq(DictDataModel::getDictId, dict.getId()));
        if (dictDataModelList == null){
            dictDataModelList = new ArrayList<>(0);
        }
        return dictDataModelList;
    }

    @Override
    public void removeDictData(DictModel dict) {
        dictDataMapper.delete(
                new QueryWrapper<DictDataModel>()
                        .lambda()
                        .eq(DictDataModel::getDictId, dict.getId()));
    }

    @Override
    public void addDictData(DictDataModel dictData) throws IllegalAccessException{
        DictModel dict = new DictModel();
        dict.setId(dictData.getDictId());
        if (! dictService.dictExisted(dict)){
            throw new IllegalAccessException("字典项不存在！");
        }

        LambdaQueryWrapper<DictDataModel> query =
                new QueryWrapper<DictDataModel>()
                        .lambda()
                        .eq(DictDataModel::getDictId, dictData.getDictId())
                        .and(consumer ->
                            consumer.eq(DictDataModel::getName, dictData.getName())
                                    .or()
                                    .eq(DictDataModel::getCode, dictData.getCode()));
        if (dictDataMapper.selectCount(query) > 0){
            throw new IllegalAccessException("字典值已存在！");
        }

        dictData.setCreateTime(new Date());
        dictDataMapper.insert(dictData);
    }

    @Override
    public void deleteDictData(DictDataModel dictData) throws IllegalAccessException {
        LambdaQueryWrapper<DictDataModel> queryWrapper =
                new QueryWrapper<DictDataModel>()
                        .lambda()
                        .eq(DictDataModel::getId, dictData.getId());

        if (dictDataMapper.delete(queryWrapper) < 1){
            throw new IllegalAccessException("删除字典值失败！");
        }
    }
}
