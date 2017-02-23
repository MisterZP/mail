package com.mal.test;

import com.github.pagehelper.Page;
import com.mail.ClientApplication;
import com.mail.config.CommonConfig;
import com.mail.config.MailConfig;
import com.mail.config.MapperScannerConfig;
import com.mail.config.MyBatisConfig;
import com.mail.model.MailQueue;
import com.mail.service.MailQueueService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import tk.mybatis.mapper.entity.Example;

import java.sql.Timestamp;


@Configuration
@ContextConfiguration(classes = {CommonConfig.class, MyBatisConfig.class, MapperScannerConfig.class, MailConfig.class})
@SpringBootTest(classes = {ClientApplication.class})
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class MailTest {

    @Autowired
    private MailQueueService mailQueueService;

    private Example example = new Example(MailQueue.class);

    @Before
    public void example(){
        example.createCriteria().andLessThanOrEqualTo("updateTime", new Timestamp(System.currentTimeMillis() - 3600 * 500)).andIsNull("sendTime");
    }

    @Test
    public void noSentCount(){
       System.out.println(mailQueueService.countByExample(example));
    }

    @Test
    public void pageData(){
        System.out.println(mailQueueService.pageByExample(example, new Page(1, 100)));
    }
}
