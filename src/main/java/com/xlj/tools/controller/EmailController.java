package com.xlj.tools.controller;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * 邮件发送
 *
 * @author xlj
 * @date 2021/5/6
 */
@Slf4j
@RestController
@RequestMapping(value = "/email")
public class EmailController {
    @Value("${spring.mail.username}")
    private String sendName;

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping("")
    public ModelAndView email(ModelAndView modelAndView) {
        modelAndView.setViewName("sendEmail");
        return modelAndView;
    }

    /**
     * 简单的邮件
     *
     * @param msg
     * @param email
     * @return
     */
    @PostMapping(value = "/simple")
    public String sendSimpleMsg(@RequestParam(value = "msg") String msg, @RequestParam(value = "email") String email) {
        if (StrUtil.isEmpty(msg) || StrUtil.isEmpty(email)) {
            return "请输入要发送消息和目标邮箱";
        }
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(sendName);
            mail.setTo(email);
            mail.setSubject("这是一封简单邮件");
            mail.setText(msg);
            mailSender.send(mail);
            return "发送成功";
        } catch (Exception e) {
            log.warn("邮件发送失败：{}", e);
            return "发送失败:" + e.getMessage();
        }
    }

    /**
     * html邮件格式
     *
     * @param msg
     * @param email
     * @return
     */
    @PostMapping(value = "/html")
    public String sendHtmlMsg(@RequestParam(value = "msg") String msg, @RequestParam(value = "email") String email) {
        if (StrUtil.isEmpty(msg) || StrUtil.isEmpty(email)) {
            return "请输入要发送消息和目标邮箱";
        }
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setFrom(sendName);
            messageHelper.setTo(email);
            messageHelper.setSubject("HTML邮件");
            String html = "<div><h1><a name=\"hello\"></a><span>Hello</span></h1><blockquote><p><span>this is a html email.</span></p></blockquote><p>&nbsp;</p><p><span>"
                    + msg + "</span></p></div>";
            messageHelper.setText(html, true);
            mailSender.send(message);
            return "发送成功";
        } catch (MessagingException e) {
            log.warn("邮件发送失败：{}", e);
            return "发送失败：" + e.getMessage();
        }
    }

    /**
     * 包含附件的邮件发送
     *
     * @param msg
     * @param email
     * @return
     */
    @PostMapping(value = "/withAnnex")
    public String sendWithFile(@RequestParam(value = "msg") String msg, @RequestParam(value = "email") String email) {
        if (StrUtil.isEmpty(msg) || StrUtil.isEmpty(email)) {
            return "请输入要发送消息和目标邮箱";
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setFrom(sendName);
            messageHelper.setTo(email);
            messageHelper.setSubject("一封包含附件的邮件");
            messageHelper.setText(msg);
            // 该文件位于resources目录下
            // 文件路径不能直接写文件名，系统会报错找不到路径，而IDEA却能直接映射过去
            // 文件路径可以写成相对路径src/main/resources/x.pdf，也可以用绝对路径：System.getProperty("user.dir") + "/src/main/resources/x.pdf"
            File file = new File(System.getProperty("user.dir") + "/src/main/resources/img/7cflw.jpg");
            log.info("文件是否存在：{}", file.exists());
            messageHelper.addAttachment(file.getName(), file);
            mailSender.send(message);
            return "发送成功";
        } catch (MessagingException e) {
            log.warn("邮件发送失败：{}", e);
            return "发送失败：" + e.getMessage();
        }
    }

    /**
     * html带有图片
     *
     * @param msg
     * @param email
     * @return
     */
    @PostMapping(value = "withImg")
    public String sendHtmlWithImg(@RequestParam(value = "msg") String msg, @RequestParam(value = "email") String email) {
        if (StrUtil.isEmpty(msg) || StrUtil.isEmpty(email)) {
            return "请输入要发送消息和目标邮箱";
        }
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setFrom(sendName);
            messageHelper.setTo(email);
            messageHelper.setSubject("带静态资源图片的HTML邮件");
            String html = "<div><h1><a name=\"hello\"></a><span>Hello</span></h1><blockquote><p><span>this is a html email.</span></p></blockquote><p>&nbsp;</p><p><span>"
                    + msg + "</span></p><img src='cid:myImg' /></div>";
            messageHelper.setText(html, true);
            File file = new File("src/main/resources/img/7cflw.jpg");
            messageHelper.addInline("myImg", file);
            mailSender.send(message);
            return "发送成功";
        } catch (MessagingException e) {
            log.warn("邮件发送失败：{}", e);
            return "发送失败：" + e.getMessage();
        }
    }
}
