package com.zongshuo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zongshuo.model.DictDataModel;
import com.zongshuo.model.DictModel;
import com.zongshuo.annotation.util.PageParam;
import com.zongshuo.annotation.util.PageResult;

import java.util.List;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-7-10
 * @Time: 11:28
 * @Description:
 */
public interface DictDataService extends IService<DictDataModel> {
    /**
     * 分页查询字典项的字典值
     * @param pageParam
     * @return
     */
    PageResult<DictDataModel> getDictDataPage(PageParam<DictDataModel> pageParam);

    /**
     * 查询所有字典值
     * 根据字典项值获取所有字典项
     * @param dictCode
     * @return
     */
    List<DictDataModel> getDictDataList(String dictCode);

    /**
     * 删除字典项的字典值
     * 根据字典项id进行删除
     * @param dict
     */
    void removeDictData(DictModel dict) ;

    /**
     * 新增字典值
     * 需要指定字典项
     * 相同字典项下的字典值及名称不能重复
     * 设置创建时间
     * @param dictData
     */
    void addDictData(DictDataModel dictData) throws IllegalAccessException;

    /**
     * 删除字典值
     * 根据字典值主键删除
     * @param dictData
     * @throws IllegalAccessException
     */
    void deleteDictData(DictDataModel dictData) throws IllegalAccessException;
}
