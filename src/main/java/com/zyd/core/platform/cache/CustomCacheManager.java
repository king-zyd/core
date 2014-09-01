package com.zyd.core.platform.cache;

import com.zyd.core.util.TimeLength;

/**
 * @author Bribin
 * @createdDate Aug 7, 2013
 */
public interface CustomCacheManager {

    void add(String cacheName, TimeLength expirationTime);

}
