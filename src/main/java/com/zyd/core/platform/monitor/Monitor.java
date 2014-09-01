package com.zyd.core.platform.monitor;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Chi
 */
@Target({TYPE})
@Retention(RUNTIME)
@Documented
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public @interface Monitor {
    String value();
}
