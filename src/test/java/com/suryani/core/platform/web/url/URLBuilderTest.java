package com.zyd.core.platform.web.url;

import com.zyd.core.http.HTTPConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author neo
 */
public class URLBuilderTest {
    URLBuilder builder;

    @Before
    public void createURLBuilder() {
        builder = new URLBuilder();
    }

    @Test
    public void withContextPath() {
        builder.setContextPath("/context");
        builder.setLogicalURL("/uri");
        String url = builder.buildRelativeURL();

        Assert.assertEquals("/context/uri", url);
    }

    @Test
    public void withRootContextPath() {
        builder.setContextPath("/");
        builder.setLogicalURL("/uri");
        String url = builder.buildRelativeURL();

        Assert.assertEquals("/uri", url);
    }

    @Test
    public void buildFullURLWithHTTP() {
        builder.setContextPath("/context");
        builder.setDeploymentPorts(8080, 8443);
        builder.setScheme(HTTPConstants.SCHEME_HTTP);
        builder.setLogicalURL("/uri");
        builder.setServerName("localhost");

        String url = builder.buildFullURL();

        Assert.assertEquals("http://localhost:8080/context/uri", url);
    }

    @Test
    public void buildFullURLWithHTTPS() {
        builder.setContextPath("/context");
        builder.setDeploymentPorts(8080, 8443);
        builder.setScheme(HTTPConstants.SCHEME_HTTPS);
        builder.setLogicalURL("/uri");
        builder.setServerName("localhost");

        String url = builder.buildFullURL();

        Assert.assertEquals("https://localhost:8443/context/uri", url);
    }

    @Test
    public void buildFullURLWithServerPort() {
        builder.setContextPath("/context");
        builder.setServerPort(8443);
        builder.setScheme(HTTPConstants.SCHEME_HTTPS);
        builder.setLogicalURL("/uri");
        builder.setServerName("localhost");

        String url = builder.buildFullURL();

        Assert.assertEquals("https://localhost:8443/context/uri", url);
    }

    @Test
    public void buildFullURLWithHTTPAndStandardPort() {
        builder.setContextPath("/context");
        builder.setDeploymentPorts(80, 443);
        builder.setScheme(HTTPConstants.SCHEME_HTTP);
        builder.setServerName("localhost");
        builder.setLogicalURL("/uri");

        String url = builder.buildFullURL();

        Assert.assertEquals("http://localhost/context/uri", url);
    }

    @Test
    public void buildFullURLWithHTTPSAndStandardPort() {
        builder.setContextPath("/context");
        builder.setDeploymentPorts(80, 443);
        builder.setScheme(HTTPConstants.SCHEME_HTTPS);
        builder.setServerName("localhost");
        builder.setLogicalURL("/uri");

        String url = builder.buildFullURL();

        Assert.assertEquals("https://localhost/context/uri", url);
    }

    @Test
    public void buildRelativeURLWithParam() {
        builder.setContextPath("/");
        builder.setLogicalURL("/uri");
        builder.addParam("version", "1");
        String url = builder.buildRelativeURL();
        Assert.assertEquals("/uri?version=1", url);
    }

    @Test
    public void buildRelativeURLContainsQueryStringWithNewParam() {
        builder.setContextPath("/");
        builder.setLogicalURL("/uri?test=1");
        builder.addParam("version", "should_be_encoded_=");
        String url = builder.buildRelativeURL();
        Assert.assertEquals("/uri?test=1&version=should_be_encoded_%3D", url);
    }

    @Test
    public void getTargetServerPortWithOriginalServerPort() {
        builder.setServerPort(8080);
        builder.setDeploymentPorts(80, 443);
        builder.setScheme(HTTPConstants.SCHEME_HTTP);
        Assert.assertEquals(8080, builder.getTargetServerPort());
    }

    @Test
    public void getHTTPTargetServerPort() {
        builder.setDeploymentPorts(80, 443);
        builder.setScheme(HTTPConstants.SCHEME_HTTP);
        Assert.assertEquals(80, builder.getTargetServerPort());
    }

    @Test
    public void getHTTPSTargetServerPort() {
        builder.setDeploymentPorts(80, 443);
        builder.setScheme(HTTPConstants.SCHEME_HTTPS);
        Assert.assertEquals(443, builder.getTargetServerPort());
    }
}
