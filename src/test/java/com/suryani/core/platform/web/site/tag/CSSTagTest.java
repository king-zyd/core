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
public class CSSTagTest {
    CSSTag cssTag;
    MockHttpServletRequest request;
    SiteSettings siteSettings;
    RuntimeSettings runtimeSettings;
    DefaultCDNSettings cdnSettings;
    DeploymentSettings deploymentSettings;

    @Before
    public void createCDNTagSupport() {
        request = new MockHttpServletRequest();
        siteSettings = new SiteSettings();
        runtimeSettings = new RuntimeSettings();
        cdnSettings = new DefaultCDNSettings();
        deploymentSettings = new DeploymentSettings();
        cssTag = new CSSTag(request, runtimeSettings, cdnSettings, siteSettings, deploymentSettings);

        cdnSettings.setCDNHostsWithCommaDelimitedValue("cdn.suryani.com");
        runtimeSettings.setVersion("1.0");
        siteSettings.setCSSDir("/static/css");
        request.setScheme("http");
    }

    @Test
    public void buildSingleCSSTagWithAbsoluteHref() throws Exception {
        HashMap<String, Object> params = new HashMap<>();
        params.put("href", new SimpleScalar("/static/css/stylesheet.css"));
        String html = cssTag.buildCSSTags(params);
        Assert.assertEquals("<link type=\"text/css\" rel=\"stylesheet\" href=\"http://cdn.suryani.com/static/css/stylesheet.css?version=1.0\"/>\n", html);
    }

    @Test
    public void buildSingleCSSTagWithRelativeHref() throws Exception {
        HashMap<String, Object> params = new HashMap<>();
        params.put("href", new SimpleScalar("stylesheet.css"));
        String html = cssTag.buildCSSTags(params);
        Assert.assertEquals("<link type=\"text/css\" rel=\"stylesheet\" href=\"http://cdn.suryani.com/static/css/stylesheet.css?version=1.0\"/>\n", html);
    }

    @Test
    public void buildMultipleCSSTagWithRelativeHref() throws Exception {
        HashMap<String, Object> params = new HashMap<>();
        params.put("href", new SimpleScalar("css1.css, css2.css"));
        String html = cssTag.buildCSSTags(params);
        Assert.assertEquals("<link type=\"text/css\" rel=\"stylesheet\" href=\"http://cdn.suryani.com/static/css/css1.css?version=1.0\"/>\n"
                + "<link type=\"text/css\" rel=\"stylesheet\" href=\"http://cdn.suryani.com/static/css/css2.css?version=1.0\"/>\n", html);
    }

    @Test
    public void buildSingleCSSTagWithExtAttributes() throws Exception {
        HashMap<String, Object> params = new HashMap<>();
        params.put("href", new SimpleScalar("stylesheet.css"));
        params.put("media", new SimpleScalar("screen and (max-width: 600px)"));
        String html = cssTag.buildCSSTags(params);
        Assert.assertEquals("<link type=\"text/css\" rel=\"stylesheet\" href=\"http://cdn.suryani.com/static/css/stylesheet.css?version=1.0\" media=\"screen and (max-width: 600px)\"/>\n", html);
    }
}
