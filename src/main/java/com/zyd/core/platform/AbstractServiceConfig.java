package com.zyd.core.platform;

import com.zyd.core.platform.web.exception.ExceptionInterceptor;
import com.zyd.core.platform.web.rest.RESTControllerAdvice;
import org.springframework.context.annotation.Bean;

/**
 * @author neo
 */
public abstract class AbstractServiceConfig extends AbstractWebConfig {
    @Bean
    RESTControllerAdvice restControllerAdvice() {
        return new RESTControllerAdvice();
    }

    @Bean
    public ExceptionInterceptor exceptionInterceptor() {
        return new ExceptionInterceptor();
    }
}
