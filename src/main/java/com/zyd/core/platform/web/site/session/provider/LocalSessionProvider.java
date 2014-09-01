package com.zyd.core.platform.web.site.session.provider;

import com.zyd.core.platform.monitor.ServiceStatus;
import com.zyd.core.platform.web.site.SiteSettings;
import com.zyd.core.util.DateUtils;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author neo
 */
public class LocalSessionProvider implements SessionProvider {
    private final Map<String, SessionValue> values = new ConcurrentHashMap<>();
    private SiteSettings siteSettings;

    @Override
    public String getAndRefreshSession(String sessionId) {
        SessionValue sessionValue = values.get(sessionId);
        if (sessionValue == null) return null;

        if (new Date().after(sessionValue.getExpiredDate())) {
            values.remove(sessionId);
            return null;
        }

        String data = sessionValue.getData();
        values.put(sessionId, new SessionValue(expirationTime(), data));
        return data;
    }

    @Override
    public void saveSession(String sessionId, String sessionData) {
        values.put(sessionId, new SessionValue(expirationTime(), sessionData));
    }

    @Override
    public void clearSession(String sessionId) {
        values.remove(sessionId);
    }

    private Date expirationTime() {
        return DateUtils.add(new Date(), Calendar.SECOND, (int) siteSettings.getSessionTimeOut().toSeconds());
    }

    @Override
    public ServiceStatus getServiceStatus() throws Exception {
        return ServiceStatus.UP;
    }

    @Override
    public String getServiceName() {
        return "LocalSession";
    }

    @Inject
    public void setSiteSettings(SiteSettings siteSettings) {
        this.siteSettings = siteSettings;
    }

    public static class SessionValue {
        private final Date expiredDate;
        private final String data;

        public SessionValue(Date expiredDate, String data) {
            this.expiredDate = expiredDate;
            this.data = data;
        }

        public Date getExpiredDate() {
            return expiredDate;
        }

        public String getData() {
            return data;
        }
    }
}
