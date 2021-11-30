package com.xlj.tools.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * 邮件发送工具
 *
 * @author legend xu
 * @date 2021/11/30
 */
@Slf4j
@Component
public class MailUtil {
    @Autowired
    private JavaMailSender mailSender;

    /**
     * 发送html邮件
     *
     * @param from    发送人
     * @param to      收件人，可单人
     * @param subject 主题
     * @param content 内容
     */
    public void sendHtmlMail(String from, String[] to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            log.warn("邮件发送失败:", e);
        }
    }
}
