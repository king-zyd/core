package com.zyd.core.platform.web.site;

import com.zyd.core.platform.web.site.session.SessionProviderType;
import com.zyd.core.util.TimeLength;

/**
 * @author neo
 */
public class SiteSettings {
    private String errorPage;
    private String resourceNotFoundPage;
    private String sessionTimeOutPage;

    private TimeLength sessionTimeOut = TimeLength.minutes(15);
    private SessionProviderType sessionProviderType = SessionProviderType.LOCAL;
    private String remoteSessionServers;
    private String jsDir;
    private String cssDir;

    public String getErrorPage() {
        return errorPage;
    }

    public void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }

    public String getResourceNotFoundPage() {
        return resourceNotFoundPage;
    }

    public void setResourceNotFoundPage(String resourceNotFoundPage) {
        this.resourceNotFoundPage = resourceNotFoundPage;
    }

    public TimeLength getSessionTimeOut() {
        return sessionTimeOut;
    }

    public void setSessionTimeOut(TimeLength sessionTimeOut) {
        this.sessionTimeOut = sessionTimeOut;
    }

    public SessionProviderType getSessionProviderType() {
        return sessionProviderType;
    }

    public void setSessionProviderType(SessionProviderType sessionProviderType) {
        this.sessionProviderType = sessionProviderType;
    }

    public String getRemoteSessionServers() {
        return remoteSessionServers;
    }

    public void setRemoteSessionServers(String remoteSessionServers) {
        this.remoteSessionServers = remoteSessionServers;
    }

    public String getSessionTimeOutPage() {
        return sessionTimeOutPage;
    }

    public void setSessionTimeOutPage(String sessionTimeOutPage) {
        this.sessionTimeOutPage = sessionTimeOutPage;
    }

    public String getJSDir() {
        return jsDir;
    }

    public void setJSDir(String jsDir) {
        this.jsDir = jsDir;
    }

    public String getCSSDir() {
        return cssDir;
    }

    public void setCSSDir(String cssDir) {
        this.cssDir = cssDir;
    }
}
