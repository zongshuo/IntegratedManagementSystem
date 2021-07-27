package com.zongshuo.util.email;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-23
 * @Time: 19:43
 * @Description:
 * 发送邮件内容的实体
 */
public class MailBean {
    //收件人邮箱列表
    private List<InternetAddress> targetEmail = new ArrayList<>();
    //邮件标题
    private String subject;
    //邮件内容
    private List<Object> context = new ArrayList<>();

    public InternetAddress [] getTargetEmail() {
        return targetEmail.toArray(new InternetAddress[targetEmail.size()]);
    }

    public void addTargetAddress(String targetAddress) throws AddressException {
        this.targetEmail.add(new InternetAddress(targetAddress));
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void addContextText(String text){
        this.context.add(text);
    }
    public void addContextImage(File file){
        this.context.add(file);
    }
    public List<Object> getContext(){
        return this.context;
    }
}
