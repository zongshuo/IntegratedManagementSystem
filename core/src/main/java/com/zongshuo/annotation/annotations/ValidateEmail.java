package com.zongshuo.annotation.annotations;

import com.zongshuo.annotation.annotations.validators.EmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import java.lang.annotation.*;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-27
 * @Time: 19:20
 * @Description:
 * springboot validate 自定义注解，校验邮箱
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "邮箱不能为空！")
@Constraint(validatedBy = EmailValidator.class)
@Documented
public @interface ValidateEmail {

    public String message() default "邮箱格式不正确！";

    Class<?> [] groups() default {};

    Class<? extends Payload> [] payload() default {};
}
