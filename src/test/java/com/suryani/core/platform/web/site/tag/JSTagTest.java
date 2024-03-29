package com.zyd.core.platform.web.site.tag;

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
 * @author walter.zheng
 */
public class JSTagTest {
    JSTag jsTag;
    MockHttpServletRequest request;
    RuntimeSettings runtimeSettings;
    SiteSettings siteSettings;
    DefaultCDNSettings cdnSettings;
    DeploymentSettings deploymentSettings;

    @Before
    public void createCDNTagSupport() {
        request = new MockHttpServletRequest();
        siteSettings = new SiteSettings();
        runtimeSettings = new RuntimeSettings();
        cdnSettings = new DefaultCDNSettings();
        deploymentSettings = new DeploymentSettings();
        jsTag = new JSTag(request, runtimeSettings, cdnSettings, siteSettings, deploymentSettings);

        cdnSettings.setCDNHostsWithCommaDelimitedValue("cdn.suryani.com");
        runtimeSettings.setVersion("1.0");
        siteSettings.setJSDir("/static/js");
        request.setScheme("http");
    }

    @Test
    public void buildSingleJSTagWithAbsoluteSrc() throws Exception {
        HashMap<String, Object> params = new HashMap<>();
        params.put("src", new SimpleScalar("/static/js/zepto.min.js"));
        String html = jsTag.buildJSTags(params);
        Assert.assertEquals("<script type=\"text/javascript\" src=\"http://cdn.suryani.com/static/js/zepto.min.js?version=1.0\"></script>\n", html);
    }

    @Test
    public void buildSingleJSTagWithRelativeSrc() throws Exception {
        HashMap<String, Object> params = new HashMap<>();
        params.put("src", new SimpleScalar("zepto.min.js"));
        String html = jsTag.buildJSTags(params);
        Assert.assertEquals("<script type=\"text/javascript\" src=\"http://cdn.suryani.com/static/js/zepto.min.js?version=1.0\"></script>\n", html);
    }

    @Test
    public void buildMultipleJSTagWithRelativeSrc() throws Exception {
        HashMap<String, Object> params = new HashMap<>();
        params.put("src", new SimpleScalar("js1.js, js2.js"));
        String html = jsTag.buildJSTags(params);
        Assert.assertEquals("<script type=\"text/javascript\" src=\"http://cdn.suryani.com/static/js/js1.js?version=1.0\"></script>\n"
                + "<script type=\"text/javascript\" src=\"http://cdn.suryani.com/static/js/js2.js?version=1.0\"></script>\n", html);
    }
}
