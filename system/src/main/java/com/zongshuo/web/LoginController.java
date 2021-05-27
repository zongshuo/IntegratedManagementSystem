package com.zongshuo.web;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zongshuo.Contains;
import com.zongshuo.model.AuthCodeCacheModel;
import com.zongshuo.model.UserModel;
import com.zongshuo.service.AuthCodeCacheService;
import com.zongshuo.util.FormatCheckUtil;
import com.zongshuo.util.IdentifyingCode;
import com.zongshuo.util.ResponseJsonMsg;
import com.zongshuo.util.email.MailBean;
import com.zongshuo.util.email.SendMail;
import com.zongshuo.annotations.validators.Insert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.time.Instant;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-23
 * @Time: 19:58
 * @Description:
 * 前端用户登录、注册、重置密码等操作控制器
 */
@Slf4j
@Api(tags = "系统功能-登录及注册")
@RestController
@RequestMapping("/api/sys/login")
public class LoginController {
    @Value("${ims.system.name}")
    String systemName;

    @Autowired
    private AuthCodeCacheService authCodeCacheService;

    @ApiOperation("注册用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "email", value = "邮箱", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "checkCode", value = "验证码", required = true, dataType = "String", paramType = "query")
    })
    @PutMapping("/register")
    public ResponseJsonMsg registerUser(@RequestBody @Validated(Insert.class) UserModel model){
        log.info("注册用户：{}",model);


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

        AuthCodeCacheModel authCodeCache = new AuthCodeCacheModel();
        authCodeCache.setUserJoin(email);
        authCodeCache.setExpireTime(Instant.now().toEpochMilli());
        authCodeCache.setChannelNo(Contains.AUTH_CODE_CHANNEL_REGISTER);
        int count = authCodeCacheService.count(
                new QueryWrapper<com.zongshuo.model.AuthCodeCacheModel>()
                        .eq("user_join", authCodeCache.getUserJoin())
                        .eq("channel_no", authCodeCache.getChannelNo())
                        .gt("expire_time", authCodeCache.getExpireTime()));
        if (count > 0){
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_DATA_STATE, "验证码未过期，请查看邮件！");
        }

        String authCode = IdentifyingCode.getCode(Contains.CHECK_CODE_LENGTH, IdentifyingCode.CodeType.NUMBER_AND_LETTER);
        authCodeCache.setAuthCode(authCode);
        try {
            MailBean mailBean = new MailBean();
            mailBean.setSubject(systemName + "注册邮箱验证");
            mailBean.addTargetAddress(email);
            mailBean.addContextText("你好！<br><br>你在"+systemName+"注册用户！本次操作的验证码为：" + authCode);
            mailBean.addContextText(",1分钟内有效。请在验证码输入框输入上述验证码完成验证。<br>如果你没有进行注册，请忽略本邮件。");

            SendMail.getSendMail("default").sendMail(mailBean);
        } catch (AddressException e) {
            log.error("注册用户-邮箱验证码发送异常：", e);
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_PARAM, "email地址不正确！");
        } catch (MessagingException e) {
            log.error("注册用户-邮箱验证码发送异常：", e);
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_SYS, "请重试或联系管理员！");
        }

        authCodeCache.setExpireTime(authCodeCache.getExpireTime() + Contains.AUTH_CODE_REGISTER_OUT_TIME);
        authCodeCacheService.save(authCodeCache);

        return ResponseJsonMsg.ok();
    }
}
