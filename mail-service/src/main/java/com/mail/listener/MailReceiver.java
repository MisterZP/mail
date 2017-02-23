package com.mail.listener;

import com.mail.service.MailQueueService;
import com.mail.utils.Constants;
import com.mail.utils.EmailSender;
import com.mail.vo.MailQueueVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;

import javax.mail.MessagingException;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by zengping on 2017/1/9.
 */

public class MailReceiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailReceiver.class);

    @Autowired
    private MailQueueService mailQueueService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private EmailSender emailSender;

    @Autowired
    private TaskExecutor taskExecutor;

    public void receiver(Serializable message){
        if(message instanceof MailQueueVo){
            MailQueueVo mailVo = (MailQueueVo) message;

            // 异步更新邮件状态为发送成功
            taskExecutor.execute(()->{
                try {
                    emailSender.sendMail(mailVo);
                    mailVo.setSendTime(new Timestamp(System.currentTimeMillis()));
                    mailVo.setCount(mailVo.getCount() + 1);
                    mailVo.setStatus(MailQueueVo.Status.SENT_SUCCESS);
                    LOGGER.info("sendMail success mailQueue id : {}", mailVo.getId());
                } catch (MessagingException e) {
                    mailVo.setStatus(MailQueueVo.Status.SENT_FAILURE);
                    redisTemplate.convertAndSend(Constants.COMMON_CHARSET, mailVo);
                    LOGGER.error("sendMail error mailQueue id : {}", mailVo.getId(), e);
                }
                mailQueueService.update(mailVo);
            });

        }
    }

}
