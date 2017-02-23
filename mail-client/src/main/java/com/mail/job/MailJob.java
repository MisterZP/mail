package com.mail.job;

import com.github.pagehelper.Page;
import com.mail.model.MailQueue;
import com.mail.service.MailQueueService;
import com.mail.utils.Constants;
import com.mail.vo.MailQueueVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by zengping on 2017/1/11.
 */
@Component
@EnableScheduling
public class MailJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailJob.class);

    @Autowired
    private MailQueueService mailQueueService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ExecutorService pool;

    @Scheduled(cron = "0 0/30 * * * ?")
    public void startJob() {
        Example example = new Example(MailQueue.class);
        example.createCriteria().andLessThanOrEqualTo("updateTime", new Timestamp(System.currentTimeMillis() - 3600 * 500)).andIsNull("sendTime");
        long total = mailQueueService.countByExample(example);
        if (total > 0) {
            int pageTotal = (int) Math.ceil(total / 100d);
            for (int i = 1; i <= pageTotal; i++) {
                Page page = new Page(i, 100);
                List<MailQueue> mailQueues = mailQueueService.pageByExample(example, page);
                if(!CollectionUtils.isEmpty(mailQueues))
                    mailQueues.forEach(mail->{
                        MailQueueVo mailQueueVo = mail.vo();
                        pool.execute(()->{
                            try{
                                redisTemplate.convertAndSend(Constants.MAIL_TOPIC, mailQueueVo);
                            }catch (Exception e){
                                LOGGER.error("email add 2 redis channel fialuer mail queue id: {}", mailQueueVo.getId(), e);
                            }
                        });
                    });
            }
        }
    }


}
