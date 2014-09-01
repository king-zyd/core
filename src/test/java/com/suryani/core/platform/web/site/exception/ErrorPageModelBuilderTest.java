package com.zyd.core.platform.web.site.exception;

import com.zyd.core.platform.runtime.RuntimeEnvironment;
import com.zyd.core.platform.runtime.RuntimeSettings;
import com.zyd.core.platform.web.site.layout.ModelContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

/**
 * @author neo
 */
public class ErrorPageModelBuilderTest {
    ErrorPageModelBuilder errorPageModelBuilder;
    RuntimeSettings runtimeSettings;

    @Before
    public void createErrorPageModelBuilder() {
        runtimeSettings = new RuntimeSettings();

        errorPageModelBuilder = new ErrorPageModelBuilder();
        errorPageModelBuilder.setRuntimeSettings(runtimeSettings);
        errorPageModelBuilder.setModelContext(new ModelContext());
    }

    @Test
    public void getErrorMessageForNullPointerException() {
        String errorMessage = errorPageModelBuilder.getErrorMessage(new NullPointerException());
        Assert.assertEquals("not showing null message for null pointer exception", ErrorPageModelBuilder.ERROR_MESSAGE_NULL_POINTER_EXCEPTION, errorMessage);
    }

    @Test
    public void putExceptionInfoForDevEnvironment() {
        runtimeSettings.setEnvironment(RuntimeEnvironment.DEV);

        Map<String, Object> model = errorPageModelBuilder.buildErrorPageModel(new RuntimeException("test"));

        ExceptionInfo exceptionInfo = (ExceptionInfo) model.get("exception");
        Assert.assertNotNull(exceptionInfo);
        Assert.assertEquals("test", exceptionInfo.getMessage());
    }

    @Test
    public void notPutExceptionInfoForProdEnvironment() {
        runtimeSettings.setEnvironment(RuntimeEnvironment.PROD);

        Map<String, Object> model = errorPageModelBuilder.buildErrorPageModel(new RuntimeException());

        Assert.assertFalse(model.containsKey("exception"));
    }
}
