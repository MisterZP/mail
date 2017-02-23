package com.mail.utils;

import com.mail.vo.MailQueueVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Created by David on 16/5/11.
 */
public class EmailSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private SimpleMailMessage simpleMailMessage;

    public void sendMail(MailQueueVo mailQueueVo) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, Constants.COMMON_CHARSET);
        messageHelper.setFrom(simpleMailMessage.getFrom());
        messageHelper.setSubject(mailQueueVo.getTitle());
        messageHelper.setText(mailQueueVo.getContent(), true);
        messageHelper.setTo(mailQueueVo.getAddress());
        LOGGER.info("ready to send a mail to [{}]", mailQueueVo.getAddress());
        javaMailSender.send(mimeMessage);
    }
}
