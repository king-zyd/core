package com.zyd.core.platform.cache;

import com.zyd.core.util.AssertUtils;
import com.zyd.core.util.TimeLength;

import net.sf.ehcache.Cache;
import net.sf.ehcache.config.CacheConfiguration;

import org.springframework.cache.CacheManager;

/**
 * @author neo
 */
public class CacheRegistryImpl implements CacheRegistry {
    private final CacheManager cacheManager;

    public CacheRegistryImpl(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void addCache(String cacheName, TimeLength expirationTime) {
        if (cacheManager instanceof CustomCacheManager) {
            ((CustomCacheManager) cacheManager).add(cacheName, expirationTime);
        } else {
            // for dev env, use simple hardcoded config
            addCache(cacheName, expirationTime, 0);
        }
    }

    @Override
    public void addCache(String cacheName, TimeLength expirationTime, int maxEntriesInHeap) {
        AssertUtils.assertTrue(cacheManager instanceof EhcacheCacheManager, "must use ehcache manager, for memcached, please use addCache(cacheName, expirationTime) method");
        CacheConfiguration cacheConfiguration = new CacheConfiguration(cacheName, maxEntriesInHeap);
        cacheConfiguration.setTimeToLiveSeconds(expirationTime.toSeconds());
        ((EhcacheCacheManager) cacheManager).addCache(new Cache(cacheConfiguration));
    }
}
