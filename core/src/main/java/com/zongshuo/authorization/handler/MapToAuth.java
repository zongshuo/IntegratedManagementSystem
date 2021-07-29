package com.zongshuo.authorization.handler;

import com.zongshuo.authorization.handler.FieldFormat;
import com.zongshuo.authorization.handler.FieldFormatImpl;

import java.lang.annotation.*;

/**
 * ClassName: MapToAuth
 * date: 2021/7/28 14:24
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 * 该注解用于标注在权限点注解的属性上
 * 权限收集时会扫描权限点注解的属性上是否标有该注解
 * 如果存在会将标注属性的值映射到authAnnotation注解的name属性上
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = MapToAuths.class)
@Documented
public @interface MapToAuth {
    // 映射鉴权注解的类型
    Class<? extends Annotation> authAnnotation() ;

    // 映射注解的属性名称，为空默认与权限点注解属性名相同
    String name() default "";

    // 注解属性映射格式转换实现类
    Class<? extends FieldFormat> format() default FieldFormatImpl.class;
}
