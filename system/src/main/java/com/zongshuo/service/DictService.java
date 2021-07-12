package com.zongshuo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zongshuo.model.DictDataModel;
import com.zongshuo.model.DictModel;
import com.zongshuo.util.PageResult;

import java.util.List;

public interface DictService extends IService<DictModel> {

    /**
     * 查询所有字典
     * @return
     */
    List<DictModel> getAllDict();


    /**
     * 新增字典
     * 检查是否重复
     * 设置创建时间
     * @param dict
     */
    void saveDict(DictModel dict) throws IllegalAccessException;

    /**
     * 编辑字典
     * 要求字典主键、字典名称、字典值三个字典均不能为空
     * @param dict
     * @throws IllegalAccessException
     */
    void editDict(DictModel dict) throws IllegalAccessException;

    /**
     * 删除字典项
     * 要求字典项主键不能为空
     * 关联删除字典项及字典值
     * @param dict
     * @throws IllegalAccessException
     */
    void removeDict(DictModel dict) throws IllegalAccessException;

}
