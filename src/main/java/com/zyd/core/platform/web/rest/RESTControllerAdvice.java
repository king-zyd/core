package com.zyd.core.platform.web.rest;

import com.zyd.core.platform.web.rest.exception.ErrorResponse;
import com.zyd.core.platform.web.rest.exception.ErrorResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.inject.Inject;

/**
 * @author neo
 */
@ControllerAdvice
public class RESTControllerAdvice {
    private ErrorResponseBuilder errorResponseBuilder;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public ErrorResponse methodNotAllowed(HttpRequestMethodNotSupportedException e) {
        return errorResponseBuilder.createErrorResponse(e);
    }

    @Inject
    public void setErrorResponseBuilder(ErrorResponseBuilder errorResponseBuilder) {
        this.errorResponseBuilder = errorResponseBuilder;
    }
}
