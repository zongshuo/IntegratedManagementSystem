package com.zongshuo.util.email;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Author: zongShuo
 * @Version: 1.0
 * @Date: 2021-5-23
 * @Time: 19:46
 * @Description:
 * 邮件发送代码
 */
public class SendMail {
    //邮件发送使用的服务器
    private String host;
    //发件人邮箱
    private String username;
    //发件人邮箱密码
    private String password;
    //是否做ssl加密
    private boolean doSSL;
    //存储邮箱基本配置信息
    private Properties properties ;
    //程序运行环境
    private Session session ;
    private static Map<String, SendMail> sendMailMap = new HashMap<>();
    private static Map<String, String> name2Bean = new HashMap<>();
    public static SendMail getSendMail(String mailName){
        return sendMailMap.get(name2Bean.get(mailName));
    }


    private SendMail(String host, String username, String password, boolean doSSL){
        this.host = host ;
        this.username = username;
        this.password = password;
        this.doSSL = doSSL;
    }

    public static SendMail getInstance(String beanName, String host, String username, String password, boolean doSLL) throws  GeneralSecurityException{
        SendMail sendMail = getInstance(host, username, password, doSLL);
        name2Bean.put(beanName, host+username);
        return sendMail;
    }
    public static SendMail getInstance(String host, String username, String password, boolean doSSL) throws GeneralSecurityException {
        String key = host+username;
        if (sendMailMap.containsKey(key))
            return sendMailMap.get(key);
        synchronized (SendMail.class){
            if (sendMailMap.containsKey(key))
                return sendMailMap.get(key);

            SendMail sendMail = new SendMail(host, username, password, doSSL);
            sendMail.setProperties();
            sendMail.setSession();
            sendMailMap.put(key, sendMail);
            return  sendMail;
        }
    }

    /**
     * 设置邮箱配置信息对象
     *
     */
    private void setProperties() throws GeneralSecurityException {
        properties = new Properties();
        //设置邮件服务器
        properties.setProperty("mail.host", this.host);
        //设置邮件发送协议
        properties.setProperty("mail.transport.protocol", "smtp");
        //需要验证用户名密码
        properties.setProperty("mail.smtp.auth", "true");
        if (this.doSSL){
            //设置ssl加密
            MailSSLSocketFactory socketFactory = new MailSSLSocketFactory();
            socketFactory.setTrustAllHosts(true);
            properties.setProperty("mail.smtp.ssl.enable", "true");
            properties.setProperty("mail.smtp.ssl.socketFactory", String.valueOf(socketFactory));
        }
    }

    /**
     * 创建程序执行所需环境信息对象
     * @return
     */
    private void setSession(){

        //Session.getDefaultInstance()获得一个默认的共享session
        //Session.getInstance()创建一个session
        this.session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    private void setMimeMessage(MailBean mail, MimeMessage message) throws MessagingException {
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, mail.getTargetEmail());
        message.setSubject(mail.getSubject());

        File file;
        StringBuilder context = new StringBuilder();
        MimeMultipart mimeMultipart = new MimeMultipart();
        for (Object object : mail.getContext()){
            if (object instanceof File){
                //添加图片
                file = (File) object;
                MimeBodyPart image = new MimeBodyPart();
                DataHandler handler = new DataHandler(new FileDataSource(file));
                image.setDataHandler(handler);
                image.setContentID(file.getName());
                mimeMultipart.addBodyPart(image);
                context.append("<img src='cid:"+file.getName()+"'>");
                continue;
            }
            context.append(object);
        }
        MimeBodyPart content = new MimeBodyPart();
        content.setContent(context.toString(), "text/html;charset=UTF-8");

        mimeMultipart.addBodyPart(content);
        mimeMultipart.setSubType("related");

        message.setContent(mimeMultipart);
        message.saveChanges();
//        message.setContent(mail.getContext(), "text/html;charset=UTF-8");

    }

    /**
     * 发送邮件
     * @param mail
     * @throws MessagingException
     */
    public void sendMail(MailBean mail) throws MessagingException {
        Transport transport = session.getTransport();
        transport.connect(host, username, password);
        //创建邮件对象
        MimeMessage message = new MimeMessage(session);
        //设置邮件对象
        setMimeMessage(mail, message);
        //发送邮件对象
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
}
