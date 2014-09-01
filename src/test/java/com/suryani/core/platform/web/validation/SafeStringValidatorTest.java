package com.zyd.core.platform.web.validation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author neo
 */
public class SafeStringValidatorTest {
    SafeStringValidator validator;

    @Before
    public void createHTMLSafeValidator() {
        validator = new SafeStringValidator();
    }

    @Test
    public void notAcceptHTMLText() {
        Assert.assertFalse(validator.isValid("<", null));
        Assert.assertFalse(validator.isValid(">", null));
        Assert.assertFalse(validator.isValid("<tag>", null));
    }

    @Test
    public void validText() {
        Assert.assertTrue(validator.isValid(null, null));
        Assert.assertTrue(validator.isValid("", null));

        Assert.assertTrue(validator.isValid("Text", null));
        Assert.assertTrue(validator.isValid("Address #100, CA", null));
    }
}
