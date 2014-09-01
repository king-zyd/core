package com.zyd.core.platform.web.site.handler;

import com.zyd.core.platform.web.DeploymentSettings;
import com.zyd.core.platform.web.url.URLBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

import javax.inject.Inject;
import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;

import static org.springframework.web.servlet.view.UrlBasedViewResolver.FORWARD_URL_PREFIX;
import static org.springframework.web.servlet.view.UrlBasedViewResolver.REDIRECT_URL_PREFIX;

/**
 * @author neo
 */
public abstract class DefaultHandlerMapping extends AbstractHandlerMapping {
    private final Logger logger = LoggerFactory.getLogger(DefaultHandlerMapping.class);

    private DeploymentSettings deploymentSettings;

    @Override
    protected final Object getHandlerInternal(HttpServletRequest request) throws Exception {
        // only handle request dispatch, not forward
        if (!DispatcherType.REQUEST.equals(request.getDispatcherType())) return null;

        logger.debug("process handler mapping, handlerMappingClass={}", getClass().getName());

        String requestedPath = request.getPathInfo(); // only get current requested path
        if (requestedPath.startsWith("/monitor") || requestedPath.startsWith("/management")) return null;

        String view = getResultView(request);
        if (view == null) return null;
        if (view.startsWith(REDIRECT_URL_PREFIX)) {
            String redirectURL = view.substring(REDIRECT_URL_PREFIX.length());

            URLBuilder builder = new URLBuilder();
            builder.setContextPath(deploymentSettings.getDeploymentContext());
            builder.setLogicalURL(redirectURL);
            String targetRedirectURL = builder.buildRelativeURL();
            logger.debug("redirect, fromPath={}, toLogicalURL={}, toURL={}", requestedPath, redirectURL, targetRedirectURL);
            return new RedirectRequestHandler(targetRedirectURL);
        }
        if (view.startsWith(FORWARD_URL_PREFIX)) {
            String forwardURL = view.substring(FORWARD_URL_PREFIX.length());
            logger.debug("forward, from={}, to={}", requestedPath, forwardURL);
            return new ForwardRequestHandler(forwardURL);
        }
        throw new IllegalStateException("unknown view, view=" + view);
    }

    protected abstract String getResultView(HttpServletRequest request);

    @Inject
    public void setDeploymentSettings(DeploymentSettings deploymentSettings) {
        this.deploymentSettings = deploymentSettings;
    }
}