package com.zyd.core.platform.web.exception;

import com.zyd.core.platform.exception.ErrorHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * track exception from handlerMethod
 *
 * @author neo
 */
public class ExceptionTrackingHandler extends AbstractHandlerExceptionResolver {
    private ErrorHandler errorHandler;

    public ExceptionTrackingHandler() {
        setOrder(HIGHEST_PRECEDENCE);
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        errorHandler.handle(ex);
        return null;
    }

    @Inject
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
}
