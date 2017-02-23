package com.mail.model;

import com.mail.vo.MailQueueVo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by zengping on 2017/1/9.
 */

@Data
@Table(name = "mail_queue")
public class MailQueue{
    /**
     *`id` bigint(20) NOT NULL AUTO_INCREMENT,
     `ip` varchar(20) NOT NULL COMMENT 'ip来源',
     `title` varchar(255) DEFAULT NULL,
     `address` varchar(800) DEFAULT NULL,
     `content` text,
     `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
     `send_time` timestamp NULL DEFAULT NULL,
     `status` tinyint(1) DEFAULT '0' COMMENT '是否发送成功,0未发送，1发送成功,2发送失败',
     `count` int(11) DEFAULT '0' COMMENT '发送次数',
     *
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ip;
    private String title;
    private String address;
    private String content;
    private Timestamp createTime;
    private Timestamp updateTime;
    private Timestamp sendTime;
    private int count;
    private Integer status;

    public MailQueueVo vo(){
        MailQueueVo vo = new MailQueueVo();
        BeanUtils.copyProperties(this, vo);
        if(null != this.status)
            vo.setStatus(MailQueueVo.Status.codeOf(this.status));
        return vo;
    }
}
