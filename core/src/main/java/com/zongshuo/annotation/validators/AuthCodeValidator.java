package com.zongshuo.annotation.validators;

import com.zongshuo.annotation.ValidateAuthCode;
import com.zongshuo.util.FormatCheckUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-27
 * @Time: 19:45
 * @Description:
 */
public class AuthCodeValidator implements ConstraintValidator<ValidateAuthCode, String> {
    @Override
    public void initialize(ValidateAuthCode constraintAnnotation) {
        //该方法未使用
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return FormatCheckUtil.isAuthCode(value);
    }
}
