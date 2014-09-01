package com.zyd.core.platform.web.rest.exception;

import com.zyd.core.platform.runtime.RuntimeEnvironment;
import com.zyd.core.platform.runtime.RuntimeSettings;
import com.zyd.core.platform.web.request.RequestContextImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author neo
 */
public class ErrorResponseBuilderTest {
    ErrorResponseBuilder errorResponseBuilder;

    RuntimeSettings runtimeSettings;

    RequestContextImpl requestContext;

    @Before
    public void createErrorResponseBuilder() {
        runtimeSettings = new RuntimeSettings();
        requestContext = new RequestContextImpl();

        errorResponseBuilder = new ErrorResponseBuilder();
        errorResponseBuilder.setRuntimeSettings(runtimeSettings);
        errorResponseBuilder.setRequestContext(requestContext);
    }

    @Test
    public void putRequestId() {
        requestContext.setRequestId("request-id");

        ErrorResponse response = errorResponseBuilder.createErrorResponse(new RuntimeException());

        Assert.assertEquals("request-id", response.getRequestId());
    }

    @Test
    public void putGetExceptionClass() {
        ErrorResponse response = errorResponseBuilder.createErrorResponse(new RuntimeException());
        Assert.assertEquals(RuntimeException.class.getName(), response.getExceptionClass());
    }

    @Test
    public void putExceptionTraceForDevEnvironment() {
        runtimeSettings.setEnvironment(RuntimeEnvironment.DEV);

        ErrorResponse response = errorResponseBuilder.createErrorResponse(new RuntimeException());

        Assert.assertNotNull(response.getExceptionTrace());
    }

    @Test
    public void notPutExceptionTraceForProdEnvironment() {
        runtimeSettings.setEnvironment(RuntimeEnvironment.PROD);

        ErrorResponse response = errorResponseBuilder.createErrorResponse(new RuntimeException());

        Assert.assertNull(response.getExceptionTrace());
    }
}
