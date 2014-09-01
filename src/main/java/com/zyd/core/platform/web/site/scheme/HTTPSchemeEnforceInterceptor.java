package com.zyd.core.platform.web.site.scheme;

import com.zyd.core.http.HTTPConstants;
import com.zyd.core.platform.web.ControllerHelper;
import com.zyd.core.platform.web.DeploymentSettings;
import com.zyd.core.platform.web.request.RequestContext;
import com.zyd.core.platform.web.request.RequestContextInterceptor;
import com.zyd.core.platform.web.url.URLBuilder;
import com.zyd.core.util.AssertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author neo
 */
public class HTTPSchemeEnforceInterceptor extends HandlerInterceptorAdapter {
    private final Logger logger = LoggerFactory.getLogger(HTTPSchemeEnforceInterceptor.class);

    private RequestContextInterceptor requestContextInterceptor;
    private DeploymentSettings deploymentSettings;
    private RequestContext requestContext;

    // http scheme enforce interceptor need to handle both request and forward dispatcher, because of "forward:" view
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AssertUtils.assertTrue(requestContextInterceptor.initialized(request), "httpSchemeEnforceInterceptor depends on requestContextInterceptor, please check WebConfig");

        HTTPOnly httpOnly = ControllerHelper.findMethodOrClassLevelAnnotation(handler, HTTPOnly.class);
        if (httpOnly != null && request.isSecure()) {
            enforceScheme(request, response, HTTPConstants.SCHEME_HTTP);
            return false;
        }

        HTTPSOnly httpsOnly = ControllerHelper.findMethodOrClassLevelAnnotation(handler, HTTPSOnly.class);
        if (httpsOnly != null && !request.isSecure()) {
            enforceScheme(request, response, HTTPConstants.SCHEME_HTTPS);
            return false;
        }
        return true;
    }

    private void enforceScheme(HttpServletRequest request, HttpServletResponse response, String scheme) {
        URLBuilder builder = new URLBuilder();

        builder.setScheme(scheme);
        builder.setServerName(request.getServerName());
        builder.setDeploymentPorts(deploymentSettings.getHTTPPort(), deploymentSettings.getHTTPSPort());
        builder.setContextPath(deploymentSettings.getDeploymentContext());
        builder.setLogicalURL(requestContext.getClientRequestedRelativeURLWithQueryString());

        String redirectURL = builder.buildFullURL();
        logger.debug("redirect to different scheme, redirectURL={}", redirectURL);

        response.setStatus(HTTPConstants.SC_MOVED_PERMANENTLY);
        response.setHeader(HTTPConstants.HEADER_REDIRECT_LOCATION, redirectURL);
    }

    @Inject
    public void setDeploymentSettings(DeploymentSettings deploymentSettings) {
        this.deploymentSettings = deploymentSettings;
    }

    @Inject
    public void setRequestContext(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    @Inject
    public void setRequestContextInterceptor(RequestContextInterceptor requestContextInterceptor) {
        this.requestContextInterceptor = requestContextInterceptor;
    }
}
