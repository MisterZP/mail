package com.mail.config;

import com.mail.listener.MailReceiver;
import com.mail.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zengping on 2017/1/9.
 */

@Configuration
@PropertySource(value = {"classpath:config.properties"
}, ignoreResourceNotFound = true)
public class MailConfig {

    @Autowired
    private RedisTemplate redisTemplate;

    @Bean
    public JdkSerializationRedisSerializer serializer() {
        return new JdkSerializationRedisSerializer();
    }

    @Bean
    public ExecutorService pool() {
        return Executors.newFixedThreadPool(20);
    }

    @Bean
    public MailReceiver mailReceiver() {
        return new MailReceiver();
    }

    @Bean
    public MessageListenerAdapter messageListener(MailReceiver mailReceiver, JdkSerializationRedisSerializer serializer) {
        MessageListenerAdapter bean = new MessageListenerAdapter(mailReceiver, Constants.RECEIVER);
        bean.setSerializer(serializer);
        return bean;
    }

    @Bean
    public RedisMessageListenerContainer listenerContainer(MessageListenerAdapter messageListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisTemplate.getConnectionFactory());
        container.addMessageListener(messageListenerAdapter, new PatternTopic(Constants.MAIL_TOPIC));
        container.setTaskExecutor(new SyncTaskExecutor());
        container.setSubscriptionExecutor(new SimpleAsyncTaskExecutor());
        return container;
    }
}
