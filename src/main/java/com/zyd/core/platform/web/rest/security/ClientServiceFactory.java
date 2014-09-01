package com.zyd.core.platform.web.rest.security;

import com.zyd.core.platform.SpringObjectFactory;

/**
 * @author neo
 */
public interface ClientServiceFactory {
    ClientService create(SpringObjectFactory springObjectFactory);
}
