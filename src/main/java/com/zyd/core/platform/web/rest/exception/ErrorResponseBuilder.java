package com.zyd.core.platform.web.rest.exception;

import com.zyd.core.platform.runtime.RuntimeEnvironment;
import com.zyd.core.platform.runtime.RuntimeSettings;
import com.zyd.core.platform.web.request.RequestContext;
import com.zyd.core.util.ExceptionUtils;

import javax.inject.Inject;

/**
 * @author neo
 */
public class ErrorResponseBuilder {
    private RuntimeSettings runtimeSettings;

    private RequestContext requestContext;

    public ErrorResponse createErrorResponse(Throwable e) {
        ErrorResponse error = new ErrorResponse();
        error.setMessage(e.getMessage());
        error.setExceptionClass(e.getClass().getName());
        error.setRequestId(requestContext.getRequestId());
        if (RuntimeEnvironment.DEV.equals(runtimeSettings.getEnvironment())) {
            error.setExceptionTrace(ExceptionUtils.stackTrace(e));
        }
        return error;
    }

    @Inject
    public void setRuntimeSettings(RuntimeSettings runtimeSettings) {
        this.runtimeSettings = runtimeSettings;
    }

    @Inject
    public void setRequestContext(RequestContext requestContext) {
        this.requestContext = requestContext;
    }
}