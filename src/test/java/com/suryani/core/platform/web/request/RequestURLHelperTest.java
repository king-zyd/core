package com.zyd.core.platform.web.request;

import com.zyd.core.platform.web.DeploymentSettings;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;

import static org.junit.Assert.assertEquals;

/**
 * @author neo
 */
public class RequestURLHelperTest {
    RequestURLHelper requestURLHelper;
    MockHttpServletRequest httpRequest;
    DeploymentSettings deploymentSettings;

    @Before
    public void prepare() {
        httpRequest = new MockHttpServletRequest();
        deploymentSettings = new DeploymentSettings();
        requestURLHelper = new RequestURLHelper(httpRequest, deploymentSettings);
    }

    @Test
    public void getClientRequestedRelativeURLWithQueryString() {
        httpRequest.setPathInfo("/url");
        httpRequest.setQueryString("param=1");

        assertEquals("/url?param=1", requestURLHelper.getClientRequestedRelativeURLWithQueryString());
        assertEquals("/url", requestURLHelper.getClientRequestedRelativeURL());
    }

    @Test
    public void getClientRequestedRelativeURLWithoutQueryString() {
        httpRequest.setPathInfo("/url");

        assertEquals("/url", requestURLHelper.getClientRequestedRelativeURLWithQueryString());
        assertEquals("/url", requestURLHelper.getClientRequestedRelativeURL());
    }

    @Test
    public void getClientRequestedFullURLWithoutQueryString() {
        deploymentSettings.setDeploymentContext("/", new MockServletContext());

        httpRequest.setScheme("http");
        httpRequest.setServerName("localhost");
        httpRequest.setPathInfo("/url");

        assertEquals("http://localhost/url", requestURLHelper.getClientRequestedFullURLWithQueryString());
    }
}
