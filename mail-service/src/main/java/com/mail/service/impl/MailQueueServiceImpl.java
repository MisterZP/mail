package com.mail.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mail.mapper.MailQueueMapper;
import com.mail.model.MailQueue;
import com.mail.service.MailQueueService;
import com.mail.utils.Constants;
import com.mail.vo.MailQueueVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by zengping on 2017/1/9.
 */

@Service
@Transactional(readOnly = true)
public class MailQueueServiceImpl implements MailQueueService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailQueueServiceImpl.class);

    @Autowired
    private ExecutorService pool;

    @Autowired
    private MailQueueMapper mailQueueMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Transactional
    public void update(MailQueueVo mailQueueVo) {
        mailQueueVo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        mailQueueMapper.updateByPrimaryKey(mailQueueVo.po());
    }

    @Override
    public int countByCondition(MailQueueVo mailQueueVo) {
        return mailQueueMapper.selectCount(mailQueueVo.po());
    }

    @Override
    public long countByExample(Example example) {
        return mailQueueMapper.selectCountByExample(example);
    }

    @Override
    public List<MailQueue> pageByExample(Example example, Page<?> page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return mailQueueMapper.selectByExample(example);
    }

    @Override
    @Transactional
    public void add(final MailQueueVo mailQueueVo) {
        mailQueueVo.setStatus(MailQueueVo.Status.NOT_SENT);
        mailQueueVo.setCreateTime(new Timestamp(System.currentTimeMillis()));
        mailQueueVo.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        final MailQueue mailQueue = mailQueueVo.po();
        mailQueueMapper.insert(mailQueue);
        mailQueueVo.setId(mailQueue.getId());
        pool.execute(()->{
            try{
                redisTemplate.convertAndSend(Constants.MAIL_TOPIC, mailQueueVo);
            }catch (Exception e){
                LOGGER.error("email add 2 redis channel fialuer mail queue id: {}", mailQueue.getId(), e);
            }
        });
    }
}
