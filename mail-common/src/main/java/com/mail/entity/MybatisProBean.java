package com.mail.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zengping on 2017/1/9.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "mybatis")
public class MybatisProBean {
    /**
     * mybatis.config=classpath:/mybatis-config.xml
     mybatis.mapperLocations= classpath:mapper/*.xml
     mybatis.typeAliasesPackage=com.mail.model
     */
    private String config;
    private String mapperLocations;
    private String typeAliasesPackage;

}
