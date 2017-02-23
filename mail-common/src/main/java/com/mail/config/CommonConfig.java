package com.mail.config;

import com.mail.entity.MailBean;
import com.mail.utils.Constants;
import com.mail.utils.EmailSender;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;

/**
 * Created by zengping on 2017/1/9.
 */
@Configuration
@PropertySource(value = {"classpath:config.properties"
}, ignoreResourceNotFound = true)
@EnableConfigurationProperties(value = {MailBean.class})
public class CommonConfig {
    @Autowired
    private MailBean mailBean;
    @Autowired
    private Environment environment;

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding(Constants.COMMON_CHARSET);
        characterEncodingFilter.setForceEncoding(true);
        registrationBean.setFilter(characterEncodingFilter);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    @Bean
    public RestTemplate getRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        for (HttpMessageConverter converter: converters) {
            if(converter instanceof StringHttpMessageConverter){
                converters.remove(converter);
                HttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
                converters.add(stringHttpMessageConverter);
                break;
            }
        }
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        BeanUtils.copyProperties(restTemplate.getRequestFactory(), requestFactory);
        requestFactory.setReadTimeout(10 * 1000);//读写超时时间
        requestFactory.setConnectTimeout(10 * 1000);//连接超时时间
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailBean.getHost());
        mailSender.setPort(mailBean.getPort());
        mailSender.setUsername(mailBean.getUserName());
        mailSender.setPassword(mailBean.getPassword());
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", mailBean.getAuth());
        prop.put("mail.smtp.timeout", mailBean.getTimeout());
        mailSender.setJavaMailProperties(prop);
        return mailSender;
    }

    @Bean
    public SimpleMailMessage simpleMailMessage() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(mailBean.getFrom());
        return simpleMailMessage;
    }


    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        return executor;
    }

    @Bean
    public EmailSender emailSender(){
        return new EmailSender();
    }
}
