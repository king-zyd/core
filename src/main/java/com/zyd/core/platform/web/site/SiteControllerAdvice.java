package com.zyd.core.platform.web.site;

import com.zyd.core.platform.web.request.RequestContext;
import com.zyd.core.platform.web.site.exception.ErrorPageModelBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.Map;

/**
 * This class defined all the global exception handler and model attribute for site
 *
 * @author neo
 */
@ControllerAdvice
public class SiteControllerAdvice {
    private SiteSettings siteSettings;
    private ErrorPageModelBuilder errorPageModelBuilder;
    private RequestContext requestContext;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView error(Throwable exception) {
        Map<String, Object> model = errorPageModelBuilder.buildErrorPageModel(exception);
        return new ModelAndView(siteSettings.getErrorPage(), model);
    }

    // method not allowed exception throws from outside of controller, so defined here
    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ModelAndView methodNotAllowed(HttpRequestMethodNotSupportedException exception) {
        Map<String, Object> model = errorPageModelBuilder.buildErrorPageModel(exception);
        return new ModelAndView(siteSettings.getResourceNotFoundPage(), model);
    }

    @ModelAttribute("requestContext")
    public RequestContext requestContext() {
        return requestContext;
    }

    @Inject
    public void setSiteSettings(SiteSettings siteSettings) {
        this.siteSettings = siteSettings;
    }

    @Inject
    public void setErrorPageModelBuilder(ErrorPageModelBuilder errorPageModelBuilder) {
        this.errorPageModelBuilder = errorPageModelBuilder;
    }

    @Inject
    public void setRequestContext(RequestContext requestContext) {
        this.requestContext = requestContext;
    }
}
