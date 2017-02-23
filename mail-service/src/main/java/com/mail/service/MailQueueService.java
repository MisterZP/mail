package com.mail.service;

import com.github.pagehelper.Page;
import com.mail.model.MailQueue;
import com.mail.vo.MailQueueVo;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by zengping on 2017/1/9.
 */

public interface MailQueueService {
    void add(MailQueueVo mailQueueVo);

    void update(MailQueueVo mailQueueVo);

    int countByCondition(MailQueueVo mailQueueVo);

    long countByExample(Example example);

    List<MailQueue> pageByExample(Example example, Page<?> page);
}
