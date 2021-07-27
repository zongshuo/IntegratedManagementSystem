package com.zongshuo.annotation.annotations;

import com.zongshuo.annotation.annotations.validators.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import java.lang.annotation.*;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-27
 * @Time: 19:34
 * @Description:
 * spring boot validate 自定义注解，校验密码
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "密码不能为空！")
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface ValidatePassword {
    public String message() default "密码格式不正确！";

    Class<?> [] groups() default {};

    Class<? extends Payload> [] payload() default {};
}
