package com.zongshuo.config;

import org.hibernate.dialect.MySQL8Dialect;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-6-29
 * @Time: 11:16
 * @Description:
 */
public class MySqlConfig extends MySQL8Dialect {

    @Override
    public String getTableTypeString() {
        return super.getTableTypeString() + " DEFAULT CHARSET=utf8mb4";
    }
}
