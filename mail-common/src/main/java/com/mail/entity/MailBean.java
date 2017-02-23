package com.mail.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zengping on 2017/1/9.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "mail.smtp")
public class MailBean {
    /**
     * mail.smtp.host=10.205.91.24
     mail.smtp.port=25
     mail.smtp.username=Promotion
     mail.smtp.password=@!m08192016
     mail.smtp.from=Promotion@le.com
     mail.smtp.auth=true
     mail.smtp.timeout=250000
     */
    private String host;
    private int port;
    private String userName;
    private String password;
    private String auth;
    private int timeout;
    private String from;
}
