package com.zyd.core.platform.web.filter;

import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author neo
 */
final class RequestCachingInputStream extends ServletInputStream {
    private final ByteArrayInputStream inputStream;

    public RequestCachingInputStream(byte[] bytes) {
        inputStream = new ByteArrayInputStream(bytes);
    }

    @Override
    public int read() throws IOException {
        return inputStream.read();
    }
}
