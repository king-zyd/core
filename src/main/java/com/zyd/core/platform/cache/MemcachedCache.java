package com.zyd.core.platform.cache;

import com.zyd.core.platform.runtime.RuntimeSettings;
import com.zyd.core.util.StopWatch;
import com.zyd.core.util.TimeLength;

import net.spy.memcached.MemcachedClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import java.util.Date;

/**
 * @author neo
 */
public class MemcachedCache implements Cache, CacheGroup {

    private static final String CACHE_REVISION = "_GLOBAL_CACHE_REVISION_";
    private static final int CACHE_REVISION_EXPIRATION = 7 * 24 * 60 * 60; // 7 days
    private final Logger logger = LoggerFactory.getLogger(MemcachedCache.class);

    private final String cacheName;
    private final TimeLength expirationTime;
    private MemcachedClient memcachedClient;
    private RuntimeSettings runtimeSettings;
    private Date lastRefreshed = new Date();

    public MemcachedCache(String cacheName, TimeLength expirationTime) {
        this.cacheName = cacheName;
        this.expirationTime = expirationTime;
    }

    @Override
    public String getName() {
        return cacheName;
    }

    @Override
    public Object getNativeCache() {
        return memcachedClient;
    }

    @Override
    public ValueWrapper get(Object key) {
        return get(key, false);
    }

    private ValueWrapper get(Object key, boolean notRevised) {
        StopWatch watch = new StopWatch();
        String memcachedKey = null;
        boolean hit = false;
        try {
            if (notRevised)
                memcachedKey = constructBaseKey(key);
            else
                memcachedKey = constructKey(key);

            Object value = memcachedClient.get(memcachedKey);
            if (value == null)
                return null;

            hit = true;
            if (NullObject.INSTANCE.equals(value))
                return new SimpleValueWrapper(null);
            return new SimpleValueWrapper(value);
        } finally {
            logger.debug("get, memcachedKey={}, hit={}, elapsedTime={}", memcachedKey, hit, watch.elapsedTime());
        }
    }

    private String constructKey(Object key) {
        return constructBaseKey(key) + ":" + getRevision();
    }

    private String constructBaseKey(Object key) {
        return cacheName + ":" + key.toString() + ":" + runtimeSettings.getVersion();
    }

    @Override
    public void put(Object key, Object value) {
        put(key, value, false, (int) expirationTime.toSeconds());
    }

    private void put(Object key, Object value, boolean notRevised, int expiration) {
        StopWatch watch = new StopWatch();
        String memcachedKey = null;
        try {
            if (notRevised)
                memcachedKey = constructBaseKey(key);
            else
                memcachedKey = constructKey(key);

            if (value == null) {
                memcachedClient.set(memcachedKey, expiration, NullObject.INSTANCE);
            } else {
                memcachedClient.set(memcachedKey, expiration, value);
            }
        } finally {
            logger.debug("put, memcachedKey={}, elapsedTime={}", memcachedKey, watch.elapsedTime());
        }
    }

    @Override
    public void evict(Object key) {
        StopWatch watch = new StopWatch();
        String memcachedKey = null;
        try {
            memcachedKey = constructKey(key);
            memcachedClient.delete(memcachedKey);
        } finally {
            logger.debug("evict, memcachedKey={}, elapsedTime={}", memcachedKey, watch.elapsedTime());
        }
    }

    @Override
    public void clear() {
        int revision = getRevision();
        put(CACHE_REVISION, Integer.valueOf(++revision), true, CACHE_REVISION_EXPIRATION);
        lastRefreshed = new Date();
        logger.debug("clear, at {}, new revision={}", lastRefreshed, revision);
    }

    @Override
    public Date getLastRefreshed() {
        return lastRefreshed;
    }

    @Override
    public int getRevision() {
        ValueWrapper wrapper = get(CACHE_REVISION, true);
        if (wrapper == null)
            return 0;
        if (wrapper.get() instanceof Integer)
            return (Integer) wrapper.get();
        return 0;
    }

    public void setMemcachedClient(MemcachedClient memcachedClient) {
        this.memcachedClient = memcachedClient;
    }

    public void setRuntimeSettings(RuntimeSettings runtimeSettings) {
        this.runtimeSettings = runtimeSettings;
    }
}
