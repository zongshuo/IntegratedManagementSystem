package com.zongshuo.annotation.validators;

import com.zongshuo.annotation.ValidateEmail;
import com.zongshuo.annotation.util.FormatCheckUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-27
 * @Time: 19:25
 * @Description:
 */
public class EmailValidator implements ConstraintValidator<ValidateEmail, String> {
    @Override
    public void initialize(ValidateEmail constraintAnnotation) {
        //该方法未使用
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return FormatCheckUtil.isEmail(value);
    }
}
