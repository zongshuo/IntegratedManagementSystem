package com.zongshuo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zongshuo.model.DictModel;

import java.util.List;

public interface DictService extends IService<DictModel> {

    /**
     * 查询所有字典
     * @return
     */
    List<DictModel> getAllDict();

}
