package com.zyd.core.platform.exception;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Chi
 */
public class ErrorHandlerTest {
    ErrorHandler errorHandler;

    @Before
    public void createErrorHandler() {
        errorHandler = new ErrorHandler();
    }

    @Test
    public void isIgnore() {
        assertTrue(errorHandler.isIgnore(null));
        assertTrue(errorHandler.isIgnore(new TestIgnoreException()));
    }

    @Test
    public void isWarning() {
        assertTrue(errorHandler.isWarning(new TestWarningException()));
    }

    @Test
    public void httpMethodNotSupportIsWarning() {
        assertTrue(errorHandler.isWarning(new HttpRequestMethodNotSupportedException(null)));
    }

    @Test
    public void isError() {
        assertFalse(errorHandler.isWarning(new TestException()));
    }

    @Warning
    public static class TestWarningException extends RuntimeException {

    }

    @Ignore
    public static class TestIgnoreException extends RuntimeException {

    }

    public static class TestException extends RuntimeException {

    }
}
