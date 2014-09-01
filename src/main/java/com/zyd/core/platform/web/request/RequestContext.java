package com.zyd.core.platform.web.request;

import java.util.Date;

/**
 * @author neo
 */
public interface RequestContext {
    String getClientRequestedFullURLWithQueryString();

    /**
     * get client/browser requested relative url, it doesn't include servletContext or deploymentContext.
     * 
     * @return logical relative url
     */
    String getClientRequestedRelativeURLWithQueryString();

    String getClientRequestedRelativeURL();

    String getClientRequestServerName();

    boolean isSecure();

    RemoteAddress getRemoteAddress();

    String getClientId();

    String getRequestId();

    Date getRequestDate();

    String getAction();

    String getQueryString();

    String getRequestHeader(String name);
}
