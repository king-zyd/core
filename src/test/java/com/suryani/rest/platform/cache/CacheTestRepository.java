package com.suryani.rest.platform.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * @author neo
 */
@Repository
public class CacheTestRepository {
    public static final String CACHE_NAME = "entities";

    @Cacheable(CACHE_NAME)
    public Entity getEntityById(String id) {
        return new Entity(id);
    }

    @CacheEvict(CACHE_NAME)
    public void evictEntityById(String id) {

    }

    @Cacheable(CACHE_NAME)
    public Entity getDefaultEntity() {
        return new Entity("1");
    }

    @CacheEvict(CACHE_NAME)
    public void evictDefaultEntity() {

    }

    public static class Entity {
        private final String id;

        public Entity(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }
}
