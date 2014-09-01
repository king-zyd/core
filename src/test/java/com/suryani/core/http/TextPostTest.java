package com.zyd.core.http;

import com.zyd.core.util.CharacterEncodings;
import junit.framework.Assert;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author neo
 */
public class TextPostTest {
    @Test
    public void textEncoding() throws IOException {
        HTTPRequest post = HTTPRequest.post("http://url").text("·", HTTPConstants.CONTENT_TYPE_JSON).request();
        HttpPost request = (HttpPost) post.getRawRequest();

        HttpEntity entity = request.getEntity();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        entity.writeTo(outputStream);

        Assert.assertEquals("·", new String(outputStream.toByteArray(), CharacterEncodings.CHARSET_UTF_8));
    }
}
