package com.zyd.core.platform;

import com.zyd.core.platform.cache.CacheProvider;
import com.zyd.core.platform.cache.CacheRegistry;
import com.zyd.core.platform.cache.CacheRegistryImpl;
import com.zyd.core.platform.cache.CacheSettings;
import com.zyd.core.platform.cache.DefaultCacheKeyGenerator;
import com.zyd.core.platform.cache.EhcacheCacheManager;
import com.zyd.core.platform.cache.MemcachedCacheManager;
import com.zyd.core.platform.runtime.RuntimeSettings;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;

/**
 * @author neo
 */
public abstract class DefaultCacheConfig implements CachingConfigurer {
    @Bean
    public CacheSettings cacheSettings() {
        return new CacheSettings();
    }

    @Override
    @Bean
    public CacheManager cacheManager() {
        CacheManager cacheManager = createCacheManager();
        addCaches(new CacheRegistryImpl(cacheManager));
        return cacheManager;
    }

    private CacheManager createCacheManager() {
        CacheSettings settings = cacheSettings();
        CacheProvider provider = settings.getCacheProvider();
        if (CacheProvider.MEMCACHED.equals(provider)) {
            MemcachedCacheManager cacheManager = new MemcachedCacheManager();
            cacheManager.setServers(settings.getRemoteCacheServers());
            // memcachedCacheManager uses following dependency during @Bean method (addCache), not able to inject
            cacheManager.setRuntimeSettings(RuntimeSettings.get());
            return cacheManager;
        } else if (CacheProvider.EHCACHE.equals(provider)) {
            return new EhcacheCacheManager();
        }
        throw new IllegalStateException("not supported cache provider, provider=" + provider);
    }

    @Override
    @Bean
    public KeyGenerator keyGenerator() {
        return new DefaultCacheKeyGenerator();
    }

    protected abstract void addCaches(CacheRegistry registry);
}
