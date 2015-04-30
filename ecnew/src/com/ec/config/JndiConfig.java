/*******************************************************************************
 * Copyright © 2014 Progress Software Corporation.  All Rights Reserved.
 ******************************************************************************/
package com.ec.config;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JndiConfig {

    @Value("${jdbc.driverClassName}")
    private String driverClassName;
    @Value("${jdbc.databaseurl}")
    private String databaseURL;
    @Value("${jdbc.username}")
    private String username;
    @Value("${jdbc.password}")
    private String password;
    @Value("${datasource.max.active}")
    private int maxActive;
    @Value("${datasource.max.idle}")
    private int maxIdle;
    @Value("${datasource.max.wait}")
    private int maxWait;
    @Value("${datasource.min.idle}")
    private int minIdle;
    @Value("${datasource.initial.size}")
    private int initialSize;
    @Value("${datasource.timeBetweenEvictionRunsMillis}")
    private long timeBetweenEvictionRunsMillis;

    @Bean(name = "dataSource")
    public DataSource dataSource() throws Exception {

	BasicDataSource dataSource = new BasicDataSource();
	
	dataSource.setDriverClassName(driverClassName);
	dataSource.setUrl(databaseURL);
	dataSource.setUsername(username);
	dataSource.setPassword(password);
	dataSource.setTestOnBorrow(true);
	dataSource.setTestWhileIdle(true);
	dataSource.setTestOnReturn(false);
	dataSource.setValidationQuery("SELECT 1");
	dataSource.setMaxActive(maxActive);
	dataSource.setMaxIdle(maxIdle);
	dataSource.setMaxWait((int) maxWait);
	dataSource.setMinIdle(minIdle);
	dataSource.setInitialSize(initialSize);
	dataSource.setTimeBetweenEvictionRunsMillis((int) timeBetweenEvictionRunsMillis);
	return dataSource;
    }
}
