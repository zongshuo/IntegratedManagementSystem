package com.zongshuo.annotations;

import com.zongshuo.annotations.validators.AuthCodeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import java.lang.annotation.*;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-27
 * @Time: 19:42
 * @Description:
 * spring boot validate 自定义属性校验注解，校验验证码
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "验证码不能为空！")
@Constraint(validatedBy = AuthCodeValidator.class)
@Documented
public @interface ValidateAuchCode {
    public String message() default "验证码格式不正确！";

    Class<?> [] groups() default {};

    Class<? extends Payload> [] payload() default {};
}
