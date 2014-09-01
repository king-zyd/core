package com.zyd.core.platform.cache;

import java.util.Date;

/**
 * @author neo
 */
public interface CacheGroup {
    String getName();

    Date getLastRefreshed();

    int getRevision();
}
