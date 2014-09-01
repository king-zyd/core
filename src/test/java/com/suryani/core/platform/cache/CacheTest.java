package com.zyd.core.platform.cache;

import com.zyd.core.SpringServiceTest;
import com.suryani.rest.platform.cache.CacheTestRepository;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.cache.CacheManager;

import javax.inject.Inject;

/**
 * @author neo
 */
public class CacheTest extends SpringServiceTest {
    @Inject
    CacheTestRepository repository;

    @Inject
    CacheManager cacheManager;

    @Test
    public void retrieveFromCache() {
        String key = "key";
        CacheTestRepository.Entity entity1 = repository.getEntityById(key);
        CacheTestRepository.Entity entity2 = repository.getEntityById(key);

        Assert.assertSame(entity1, entity2);
    }

    @Test
    public void evictByCacheManager() {
        String key = "key";
        CacheTestRepository.Entity entity1 = repository.getEntityById(key);

        repository.evictEntityById(key);

        CacheTestRepository.Entity entity2 = repository.getEntityById(key);

        Assert.assertNotSame(entity1, entity2);
    }

    @Test
    public void retrieveFromCacheWithDefaultKey() {
        CacheTestRepository.Entity entity1 = repository.getDefaultEntity();
        CacheTestRepository.Entity entity2 = repository.getDefaultEntity();

        Assert.assertSame(entity1, entity2);
    }

    @Test
    public void evictDefaultKeyCache() {
        CacheTestRepository.Entity entity1 = repository.getDefaultEntity();

        repository.evictDefaultEntity();

        CacheTestRepository.Entity entity2 = repository.getDefaultEntity();

        Assert.assertNotSame(entity1, entity2);
    }
}
