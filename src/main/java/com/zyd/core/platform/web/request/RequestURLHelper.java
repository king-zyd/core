package com.zyd.core.platform.web.request;

import com.zyd.core.platform.web.DeploymentSettings;
import com.zyd.core.platform.web.url.URLBuilder;
import com.zyd.core.util.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * @author neo
 */
public class RequestURLHelper {
    private final HttpServletRequest request;
    private final DeploymentSettings deploymentSettings;

    public RequestURLHelper(HttpServletRequest request, DeploymentSettings deploymentSettings) {
        this.request = request;
        this.deploymentSettings = deploymentSettings;
    }

    public String getClientRequestedFullURLWithQueryString() {
        URLBuilder builder = new URLBuilder();
        builder.setScheme(request.getScheme());
        builder.setServerName(request.getServerName());
        builder.setServerPort(request.getServerPort());
        builder.setContextPath(deploymentSettings.getDeploymentContext());
        builder.setLogicalURL(getClientRequestedRelativeURLWithQueryString());
        return builder.buildFullURL();
    }

    public String getClientRequestedRelativeURLWithQueryString() {
        String pathInfo = getClientRequestedPathInfo();
        String queryString = getClientRequestedQueryString();
        if (StringUtils.hasText(queryString)) {
            return pathInfo + '?' + queryString;
        } else {
            return pathInfo;
        }
    }

    public String getClientRequestedRelativeURL() {
        return getClientRequestedPathInfo();
    }

    // path info starts with '/' and doesn't include any context (servletContext or deploymentContext)
    private String getClientRequestedPathInfo() {
        String forwardPath = (String) request.getAttribute(RequestDispatcher.FORWARD_PATH_INFO);
        if (forwardPath != null)
            return forwardPath;
        return request.getPathInfo();
    }

    public String getClientRequestedQueryString() {
        String forwardQueryString = (String) request.getAttribute(RequestDispatcher.FORWARD_QUERY_STRING);
        if (forwardQueryString != null)
            return forwardQueryString;
        return request.getQueryString();
    }
}
