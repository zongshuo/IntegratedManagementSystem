package com.zongshuo.annotation;

import com.zongshuo.authorization.handler.FieldFormat;

/**
 * ClassName: FormatPreAuthorizeValue
 * date: 2021/7/30 17:53
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 */
public class FormatPreAuthorizeValue implements FieldFormat {
    @Override
    public Object format(Object value) {
        return "hasAuthority('"+value+"')";
    }
}
