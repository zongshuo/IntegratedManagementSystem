package com.zongshuo.authorization.handler;

/**
 * ClassName: FieldFormatImpl
 * date: 2021/7/29 10:13
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 * 属性值转换接口的默认实现
 */
class FieldFormatImpl implements FieldFormat {
    @Override
    public Object format(Object value) {
        return value;
    }
}
