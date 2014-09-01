package com.zyd.core.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * @author neo
 */
public class FormPostTest {
    @Test
    public void buildPostEntity() throws IOException {
        HTTPRequest post = HTTPRequest.post("http://url").form().addParameter("key1", "value1").addParameter("key2", "value2").request();
        HttpPost request = (HttpPost) post.getRawRequest();

        HttpEntity entity = request.getEntity();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        entity.writeTo(outputStream);

        assertEquals("application/x-www-form-urlencoded; charset=UTF-8", entity.getContentType().getValue());
        assertEquals("key1=value1&key2=value2", new String(outputStream.toByteArray()));
    }

    @Test
    public void addParameter() {
        HTTPRequest post = HTTPRequest.post("http://url").form().addParameter("key1", "value1").addParameter("key2", "value2").request();
        assertEquals(2, post.parameters().size());
        assertEquals("value1", post.parameters().get(0).getValue());
        assertEquals("value2", post.parameters().get(1).getValue());
    }

    @Test
    public void setParameterShouldRemoveAllPriorValues() {
        HTTPRequest post = HTTPRequest.post("http://url").form().addParameter("key1", "priorValue1").addParameter("key1", "priorValue2").setParameter("key1", "correctValue").request();

        assertEquals(1, post.parameters().size());
        assertEquals("correctValue", post.parameters().get(0).getValue());
    }

    @Test
    public void getBody() {
        HTTPRequest post = HTTPRequest.post("http://url").form().addParameter("key1", "value1").addParameter("key2", "value2").request();

        assertEquals("key1=value1&key2=value2", post.body());
    }

    @Test
    public void url() {
        HTTPRequest post = HTTPRequest.post("http://url").form().addParameter("key1", "value1").addParameter("key2", "value2").request();
        assertEquals("http://url", post.url());
    }
}
