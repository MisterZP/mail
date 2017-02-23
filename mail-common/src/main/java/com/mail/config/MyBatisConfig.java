package com.mail.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageHelper;
import com.mail.entity.DruidBean;
import com.mail.entity.MybatisProBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author zengping
 */
@Configuration
@PropertySource(value = {"classpath:config.properties"
}, ignoreResourceNotFound = true)
@EnableConfigurationProperties({DruidBean.class, MybatisProBean.class})
@EnableTransactionManagement
public class MyBatisConfig {

    @Autowired
    private DruidBean druidBean;
    @Autowired
    private MybatisProBean mybatisProBean;

    @Bean(name = "dataSource", initMethod = "init", destroyMethod = "close")
    public DataSource dataSource() throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(druidBean.getDriverClassName());
        dataSource.setUrl(druidBean.getUrl());
        dataSource.setUsername(druidBean.getUsername());
        dataSource.setPassword(druidBean.getPassword());
        dataSource.setInitialSize(druidBean.getInitialSize());
        dataSource.setMinIdle(druidBean.getMinIdle());
        dataSource.setMaxActive(druidBean.getMaxActive());
        dataSource.setMaxWait(druidBean.getMaxWait());
        dataSource.setMinEvictableIdleTimeMillis(druidBean.getMinEvictableIdleTimeMillis());
        dataSource.setTimeBetweenEvictionRunsMillis(druidBean.getTimeBetweenEvictionRunsMillis());
        dataSource.setValidationQuery(druidBean.getValidationQuery());
        dataSource.setTestWhileIdle(druidBean.isTestWhileIdle());
        dataSource.setFilters(druidBean.getFilters());
        return dataSource;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage(mybatisProBean.getTypeAliasesPackage());
        //add mybatis config.xml
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        if(StringUtils.hasText(mybatisProBean.getConfig()))
            bean.setConfigLocation(resourceLoader.getResource(mybatisProBean.getConfig()));
        //add  apge plugins
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        properties.load(new ClassPathResource("pagehelper.properties").getInputStream());
        pageHelper.setProperties(properties);
        bean.setPlugins(new Interceptor[]{pageHelper});
        //add xml
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        bean.setMapperLocations(resolver.getResources(mybatisProBean.getMapperLocations()));
        return bean.getObject();
    }

    @Bean(name = "transactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
