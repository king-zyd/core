package com.zyd.core.http;

import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author neo
 */
public class GetTest {
    @Test
    public void buildURLWithoutParameters() {
        HTTPRequest get = HTTPRequest.get("http://www.google.com/search").request();
        assertEquals("http://www.google.com/search", get.url());
    }

    @Test
    public void buildURLWithParameters() {
        HTTPRequest get = HTTPRequest.get("http://www.google.com/search").addParameter("q", "value1 value2").request();
        assertEquals("http://www.google.com/search?q=value1+value2", get.url());
    }

    @Test
    public void buildURLWithRootURLHasParameters() {
        HTTPRequest get = HTTPRequest.get("http://www.google.com/search?output=csv").addParameter("q", "value1 value2").request();
        assertEquals("http://www.google.com/search?output=csv&q=value1+value2", get.url());
    }

    @Test
    public void createRequestWithoutParams() {
        HttpRequestBase request = HTTPRequest.get("http://www.google.com/search").request().getRawRequest();
        assertEquals("http://www.google.com/search", request.getURI().toString());
    }
}
