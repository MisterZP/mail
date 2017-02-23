package com.mail.vo;

import com.mail.common.ValidGroup;
import com.mail.enums.DataEnum;
import com.mail.model.MailQueue;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.BeanUtils;

import javax.validation.GroupSequence;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by zengping on 2017/1/9.
 */

@Data
@GroupSequence(value = {ValidGroup.First.class, ValidGroup.Second.class, MailQueueVo.class})
public class MailQueueVo implements Serializable{
    private Long id;
    private String ip;
    @NotBlank(message = "标题不能为空", groups = {ValidGroup.First.class})
    @Length(max = 255, message = "邮件标题长度不能超过255个字符或者汉字", groups = {ValidGroup.Second.class})
    private String title;
    @NotBlank(message = "地址不能为空", groups = {ValidGroup.First.class})
    @Length(max = 800, message = "邮件标题长度不能超过800个字符或者汉字", groups = {ValidGroup.Second.class})
    private String address;
    @NotBlank(message = "内容不能为空", groups = {ValidGroup.First.class})
    @Length(max = 10000, message = "邮件标题长度不能超过10000个字符或者汉字", groups = {ValidGroup.Second.class})
    private String content;
    private Timestamp createTime;
    private Timestamp updateTime;
    private Timestamp sendTime;
    private int count;
    private Status status;

    public enum Status implements DataEnum<Status> {
        NOT_SENT(0),
        SENT_SUCCESS(1),
        SENT_FAILURE(2);

        Status(int code ){
            this.code = code;
        }
        private int code;

        public static Status codeOf(int code){
            for (Status status :values()) {
                if(status.getCode() == code)
                    return status;
            }
            return null;
        }

        public int getCode(){
            return this.code;
        }

    }

    public MailQueue po(){
        MailQueue mailQueue = new MailQueue();
        BeanUtils.copyProperties(this, mailQueue);
        if(null != this.status)
            mailQueue.setStatus(this.status.getCode());
        return mailQueue;
    }

}
