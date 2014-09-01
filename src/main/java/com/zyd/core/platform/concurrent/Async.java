package com.zyd.core.platform.concurrent;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Chi
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface Async {
    long timeout();
}
