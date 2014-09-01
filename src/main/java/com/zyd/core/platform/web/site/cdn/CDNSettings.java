package com.zyd.core.platform.web.site.cdn;

/**
 * @author neo
 */
public interface CDNSettings {
    String[] getCDNHosts();

    boolean supportHTTPS();
}
