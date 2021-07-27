package com.zongshuo.util;

/**
 * ClassName: Convert
 * date: 2021/7/26 14:37
 *
 * @author zongshuo
 * version: 1.0
 * Description:
 */
public class Convert {
    private Convert(){}

    public static <T> T convert(Class<T> retType, Object sourceObj){
        if (sourceObj == null) return  null;
        return retType.cast(sourceObj);
    }
}
