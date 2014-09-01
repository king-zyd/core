package com.zyd.core.platform.web.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * @author neo
 */
@Documented
@Constraint(validatedBy = SafeStringValidator.class)
@Target({METHOD, FIELD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SafeString {
    String message() default "{com.suryani.platform.validation.safeString.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}