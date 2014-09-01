package com.suryani.rest;

import com.zyd.core.platform.AbstractServiceConfig;
import com.zyd.core.platform.PlatformScopeResolver;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;

/**
 * @author neo
 */
@Configuration
@ComponentScan(basePackageClasses = WebConfig.class, scopeResolver = PlatformScopeResolver.class)
@EnableAspectJAutoProxy
@EnableTransactionManagement(proxyTargetClass = true)
public class WebConfig extends AbstractServiceConfig {
    @Inject
    EntityManagerFactory entityManagerFactory;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(exceptionInterceptor());
        registry.addInterceptor(requestContextInterceptor());
        registry.addInterceptor(trackInterceptor());
        OpenEntityManagerInViewInterceptor interceptor = new OpenEntityManagerInViewInterceptor();
        interceptor.setEntityManagerFactory(entityManagerFactory);
    }
}
