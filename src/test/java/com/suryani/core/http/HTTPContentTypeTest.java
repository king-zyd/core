package com.zyd.core.http;

import com.zyd.core.util.CharacterEncodings;
import junit.framework.Assert;
import org.apache.http.entity.ContentType;
import org.junit.Test;

/**
 * @author Chi
 */
public class HTTPContentTypeTest {
    @Test
    public void testGetCharsetAndMimeType() {
        HTTPContentType contentType = new HTTPContentType();
        contentType.setContentType(ContentType.create(HTTPConstants.CONTENT_TYPE_PLAIN, CharacterEncodings.CHARSET_ISO_8859_1));

        Assert.assertEquals(HTTPConstants.CONTENT_TYPE_PLAIN, contentType.getMimeType());
        Assert.assertEquals(CharacterEncodings.CHARSET_ISO_8859_1, contentType.getCharset());
    }

    @Test
    public void testGetDefaultCharsetAndMimeType() {
        HTTPContentType contentType = new HTTPContentType();
        Assert.assertEquals(HTTPConstants.CONTENT_TYPE_JSON, contentType.getMimeType());
        Assert.assertEquals(CharacterEncodings.CHARSET_UTF_8, contentType.getCharset());
    }
}
