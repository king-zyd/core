package com.zyd.core.platform.cache;

import com.zyd.core.platform.monitor.ServiceMonitor;
import com.zyd.core.platform.monitor.ServiceStatus;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Status;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author neo
 */
public class EhcacheCacheManager extends EhCacheCacheManager implements ServiceMonitor {
    public EhcacheCacheManager() {
        setCacheManager(new net.sf.ehcache.CacheManager());
    }

    public void addCache(Ehcache cache) {
        getCacheManager().addCache(cache);
    }

    @Override
    public ServiceStatus getServiceStatus() throws Exception {
        CacheManager cacheManager = getCacheManager();
        boolean alive = Status.STATUS_ALIVE.equals(cacheManager.getStatus());
        if (alive) return ServiceStatus.UP;
        return ServiceStatus.DOWN;
    }

    @Override
    public String getServiceName() {
        return "Ehcache";
    }

    @Override
    protected Collection<org.springframework.cache.Cache> loadCaches() {
        CacheManager cacheManager = getCacheManager();

        String[] names = cacheManager.getCacheNames();
        List<Cache> caches = new ArrayList<>(names.length);
        for (String name : names) {
            caches.add(new EhcacheCache(cacheManager.getEhcache(name)));
        }
        return caches;
    }
}
