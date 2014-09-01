package com.suryani.rest;

import com.zyd.core.platform.DefaultCacheConfig;
import com.zyd.core.platform.cache.CacheRegistry;
import com.zyd.core.util.TimeLength;
import com.suryani.rest.platform.cache.CacheTestRepository;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * @author neo
 */
@Configuration
@EnableCaching(proxyTargetClass = true)
public class CacheConfig extends DefaultCacheConfig {
    @Override
    protected void addCaches(CacheRegistry cacheRegistry) {
        cacheRegistry.addCache(CacheTestRepository.CACHE_NAME, TimeLength.hours(2));
    }
}
