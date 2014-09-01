package com.zyd.core.platform.web.url;

import com.zyd.core.http.HTTPConstants;
import com.zyd.core.util.AssertUtils;
import com.zyd.core.util.EncodingUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Provide standard way to construct url
 * <p/>
 * history
 * 1.1 optimized object creation according to profiling
 * 1.2 added param support and changed API
 * 1.3 if target scheme matches current scheme, use current ServerPort instead defaultPort
 * 1.4 refactored RequestWrapper to encapsulate context/ports handling, simplified URLBuilder
 *
 * @author neo
 * @version 1.4
 */
public final class URLBuilder {
    private Integer deploymentHTTPPort;
    private Integer deploymentHTTPSPort;

    private String scheme;
    private String serverName;
    private Integer serverPort;
    private String contextPath;
    private String logicalURL;
    private List<URLParam> params;

    /**
     * build relative URL starts with root, this relative url is for browser, it appends context to match the actual URL used by browser
     *
     * @return relative url valid for browser
     */
    public String buildRelativeURL() {
        StringBuilder builder = new StringBuilder();
        buildRelativeURLPart(builder);
        buildParameterPart(builder);
        return builder.toString();
    }

    public String buildFullURL() {
        AssertUtils.assertTrue(logicalURL != null && logicalURL.startsWith("/"), "logical url must start with '/' to build full url");
        AssertUtils.assertHasText(scheme, "scheme is required to build full url");
        AssertUtils.assertHasText(serverName, "serverName is required to build full url");
        AssertUtils.assertNotNull((deploymentHTTPPort != null && deploymentHTTPSPort != null) || serverPort != null, "deploymentHTTPPort/deploymentHTTPSPort or serverPort is required to build full url");

        StringBuilder builder = new StringBuilder();
        buildURLPrefix(builder);
        buildRelativeURLPart(builder);
        buildParameterPart(builder);
        return builder.toString();
    }

    private void buildParameterPart(StringBuilder builder) {
        if (params == null) return;
        char connector = logicalURL.indexOf('?') < 0 ? '?' : '&';
        builder.append(connector);
        boolean first = true;
        for (URLParam param : params) {
            if (!first) builder.append('&');
            builder.append(EncodingUtils.url(param.getName()))
                    .append('=')
                    .append(EncodingUtils.url(param.getValue()));
            first = false;
        }
    }

    void buildRelativeURLPart(StringBuilder builder) {
        if (logicalURL.startsWith("/")) {
            String context = getDeployedContext();
            builder.append(context)
                    .append(logicalURL);
        } else {
            builder.append(logicalURL);
        }
    }

    private String getDeployedContext() {
        AssertUtils.assertNotNull(contextPath, "contextPath is required");
        if ("/".equals(contextPath)) return ""; // because the url value contains '/' already
        return contextPath;
    }

    private void buildURLPrefix(StringBuilder builder) {
        builder.append(scheme)
                .append("://")
                .append(serverName);

        int port = getTargetServerPort();

        if (!isDefaultPort(port)) {
            builder.append(':').append(port);
        }
    }

    private boolean isDefaultPort(int port) {
        return (HTTPConstants.SCHEME_HTTP.equals(scheme) && port == HTTPConstants.STANDARD_PORT_HTTP)
                || (HTTPConstants.SCHEME_HTTPS.equals(scheme) && port == HTTPConstants.STANDARD_PORT_HTTPS);
    }

    int getTargetServerPort() {
        if (serverPort != null) return serverPort;
        if (HTTPConstants.SCHEME_HTTP.equals(scheme)) return deploymentHTTPPort;
        if (HTTPConstants.SCHEME_HTTPS.equals(scheme)) return deploymentHTTPSPort;
        throw new IllegalStateException("unknown scheme, scheme=" + scheme);
    }

    public void addParam(String name, String value) {
        if (params == null) params = new ArrayList<>();
        params.add(new URLParam(name, value));
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme.toLowerCase();
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setLogicalURL(String logicalURL) {
        this.logicalURL = logicalURL;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public void setDeploymentPorts(Integer deploymentHTTPPort, Integer deploymentHTTPSPort) {
        this.deploymentHTTPPort = deploymentHTTPPort;
        this.deploymentHTTPSPort = deploymentHTTPSPort;
    }
}
