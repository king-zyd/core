package com.zyd.core.platform.web.validation;

import com.zyd.core.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author neo
 */
public class SafeStringValidator implements ConstraintValidator<SafeString, String> {
    // the white list of safe string for XSS, no HTML related char here
    private static final Pattern PATTERN_SAFE_STRING = Pattern.compile("^[.\\-,#&\\p{Alnum}\\p{Space}]*$");

    @Override
    public void initialize(SafeString safeString) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!StringUtils.hasText(value)) {
            return true;
        }
        return PATTERN_SAFE_STRING.matcher(value).matches();
    }
}

