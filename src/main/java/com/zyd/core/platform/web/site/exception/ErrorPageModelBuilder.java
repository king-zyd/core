package com.zyd.core.platform.web.site.exception;

import com.zyd.core.platform.runtime.RuntimeEnvironment;
import com.zyd.core.platform.runtime.RuntimeSettings;
import com.zyd.core.platform.web.request.RequestContext;
import com.zyd.core.platform.web.site.layout.ModelContext;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Single place to build model needed to render error page for web site only
 *
 * @author neo
 */
public class ErrorPageModelBuilder {
    static final String ERROR_MESSAGE_NULL_POINTER_EXCEPTION = "null pointer exception";
    private RuntimeSettings runtimeSettings;
    private ModelContext modelContext;
    private RequestContext requestContext;

    public Map<String, Object> buildErrorPageModel(Throwable exception) {
        return buildErrorPageModel(getErrorMessage(exception), exception);
    }

    String getErrorMessage(Throwable exception) {
        if (exception instanceof NullPointerException) return ERROR_MESSAGE_NULL_POINTER_EXCEPTION;
        return exception.getMessage();
    }

    public Map<String, Object> buildErrorPageModel(String errorMessage, Throwable exception) {
        Map<String, Object> model = new HashMap<>();

        if (RuntimeEnvironment.DEV.equals(runtimeSettings.getEnvironment())) {
            model.put("exception", new ExceptionInfo(errorMessage, exception));
        }

        model.put("requestContext", requestContext);
        modelContext.mergeToModel(model);
        return model;
    }

    @Inject
    public void setRuntimeSettings(RuntimeSettings runtimeSettings) {
        this.runtimeSettings = runtimeSettings;
    }

    @Inject
    public void setModelContext(ModelContext modelContext) {
        this.modelContext = modelContext;
    }

    @Inject
    public void setRequestContext(RequestContext requestContext) {
        this.requestContext = requestContext;
    }
}