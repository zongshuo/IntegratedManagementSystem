package com.zongshuo.authorization.handler;

/**
 * ClassName: FieldFormat
 * date: 2021/7/29 9:58
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 * 创建鉴权注解时需要映射权限点注解与鉴权注解属性
 * 本接口提供属性值格式转换功能
 * 对属性进行格式转换时可以实现本接口
 * 需要将本接口的实现类类型赋值给MapToAuth.
 */
public interface FieldFormat {

    /**
     * 属性格式转换
     * 将权限点注解属性值转换成鉴权注解属性值
     * @param value 权限点属性值
     * @return 鉴权点属性值
     */
    Object format(Object value);
}
