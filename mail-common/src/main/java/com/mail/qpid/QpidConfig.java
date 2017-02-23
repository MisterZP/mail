package com.mail.qpid;

import org.apache.qpid.client.AMQAnyDestination;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.destination.JndiDestinationResolver;
import org.springframework.jndi.JndiTemplate;

import javax.jms.ConnectionFactory;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by zengping on 2017/1/9.
 */
@Configuration
@PropertySource(value = {"classpath:config.properties"
}, ignoreResourceNotFound = true)
public class QpidConfig {

    @Value("${qpid.url:qpid.properties}")
    private String qpidUrl;

    @Bean
    public JndiTemplate jndiTemplate() throws IOException {
        JndiTemplate jndiTemplate = new JndiTemplate();
        Properties properties = new Properties();
        properties.load(new ClassPathResource(qpidUrl).getInputStream());
        jndiTemplate.setEnvironment(properties);
        return jndiTemplate;
    }

    @Bean
    public JmsTemplate jmsTemplate(JndiTemplate jndiTemplate) throws IOException, NamingException {
        JmsTemplate jmsTemplate = new JmsTemplate();
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory((ConnectionFactory) jndiTemplate.lookup("qpidConnectionfactory"));
        cachingConnectionFactory.setSessionCacheSize(5);
        jmsTemplate.setConnectionFactory(cachingConnectionFactory);
        jmsTemplate.setDefaultDestination(((AMQAnyDestination) jndiTemplate.lookup("topicExchange")));
        jmsTemplate.setSessionTransacted(true);
        return jmsTemplate;
    }

    @Bean
    public JndiDestinationResolver destinationResolver(JndiTemplate jndiTemplate) throws IOException {
        JndiDestinationResolver jndiDestinationResolver = new JndiDestinationResolver();
        jndiDestinationResolver.setJndiTemplate(jndiTemplate);
        return jndiDestinationResolver;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(JndiDestinationResolver destinationResolver) throws IOException, NamingException {
        DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory((ConnectionFactory) destinationResolver.getJndiTemplate().lookup("qpidConnectionfactory"));
        cachingConnectionFactory.setSessionCacheSize(5);
        defaultJmsListenerContainerFactory.setConnectionFactory(cachingConnectionFactory);
        defaultJmsListenerContainerFactory.setDestinationResolver(destinationResolver);
        defaultJmsListenerContainerFactory.setConcurrency("3-10");
        return defaultJmsListenerContainerFactory;
    }
}
