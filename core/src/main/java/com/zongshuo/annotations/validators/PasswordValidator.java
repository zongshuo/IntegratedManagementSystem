package com.zongshuo.annotations.validators;

import com.zongshuo.annotations.ValidatePassword;
import com.zongshuo.util.FormatCheckUtil;

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

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return FormatCheckUtil.isPassword(value);
    }
}