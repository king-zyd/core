package com.zyd.core.http;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author neo
 */
public class HTTPHeadersTest {
    @Test
    public void httpHeadersAreIterable() {
        HTTPHeaders headers = new HTTPHeaders();
        headers.add("header", "value");

        for (HTTPHeader header : headers) {
            Assert.assertEquals("header", header.name());
            Assert.assertEquals("value", header.value());
        }
    }

    @Test
    public void getFirstHeaderValue() {
        HTTPHeaders headers = new HTTPHeaders();
        headers.add("header", "value1");
        headers.add("header", "value2");

        Assert.assertEquals("value1", headers.firstHeaderValue("header"));
    }
}
