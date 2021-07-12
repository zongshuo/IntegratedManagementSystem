package com.zongshuo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zongshuo.model.DictDataModel;
import com.zongshuo.model.DictModel;
import com.zongshuo.util.PageParam;
import com.zongshuo.util.PageResult;

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
     * 删除字典项的字典值
     * 根据字典项id进行删除
     * @param dict
     */
    void removeDictData(DictModel dict) ;
}
