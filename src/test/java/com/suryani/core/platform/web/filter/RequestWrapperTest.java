package com.zyd.core.platform.web.filter;

import com.zyd.core.http.HTTPConstants;
import com.zyd.core.platform.web.DeploymentSettings;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;
import java.util.Enumeration;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author neo
 */
public class RequestWrapperTest {
    @Test
    public void getHTTPServerPortFromDeploymentSettings() throws IOException {
        DeploymentSettings deploymentSettings = new DeploymentSettings();
        deploymentSettings.setHTTPPort(8080);
        MockHttpServletRequest request = createMockHTTPRequest();

        RequestWrapper requestWrapper = new RequestWrapper(request, deploymentSettings);
        assertThat(requestWrapper.getServerPort(), equalTo(8080));
    }

    @Test
    public void getHTTPServerPortFromDefault() throws IOException {
        DeploymentSettings deploymentSettings = new DeploymentSettings();
        MockHttpServletRequest request = createMockHTTPRequest();

        RequestWrapper requestWrapper = new RequestWrapper(request, deploymentSettings);
        assertThat(requestWrapper.getServerPort(), equalTo(80));
    }

    @Test
    public void getHTTPSServerPortFromDeploymentSettings() throws IOException {
        DeploymentSettings deploymentSettings = new DeploymentSettings();
        deploymentSettings.setHTTPSPort(8443);
        MockHttpServletRequest request = createMockHTTPRequest();
        request.setScheme(HTTPConstants.SCHEME_HTTPS);
        request.setServerPort(443);

        RequestWrapper requestWrapper = new RequestWrapper(request, deploymentSettings);
        assertThat(requestWrapper.getServerPort(), equalTo(8443));
    }

    @Test
    public void getHTTPSServerPortFromDefault() throws IOException {
        DeploymentSettings deploymentSettings = new DeploymentSettings();
        MockHttpServletRequest request = createMockHTTPRequest();
        request.setScheme(HTTPConstants.SCHEME_HTTPS);
        request.setServerPort(443);

        RequestWrapper requestWrapper = new RequestWrapper(request, deploymentSettings);
        assertThat(requestWrapper.getServerPort(), equalTo(443));
    }

    @Test
    public void getOverrideContentType() throws IOException {
        MockHttpServletRequest request = createMockHTTPRequest();
        request.addParameter("_contentType", "application/xml");

        RequestWrapper requestWrapper = new RequestWrapper(request, new DeploymentSettings());
        assertThat(requestWrapper.getContentType(), equalTo("application/xml"));
    }

    @Test
    public void getAcceptHeaders() throws IOException {
        MockHttpServletRequest request = createMockHTTPRequest();
        request.addHeader("accept", "application/xml");

        RequestWrapper requestWrapper = new RequestWrapper(request, new DeploymentSettings());
        Enumeration<String> headers = requestWrapper.getHeaders("accept");

        Assert.assertTrue(headers.hasMoreElements());
        assertThat(headers.nextElement(), equalTo("application/xml"));
    }

    private MockHttpServletRequest createMockHTTPRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContent(new byte[0]);
        return request;
    }
}
