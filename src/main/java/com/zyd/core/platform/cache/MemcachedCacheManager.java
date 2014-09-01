package com.zyd.core.platform.cache;

import com.zyd.core.platform.monitor.ServiceMonitor;
import com.zyd.core.platform.monitor.ServiceStatus;
import com.zyd.core.platform.runtime.RuntimeSettings;
import com.zyd.core.util.AssertUtils;
import com.zyd.core.util.TimeLength;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.spring.MemcachedClientFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

import javax.annotation.PreDestroy;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author neo
 */
public class MemcachedCacheManager extends AbstractCacheManager implements CustomCacheManager, ServiceMonitor {
    private final Logger logger = LoggerFactory.getLogger(MemcachedCacheManager.class);
    private static final TimeLength MEMCACHED_TIME_OUT = TimeLength.seconds(3);

    private final List<MemcachedCache> caches = new ArrayList<>();
    private MemcachedClient memcachedClient;
    private RuntimeSettings runtimeSettings;

    @PreDestroy
    public void shutdown() {
        logger.info("shutdown memcached cache client");
        memcachedClient.shutdown();
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return caches;
    }

    @Override
    public void add(String cacheName, TimeLength expirationTime) {
        AssertUtils.assertNotNull(memcachedClient, "memcachedClient is required");
        AssertUtils.assertNotNull(runtimeSettings, "runtimeSettings is required");

        MemcachedCache cache = new MemcachedCache(cacheName, expirationTime);
        cache.setMemcachedClient(memcachedClient);
        cache.setRuntimeSettings(runtimeSettings);
        caches.add(cache);
    }

    public void setServers(String remoteServers) {
        MemcachedClientFactoryBean factory = new MemcachedClientFactoryBean();
        factory.setServers(remoteServers);
        factory.setOpTimeout(MEMCACHED_TIME_OUT.toMilliseconds());
        factory.setProtocol(ConnectionFactoryBuilder.Protocol.BINARY);
        try {
            memcachedClient = (MemcachedClient) factory.getObject();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public ServiceStatus getServiceStatus() throws Exception {
        Collection<SocketAddress> availableServers = memcachedClient.getAvailableServers();
        return availableServers.isEmpty() ? ServiceStatus.DOWN : ServiceStatus.UP;
    }

    @Override
    public String getServiceName() {
        return "Memcached";
    }

    public void setRuntimeSettings(RuntimeSettings runtimeSettings) {
        this.runtimeSettings = runtimeSettings;
    }
}
