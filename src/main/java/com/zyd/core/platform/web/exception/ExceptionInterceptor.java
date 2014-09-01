package com.zyd.core.platform.web.exception;

import com.zyd.core.platform.exception.ErrorHandler;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * In spring framework to catch exception on view rendering, requires interceptor.afterCompletion
 *
 * @author neo
 */
public class ExceptionInterceptor extends HandlerInterceptorAdapter {
    private ErrorHandler errorHandler;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        errorHandler.handle(ex);
    }

    @Inject
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
}
