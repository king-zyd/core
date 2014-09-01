package com.zyd.core.platform.web.site.cdn;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author neo
 */
public class DefaultCDNSettingsTest {
    DefaultCDNSettings settings;

    @Before
    public void createDefaultCDNSettings() {
        settings = new DefaultCDNSettings();
    }

    @Test
    public void setCDNHostsWithCommaDelimitedValueWithEmpty() {
        settings.setCDNHostsWithCommaDelimitedValue(null);
        Assert.assertNull(settings.getCDNHosts());

        settings.setCDNHostsWithCommaDelimitedValue("");
        Assert.assertNull(settings.getCDNHosts());
    }

    @Test
    public void setCDNHostsWithCommaDelimitedValue() {
        settings.setCDNHostsWithCommaDelimitedValue("c1.diapers.com,c2.diapers.com");
        Assert.assertEquals(2, settings.getCDNHosts().length);
        Assert.assertEquals("c1.diapers.com", settings.getCDNHosts()[0]);
        Assert.assertEquals("c2.diapers.com", settings.getCDNHosts()[1]);
    }
}
