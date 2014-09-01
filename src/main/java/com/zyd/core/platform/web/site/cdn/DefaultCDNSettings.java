package com.zyd.core.platform.web.site.cdn;

import com.zyd.core.util.StringUtils;

/**
 * @author neo
 */
public class DefaultCDNSettings implements CDNSettings {
    private String[] cdnHosts;
    private boolean supportHTTPS;

    @Override
    public String[] getCDNHosts() {
        return cdnHosts;
    }

    @Override
    public boolean supportHTTPS() {
        return supportHTTPS;
    }

    public void setCDNHostsWithCommaDelimitedValue(String cdnHosts) {
        if (StringUtils.hasText(cdnHosts))
            this.cdnHosts = cdnHosts.split(",");
    }

    public void setSupportHTTPS(boolean supportHTTPS) {
        this.supportHTTPS = supportHTTPS;
    }
}
