package com.zongshuo.annotation.annotations.validators;

import com.zongshuo.annotation.annotations.ValidateUsername;
import com.zongshuo.annotation.util.FormatCheckUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-27
 * @Time: 17:04
 * @Description:
 *
 * 系统登录用户名校验注解实现类
 */
public class UsernameValidator implements ConstraintValidator<ValidateUsername, String> {
    @Override
    public void initialize(ValidateUsername constraintAnnotation) {
        //该方法未使用
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return FormatCheckUtil.isUsername(value);
    }
}
