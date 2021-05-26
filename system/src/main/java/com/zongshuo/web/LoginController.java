package com.zongshuo.web;

import com.alibaba.fastjson.JSONObject;
import com.zongshuo.Contains;
import com.zongshuo.util.FormatCheckUtil;
import com.zongshuo.util.IdentifyingCode;
import com.zongshuo.util.ResponseJsonMsg;
import com.zongshuo.util.email.MailBean;
import com.zongshuo.util.email.SendMail;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-23
 * @Time: 19:58
 * @Description:
 */
@Slf4j
@CrossOrigin
@Api(tags = "系统功能-登录及注册")
@RestController
@RequestMapping("/api/sys/login")
public class LoginController {
    @Value("${ims.system.name}")
    String systemName;

    @ApiOperation("注册用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "body"),
            @ApiImplicitParam(name = "checkCode", value = "验证码", required = true, dataType = "String", paramType = "body")
    })
    @PostMapping("/register")
    public ResponseJsonMsg registerUser(@RequestBody JSONObject request){
        log.info("注册用户：{}",request);

        return ResponseJsonMsg.ok();
    }

    @ApiOperation("注册用户发送验证码")
    @ApiImplicitParam(name = "email", value = "注册邮箱", required = true, dataType = "String", paramType = "query")
    @PutMapping("/sendCheckCode")
    public ResponseJsonMsg sendCheckCode(@RequestBody JSONObject request){
        log.info("注册用户发送验证码：{}", request);
        String email = request.getString("email");
        if (!FormatCheckUtil.isEmail(email)){
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_PARAM, "不支持的邮箱格式！");
        }

        if (true){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return ResponseJsonMsg.error();
        }

        String checkCode = IdentifyingCode.getCode(Contains.CHECK_CODE_LENGTH, IdentifyingCode.CodeType.NUMBER_AND_LETTER);
        try {
            MailBean mailBean = new MailBean();
            mailBean.setSubject(systemName + "注册邮箱验证");
            mailBean.addTargetAddress(email);
            mailBean.addContextText("你好！<br><br>你在"+systemName+"注册用户！本次操作的验证码为：" + checkCode);
            mailBean.addContextText(",1分钟内有效。请在验证码输入框输入上述验证码完成验证。<br>如果你没有进行注册，请忽略本邮件。");

            SendMail.getSendMail("default").sendMail(mailBean);
        } catch (AddressException e) {
            log.error("注册用户-邮箱验证码发送异常：", e);
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_PARAM, "email地址不正确！");
        } catch (MessagingException e) {
            log.error("注册用户-邮箱验证码发送异常：", e);
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_SYS, "请重试或联系管理员！");
        }


        return ResponseJsonMsg.ok();
    }
}