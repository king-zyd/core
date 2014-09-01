package com.zyd.core.platform.web.site.tag;

import com.zyd.core.platform.runtime.RuntimeSettings;
import com.zyd.core.platform.web.DeploymentSettings;
import com.zyd.core.platform.web.site.cdn.DefaultCDNSettings;
import freemarker.template.SimpleScalar;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author neo
 */
public class CDNTagSupportTest {
    CDNTagSupport cdnTagSupport;
    MockHttpServletRequest request;
    RuntimeSettings runtimeSettings;
    DefaultCDNSettings cdnSettings;
    DeploymentSettings deploymentSettings;

    @Before
    public void createCDNTagSupport() {
        request = new MockHttpServletRequest();
        cdnSettings = new DefaultCDNSettings();
        runtimeSettings = new RuntimeSettings();
        deploymentSettings = new DeploymentSettings();
        cdnTagSupport = new CDNTagSupport(request, runtimeSettings, cdnSettings, deploymentSettings);
    }

    @Test
    public void constructCDNURL() {
        cdnSettings.setCDNHostsWithCommaDelimitedValue("cdn.suryani.com");
        runtimeSettings.setVersion("1.0");
        request.setScheme("http");
        String url = cdnTagSupport.constructCDNURL("/static/css/default.css");

        Assert.assertEquals("http://cdn.suryani.com/static/css/default.css?version=1.0", url);
    }

    @Test
    public void constructCDNURLWithParam() {
        cdnSettings.setCDNHostsWithCommaDelimitedValue("cdn.suryani.com");
        runtimeSettings.setVersion("1.0");
        request.setScheme("http");
        String url = cdnTagSupport.constructCDNURL("/static/css/default.css?param=1");

        Assert.assertEquals("http://cdn.suryani.com/static/css/default.css?param=1&version=1.0", url);
    }

    @Test
    public void determineCDNHostWithOneCDNHost() {
        cdnSettings.setCDNHostsWithCommaDelimitedValue("cdn.suryani.com");
        String host = cdnTagSupport.determineCDNHost("/a");

        Assert.assertEquals("cdn.suryani.com", host);
    }

    @Test
    public void determineCDNHost() {
        cdnSettings.setCDNHostsWithCommaDelimitedValue("c1.suryani.com,c2.suryani.com");
        Assert.assertEquals("c1.suryani.com", cdnTagSupport.determineCDNHost("/a")); // hash = 1554
        Assert.assertEquals("c2.suryani.com", cdnTagSupport.determineCDNHost("/b")); // hash = 1555

        // from prod bug
        cdnSettings.setCDNHostsWithCommaDelimitedValue("c1.diapers.com,c2.diapers.com,c3.diapers.com,c4.diapers.com");
        Assert.assertEquals("c3.diapers.com", cdnTagSupport.determineCDNHost("/mstatic/images/nvhp_rvhp_banner.gif")); // hash = -2026818022
    }

    @Test
    public void buildExtAttributes() {
        Map<String, Object> params = new TreeMap<>(); // use tree map to preserve order of keys for test
        params.put("key1", new SimpleScalar("value1"));
        params.put("key2", new SimpleScalar("value2"));
        params.put("key3", new SimpleScalar("value3"));
        String attributeText = cdnTagSupport.buildExtAttributes(params, "key1");
        Assert.assertEquals(" key2=\"value2\" key3=\"value3\"", attributeText);
    }

    @Test
    public void supportCDNBasedOnScheme() {
        cdnSettings.setCDNHostsWithCommaDelimitedValue("cdn.suryani.com");
        cdnSettings.setSupportHTTPS(false);
        request.setSecure(true);

        Assert.assertFalse(cdnTagSupport.supportCDN());
    }

    @Test
    public void constructLocalURLShouldAppendVersion() {
        runtimeSettings.setVersion("1.0");
        String url = cdnTagSupport.constructLocalURL("/static/css/default.css");

        Assert.assertEquals("/static/css/default.css?version=1.0", url);
    }

    @Test
    public void constructCDNURLWithFullURL() {
        Assert.assertEquals("http://www.diapers.com/static/css/default.css?version=1.0", cdnTagSupport.constructCDNURL("http://www.diapers.com/static/css/default.css?version=1.0"));
        Assert.assertEquals("https://www.diapers.com/static/css/default.css?version=1.0", cdnTagSupport.constructCDNURL("https://www.diapers.com/static/css/default.css?version=1.0"));
    }
}
