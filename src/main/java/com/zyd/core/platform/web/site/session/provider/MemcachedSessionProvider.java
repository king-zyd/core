package com.zyd.core.platform.web.site.session.provider;

import com.zyd.core.platform.monitor.ServiceStatus;
import com.zyd.core.platform.web.site.SiteSettings;
import com.zyd.core.util.TimeLength;
import net.spy.memcached.CASValue;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.spring.MemcachedClientFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.net.SocketAddress;
import java.util.Collection;

/**
 * @author neo
 */
public class MemcachedSessionProvider implements SessionProvider {
    private final Logger logger = LoggerFactory.getLogger(MemcachedSessionProvider.class);

    private static final TimeLength MEMCACHED_TIME_OUT = TimeLength.seconds(3);
    private MemcachedClient memcachedClient;
    private SiteSettings siteSettings;

    @PostConstruct
    public void initialize() throws Exception {
        MemcachedClientFactoryBean factory = new MemcachedClientFactoryBean();
        factory.setServers(siteSettings.getRemoteSessionServers());
        factory.setOpTimeout(MEMCACHED_TIME_OUT.toMilliseconds());
        factory.setProtocol(ConnectionFactoryBuilder.Protocol.BINARY);
        memcachedClient = (MemcachedClient) factory.getObject();
    }

    @PreDestroy
    public void shutdown() {
        logger.info("shutdown memcached session client");
        memcachedClient.shutdown();
    }

    @Override
    public String getAndRefreshSession(String sessionId) {
        String sessionKey = getCacheKey(sessionId);
        CASValue<Object> cacheValue = memcachedClient.getAndTouch(sessionKey, expirationTime());
        if (cacheValue == null) {
            logger.warn("can not find session or session expired, sessionKey=" + sessionKey);
            return null;
        }
        return (String) cacheValue.getValue();
    }

    @Override
    public void saveSession(String sessionId, String sessionData) {
        memcachedClient.set(getCacheKey(sessionId), expirationTime(), sessionData);
    }

    @Override
    public void clearSession(String sessionId) {
        memcachedClient.delete(getCacheKey(sessionId));
    }

    private int expirationTime() {
        return (int) siteSettings.getSessionTimeOut().toSeconds();
    }

    //TODO(neo): use different namespace for secure session?
    private String getCacheKey(String sessionId) {
        return "session:" + sessionId;
    }

    @Override
    public ServiceStatus getServiceStatus() throws Exception {
        Collection<SocketAddress> availableServers = memcachedClient.getAvailableServers();
        return availableServers.isEmpty() ? ServiceStatus.DOWN : ServiceStatus.UP;
    }

    @Override
    public String getServiceName() {
        return "MemcachedSession";
    }

    @Inject
    public void setSiteSettings(SiteSettings siteSettings) {
        this.siteSettings = siteSettings;
    }
}
