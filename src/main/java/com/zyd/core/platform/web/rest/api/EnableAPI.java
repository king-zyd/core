package com.zyd.core.platform.web.rest.api;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Raymond
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Import(EnableAPISelector.class)
@Inherited
public @interface EnableAPI {
    AdviceMode mode() default AdviceMode.PROXY;

    String serverURL();

    String scanPackage();

    String clientId() default "";

    String clientKey() default "";
}
