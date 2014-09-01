package com.zyd.core.http;

import junit.framework.Assert;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by Lance.zhou on 14-3-18.
 */
public class CompressTest {

    HTTPClient client;

    @Test
    public void gzipTest() throws IOException {
        byte[] decoded = client.decodeResponse(gzipEntity("GZIP"));
        Assert.assertEquals("GZIP", new String(decoded));
    }

    @Test
    public void deflateTest() throws IOException {
        byte[] decoded = client.decodeResponse(deflateEntity("deflate"));
        Assert.assertEquals("deflate", new String(decoded));
    }

    @Test
    public void plainTest() throws IOException {
        AbstractHttpEntity entity = new ByteArrayEntity("plain".getBytes(), ContentType.APPLICATION_ATOM_XML);
        byte[] decoded = client.decodeResponse(entity);
        Assert.assertEquals("plain", new String(decoded));
    }

    @Before
    public void createHTTPClient() {
        client = new HTTPClient();
    }

    public AbstractHttpEntity gzipEntity(String content) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream stream = new GZIPOutputStream(out);
        stream.write(content.getBytes());
        stream.close();

        AbstractHttpEntity entity = new ByteArrayEntity(out.toByteArray(), ContentType.APPLICATION_ATOM_XML);
        entity.setContentEncoding("gzip");
        return entity;
    }

    public AbstractHttpEntity deflateEntity(String content) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DeflaterOutputStream stream = new DeflaterOutputStream(out);
        stream.write(content.getBytes());
        stream.close();

        AbstractHttpEntity entity = new ByteArrayEntity(out.toByteArray(), ContentType.APPLICATION_ATOM_XML);
        entity.setContentEncoding("deflate");
        return entity;
    }
}
