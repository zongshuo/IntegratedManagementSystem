package com.zongshuo.annotation.annotations.validators;

import com.zongshuo.annotation.annotations.ValidatePassword;
import com.zongshuo.annotation.util.FormatCheckUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-27
 * @Time: 19:39
 * @Description:
 */
public class PasswordValidator implements ConstraintValidator<ValidatePassword, String> {
    @Override
    public void initialize(ValidatePassword constraintAnnotation) {
        //该方法未使用
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return FormatCheckUtil.isPassword(value);
    }
}
