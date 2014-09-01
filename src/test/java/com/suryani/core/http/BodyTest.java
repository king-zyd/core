package com.zyd.core.http;

import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * @author Chi
 */
public class BodyTest {
    @Test
    public void emptyBody() throws UnsupportedEncodingException {
        HTTPRequest get = HTTPRequest.get("http://url").request();
        HTTPRequest delete = HTTPRequest.get("http://url").request();

        Assert.assertEquals("", get.body());
        Assert.assertEquals("", delete.body());
    }

    @Test
    public void textBody() throws UnsupportedEncodingException {
        HTTPRequest post = HTTPRequest.post("http://url").text("{}", HTTPConstants.CONTENT_TYPE_JSON).request();
        Assert.assertEquals("{}", post.body());
    }

    @Test
    public void binaryBody() throws UnsupportedEncodingException {
        HTTPRequest post = HTTPRequest.post("http://url").binary("{}".getBytes()).request();
        Assert.assertEquals("{}", post.body());
    }
}
