package com.zyd.core.platform.web.site.session.provider;

import com.zyd.core.platform.monitor.ServiceMonitor;

/**
 * @author neo
 */
public interface SessionProvider extends ServiceMonitor {
    String getAndRefreshSession(String sessionId);

    void saveSession(String sessionId, String sessionData);

    void clearSession(String sessionId);
}
