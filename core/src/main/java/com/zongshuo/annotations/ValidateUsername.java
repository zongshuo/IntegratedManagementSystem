package com.zongshuo.annotations;

import com.zongshuo.util.validators.UsernameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotBlank;
import java.lang.annotation.*;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-27
 * @Time: 14:05
 * @Description:
 * springboot-validation的自定义注解
 * 该注解用于校验用户的用户名属性
 * value是默认值
 * message为校验失败后提示的信息
 * groups是注解生效的组
 * payload TODO干啥用的？
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@NotBlank(message = "用户名不能为空！")
@Constraint(validatedBy = UsernameValidator.class)
@Documented
public @interface ValidateUsername {

    public String message() default "用户名格式不正确！";

    Class<?> [] groups() default {};

    Class<? extends Payload> [] payload() default {};
}
