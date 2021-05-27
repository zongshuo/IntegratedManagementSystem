package com.zongshuo.util.validators;

import com.zongshuo.annotations.ValidateUsername;
import com.zongshuo.util.FormatCheckUtil;

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
    String username ;
    @Override
    public void initialize(ValidateUsername constraintAnnotation) {
        username = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return FormatCheckUtil.isUsername(value);
    }
}
