package com.zyd.core.platform.cache;

import com.zyd.core.util.TimeLength;

/**
 * @author neo
 */
public interface CacheRegistry {
    void addCache(String cacheName, TimeLength expirationTime);

    /**
     * Only used for in memory cache, ehcache
     *
     * @param cacheName        the name of cache
     * @param expirationTime   expiration time
     * @param maxEntriesInHeap max entries in heap
     */
    void addCache(String cacheName, TimeLength expirationTime, int maxEntriesInHeap);
}
