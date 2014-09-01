package com.zyd.core.http;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

/**
 * @author neo
 */
public class ByteArrayPostTest {
    @Test
    public void useOctetStreamAsContentType() throws UnsupportedEncodingException {
        HTTPRequest request = HTTPRequest.post("http://url").binary(new byte[10]).request();
        assertEquals("binary/octet-stream", request.getEntity().getContentType().getValue());
    }
}
