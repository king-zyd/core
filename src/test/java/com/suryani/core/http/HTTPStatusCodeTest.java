package com.zyd.core.http;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author neo
 */
public class HTTPStatusCodeTest {
    @Test
    public void isRedirectStatusCode() {
        HTTPStatusCode statusCode = new HTTPStatusCode(HTTPConstants.SC_MOVED_PERMANENTLY);

        Assert.assertTrue(statusCode.isRedirect());
    }

    @Test
    public void isSuccessStatusCode() {
        HTTPStatusCode statusCode = new HTTPStatusCode(HTTPConstants.SC_OK);

        Assert.assertTrue(statusCode.isSuccess());
    }
}
