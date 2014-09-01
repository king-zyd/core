package com.zyd.core.platform.web.rest.security;

import com.zyd.core.platform.runtime.RuntimeEnvironment;
import com.zyd.core.platform.runtime.RuntimeSettings;
import com.zyd.core.util.Convert;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Date;

/**
 * @author neo
 */
public class ClientAuthorizationInterceptorTest {
    ClientAuthorizationInterceptor interceptor;
    RuntimeSettings runtimeSettings;

    @Before
    public void createClientAuthorizationInterceptor() {
        runtimeSettings = new RuntimeSettings();
        interceptor = new ClientAuthorizationInterceptor();
        interceptor.setRuntimeSettings(runtimeSettings);
    }

    @Test
    public void authenticateProdEnv() {
        runtimeSettings.setEnvironment(RuntimeEnvironment.PROD);
        Assert.assertTrue(interceptor.shouldAuthenticate(""));
        Assert.assertTrue(interceptor.shouldAuthenticate("clientId"));
    }

    @Test
    public void bypassAuthenticateOnDevEnvWithoutClientId() {
        runtimeSettings.setEnvironment(RuntimeEnvironment.DEV);
        Assert.assertTrue(interceptor.shouldAuthenticate("clientId"));
    }

    @Test
    public void authenticateOnDevEnvWithClientId() {
        runtimeSettings.setEnvironment(RuntimeEnvironment.DEV);
        Assert.assertTrue(interceptor.shouldAuthenticate("clientId"));
    }

    @Test
    public void getClientTimestampByMilliSeconds() {
        Date now = new Date();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(ClientAuthorizationInterceptor.HEADER_TIMESTAMP, now.getTime());
        Assert.assertEquals(now, interceptor.getClientTimestamp(request));
    }

    @Test
    public void getClientTimestampByDateFormat() {
        Date now = new Date();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(ClientAuthorizationInterceptor.HEADER_TIMESTAMP, Convert.toString(now, Convert.DATE_FORMAT_DATETIME));
        Assert.assertEquals(Convert.toString(now, Convert.DATE_FORMAT_DATETIME), Convert.toString(interceptor.getClientTimestamp(request), Convert.DATE_FORMAT_DATETIME));
    }
}
