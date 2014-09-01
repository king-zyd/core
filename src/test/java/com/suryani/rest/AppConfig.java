package com.suryani.rest;

import com.zyd.core.database.JDBCAccess;
import com.zyd.core.database.JPAAccess;
import com.zyd.core.platform.DefaultAppConfig;
import com.zyd.core.platform.concurrent.AsyncAspect;
import com.zyd.core.platform.concurrent.TaskExecutor;
import org.hibernate.dialect.HSQLDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.inject.Inject;
import javax.sql.DataSource;

/**
 * @author neo
 */
@Configuration
public class AppConfig extends DefaultAppConfig {

    @Inject
    Environment env;

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan(AppConfig.class.getPackage().getName());
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabasePlatform(HSQLDialect.class.getName());
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(true);
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setDataSource(dataSource());
        return transactionManager;
    }

    @Bean
    public JDBCAccess jdbcAccess() {
        JDBCAccess jdbcAccess = new JDBCAccess();
        jdbcAccess.setDataSource(dataSource());
        return jdbcAccess;
    }

    @Bean
    public JPAAccess jpaAccess() {
        return new JPAAccess();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return TaskExecutor.unlimitedExecutor();
    }

    @Bean
    public AsyncAspect asyncAspect() {
        return new AsyncAspect();
    }
}
