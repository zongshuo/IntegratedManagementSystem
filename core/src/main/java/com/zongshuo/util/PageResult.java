package com.zongshuo.util;

import com.zongshuo.Contains;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-6-25
 * @Time: 15:12
 * @Description:
 * 分页查询返回结果封装
 */
@Data
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    //  响应码
    private String retCode = Contains.RET_CODE_SUCCESS;

    //  响应信息
    private String msg;

    //总数量
    private Long count;

    private List<T> data ;


    public PageResult(List<T> rows){
        this(rows, rows == null ? 0L : rows.size());
    }
    public PageResult(List<T> rows, Long total){
        setData(rows);
        setCount(total);
    }
}
