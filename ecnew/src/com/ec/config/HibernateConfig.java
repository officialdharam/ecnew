package com.ec.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableAspectJAutoProxy
@Profile(value = { "production", "local" })
@PropertySource({ "classpath:app.properties" })
public class HibernateConfig {

    @Autowired
    private DataSource dataSource;
    
    @Value("${hibernate.dialect}")
    private String hibernateDialect;

    @Autowired
    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory() throws Exception {

        Properties properties = new Properties();
        properties.put("hibernate.dialect", hibernateDialect);
        properties.put("hibernate.show_sql", "true");
        properties.put("current_session_context_class", "thread");
        properties.put("dynamic-update", "true");

        LocalSessionFactoryBean factory = new LocalSessionFactoryBean();
        factory.setPackagesToScan(new String[] { "com.ec.entity" });
        factory.setDataSource(dataSource);
        factory.setHibernateProperties(properties);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean(name = "transactionManager")
    public HibernateTransactionManager getTransactionManager() throws Exception {
        return new HibernateTransactionManager(getSessionFactory());
    }
    
    @Bean(name = "appProperty")
    public static PropertySourcesPlaceholderConfigurer appProperty() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
