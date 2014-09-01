package com.zyd.core.platform.web.rest.api;

import com.zyd.core.SpringServiceTest;
import com.zyd.core.http.HTTPClient;
import com.zyd.core.platform.web.rest.client.RESTServiceClient;
import junit.framework.Assert;
import org.junit.Test;

import javax.inject.Inject;

/**
 * @author Raymond
 */
public class APIProxyCreatorTest extends SpringServiceTest {
    @Inject
    HTTPClient httpClient;

    @Test
    public void testCreate() throws Exception {

        RESTServiceClient serviceClient = new RESTServiceClient();
        serviceClient.setHTTPClient(httpClient);

        APIProxyCreator proxyCreator = new APIProxyCreator("http://localhost:8080", APIInterface.class, serviceClient);
        APIInterface apiInterface = (APIInterface) proxyCreator.create();

        Assert.assertNotNull(apiInterface);
    }

}
