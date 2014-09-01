package com.zyd.core.platform.web.site.tag;

import com.zyd.core.platform.runtime.RuntimeEnvironment;
import com.zyd.core.platform.runtime.RuntimeSettings;
import com.zyd.core.platform.web.DeploymentSettings;
import com.zyd.core.platform.web.site.SiteSettings;
import com.zyd.core.platform.web.site.cdn.DefaultCDNSettings;
import freemarker.template.SimpleScalar;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.HashMap;

/**
 * User: gary.zeng
 * Date: 14-6-10
 */
public class SeaJsUrlTagTest {
    SeaJsUrlTag seaJsUrlTag;
    MockHttpServletRequest request;
    RuntimeSettings devRuntimeSettings;
    RuntimeSettings prodRuntimeSettings;
    SiteSettings siteSettings;
    DefaultCDNSettings cdnSettings;
    DeploymentSettings deploymentSettings;

    @Before
    public void createCDNTagSupport() {
        request = new MockHttpServletRequest();
        siteSettings = new SiteSettings();
        devRuntimeSettings = new RuntimeSettings();
        prodRuntimeSettings = new RuntimeSettings();
        prodRuntimeSettings.setEnvironment(RuntimeEnvironment.PROD);
        cdnSettings = new DefaultCDNSettings();
        deploymentSettings = new DeploymentSettings();
        seaJsUrlTag = new SeaJsUrlTag(request, devRuntimeSettings, cdnSettings, siteSettings, deploymentSettings);

        cdnSettings.setCDNHostsWithCommaDelimitedValue("cdn.suryani.com");
        devRuntimeSettings.setVersion("1.0");
        siteSettings.setJSDir("/static/js");
        request.setScheme("http");
    }

    @Test
    public void buildSingleUrlInDev() throws Exception {
        HashMap<String, Object> params = new HashMap<>();
        params.put("value", new SimpleScalar("app/moduleA.js"));
        String html = seaJsUrlTag.buildContent(params);
        Assert.assertEquals("\"/static/js/app/moduleA.js\"", html);
    }

    @Test
    public void buildMultipleUrlsInDev() throws Exception {
        HashMap<String, Object> params = new HashMap<>();
        params.put("value", new SimpleScalar("app/moduleA.js, app/moduleB.js"));
        String html = seaJsUrlTag.buildContent(params);
        Assert.assertEquals("[\"/static/js/app/moduleA.js\",\"/static/js/app/moduleB.js\"]", html);
    }

    @Test
    public void buildSingleUrlInProd() throws Exception {
        HashMap<String, Object> params = new HashMap<>();
        params.put("value", new SimpleScalar("app/moduleA.js"));
        SeaJsUrlTag prodSeaJsUrlTag = new SeaJsUrlTag(request, prodRuntimeSettings, cdnSettings, siteSettings, deploymentSettings);
        String html = prodSeaJsUrlTag.buildContent(params);
        Assert.assertEquals("\"app/moduleA.js\"", html);
    }

    @Test
    public void buildMultipleUrlsInProd() throws Exception {
        HashMap<String, Object> params = new HashMap<>();
        params.put("value", new SimpleScalar("app/moduleA.js, app/moduleB.js"));
        SeaJsUrlTag prodSeaJsUrlTag = new SeaJsUrlTag(request, prodRuntimeSettings, cdnSettings, siteSettings, deploymentSettings);
        String html = prodSeaJsUrlTag.buildContent(params);
        Assert.assertEquals("[\"app/moduleA.js\",\"app/moduleB.js\"]", html);
    }
}
