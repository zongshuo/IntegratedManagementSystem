package com.zongshuo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zongshuo.model.DictDataModel;
import com.zongshuo.annotation.util.PageParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DictDataMapper extends BaseMapper<DictDataModel> {
    /**
     * 分页查询字典值
     * @param pageParam
     * @return
     */
    List<DictDataModel> selectPage(@Param("page")PageParam<DictDataModel> pageParam);
}
