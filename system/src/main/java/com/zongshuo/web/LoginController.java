package com.zongshuo.web;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zongshuo.Contains;
import com.zongshuo.model.AuthCodeCacheModel;
import com.zongshuo.model.UserModel;
import com.zongshuo.service.AuthCodeCacheService;
import com.zongshuo.service.UserService;
import com.zongshuo.util.FormatCheckUtil;
import com.zongshuo.util.IdentifyingCode;
import com.zongshuo.util.ResponseJsonMsg;
import com.zongshuo.util.email.MailBean;
import com.zongshuo.util.email.SendMail;
import com.zongshuo.annotations.validators.Insert;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.time.Instant;
import java.util.Date;

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
@RequestMapping("/sys/login")
public class LoginController {
    @Value("${ims.system.name}")
    String systemName;

    @Autowired
    private AuthCodeCacheService authCodeCacheService;
    @Autowired
    private UserService userModelService;

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
        int count = authCodeCacheService.count(new QueryWrapper<AuthCodeCacheModel>()
                .eq("auth_code", model.getAuthCode())
                .eq("user_join", model.getEmail())
                .gt("expire_time", Instant.now().toEpochMilli()));
        if (count < 1){
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_DATA_STATE, "验证码不正确或已过期！");
        }

        count = userModelService.count(new QueryWrapper<UserModel>().eq("username", model.getUsername()));
        if (count > 0) {
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_DATA_STATE, "用户名已被注册！");
        }

        count = userModelService.count(new QueryWrapper<UserModel>().eq("email", model.getEmail()));
        if (count > 0){
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_DATA_STATE, "邮箱已注册过用户！");
        }
        //密码加密
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        model.setPassword(encoder.encode(model.getPassword()));
        model.setIsEnabled(true);
        model.setIsAccountNonLocked(true);
        model.setIsAccountNonExpired(true);
        model.setIsCredentialsNonExpired(true);
        model.setCreateTime(new Date());
        userModelService.save(model);

        authCodeCacheService.remove(new UpdateWrapper<AuthCodeCacheModel>()
                .eq("auth_code", model.getAuthCode())
                .eq("channel_no", Contains.CHANNEL_AUTH_CODE_REGISTER)
                .eq("user_join", model.getEmail()));

        return ResponseJsonMsg.ok();
    }

    @ApiOperation("注册用户发送验证码")
    @ApiImplicitParam(name = "email", value = "注册邮箱", required = true, dataType = "String", paramType = "query")
    @PutMapping("/sendAuthCode")
    public ResponseJsonMsg sendAuthCode(@RequestBody JSONObject request){
        log.info("注册用户发送验证码：{}", request);
        String email = request.getString("email");
        if (!FormatCheckUtil.isEmail(email)){
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_PARAM, "不支持的邮箱格式！");
        }

        AuthCodeCacheModel authCodeCache = new AuthCodeCacheModel();
        authCodeCache.setUserJoin(email);
        authCodeCache.setExpireTime(Instant.now().toEpochMilli());
        authCodeCache.setChannelNo(Contains.CHANNEL_AUTH_CODE_REGISTER);
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
            mailBean.addContextText("您好！<br><br>您在"+systemName+"注册用户！本次操作的验证码为：" + authCode);
            mailBean.addContextText("，1小时内有效。请在验证码输入框输入上述验证码完成验证。<br>如果您没有进行注册，可以忽略本邮件。");

            SendMail.getSendMail("default").sendMail(mailBean);
        } catch (AddressException e) {
            log.error("注册用户-邮箱验证码发送异常：", e);
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_PARAM, "email地址不正确！");
        } catch (MessagingException e) {
            log.error("注册用户-邮箱验证码发送异常：", e);
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_SYS, "请重试或联系管理员！");
        }

        authCodeCache.setExpireTime(authCodeCache.getExpireTime() + Contains.EFFECTIVE_TIME_AUTH_CODE_REGISTER);
        authCodeCacheService.save(authCodeCache);

        return ResponseJsonMsg.ok();
    }


    @ApiOperation("重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "authCode", value = "验证码", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "query")
    })
    @PostMapping("/resetPassword")
    public ResponseJsonMsg resetPassword(@RequestBody UserModel userModel){
        if (FormatCheckUtil.isNotUsername(userModel.getUsername())){
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_PARAM, "用户名不正确！");
        }
        if (FormatCheckUtil.isNotAuthCode(userModel.getAuthCode())){
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_PARAM, "验证码不正确！");
        }
        if (FormatCheckUtil.isNotPassword(userModel.getPassword())){
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_PARAM, "密码格式错误！");
        }

        int count = authCodeCacheService.count(
                new QueryWrapper<AuthCodeCacheModel>()
                        .eq("channel_no", Contains.CHANNEL_AUTH_CODE_RESET_PASSWORD)
                        .eq("user_join", userModel.getUsername())
                        .gt("expire_time", Instant.now().toEpochMilli()));
        if (count < 1){
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_DATA_STATE, "验证码错误或已过期！");
        }

        userModel.setPassword(new BCryptPasswordEncoder().encode(userModel.getPassword()));

        userModelService.update(
                new UpdateWrapper<UserModel>()
                        .set("password", userModel.getPassword())
                        .eq("username", userModel.getUsername()));
        authCodeCacheService.remove(
                new UpdateWrapper<AuthCodeCacheModel>()
                        .eq("auth_code", userModel.getAuthCode())
                        .eq("channel_no", Contains.CHANNEL_AUTH_CODE_RESET_PASSWORD)
                        .eq("user_join", userModel.getUsername()));

        return ResponseJsonMsg.ok();
    }

    @ApiOperation("重置密码发送验证码")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "query")
    @PutMapping("/sendResetPasswordAuthCode")
    public ResponseJsonMsg sendResetPasswordAuthCode(@RequestBody JSONObject request){
        log.info("重置密码发送验证码：{}", request);
        String username = request.getString("username");
        if (!FormatCheckUtil.isUsername(username)){
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_PARAM, "用户名不正确！");
        }

        int count = authCodeCacheService.count(
                new QueryWrapper<AuthCodeCacheModel>()
                        .eq("user_join", username)
                        .eq("channel_no", Contains.CHANNEL_AUTH_CODE_RESET_PASSWORD)
                        .gt("expire_time", Instant.now().toEpochMilli()));
        if (count > 0){
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_DATA_STATE, "验证码未过期，请查看邮件！");
        }

        UserModel userModel = userModelService.getOne(new QueryWrapper<UserModel>().eq("username", username));
        if (userModel == null){
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_PARAM, "用户名不正确！");
        }

        String authCode = IdentifyingCode.getCode(Contains.CHECK_CODE_LENGTH, IdentifyingCode.CodeType.NUMBER_AND_LETTER);
        try {
            MailBean mailBean = new MailBean();
            mailBean.setSubject(systemName + "重置密码");
            mailBean.addTargetAddress(userModel.getEmail());
            mailBean.addContextText("您好！<br><br>您在"+systemName+"重置账号密码！本次操作的验证码为：" + authCode);
            mailBean.addContextText("，10分钟内有效。请在验证码输入框输入上述验证码完成验证。<br>如果非您本人操作，请及时更新密码。");

            SendMail.getSendMail("default").sendMail(mailBean);
        } catch (AddressException e) {
            log.error("重置密码-邮箱验证码发送异常：", e);
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_PARAM, "email地址不正确！");
        } catch (MessagingException e) {
            log.error("重置密码-邮箱验证码发送异常：", e);
            return ResponseJsonMsg.error(Contains.RET_CODE_FAILED_SYS, "请重试或联系管理员！");
        }

        AuthCodeCacheModel authCodeCache = new AuthCodeCacheModel();
        authCodeCache.setChannelNo(Contains.CHANNEL_AUTH_CODE_RESET_PASSWORD);
        authCodeCache.setUserJoin(userModel.getUsername());
        authCodeCache.setAuthCode(authCode);
        authCodeCache.setExpireTime(Instant.now().toEpochMilli() + Contains.EFFECTIVE_TIME_AUTH_CODE_RESET_PASSWORD);
        authCodeCacheService.save(authCodeCache);

        return ResponseJsonMsg.ok("验证码已发送！");
    }
}
