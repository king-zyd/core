package com.zyd.core.platform.web.rest;

import com.zyd.core.platform.exception.InvalidRequestException;
import com.zyd.core.platform.exception.ResourceNotFoundException;
import com.zyd.core.platform.exception.UserAuthorizationException;
import com.zyd.core.platform.web.DefaultController;
import com.zyd.core.platform.web.rest.exception.ErrorResponse;
import com.zyd.core.platform.web.rest.exception.ErrorResponseBuilder;
import com.zyd.core.platform.web.rest.exception.FieldError;
import com.zyd.core.platform.web.rest.exception.ValidationErrorResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;

/**
 * @author neo
 */
public class RESTController extends DefaultController {
    private ErrorResponseBuilder errorResponseBuilder;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse error(Throwable e) {
        return errorResponseBuilder.createErrorResponse(e);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ResponseBody
    public ErrorResponse error(HttpMediaTypeNotSupportedException e) {
        return errorResponseBuilder.createErrorResponse(e);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse notFound(ResourceNotFoundException e) {
        return errorResponseBuilder.createErrorResponse(e);
    }

    @ExceptionHandler(UserAuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorResponse unauthorized(UserAuthorizationException e) {
        return errorResponseBuilder.createErrorResponse(e);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse validationError(BindException e) {
        return createValidationResponse(e.getBindingResult());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse validationError(MethodArgumentNotValidException e) {
        return createValidationResponse(e.getBindingResult());
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse validationError(InvalidRequestException e) {
        Locale locale = LocaleContextHolder.getLocale();
        ValidationErrorResponse response = new ValidationErrorResponse();
        FieldError error = new FieldError();
        error.setField(e.getField());
        error.setMessage(messages.getMessage(e.getMessage(), locale));
        response.getFieldErrors().add(error);
        return response;
    }

    private ValidationErrorResponse createValidationResponse(BindingResult bindingResult) {
        Locale locale = LocaleContextHolder.getLocale();
        ValidationErrorResponse response = new ValidationErrorResponse();
        List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (org.springframework.validation.FieldError fieldError : fieldErrors) {
            FieldError error = new FieldError();
            error.setField(fieldError.getField());
            error.setMessage(messages.getMessage(fieldError, locale));
            response.getFieldErrors().add(error);
        }
        return response;
    }

    @Inject
    public void setErrorResponseBuilder(ErrorResponseBuilder errorResponseBuilder) {
        this.errorResponseBuilder = errorResponseBuilder;
    }
}
