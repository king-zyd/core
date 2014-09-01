package com.zyd.core.platform.web.site;

import com.zyd.core.platform.exception.ResourceNotFoundException;
import com.zyd.core.platform.exception.SessionTimeOutException;
import com.zyd.core.platform.web.DefaultController;
import com.zyd.core.platform.web.site.exception.ErrorPageModelBuilder;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Map;

/**
 * @author neo
 */
public class SiteController extends DefaultController {
    protected SiteSettings siteSettings;
    protected ErrorPageModelBuilder errorPageModelBuilder;

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView notFound(ResourceNotFoundException exception) {
        Map<String, Object> model = errorPageModelBuilder.buildErrorPageModel(exception);
        return new ModelAndView(siteSettings.getResourceNotFoundPage(), model);
    }

    @ExceptionHandler(SessionTimeOutException.class)
    public ModelAndView sessionTimeOut(SessionTimeOutException exception) {
        Map<String, Object> model = errorPageModelBuilder.buildErrorPageModel(exception);
        return new ModelAndView(siteSettings.getSessionTimeOutPage(), model);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ModelAndView validationError(MethodArgumentNotValidException exception) {
        return validationErrorPage(exception, exception.getBindingResult());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ModelAndView validationError(BindException exception) {
        return validationErrorPage(exception, exception.getBindingResult());
    }

    private ModelAndView validationErrorPage(Exception exception, BindingResult bindingResult) {
        Map<String, Object> model = errorPageModelBuilder.buildErrorPageModel(buildValidationErrorMessage(bindingResult), exception);
        return new ModelAndView(siteSettings.getErrorPage(), model);
    }

    private String buildValidationErrorMessage(BindingResult bindingResult) {
        Locale locale = LocaleContextHolder.getLocale();
        StringBuilder builder = new StringBuilder(120);
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append(fieldError.getObjectName())
                    .append('.')
                    .append(fieldError.getField())
                    .append(" => ")
                    .append(messages.getMessage(fieldError, locale))
                    .append(", rejectedValue=")
                    .append(fieldError.getRejectedValue())
                    .append('\n');
        }
        return builder.toString();
    }

    @Inject
    public void setSiteSettings(SiteSettings siteSettings) {
        this.siteSettings = siteSettings;
    }

    @Inject
    public void setErrorPageModelBuilder(ErrorPageModelBuilder errorPageModelBuilder) {
        this.errorPageModelBuilder = errorPageModelBuilder;
    }
}
