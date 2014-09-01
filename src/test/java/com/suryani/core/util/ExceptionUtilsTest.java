package com.zyd.core.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;

/**
 * @author neo
 */
public class ExceptionUtilsTest {
    @Test
    public void getStackTrace() {
        RuntimeException exception = new RuntimeException();
        String trace = ExceptionUtils.stackTrace(exception);

        Assert.assertThat(trace, JUnitMatchers.containsString(RuntimeException.class.getName()));
    }
}
